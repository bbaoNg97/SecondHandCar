package my.edu.tarc.secondhandcar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
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

    private TextView textViewDate, textViewTime;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private Button btnSendRequest;
    private ProgressBar booking;
    private String time, custID;
    private Date selectedTime;
    SharedPreferences sharePref;
    private String appID, carID;
    RequestQueue requestQueue;
    List<String> bookingList;
    private int totalBooking;
    public static final String TAG = "my.edu.tarc.secondhandcar";
    String nextAppID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_appointment);

        bookingList = new ArrayList<>();
        setTitle(R.string.title_chooseDateTime);
        if (!LoginActivity.isConnected(MakeAppointmentActivity.this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MakeAppointmentActivity.this);
            builder.setTitle("Connection Error");
            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();
        }

        //get share preference
        sharePref = getSharedPreferences("My_Pref", Context.MODE_PRIVATE);
        custID = sharePref.getString("custID", null);

        btnSendRequest = (Button) findViewById(R.id.buttonSendAppReq);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        booking=(ProgressBar)findViewById(R.id.booking);
        proceed();
        getAllAppointment(MakeAppointmentActivity.this,getString(R.string.get_appointment_url));
        textViewDate.setOnClickListener(new View.OnClickListener() {
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

        textViewTime.setOnClickListener(new View.OnClickListener() {
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
                textViewDate.setText(date);
            }
        };


        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                //'sh' indicates showing purpose
                String shTime;
                SimpleDateFormat shFormatter = new SimpleDateFormat("hh:mm a");

                time = hour + ":" + minute;

                if (hour >= 12) {
                    hour = hour - 12;
                    shTime = hour + ":" + minute + " PM";
                } else {

                    shTime = hour + ":" + minute + " AM";
                }

                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

                ParsePosition pos = new ParsePosition(0);
                selectedTime = formatter.parse(time, pos);

                //convert time to Date object, and format to String object
                ParsePosition pos1 = new ParsePosition(0);
                Date shSelectedTime = shFormatter.parse(shTime, pos1);
                shTime = shFormatter.format(shSelectedTime);
                textViewTime.setText(shTime);
            }
        };

        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    final String strDate = textViewDate.getText().toString();
                    final String strTime = textViewTime.getText().toString();

                    //date and time can not be null
                    if (strDate.equals("") || strTime.equals("")) {
                        Toast.makeText(getApplicationContext(), "Error: Please select date and time.", Toast.LENGTH_LONG).show();
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

                        //get the hour from selected time, must between 9am-5pm
                        cal.setTime(selectedTime);
                        int hour = cal.get(Calendar.HOUR_OF_DAY);


                        //check if selected date is before currentDate
                        if (selectedDate.before(currentDate)) {
                            Toast.makeText(getApplicationContext(), "Error: Date cannot earlier than today.", Toast.LENGTH_LONG).show();
                        }
                        //check if selected date is after one month
                        else if (selectedDate.after(validDate)) {
                            Toast.makeText(getApplicationContext(), "Error: Only can make booking within one month.", Toast.LENGTH_LONG).show();
                        }
                        //check if booking time is between 9-5pm
                        else if (hour < 9 || hour >= 17) {
                            Toast.makeText(getApplicationContext(), "Error: Only can book time in between 9am-5pm", Toast.LENGTH_LONG).show();
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
                            AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(MakeAppointmentActivity.this);
                            confirmBuilder.setTitle("Request Confirmation");
                            confirmBuilder.setMessage("Confirm to send request?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {


                                    booking.setVisibility(View.VISIBLE);
                                    textViewDate.setEnabled(false);
                                    textViewTime.setEnabled(false);
                                    btnSendRequest.setEnabled(false);

                                    //Todo: store date, time, appID,custID,carID,status(pending by default)(no need) ,agentID(no need)
                                    //custID is get,date time also got(strDate, strTime)
                                    //Todo: get carID(using getString after done choosing car that part)

                                    makeServiceCall(MakeAppointmentActivity.this,getString(R.string.insert_booking_url),nextAppID,strDate,strTime,"C0001",custID);



                                    //Todo:send notification to seller app
                                    //Intent intent = new Intent();
                                    //intent.setAction("my.edu.tarc.secondhandcar.MY_NOTIFICATION");
                                    //intent.putExtra("data", "Hello world");
                                    //sendBroadcast(intent);


                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.cancel();

                                }
                            }).create().show();

                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    private void makeServiceCall(final Context context, String url, final String appID, final String appDate, final String appTime, final String carID, final String custID ) {
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
                                //if register successful
                                if (success.equals("1")) {
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                    //loading.setVisibility(View.GONE);
                                    //btnSignUp.setEnabled(true);
                                    //if success, go to login page
                                    Intent mainIntent = new Intent(MakeAppointmentActivity.this, MakeAppointmentActivity.class);
                                    startActivity(mainIntent);
                                }

                            } catch (JSONException e) {

                                checkError(e, MakeAppointmentActivity.this);
                                proceed();

                            }

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
                                nextAppID=generateAppID(totalBooking);

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
            Toast.makeText(MakeAppointmentActivity.this, "Register failed! \n" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    private String generateAppID(int countAppointment) {
        String id = "A";

        String count = Integer.toString(countAppointment + 1);
        //if total appointment is 0-9
        if (countAppointment < 10) {
            id = id + "000" + count;
        }
        //if total appointment is 10-99
        else if (countAppointment >= 10 && countAppointment < 100) {
            id = id + "00" + count;
        }
        //if total appointment is 100-999
        else if (countAppointment >= 100 && countAppointment < 1000) {
            id = id + "0" + count;
        }
        //if total appointment is more than 1000
        else {
            id = id + count;
        }

        return id;
    }

    private void proceed(){
        booking.setVisibility(View.GONE);
        textViewDate.setEnabled(true);
        textViewTime.setEnabled(true);
        btnSendRequest.setEnabled(true);
    }
}
