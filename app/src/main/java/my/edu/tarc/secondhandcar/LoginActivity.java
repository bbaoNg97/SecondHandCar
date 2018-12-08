package my.edu.tarc.secondhandcar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPw;
    private String email, pw;
    private Button buttonLogin;
    private TextView textViewSignUp, textViewReset;
    ProgressBar loading;

    View v;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        editTextEmail = (EditText) findViewById(R.id.etLoginEmail);
        editTextPw = (EditText) findViewById(R.id.editTextPw);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);
        textViewReset = (TextView) findViewById(R.id.textViewResetPw);
        loading = (ProgressBar) findViewById(R.id.loadingLogin);
        loading.setVisibility(View.GONE);
        setTitle(R.string.login);
        Intent intent=getIntent();
        //if come from password recovery, no need back arrow
        if(!intent.getStringExtra("from").equals("PwRecovery")){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //if no internet
        if (!isConnected(LoginActivity.this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Connection Error");
            builder.setIcon(R.drawable.ic_action_info);
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
                //store email and pw to compare with database
                String email = editTextEmail.getText().toString();
                String password = editTextPw.getText().toString();

                if (email.isEmpty()) {
                    editTextEmail.setError("Please enter your email address");
                } else if (password.isEmpty()) {
                    editTextPw.setError("Please enter your password");
                } else {
                    onLogin(email, password);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public void onLogin(final String email, final String pw) {
        loading.setVisibility(View.VISIBLE);
        buttonLogin.setEnabled(false);
        textViewSignUp.setEnabled(false);
        textViewReset.setEnabled(false);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.login_url), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    JSONArray jsonArray = jsonObject.getJSONArray("login");

                    String id = "";
                    String name = "";
                    String contactNo = "";
                    String email = "";
                    String pw = "";
                    //if login success
                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            //follow index
                            id = object.getString("id").trim();
                            name = object.getString("name");
                            contactNo = object.getString("contactNo");
                            email = object.getString("email").trim();
                            pw = object.getString("password");


                            loading.setVisibility(View.GONE);
                            buttonLogin.setEnabled(true);
                            textViewSignUp.setEnabled(true);
                            textViewReset.setEnabled(true);


                        }
                        Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_LONG).show();

                        SharedPreferences.Editor shPr = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                        shPr.clear();
                        shPr.putString("custEmail", email);
                        //shPr.commit();
                        shPr.putString("custID", id);
                        // shPr.commit();
                        shPr.putString("custName", name);
                        //shPr.commit();
                        shPr.putString("custContactNo", contactNo);
                        //shPr.commit();
                        shPr.putString("password", pw);
                        shPr.apply();
                        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish();


                    }
                    //if wrong password
                    else if (success.equals("-1")) {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                        loading.setVisibility(View.GONE);
                        buttonLogin.setEnabled(true);
                        textViewSignUp.setEnabled(true);
                        textViewReset.setEnabled(true);
                    }
                    //if wrong email
                    else {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                        loading.setVisibility(View.GONE);
                        buttonLogin.setEnabled(true);
                        textViewSignUp.setEnabled(true);
                        textViewReset.setEnabled(true);
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Error: \n" + e.toString(), Toast.LENGTH_LONG).show();
                    loading.setVisibility(View.GONE);
                    buttonLogin.setEnabled(true);
                    textViewSignUp.setEnabled(true);
                    textViewReset.setEnabled(true);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (!isConnected(LoginActivity.this)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("Connection Error");
                            builder.setIcon(R.drawable.ic_action_info);
                            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

                        } else {
                            Toast.makeText(LoginActivity.this, "Error:\n" + error.toString(), Toast.LENGTH_LONG).show();

                        }
                        loading.setVisibility(View.GONE);
                        buttonLogin.setEnabled(true);
                        textViewSignUp.setEnabled(true);
                        textViewReset.setEnabled(true);

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("custEmail", email);
                params.put("password", pw);


                return params;
            }
        };
        //10000 is the time in milliseconds adn is equal to 10 sec
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    //check internet
    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }
}
