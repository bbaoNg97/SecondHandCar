package my.edu.tarc.secondhandcar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BookingDetailActivity extends AppCompatActivity {

    private Button btnBackMyBooking;
    private TextView tvCarName, tvAppDate, tvAppTime, tvPrice, tvDealerLoc, tvAgentName, tvAgentContactNo, tvAgentEmail;
    private String carName, appDate, appTime, price, carPhoto, agentID, custID;
    private ImageView ivCarPhoto;
    private ProgressBar downloadingAppDetail;
    SharedPreferences sharePref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
        setTitle(R.string.title_booking_detail);

        sharePref = this.getSharedPreferences("My_Pref", Context.MODE_PRIVATE);
        custID = sharePref.getString("custID", null);

        tvCarName = (TextView) findViewById(R.id.textViewCarName);
        tvAppDate = (TextView) findViewById(R.id.textViewAppDate);
        tvAppTime = (TextView) findViewById(R.id.textViewAppTime);
        tvPrice = (TextView) findViewById(R.id.textViewPrice);
        tvDealerLoc = (TextView) findViewById(R.id.textViewDealerLocation);
        tvAgentName = (TextView) findViewById(R.id.textViewAgent);
        tvAgentContactNo = (TextView) findViewById(R.id.textViewAgentContactNo);
        tvAgentEmail = (TextView) findViewById(R.id.textViewAgentEmail);
        ivCarPhoto = (ImageView) findViewById(R.id.imageViewCarPhoto);
        btnBackMyBooking = (Button) findViewById(R.id.buttonBackMyBooking);
        downloadingAppDetail = (ProgressBar) findViewById(R.id.downloadingAppDetail);

        downloadingAppDetail.setVisibility(View.GONE);
        Intent intent = getIntent();
        carName = intent.getStringExtra("CarName");
        appDate = intent.getStringExtra("appDate");
        appTime = intent.getStringExtra("appTime");
        price = intent.getStringExtra("price");
        carPhoto = intent.getStringExtra("carPhoto");
        agentID = intent.getStringExtra("agentID");

        getAppointmentDetail(this, getString(R.string.get_booking_detail_url), custID, agentID);


        btnBackMyBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void getAppointmentDetail(Context context, String url, final String custID, final String agentID) {
        downloadingAppDetail.setVisibility(View.VISIBLE);
        btnBackMyBooking.setEnabled(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            //String message = jsonObject.getString("message");
                            JSONArray jsonArray = jsonObject.getJSONArray("DETAIL");
                            //if HAVE RECORD
                            if (success.equals("1")) {
                                String agentContact = "";
                                String agentName = "";
                                String agentEmail = "";
                                String dealerLocation = "";
                                //retrive the record
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject userResponse = jsonArray.getJSONObject(i);


                                    agentContact = userResponse.getString("agentContactNo");
                                    agentName = userResponse.getString("agentName");
                                    agentEmail = userResponse.getString("agentEmail");
                                    dealerLocation = userResponse.getString("dealerLocation");


                                }

                                tvCarName.setText(carName.toString());
                                tvAppDate.setText(appDate.toString());
                                tvAppTime.setText(appTime.toString());
                                tvPrice.setText("RM " + price.toString() + ".00");
                                Glide.with(getApplicationContext()).asBitmap().load(carPhoto).into(ivCarPhoto);
                                tvDealerLoc.setText(dealerLocation.toString());
                                tvAgentName.setText(agentName.toString());
                                tvAgentEmail.setText(agentEmail.toString());
                                tvAgentContactNo.setText(agentContact.toString());
                                downloadingAppDetail.setVisibility(View.GONE);
                                btnBackMyBooking.setEnabled(true);


                            } else {
                                Toast.makeText(BookingDetailActivity.this, "No record", Toast.LENGTH_LONG).show();
                                downloadingAppDetail.setVisibility(View.GONE);
                                btnBackMyBooking.setEnabled(true);
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Error:  " + e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                            downloadingAppDetail.setVisibility(View.GONE);
                            btnBackMyBooking.setEnabled(true);

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error: " + error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                        downloadingAppDetail.setVisibility(View.GONE);
                        btnBackMyBooking.setEnabled(true);

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //LHS is from php, RHS is getText there
                params.put("custID", custID);
                params.put("agentID", agentID);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }
}
