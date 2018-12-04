package my.edu.tarc.secondhandcar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingDetailActivity extends AppCompatActivity {

    private Button btnEditBooking;
    private TextView tvCarName, tvAppDate, tvAppTime, tvPrice, tvDealerLoc, tvAgentName, tvAgentContactNo, tvAgentEmail;
    private String carName, appDate, appTime, price, carPhoto, custID, bookStatus, dealerID;
    private String appID;
    private String agentContact = "-";
    private String agentName = "-";
    private String agentEmail = "-";
    private String dealerLocation;
    private String reason = "none";
    private String agentID = "-";
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

                            JSONArray jsonArray = jsonObject.getJSONArray("DETAIL");
                            //if HAVE RECORD
                            if (success.equals("1")) {
                                //retrive the record
                                JSONObject userResponse;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    userResponse = jsonArray.getJSONObject(i);

                                    agentContact = userResponse.getString("agentContactNo");
                                    agentName = userResponse.getString("agentName");
                                    agentEmail = userResponse.getString("agentEmail");
                                    dealerLocation = userResponse.getString("dealerLocation");
                                    dealerID = userResponse.getString("dealerID");
                                }
                                retrieveData();
                                downloadingAppDetail.setVisibility(View.GONE);


                            } else {
                                Toast.makeText(BookingDetailActivity.this, "No record", Toast.LENGTH_LONG).show();
                                downloadingAppDetail.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            checkError(e,BookingDetailActivity.this);
                            e.printStackTrace();
                            downloadingAppDetail.setVisibility(View.GONE);

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       checkError(error,BookingDetailActivity.this);
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

                        //check status, can cancel booking at Pending status without reason
                        if (bookStatus.equals("Pending")) {

                            cancelBooking(BookingDetailActivity.this, getString(R.string.cancel_booking_url), appID, agentID);
                        }
                        //if the status is Booked, customer have to provide reason
                        else {

                            //provide valid reason in alertdialog
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(BookingDetailActivity.this);
                            alertDialog.setTitle("Enter Cancel Booking Reason");
                            alertDialog.setMessage("Please state down the reason of cancel booking. ");
                            final ArrayAdapter<CharSequence> reasonAdapter = ArrayAdapter.createFromResource(BookingDetailActivity.this, R.array.cancel_reason, android.R.layout.simple_spinner_item);
                            //using drop down list to let the customer choose a reason( prevent they type meaningless reason )
                            reasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            final Spinner spReason = new Spinner(BookingDetailActivity.this);
                            spReason.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
                            spReason.setAdapter(reasonAdapter);
                            alertDialog.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    reason = spReason.getSelectedItem().toString();
                                    cancelBookingToAgent(BookingDetailActivity.this, getString(R.string.cancel_booking_url), appID, agentID,reason);
                                }
                            }).setNegativeButton("Back", null);
                            alertDialog.setView(spReason).create().show();

                        }

                    }
                }).setNegativeButton("No", null).create().show();
                return true;
            case R.id.generateCode:
                Intent intent = new Intent(BookingDetailActivity.this, QRcodeActivity.class);
                intent.putExtra("appID", appID);
                intent.putExtra("agentID", agentID);
                startActivity(intent);
                return true;
            default:
                onBackPressed();
                return super.onOptionsItemSelected(item);
        }


    }

    private void cancelBookingToAgent(final Context context, String url, final String appID, final String agentID, final String reason) {
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
                                    sendEmail(reason);
                                } else {
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                                }
                            } catch (JSONException e) {
                                checkError(e,BookingDetailActivity.this);
                            }
                            proceed();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                           checkError(error,BookingDetailActivity.this);
                            proceed();

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("appID", appID);
                    params.put("agentID", agentID);
                    params.put("cancelReason", reason);

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
            checkError(e,BookingDetailActivity.this);
            proceed();
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
                                    Toast.makeText(BookingDetailActivity.this,"Cancelled",Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                                }
                            } catch (JSONException e) {
                                checkError(e,BookingDetailActivity.this);
                            }
                            proceed();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            checkError(error,BookingDetailActivity.this);
                            proceed();

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("appID", appID);
                    params.put("agentID", agentID);
                    params.put("cancelReason", reason);

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
            checkError(e,BookingDetailActivity.this);
            proceed();
        }
    }

    public void onEdit(View v) {

        Intent editBookingIntent = new Intent(BookingDetailActivity.this, MakeAppointmentActivity.class);
        SharedPreferences.Editor editor = sharePref.edit();
        editor.putString("appDate", appDate);
        editor.putString("appTime", appTime);
        editor.apply();
        editBookingIntent.putExtra("from", "editBooking");
        editBookingIntent.putExtra("appID", appID);
        editBookingIntent.putExtra("Price", price);
        editBookingIntent.putExtra("CarName", carName);
        editBookingIntent.putExtra("dealerID", dealerID);
        startActivity(editBookingIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (bookStatus.equals("Pending")) {
            getMenuInflater().inflate(R.menu.action_bar_only_cancel_book, menu);
        } else if (bookStatus.equals("Booked")) {
            getMenuInflater().inflate(R.menu.action_bar_cancel, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void proceed() {
        downloadingAppDetail.setVisibility(View.GONE);
        btnEditBooking.setEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharePref = this.getSharedPreferences("My_Pref", Context.MODE_PRIVATE);
        custID = sharePref.getString("custID", null);
        appDate = sharePref.getString("appDate", null);
        appTime = sharePref.getString("appTime", null);
        //set the default agentID to '-'
        agentID = "-";
        Intent intent = getIntent();

        appID = intent.getStringExtra("appID");
        carName = intent.getStringExtra("CarName");
        price = intent.getStringExtra("price");
        bookStatus = intent.getStringExtra("bookStatus");
        carPhoto = intent.getStringExtra("carPhoto");
        dealerLocation = intent.getStringExtra("dealerLocation");
        dealerID = intent.getStringExtra("dealerID");


        dPrice = Double.parseDouble(price);
        price = formatter.format(dPrice);

        if (bookStatus.equals("Pending")) {                             //pending status can edit booking date time, while other status cannot
            btnEditBooking.setEnabled(true);
            retrieveData();                             //directly get booking detail without retrieving dealer detail
        } else {
            //if it is Booked, Met, Cancelled
            btnEditBooking.setVisibility(View.INVISIBLE);
            //get apppointment and Dealer detail
            agentID = intent.getStringExtra("agentID");
            getAppointmentDetail(this, getString(R.string.get_booking_detail_url), custID, agentID);
        }


    }
    private void sendEmail(String strReason) {

        String Recipient = agentEmail;
        List<String> recipients = Arrays.asList(Recipient.split("\\s*,\\s*"));
        String strDate, strTime, strCar, strSenderEmail, strSenderPw, strSender;
        strDate = appDate;
        strTime = appTime;
        strSenderEmail = sharePref.getString("custEmail", null);
        strSenderPw = sharePref.getString("password", null);
        strSender = sharePref.getString("custName", null);

        strCar =carName;
        String subject = "Cancel booking on "+strDate+" "+ strTime;
        String body = "Hi,\nI would like to apologize that I have cancelled the booking on " +strCar+". The reason is: "+strReason+
                ". Hope you can understand. Thanks!" +
                "\nRegards,\n" + strSender;
        SendMailTask smt = new SendMailTask(BookingDetailActivity.this);
        smt.execute(strSenderEmail, strSenderPw, recipients, subject, body);


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
