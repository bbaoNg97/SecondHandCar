package my.edu.tarc.secondhandcar;

import android.content.Context;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    private EditText etName, etContactNo, etEmail, etPassword, etConfPassword;
    private Button btnSignUp;
    private ProgressBar loading;
    RequestQueue requestQueue;

    List<String> custEmailList;
    public static final String TAG = "my.edu.tarc.secondhandcar";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setTitle(R.string.title_reg);

        custEmailList = new ArrayList<>();
        etName = (EditText) findViewById(R.id.editTextRegName);
        etContactNo = (EditText) findViewById(R.id.editTextRegContactNo);
        etEmail = (EditText) findViewById(R.id.editTextRegEmail);
        etPassword = (EditText) findViewById(R.id.editTextRegPwd);
        etConfPassword = (EditText) findViewById(R.id.editTextRegConPwd);
        btnSignUp = (Button) findViewById(R.id.buttonRegSignUp);
        loading = (ProgressBar) findViewById(R.id.loading);

        //to store all customer detail from db
        getAllCustomer(getApplicationContext(),getString(R.string.get_cust_url));
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registration();
            }
        });


    }

    private void registration() {
        Customer customer=new Customer();

        final String confPwd=this.etConfPassword.getText().toString();
        final String name = this.etName.getText().toString();
        final String contactNo = this.etContactNo.getText().toString();
        final String email = this.etEmail.getText().toString();
        final String password = this.etPassword.getText().toString();

        //check duplicate username
        AlertDialog.Builder builder=new AlertDialog.Builder(RegistrationActivity.this);
        if(name.equals("")){
            etName.setError("Please fill in your name");
        }
        else if(contactNo.equals("")){
            etContactNo.setError("Please fill in your contact number");
        }
        else if(email.equals("")){
            etEmail.setError("Email please");
        }
        else if(password.equals("")){
            etPassword.setError("Password please");
        }
        else if(confPwd.equals("")){
            etConfPassword.setError("Please confirm your password");
        }
        else{
            loading.setVisibility(View.VISIBLE);
            btnSignUp.setEnabled(false);
            if(foundEmail(email)){
                builder.setMessage("Email is exist. Please try another.").setNegativeButton("Retry",null).create().show();
                loading.setVisibility(View.GONE);
                btnSignUp.setEnabled(true);
            }
            else if(!password.equals(confPwd)){
                builder.setMessage("Please make sure password is match with confirm password.").setNegativeButton("Retry",null).create().show();
                loading.setVisibility(View.GONE);
                btnSignUp.setEnabled(true);
            }
            else{
                customer.setCustName(name);
                customer.setCustContactNo(contactNo);
                customer.setCustEmail(email);
                customer.setPassword(password);
                try{
                    makeServiceCall(this,getString(R.string.reg_url),customer);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Error: at reg()"+e.getMessage(),Toast.LENGTH_LONG).show();
                    loading.setVisibility(View.GONE);
                    btnSignUp.setEnabled(true);
                }
            }
        }



    }

    private void makeServiceCall(final Context context, String url, final Customer customer ){
        RequestQueue queue = Volley.newRequestQueue(context);
        try{
            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.reg_url),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                String message = jsonObject.getString("message");
                                //if register successful
                                if (success.equals("1")) {
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                    loading.setVisibility(View.GONE);
                                    btnSignUp.setEnabled(true);
                                }

                            } catch (JSONException e) {

                                e.printStackTrace();
                                Toast.makeText(context, "Register failed! at onResponse()" + e.toString(), Toast.LENGTH_LONG).show();
                                loading.setVisibility(View.GONE);
                                btnSignUp.setEnabled(true);

                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Register failed!\n" + error.toString(), Toast.LENGTH_LONG).show();
                            loading.setVisibility(View.GONE);
                            btnSignUp.setEnabled(true);

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //LHS is from php, RHS is getText there
                    params.put("custID",customer.getCustID());
                    params.put("custName", customer.getCustName());
                    params.put("custEmail", customer.getCustEmail());
                    params.put("custContactNo", customer.getCustContactNo());
                    params.put("password", customer.getPassword());

                    return params;
                }
            };


            queue.add(stringRequest);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "Register failed! Onmake service call() " + e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    private void getAllCustomer(Context context, String url) {
        // Instantiate the RequestQueue
        requestQueue = Volley.newRequestQueue(context);

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
                                //String strID=userResponse.getString("custID");
                                custEmailList.add(strEmail);

                            }

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error: getAllCustomer" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Error: getAllCUstomerOutside " + volleyError.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

        // Set the tag on the request.
        jsonObjectRequest.setTag(TAG);

        // Add the request to the RequestQueue.
        requestQueue.add(jsonObjectRequest);
    }

    public boolean foundEmail(String emails) {
        //check whether the username exist or not
        boolean found = false;
        for (int i = 0; i < custEmailList.size(); ++i) {
            if (custEmailList.get(i).equals(emails)) {
                found = true;

            }
        }
        return found;
    }

    @Override
    protected void onPause() {
        //if user pause the activity, must clear the queue
        super.onPause();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
}
