package my.edu.tarc.secondhandcar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class EditProfActivity extends AppCompatActivity {

    private TextView tvCustEmail, tvCustID;
    private EditText etCustName, etCustContact;
    private Button btnUpdate;
    public String email, id, name, contactNo;
    private ProgressBar updating;
    SharedPreferences sharePref;
    SharedPreferences.Editor editor;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_prof);
        setTitle(R.string.title_edit_prof);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //get share preference
        sharePref = getSharedPreferences("My_Pref", Context.MODE_PRIVATE);
        email = sharePref.getString("custEmail", null);
        id = sharePref.getString("custID", null);
        name = sharePref.getString("custName", null);
        contactNo = sharePref.getString("custContactNo", null);

        //link UI
        tvCustEmail = (TextView) findViewById(R.id.textViewCustEmail);
        tvCustID = (TextView) findViewById(R.id.textViewCustID);
        etCustName = (EditText) findViewById(R.id.editTextcustName);
        etCustContact = (EditText) findViewById(R.id.editTextCustContNo);
        btnUpdate = (Button) findViewById(R.id.buttonUpdateProf);
        updating = (ProgressBar) findViewById(R.id.updating);

        tvCustID.setText(id);
        tvCustEmail.setText(email);
        etCustName.setText(name);
        etCustContact.setText(contactNo);
        updating.setVisibility(View.INVISIBLE);

        if (!LoginActivity.isConnected(EditProfActivity.this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditProfActivity.this);
            builder.setTitle("Connection Error");
            builder.setIcon(R.drawable.ic_action_info);
            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();
        }

        //when clicking update button
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updating.setVisibility(View.VISIBLE);
                etCustName.setEnabled(false);
                etCustContact.setEnabled(false);
                if (!LoginActivity.isConnected(EditProfActivity.this)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditProfActivity.this);
                    builder.setTitle("Connection Error");
                    builder.setIcon(R.drawable.ic_action_info);
                    builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();
                    proceed();

                } else {
                    try {
                        name = etCustName.getText().toString();
                        contactNo = etCustContact.getText().toString();
                        //check ContactNo digit
                        //case1: if contact no is 011
                        String con = contactNo.substring(2, 3);
                        if (con.equals("1")) {
                            //if range is between 11-12
                            if (contactNo.length() >= 11 && contactNo.length() <= 12) {
                                makeServiceCall(EditProfActivity.this, getString(R.string.update_prof_url), id);
                            } else {
                                Toast.makeText(EditProfActivity.this, "Invalid contact number.\nPlease try again.", Toast.LENGTH_LONG).show();
                                proceed();
                            }
                        }
                        //case 2: if contact no is not 011
                        else {
                            //if digit is more than 11 or less than 11
                            if (contactNo.length() > 11 || contactNo.length() < 11) {
                                Toast.makeText(EditProfActivity.this, "Invalid digit range of contact number.\nPlease try again.", Toast.LENGTH_LONG).show();
                                proceed();
                            } else {
                                makeServiceCall(EditProfActivity.this, getString(R.string.update_prof_url), id);
                            }
                        }

                    } catch (Exception e) {
                        checkError(e,EditProfActivity.this);
                        proceed();
                    }
                }


            }
        });
    }

    private void makeServiceCall(final Context context, String url, final String id) {
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

                                    SharedPreferences.Editor editor = getSharedPreferences("My_Pref", MODE_PRIVATE).edit();
                                    editor.putString("custName", name);
                                    editor.putString("custContactNo", contactNo);
                                    editor.apply();

                                    Toast.makeText(EditProfActivity.this, message, Toast.LENGTH_LONG).show();
                                    onBackPressed();

                                } else {
                                    Toast.makeText(EditProfActivity.this, message, Toast.LENGTH_LONG).show();

                                }
                                proceed();

                            } catch (JSONException e) {
                                checkError(e,EditProfActivity.this);
                                proceed();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            checkError(error,EditProfActivity.this);
                            proceed();

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("custID", id);
                    params.put("custEmail", email);
                    params.put("custContactNo", contactNo);
                    params.put("custName", name);
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
            checkError(e,EditProfActivity.this);
            proceed();
        }
    }

    private void proceed() {
        updating.setVisibility(View.GONE);
        etCustName.setEnabled(true);
        etCustContact.setEnabled(true);
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
