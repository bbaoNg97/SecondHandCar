package my.edu.tarc.secondhandcar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MakeAppointmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_appointment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.title_chooseDateTime);
    }
}
