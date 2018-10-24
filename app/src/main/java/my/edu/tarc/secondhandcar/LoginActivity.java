package my.edu.tarc.secondhandcar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPw;
    private String email, pw;
    private Button buttonLogin;
    private TextView textViewSignUp, textViewReset;
    public static final String MY_PREF = "my_pref";
    public static final String NAME = "name";
    final String EMAIL = "email";
    RequestQueue queue;
    View v;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPw = (EditText) findViewById(R.id.editTextPw);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);
        textViewReset = (TextView) findViewById(R.id.textViewResetPw);

        SharedPreferences chkuser = getSharedPreferences(MY_PREF, MODE_PRIVATE);
        String chkUsername = chkuser.getString(EMAIL, null);

        //if no internet
        pDialog = new ProgressDialog(this);
        if (!isConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Connection Error");
            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

        }

        textViewReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resetPwIntent = new Intent(LoginActivity.this, ResetPwActivity.class);
                startActivity(resetPwIntent);
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(regIntent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnLogin();
            }
        });

    }

    //check internet
    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }


    public void OnLogin() {
        //store username and pw to compare with database
        String username = editTextEmail.getText().toString();
        String password = editTextPw.getText().toString();

        try {
            if (!pDialog.isShowing())
                pDialog.setMessage("Logging in...");
            pDialog.show();
            makeServiceCall(this, getString(R.string.login_url), username, password);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(
                    this.getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void makeServiceCall(Context context, String url, final String username, final String password) {
        queue = Volley.newRequestQueue(context);
        //Send data
        try {
            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (pDialog.isShowing())
                                    pDialog.dismiss();

                                if (success) {
                                    //get all data, because every activity have to use the data
                                    String Email = jsonObject.getString("custEmail");
                                    String password = jsonObject.getString("password");


                                    //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                   // startActivity(intent);

                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setTitle("Invalid password or username");
                                    builder.setMessage("Invalid password or username. Please try again.").setNegativeButton("Retry", null).create().show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (!isConnected()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setTitle("Connection Error");
                                builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

                            } else
                                Toast.makeText(LoginActivity.this.getApplicationContext(), "Error : " + error.toString(), Toast.LENGTH_LONG).show();
                            if (pDialog.isShowing())
                                pDialog.dismiss();

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("custEmail", username);
                    params.put("password", password);

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            queue.add(postRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
