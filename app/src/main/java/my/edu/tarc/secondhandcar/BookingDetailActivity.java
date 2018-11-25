package my.edu.tarc.secondhandcar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BookingDetailActivity extends AppCompatActivity {

    private Button btnEditBooking;
    private TextView tvCarName, tvAppDate, tvAppTime, tvPrice, tvDealerLoc, tvAgentName, tvAgentContactNo, tvAgentEmail;
    private String carName, appDate, appTime, price, carPhoto, agentID, custID, bookStatus;
    private String appID, agentContact, agentName, agentEmail, dealerLocation, acceptDate, acceptTime;
    private ImageView ivCarPhoto;
    private ProgressBar downloadingAppDetail;
    SharedPreferences sharePref;
    NumberFormat formatter = NumberFormat.getCurrencyInstance();
    private Double dPrice;
    private Date acceptDateTime, validDateTime, currentDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
        setTitle(R.string.title_booking_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        downloadingAppDetail = (ProgressBar) findViewById(R.id.downloadingAppDetail);
        btnEditBooking = (Button) findViewById(R.id.buttonEdit);

        downloadingAppDetail.setVisibility(View.GONE);


        Intent intent = getIntent();

        appID = intent.getStringExtra("appID");
        carName = intent.getStringExtra("CarName");
        appDate = intent.getStringExtra("appDate");
        appTime = intent.getStringExtra("appTime");
        price = intent.getStringExtra("price");
        bookStatus = intent.getStringExtra("bookStatus");
        acceptDate = intent.getStringExtra("acceptDate");
        acceptTime = intent.getStringExtra("acceptTime");
        if (bookStatus.equals("Cancelled") || bookStatus.equals("Met")) {
            btnEditBooking.setEnabled(false);
        } else
            btnEditBooking.setEnabled(true);
        SimpleDateFormat shFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        ParsePosition pp = new ParsePosition(0);
        currentDateTime = new Date();
        //store the accept date time, if want to cancel booking,can check if it can be cancelled
        acceptDateTime = shFormatter.parse(acceptDate + " " + acceptTime, pp);
        //to make sure the valid cancel booking date time is in the 24 hours
        Calendar cal = Calendar.getInstance();
        cal.setTime(acceptDateTime);
        cal.add(Calendar.HOUR_OF_DAY, 24);
        validDateTime = cal.getTime();

        dPrice = Double.parseDouble(price);
        price = formatter.format(dPrice);
        carPhoto = intent.getStringExtra("carPhoto");
        agentID = intent.getStringExtra("agentID");

        getAppointmentDetail(this, getString(R.string.get_booking_detail_url), custID, agentID);

    }

    private void getAppointmentDetail(Context context, String url, final String custID, final String agentID) {
        downloadingAppDetail.setVisibility(View.VISIBLE);
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
                                //retrive the record
                                JSONObject userResponse;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    userResponse = jsonArray.getJSONObject(i);

                                    // appID = userResponse.getString("appID");
                                    agentContact = userResponse.getString("agentContactNo");
                                    agentName = userResponse.getString("agentName");
                                    agentEmail = userResponse.getString("agentEmail");
                                    dealerLocation = userResponse.getString("dealerLocation");

                                }
                                retrieveData();
                                downloadingAppDetail.setVisibility(View.GONE);


                            } else {
                                Toast.makeText(BookingDetailActivity.this, "No record", Toast.LENGTH_LONG).show();
                                downloadingAppDetail.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Error:  " + e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                            downloadingAppDetail.setVisibility(View.GONE);

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error: " + error.toString(), Toast.LENGTH_LONG).show();
                        // error.printStackTrace();
                        downloadingAppDetail.setVisibility(View.GONE);

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

    private void retrieveData() {
        tvCarName.setText(carName);
        tvAppDate.setText(appDate);
        tvAppTime.setText(appTime);
        tvPrice.setText(price);
        Glide.with(getApplicationContext()).asBitmap().load(carPhoto).into(ivCarPhoto);
        tvDealerLoc.setText(dealerLocation);
        tvAgentName.setText(agentName);
        tvAgentEmail.setText(agentEmail);
        tvAgentContactNo.setText(agentContact);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancel_book:
                AlertDialog.Builder builder = new AlertDialog.Builder(BookingDetailActivity.this);
                builder.setTitle("Cancel Booking");
                builder.setMessage("This booking will be cancelled.\nAre you sure to cancel booking?").setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //if the current dateTime is after the valid date time that want to cancel
                        if (currentDateTime.after(validDateTime)) {
                            AlertDialog.Builder buider = new AlertDialog.Builder(BookingDetailActivity.this);
                            //Toast.makeText(BookingDetailActivity.this,"\t\t\t\tCancel booking failed.\nOnly can cancel the booking within 24 hours after the agent accept the booking",Toast.LENGTH_LONG).show();
                            buider.setTitle("Cancel booking failed");
                            buider.setMessage("Only can cancel the booking within 24 hours after agent has accepted the booking \n( Agent has accepted the booking at " + acceptDate + " " + acceptTime).setNegativeButton("OK", null).create().show();
                        } else {
                            cancelBooking(BookingDetailActivity.this, getString(R.string.cancel_booking_url), appID, agentID);
                        }

                    }
                }).setNegativeButton("No", null).create().show();
                return true;
            case R.id.generateCode:
                Intent intent=new Intent(BookingDetailActivity.this,QRcodeActivity.class);
                intent.putExtra("appID",appID);
                intent.putExtra("agentID",agentID);
                startActivity(intent);
                return true;
            default:
                onBackPressed();
                return super.onOptionsItemSelected(item);
        }


    }

    private void cancelBooking(final Context context, String url, final String appID, final String agentID) {
        downloadingAppDetail.setVisibility(View.VISIBLE);
        btnEditBooking.setEnabled(false);
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
                                    finish();
                                } else {
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                                }
                            } catch (JSONException e) {
                                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                            }
                            proceed();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Error  " + error.toString(), Toast.LENGTH_LONG).show();
                            proceed();

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("appID", appID);
                    params.put("agentID", agentID);

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
            Toast.makeText(context, "Error : " + e.toString(), Toast.LENGTH_LONG).show();
            proceed();
        }
    }

    public void onEdit(View v) {
        if (currentDateTime.after(validDateTime)) {
            AlertDialog.Builder buider = new AlertDialog.Builder(BookingDetailActivity.this);
            //Toast.makeText(BookingDetailActivity.this,"\t\t\t\tCancel booking failed.\nOnly can cancel the booking within 24 hours after the agent accept the booking",Toast.LENGTH_LONG).show();
            buider.setTitle("Edit booking denied");
            buider.setMessage("Only can edit the booking within 24 hours after agent has accepted the booking \n( Agent has accepted the booking at " + acceptDate + " " + acceptTime).setNegativeButton("OK", null).create().show();
        }
        else{
            Intent editBookingIntent = new Intent(BookingDetailActivity.this, MakeAppointmentActivity.class);
            editBookingIntent.putExtra("from", "editBooking");
            editBookingIntent.putExtra("appID", appID);
            editBookingIntent.putExtra("Price", price);
            editBookingIntent.putExtra("CarName", carName);
            editBookingIntent.putExtra("appDate", appDate);
            editBookingIntent.putExtra("appTime", appTime);
            startActivity(editBookingIntent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (bookStatus.toString().equals("Booked")) {
            getMenuInflater().inflate(R.menu.action_bar_cancel, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void proceed() {
        downloadingAppDetail.setVisibility(View.GONE);
        btnEditBooking.setEnabled(true);
    }
}
