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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MakeAppointmentActivity extends AppCompatActivity {

    private TextView textViewDate, textViewTime;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private Button btnSendRequest;
    private String time, custID;
    private Date selectedTime;
    SharedPreferences sharePref;
    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_appointment);

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

        btnSendRequest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                    try {

                        String strDate = textViewDate.getText().toString();
                        String strTime = textViewTime.getText().toString();

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
                            else if (custID==null) {
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
                                        //Todo:store the time into database(the below one)
                                        //time = formatter.format(selectedTime);
                                        //Todo:send notification to seller app
                                        Intent intent=new Intent();
                                        intent.setAction("my.edu.tarc.secondhandcar.MY_NOTIFICATION");
                                        intent.putExtra("data","Hello world");
                                        sendBroadcast(intent);



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
}
