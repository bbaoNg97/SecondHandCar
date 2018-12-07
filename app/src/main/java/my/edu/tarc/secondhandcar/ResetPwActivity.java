package my.edu.tarc.secondhandcar;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ResetPwActivity extends AppCompatActivity {
    private Button btnReset;
    private EditText etEmailAddr;
    private ProgressBar checkingEmail;
    private String email, strCustID;
    public static final String TAG = "my.edu.tarc.secondhandcar";
    List<String> custEmailList;
    List<String> custIDList;
    public String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pw);
        setTitle(R.string.title_reset_pw);
        custEmailList = new ArrayList<>();
        custIDList = new ArrayList<>();
        btnReset = (Button) findViewById(R.id.buttonReset);
        etEmailAddr = (EditText) findViewById(R.id.etPwCode);
        checkingEmail = (ProgressBar) findViewById(R.id.checkingEmail);
        checkingEmail.setVisibility(View.GONE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getCustomer(getApplicationContext(), getString(R.string.get_cust_url));

    }

    public void OnReset(View v) {
        email = etEmailAddr.getText().toString();
        btnReset.setEnabled(false);
        etEmailAddr.setEnabled(false);
        checkingEmail.setVisibility(View.VISIBLE);
        if (!LoginActivity.isConnected(ResetPwActivity.this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ResetPwActivity.this);
            builder.setTitle("Connection Error");
            builder.setIcon(R.drawable.ic_action_info);
            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

        } else if (etEmailAddr.getText().toString().isEmpty()) {
            etEmailAddr.setError("Please fill in your email address");
        } else {
            if (foundEmail(email)) {
                int rand = generatePwRecoveryCode();
                code = rand + "";
                sendPwRescoveryEmail(code);
                //updateCode(ResetPwActivity.this, getString(R.string.update_code_url), code, email);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPwActivity.this);
                builder.setTitle("Incorrect Email");
                builder.setMessage("The email entered is not in the record.\nPlease try again").setNegativeButton("Retry", null).create().show();
            }

        }
        btnReset.setEnabled(true);
        etEmailAddr.setEnabled(true);
        checkingEmail.setVisibility(View.GONE);


    }

    private void updateCode(Context context, String url, final String code, final String email) {

        RequestQueue queue = Volley.newRequestQueue(context);
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
                                String success = jsonObject.getString("success");
                                String message = jsonObject.getString("message");
                                //update code success
                                if (success.equals("1")) {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    SharedPreferences.Editor editor = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                                    editor.putString("custEmail", email);
                                    editor.apply();
                                } else {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                checkError(e, ResetPwActivity.this);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            checkError(error, ResetPwActivity.this);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("code", code);
                    params.put("email", email);

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
            checkError(e, ResetPwActivity.this);

        }
    }

    private static final Random generator = new Random();

    public static int generatePwRecoveryCode() {
        return 100000 + generator.nextInt(900000);
    }


    private void sendPwRescoveryEmail(String value) {
        String sender = "secondHandCarSellingSystem";
        String senderPw = "shcss,test";
        String receipient = email;
        List<String> recipients = Arrays.asList(receipient.split("\\s*,\\s*"));
        String subject = "Password Recovery";
        String body = "Your code is " + value + ". Plase enter this code to reset your password";
        SendPwRecoverMailTask smt = new SendPwRecoverMailTask(ResetPwActivity.this,code);
        smt.execute(sender, senderPw, recipients, subject, body);

    }

    //retrieve all the customer to check the email is exist or not
    private void getCustomer(Context context, String url) {
        // Instantiate the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            //everytime i listen to the server, i clear the list
                            custEmailList.clear();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject userResponse = (JSONObject) response.get(i);
                                //json object that contains all of the customer in the user table
                                String strEmail = userResponse.getString("custEmail");
                                String strCustID = userResponse.getString("custID");
                                custEmailList.add(strEmail);
                                custIDList.add(strCustID);
                            }


                        } catch (Exception e) {
                            checkError(e, ResetPwActivity.this);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        checkError(volleyError, ResetPwActivity.this);

                    }
                });

        // Set the tag on the request.
        jsonObjectRequest.setTag(TAG);

        // Add the request to the RequestQueue.
        requestQueue.add(jsonObjectRequest);
    }

    private void checkError(Exception e, Context context) {
        if (!LoginActivity.isConnected(context)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Connection Error");
            builder.setIcon(R.drawable.ic_action_info);
            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

        } else {
            Toast.makeText(context, "Send email failed. \n" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    //to check if the email entered is repeated or not
    public boolean foundEmail(String emails) {
        //check whether the username exist or not

        boolean found = false;
        for (int i = 0; i < custEmailList.size(); ++i) {
            if (custEmailList.get(i).equals(emails)) {
                strCustID = custIDList.get(i);
                found = true;

            }
        }
        return found;
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

}
