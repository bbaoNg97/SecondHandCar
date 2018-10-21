package my.edu.tarc.secondhandcar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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

import org.w3c.dom.Text;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MakeAppointmentActivity extends AppCompatActivity {

    private TextView textViewDate,textViewTime;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private Button btnSendRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_appointment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.title_chooseDateTime);

        btnSendRequest=(Button)findViewById(R.id.buttonSendAppReq);
        textViewDate=(TextView) findViewById(R.id.textViewDate);
        textViewTime=(TextView)findViewById(R.id.textViewTime);
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
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        textViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hour=cal.get(Calendar.HOUR);
                int minute=cal.get(Calendar.MINUTE);

                TimePickerDialog dialog=new TimePickerDialog(
                        MakeAppointmentActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mTimeSetListener,hour,minute,false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                // Log.d(RegisterActivity.TAG, "onDateSet: yyyy/mm/dd: " + month + "/" + day + "/" + year);
                String date = day + "/" + month +"/"+year;
                textViewDate.setText(date);
            }
        };

        mTimeSetListener=new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                String time;

                SimpleDateFormat formatter=new SimpleDateFormat("hh:mm a");
                if(hour>=12){
                    hour=hour-12;
                    time = hour +":"+minute+" PM";
                }
                else{

                    time = hour +":"+minute+" AM";
                }

                ParsePosition pos = new ParsePosition(0);
                Date date=formatter.parse(time,pos);
                //Todo:store the time into database(the below one)
                time=formatter.format(date);
                textViewTime.setText(time);
            }
        };

        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo: date can not < today(MUST >=today),time must be 9-5
                if(textViewDate.getText().equals("")|| textViewTime.getText().equals("")){
                    Toast.makeText(getApplicationContext(), "Error: Please select date and time.", Toast.LENGTH_LONG).show();
                }
                else{
                    AlertDialog.Builder confirmBuilder=new AlertDialog.Builder(MakeAppointmentActivity.this);
                    confirmBuilder.setTitle("Request Confirmation");
                    final AlertDialog dialog=confirmBuilder.setMessage("Confirm to send request?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Todo:send notification to seller app

                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.cancel();

                        }
                    }).create();
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialogInterface) {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorBlack));
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorBlack));
                        }
                    });
                    dialog.show();
                }
            }
        });
    }
}
