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

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
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
    private String appID, carID, appDate, appTime, strDate, strTime;
    RequestQueue requestQueue;
    List<String> bookingList = new ArrayList<>();

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

        Intent intent = getIntent();
        if (intent.getStringExtra("from").equals("booking")) {
            setTitle(R.string.title_chooseDateTime);
        } else {
            setTitle(R.string.title_edit_booking);
            appID = intent.getStringExtra("appID");
            appDate = intent.getStringExtra("appDate");
            appTime = intent.getStringExtra("appTime");
            tvDate.setText(appDate);
            tvTime.setText(appTime);
            btnSendRequest.setText(R.string.update_booking);
        }
        carID = intent.getStringExtra("carID");
        price = getIntent().getStringExtra("Price");
        carName = getIntent().getStringExtra("CarName");


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
                        mTimeSetListener, hour, minute,false);
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
                    if (strDate.equals("") || strTime.equals("")) {
                        Toast.makeText(getApplicationContext(), "\t\tInvalid date and time.\nPlease select date and time.", Toast.LENGTH_LONG).show();
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
                        ParsePosition pp=new ParsePosition(0);
                        Date selectedTime = formatter.parse(strTime, pp);
                        //get the hour from selected time, must between 9am-5pm
                        cal.setTime(selectedTime);
                        int hour = cal.get(Calendar.HOUR_OF_DAY);


                        //check if selected date is before currentDate
                        if (selectedDate.before(currentDate)) {
                            Toast.makeText(getApplicationContext(), "Date cannot earlier than today.\n\t\t\t\t\t\tPlease try again.", Toast.LENGTH_LONG).show();
                        }
                        //check if selected date is after one month
                        else if (selectedDate.after(validDate)) {
                            Toast.makeText(getApplicationContext(), "Only can make booking within one month.\nPlease try again.", Toast.LENGTH_LONG).show();
                        }
                        //check if booking time is between 9-5pm
                        else if (hour < 9 || hour >= 17) {
                            Toast.makeText(getApplicationContext(), "Only can book time in between 9am-5pm\n\t\t\t\t\t\t\t\t\t\tPlease try again.", Toast.LENGTH_LONG).show();
                        }
                        //check connection
                        else if (!LoginActivity.isConnected(MakeAppointmentActivity.this)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MakeAppointmentActivity.this);
                            builder.setTitle("Connection Error");
                            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();
                        }
                        //if havent login yet
                        else if (custID == null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MakeAppointmentActivity.this);
                            builder.setMessage("Please login first").setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent loginIntent = new Intent(MakeAppointmentActivity.this, LoginActivity.class);
                                    startActivity(loginIntent);
                                }
                            }).create().show();
                        } else {
                            //if the button is send appointment request

                            if (btnSendRequest.getText().toString().equals(getString(R.string.send_appointment_request))) {
                                AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(MakeAppointmentActivity.this);
                                confirmBuilder.setTitle("Request Confirmation");
                                confirmBuilder.setMessage("Confirm to send request?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        booking.setVisibility(View.VISIBLE);
                                        tvDate.setEnabled(false);
                                        tvTime.setEnabled(false);
                                        btnSendRequest.setEnabled(false);

                                        //Todo: store date, time, appID,custID,carID,status(pending by default)(no need) ,agentID(no need)
                                        //custID is get,date time also got(strDate, strTime)
                                        makeServiceCall(MakeAppointmentActivity.this, getString(R.string.insert_booking_url), nextAppID, strDate, strTime, carID, custID);

                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
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
                                    sendEmail();

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

    private void sendEmail() {
        //TODO: get all agentEmail,pass it in String[] with ","
        String recipientList = "nglp-wa15@student.tarc.edu.my,bbaong2012@gmail.com";
        String[] recipients = recipientList.split(",");

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_COMPONENT_NAME, "testing");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Request for make an appointment at Date Time");
        intent.putExtra(Intent.EXTRA_TEXT, "Hi, I would like to make an appointment with you at WHAT DATE WHAT TIME with WHAT CAR.\nHope to receive your email.\nThanks!");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
        if (btnSendRequest.getText().toString().equals(getString(R.string.send_appointment_request))) {
            Toast.makeText(MakeAppointmentActivity.this, "Appointment Added.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MakeAppointmentActivity.this, "Appointment Updated.", Toast.LENGTH_LONG).show();
        }
        finish();
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
        if (!LoginActivity.isConnected(MakeAppointmentActivity.this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MakeAppointmentActivity.this);
            builder.setTitle("Connection Error");
            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

        } else {
            Toast.makeText(MakeAppointmentActivity.this, "Error:  \n" + e.toString(), Toast.LENGTH_LONG).show();
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
                                //if make appointment successful
                                if (success.equals("1")) sendEmail();
                                else Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

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
}
