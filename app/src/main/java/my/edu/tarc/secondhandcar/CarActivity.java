package my.edu.tarc.secondhandcar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class CarActivity extends AppCompatActivity {

    private ViewPager viewPager1;
    private Button btnMakeAppointment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        viewPager1=(ViewPager)findViewById(R.id.viewPager1);
        btnMakeAppointment=(Button)findViewById(R.id.buttonAppointment);

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(this);

        setTitle("Car Name");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager1.setAdapter(viewPagerAdapter);
        btnMakeAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bookingIntent=new Intent(CarActivity.this,MakeAppointmentActivity.class);
                startActivity(bookingIntent);
            }
        });


    }
}
