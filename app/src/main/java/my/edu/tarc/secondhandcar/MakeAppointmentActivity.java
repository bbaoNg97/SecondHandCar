package my.edu.tarc.secondhandcar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MakeAppointmentActivity extends AppCompatActivity {

    private TextView tvDate, tvTime, tvSelectedCar, tvPrice;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private Button btnSendRequest;
    private ProgressBar booking;
    private String custID, price, carName;
    //private Date selectedTime;
    SharedPreferences sharePref;
    private String appID, carID, appDate, appTime, strDate, strTime, strDealerID, agentEmail, currentAgentEmail;

    RequestQueue requestQueue;
    List<String> bookingList = new ArrayList<>();
    private ArrayList<String> arrAgentEmail = new ArrayList<>();

    private int totalBooking;
    public static final String TAG = "my.edu.tarc.secondhandcar";
    String nextAppID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_appointment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnSendRequest = (Button) findViewById(R.id.buttonSendAppReq);
        tvDate = (TextView) findViewById(R.id.textViewDate);
        tvTime = (TextView) findViewById(R.id.textViewTime);
        booking = (ProgressBar) findViewById(R.id.booking);
        tvSelectedCar = (TextView) findViewById(R.id.textViewSelectedCarName);
        tvPrice = (TextView) findViewById(R.id.textViewSelectedCarPrice);

        if (!LoginActivity.isConnected(MakeAppointmentActivity.this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MakeAppointmentActivity.this);
            builder.setTitle("Connection Error");
            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();
        }

        sharePref = getSharedPreferences("My_Pref", Context.MODE_PRIVATE);
        custID = sharePref.getString("custID", null);
        appDate = sharePref.getString("appDate", null);
        appTime = sharePref.getString("appTime", null);

        Intent intent = getIntent();
        strDealerID = intent.getStringExtra("dealerID");
        //if is from booking
        if (intent.getStringExtra("from").equals("booking")) {
            setTitle(R.string.title_chooseDateTime);

            currentAgentEmail = intent.getStringExtra("agentEmail");

        }
        // if is from edit booking
        else {
            setTitle(R.string.title_edit_booking);
            appID = intent.getStringExtra("appID");
            tvDate.setText(appDate);
            tvTime.setText(appTime);
            btnSendRequest.setText(R.string.update_booking);
        }
        carID = intent.getStringExtra("carID");
        price = getIntent().getStringExtra("Price");
        carName = getIntent().getStringExtra("CarName");
        getAllAgentEmail(MakeAppointmentActivity.this, getString(R.string.get_agent_url), strDealerID);

        tvSelectedCar.setText(carName);
        tvPrice.setText(price);
        proceed();
        getAllAppointment(MakeAppointmentActivity.this, getString(R.string.get_appointment_url));

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        MakeAppointmentActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR);
                int minute = cal.get(Calendar.MINUTE);


                TimePickerDialog dialog = new TimePickerDialog(
                        MakeAppointmentActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mTimeSetListener, hour, minute, false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //initially month start at 0,so plus 1
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                tvDate.setText(date);
            }
        };


        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                //'sh' indicates showing purpose
                String shTime;
                SimpleDateFormat shFormatter = new SimpleDateFormat("hh:mm a");

                // time = hour + ":" + minute;

                if (hour >= 12) {
                    hour = hour - 12;
                    shTime = hour + ":" + minute + " PM";
                } else {

                    shTime = hour + ":" + minute + " AM";
                }
                //SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                //ParsePosition ps = new ParsePosition(0);
                //selectedTime = formatter.parse(time, ps);

                //convert time to Date object, and format to String object
                ParsePosition pos1 = new ParsePosition(0);
                Date shSelectedTime = shFormatter.parse(shTime, pos1);
                shTime = shFormatter.format(shSelectedTime);
                tvTime.setText(shTime);
            }
        };

        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    strDate = tvDate.getText().toString();
                    strTime = tvTime.getText().toString();

                    //date and time can not be null
                    if (strDate.equals("")) {
                        Toast.makeText(getApplicationContext(), "\t\t\t\tInvalid date.\nPlease select booking date.", Toast.LENGTH_LONG).show();

                    } else if (strTime.equals("")) {
                        Toast.makeText(getApplicationContext(), "\t\t\t\tInvalid time.\nPlease select booking time.", Toast.LENGTH_LONG).show();

                    } else {
                        Date validDate = new Date();
                        Calendar cal = Calendar.getInstance();
                        //valid booking date is one month from today
                        cal.setTime(validDate);
                        cal.add(Calendar.MONTH, 1);
                        //store the valid date(month)
                        validDate = cal.getTime();

                        //store current date to make comparison with selected date
                        Date currentDate = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        //convert to Date object to make comparison with currentDate
                        ParsePosition pos = new ParsePosition(0);
                        Date selectedDate = dateFormat.parse(strDate, pos);

                        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
                        ParsePosition pp = new ParsePosition(0);
                        Date selectedTime = formatter.parse(strTime, pp);
                        //get the hour from selected time, must between 9am-5pm
                        cal.setTime(selectedTime);
                        int hour = cal.get(Calendar.HOUR_OF_DAY);


                        //check if selected date is before currentDate
                        if (selectedDate.before(currentDate)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MakeAppointmentActivity.this);
                            builder.setTitle("Invalid Date");
                            builder.setMessage("The booking date should be after today.\nPlease try again.");
                            builder.setNegativeButton("Retry", null).create().show();
                        }
                        //check if selected date is after one month
                        else if (selectedDate.after(validDate)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MakeAppointmentActivity.this);
                            builder.setTitle("Invalid Month");
                            builder.setMessage("Only can make booking within one month.\nPlease try again.");
                            builder.setNegativeButton("Retry", null).create().show();
                        }
                        //check if booking time is between 9-5pm
                        else if (hour < 9 || hour >= 17) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MakeAppointmentActivity.this);
                            builder.setTitle("Invalid Time");
                            builder.setMessage("Only can book time at working hour ( 9am - 5pm ).\nPlease try again.");
                            builder.setNegativeButton("Retry", null).create().show();
                        }
                        //check connection
                        else if (!LoginActivity.isConnected(MakeAppointmentActivity.this)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MakeAppointmentActivity.this);
                            builder.setTitle("Connection Error");
                            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();
                        } else {
                            //if the button is send appointment request

                            if (btnSendRequest.getText().toString().equals(getString(R.string.send_appointment_request))) {
                                AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(MakeAppointmentActivity.this);
                                confirmBuilder.setTitle("Request Confirmation");
                                confirmBuilder.setMessage("Confirm to send request?\n(You may edit booking date time while waiting agent accept, but once " +
                                        "the agent accepted your boking request, you may not be able to change the date time again)\n" +
                                        "Are you sure? ").setCancelable(false).setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        booking.setVisibility(View.VISIBLE);
                                        tvDate.setEnabled(false);
                                        tvTime.setEnabled(false);
                                        btnSendRequest.setEnabled(false);

                                        makeServiceCall(MakeAppointmentActivity.this, getString(R.string.insert_booking_url), nextAppID, strDate, strTime, carID, custID);

                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        dialogInterface.cancel();

                                    }
                                }).create().show();
                            }
                            //if the button name is "Update booking"
                            else {
                                AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(MakeAppointmentActivity.this);
                                confirmBuilder.setTitle("Update Confirmation");
                                confirmBuilder.setMessage(R.string.msg_confirm_updt).setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        booking.setVisibility(View.VISIBLE);
                                        tvDate.setEnabled(false);
                                        tvTime.setEnabled(false);
                                        btnSendRequest.setEnabled(false);

                                        updateAppointment(MakeAppointmentActivity.this, getString(R.string.update_app_url), appID);

                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        dialogInterface.cancel();

                                    }
                                }).create().show();

                            }


                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                }


            }
        });
    }


    private void getAllAgentEmail(final Context context, String url, final String strDealerID) {
        arrAgentEmail.clear();
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            //String message = jsonObject.getString("message");
                            JSONArray jsonArray = jsonObject.getJSONArray("EMAIL");
                            //if HAVE RECORD
                            if (success.equals("1")) {
                                //retrive the record
                                JSONObject userResponse;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    userResponse = jsonArray.getJSONObject(i);
                                    agentEmail = userResponse.getString("agentEmail");

                                    arrAgentEmail.add(agentEmail);
                                }


                            } else {
                                Toast.makeText(context, "No record", Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Error:  " + e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();


                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error: " + error.toString(), Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //LHS is from php, RHS is getText there
                params.put("dealerID", strDealerID);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private void makeServiceCall(final Context context, String url, final String appID, final String appDate, final String appTime, final String carID, final String custID) {
        RequestQueue queue = Volley.newRequestQueue(context);
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                String message = jsonObject.getString("message");
                                //if make appointment successful
                                if (success.equals("1")) {
                                    sendEmailToAll();

                                } else {

                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {

                                checkError(e, MakeAppointmentActivity.this);

                            }

                            proceed();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            checkError(error, MakeAppointmentActivity.this);
                            error.printStackTrace();
                            proceed();

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //LHS is from php, RHS is getText there
                    params.put("appID", appID);
                    params.put("appDate", appDate);
                    params.put("appTime", appTime);
                    params.put("carID", carID);
                    params.put("custID", custID);

                    return params;
                }
            };


            queue.add(stringRequest);
        } catch (Exception e) {
            checkError(e, MakeAppointmentActivity.this);
            proceed();

        }

    }

    private void sendEmailToAll() {

        String Recipient = arrAgentEmail.toString().replace("[", "").replace("]", "");
        List<String> recipients = Arrays.asList(Recipient.split("\\s*,\\s*"));
        String strDate, strTime, strCar, strSenderEmail, strSenderPw, strSender;
        strDate = tvDate.getText().toString();
        strTime = tvTime.getText().toString();
        strSenderEmail = sharePref.getString("custEmail", null);
        strSenderPw = sharePref.getString("password", null);
        strSender = sharePref.getString("custName", null);

        strCar = tvSelectedCar.getText().toString();
        String subject = "Request for making appointment on car review";
        String body = "Hi,\nI would like to make an appointment with you at " + strDate + " "
                + strTime + " with " + strCar + ".\nHope to receive your email.\nThanks!" +
                "\nRegards,\n" + strSender;
        SendMailTask smt = new SendMailTask(MakeAppointmentActivity.this);
        smt.execute(strSenderEmail, strSenderPw, recipients, subject, body);


    }

    private void sendUpdateEmail() {
        String Recipient = arrAgentEmail.toString().replace("[", "").replace("]", "");
        List<String> recipients = Arrays.asList(Recipient.split("\\s*,\\s*"));
        String strDate, strTime, strCar, strSender,strSenderEmail,strSenderPw;
        strDate = tvDate.getText().toString();
        strTime = tvTime.getText().toString();
        strSenderEmail = sharePref.getString("custEmail", null);
        strSenderPw = sharePref.getString("password", null);
        strSender = sharePref.getString("custName", null);
        strCar = tvSelectedCar.getText().toString();
        String subject = "Updating appointment on car review ";
        String body = "Hi,\nThe appointment date time is updated to " + strDate + " "
                + strTime + " with " + strCar + ".\nHope to receive your email.\nThanks!" +
                "\nRegards,\n" + strSender;

        SendMailTask smt = new SendMailTask(MakeAppointmentActivity.this);
        smt.execute(strSenderEmail, strSenderPw, recipients, subject, body);
    }

    //to count total number of appointment and count the total number of customer to generate ID
    private void getAllAppointment(Context context, String url) {
        // Instantiate the RequestQueue
        requestQueue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            //everytime i listen to the server, i clear the list
                            bookingList.clear();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject userResponse = (JSONObject) response.get(i);
                                //json object that contains all of the customer in the user table
                                String appID = userResponse.getString("appID");
                                bookingList.add(appID);
                                totalBooking++;
                            }
                            //to generate next appointment ID
                            nextAppID = generateAppID(totalBooking);

                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //checkError(volleyError, MakeAppointmentActivity.this);
                        volleyError.printStackTrace();

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
            Toast.makeText(context, "Error:  \n" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private String generateAppID(int countAppointment) {
        String id = "A";

        String count = Integer.toString(countAppointment + 1);
        //if total appointment is 0-9
        if (countAppointment < 9) {
            id = id + "000" + count;
        }
        //if total appointment is 10-99
        else if (countAppointment >= 9 && countAppointment < 99) {
            id = id + "00" + count;
        }
        //if total appointment is 100-999
        else if (countAppointment >= 99 && countAppointment < 999) {
            id = id + "0" + count;
        }
        //if total appointment is more than 1000
        else {
            id = id + count;
        }

        return id;
    }

    private void proceed() {
        booking.setVisibility(View.GONE);
        tvDate.setEnabled(true);
        tvTime.setEnabled(true);
        btnSendRequest.setEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void updateAppointment(final Context context, String url, final String appID) {
        RequestQueue queue = Volley.newRequestQueue(context);
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                String message = jsonObject.getString("message");
                                //if update appointment successful
                                if (success.equals("1")) {
                                    SharedPreferences.Editor editor = sharePref.edit();
                                    editor.putString("appDate", strDate);
                                    editor.putString("appTime", strTime);
                                    editor.apply();
                                    sendUpdateEmail();
                                } else Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                checkError(e, context);
                            }
                            proceed();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            checkError(error, context);
                            proceed();

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //LHS is from php, RHS is getText there
                    params.put("appID", appID);
                    params.put("updateDate", strDate);
                    params.put("updateTime", strTime);
                    return params;
                }
            };


            queue.add(stringRequest);
        } catch (Exception e) {
            checkError(e, context);
            proceed();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
