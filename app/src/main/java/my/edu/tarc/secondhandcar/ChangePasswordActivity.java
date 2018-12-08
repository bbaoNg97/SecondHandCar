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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText etCurrentPw, etNewPw, etConfirmPw;
    private String currentPw, custID;
    private ProgressBar changingPw;
    private Button btnSave, btnReset;
    SharedPreferences sharePref;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setTitle(R.string.change_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //get share preference
        sharePref = getSharedPreferences("My_Pref", Context.MODE_PRIVATE);
        currentPw = sharePref.getString("password", null);
        custID = sharePref.getString("custID", null);

        //link UI
        etCurrentPw = (EditText) findViewById(R.id.editTextOldPw);
        etNewPw = (EditText) findViewById(R.id.editTextNewPw);
        etConfirmPw = (EditText) findViewById(R.id.editTextConfirmNewPw);
        changingPw = (ProgressBar) findViewById(R.id.changingPw);
        btnSave = (Button) findViewById(R.id.buttonSave);
        btnReset = (Button) findViewById(R.id.buttonPwRec);
        //progressbar invisible
        changingPw.setVisibility(View.INVISIBLE);

        if (!LoginActivity.isConnected(ChangePasswordActivity.this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
            builder.setTitle("Connection Error");
            builder.setIcon(R.drawable.ic_action_info);
            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strCurrentPw = etCurrentPw.getText().toString();
                String strNewPw = etNewPw.getText().toString();
                String strConfPw = etConfirmPw.getText().toString();

                if (!LoginActivity.isConnected(ChangePasswordActivity.this)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
                    builder.setTitle("Connection Error");
                    builder.setIcon(R.drawable.ic_action_info);
                    builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();
                } else if (strCurrentPw.isEmpty()) {
                    etCurrentPw.setError("Please enter current password");
                } else if (strNewPw.isEmpty()) {
                    etNewPw.setError("Please enter new password");
                } else if (strConfPw.isEmpty()) {
                    etConfirmPw.setError("Please enter confirm password");
                } else if (!strCurrentPw.equals(currentPw)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
                    builder.setTitle("Incorrect Current Password");
                    builder.setMessage("Wrong current password.\nPlease try again ").setNegativeButton("Retry", null).create().show();
                    etCurrentPw.requestFocus();
                } else {
                    if (!strNewPw.equals(strConfPw)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
                        builder.setTitle("Incorrect Password");
                        builder.setMessage("Please make sure the confirm password\nis match with the new password.").setNegativeButton("Retry", null).create().show();
                        etNewPw.requestFocus();
                    } else {
                        try {
                            changingPw.setVisibility(View.VISIBLE);
                            etCurrentPw.setEnabled(false);
                            etNewPw.setEnabled(false);
                            etConfirmPw.setEnabled(false);
                            btnReset.setEnabled(false);
                            btnSave.setEnabled(false);
                            makeServiceCall(ChangePasswordActivity.this, getString(R.string.update_password_url), custID, strNewPw);
                        } catch (Exception e) {
                            checkError(e,ChangePasswordActivity.this);
                        }
                    }
                }

            }


        });

    }

    private void makeServiceCall(Context context, String url, final String custID, final String strNewPw) {
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
                                String success = jsonObject.getString("success");
                                String message = jsonObject.getString("message");

                                if (success.equals("1")) {//UPDATED success
                                    proceed();
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    SharedPreferences.Editor editor = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                                    editor.putString("password", strNewPw);
                                    editor.apply();
                                    onBackPressed();
                                } else {
                                    proceed();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
                                    builder.setTitle("Failed to update");
                                    builder.setMessage(message).setNegativeButton("Retry", null).create().show();
                                }
                            } catch (JSONException e) {
                                checkError(e,ChangePasswordActivity.this);
                                proceed();

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            checkError(error,ChangePasswordActivity.this);
                            proceed();

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("custID", custID);
                    params.put("password", strNewPw);

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
            checkError(e,ChangePasswordActivity.this);
            proceed();
        }
    }

    public void OnReset(View v) {
        etCurrentPw.setText("");
        etNewPw.setText("");
        etConfirmPw.setText("");
        etCurrentPw.requestFocus();

    }

    private void proceed() {
        changingPw.setVisibility(View.GONE);
        etCurrentPw.setEnabled(true);
        etNewPw.setEnabled(true);
        etConfirmPw.setEnabled(true);
        btnReset.setEnabled(true);
        btnSave.setEnabled(true);
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
    private void checkError(Exception e, Context context) {
        if (!LoginActivity.isConnected(context)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Connection Error");
            builder.setIcon(R.drawable.ic_action_info);
            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

        } else {
            Toast.makeText(context, "Error:  \n" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
