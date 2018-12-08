package my.edu.tarc.secondhandcar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class PwRecoveryActivity extends AppCompatActivity {
    private EditText etNewPw, etConfNewPw;
    private Button btnConfReset;
    private ProgressBar pbResetingPw;
    private SharedPreferences sharePref;
    private String email,newPw,confNewPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pw_recovery);
        etNewPw=(EditText)findViewById(R.id.etNewPwRecovery);
        etConfNewPw=(EditText)findViewById(R.id.etConfPwRecovery);
        btnConfReset=(Button) findViewById(R.id.buttonConfPwRecov);
        pbResetingPw=(ProgressBar)findViewById(R.id.pbResetingPw);
        pbResetingPw.setVisibility(View.GONE);
        setTitle(getString(R.string.title_reset_pw));

        btnConfReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPw=etNewPw.getText().toString();
                confNewPw=etConfNewPw.getText().toString();

                if(newPw.equals(confNewPw)){
                    makeServiceCall(PwRecoveryActivity.this,getString(R.string.update_password_email_url));
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(PwRecoveryActivity.this);
                    builder.setTitle("Incorrect Password");
                    builder.setMessage("Please make sure the confirm password\nis match with the new password.").setNegativeButton("Retry", null).create().show();
                    etNewPw.requestFocus();
                }
            }
        });
    }

    private void makeServiceCall(final Context context, String url) {
        sharePref = getSharedPreferences("My_Pref", Context.MODE_PRIVATE);
        email = sharePref.getString("custEmail", null);
        pbResetingPw.setVisibility(View.VISIBLE);
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

                                if (success.equals("1")) {//UPDATED success
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                    SharedPreferences.Editor editor = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                                    editor.putString("password", newPw);
                                    editor.apply();
                                    Intent intent=new Intent(PwRecoveryActivity.this,LoginActivity.class);
                                    intent.putExtra("from","PwRecovery");
                                    startActivity(intent);
                                } else {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(PwRecoveryActivity.this);
                                    builder.setTitle("Failed to update");
                                    builder.setMessage(message).setNegativeButton("Retry", null).create().show();
                                }
                                pbResetingPw.setVisibility(View.GONE);
                            } catch (JSONException e) {
                                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                                pbResetingPw.setVisibility(View.GONE);

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                            pbResetingPw.setVisibility(View.GONE);

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("custEmail", email);
                    params.put("password", newPw);

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
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            pbResetingPw.setVisibility(View.GONE);
        }
    }
}
