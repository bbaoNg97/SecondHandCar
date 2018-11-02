package my.edu.tarc.secondhandcar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class CarActivity extends AppCompatActivity {

    private ImageView imageViewCars;
    private Button btnMakeAppointment;
    private TextView textViewDesc, textViewName, textViewPrice, textViewColor, textViewMileage, textViewYear, textViewLocation;
    private String id, name, img, brand, price, color, desc, year, mile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        imageViewCars = (ImageView) findViewById(R.id.imageViewCars);
        btnMakeAppointment = (Button) findViewById(R.id.buttonAppointment);
        textViewDesc = (TextView) findViewById(R.id.textViewDesc);
        textViewPrice = (TextView) findViewById(R.id.textViewPrice);
        textViewColor = (TextView) findViewById(R.id.textViewColor);
        textViewMileage = (TextView) findViewById(R.id.textViewMileage);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewYear = (TextView) findViewById(R.id.textViewYear);
        textViewLocation = (TextView) findViewById(R.id.textViewLocation);


        Intent intent = getIntent();
        id = intent.getStringExtra("CarID");
        name = intent.getStringExtra("CarName");
        img = intent.getStringExtra("CarImg");
        brand = intent.getStringExtra("CarBrand");
        price = intent.getStringExtra("CarPrice");
        color = intent.getStringExtra("CarColor");
        desc = intent.getStringExtra("CarDesc");
        year = intent.getStringExtra("CarYear");
        mile = intent.getStringExtra("CarMile");

        textViewName.setText(brand +" "+ name);
        textViewColor.setText(color);
        textViewPrice.setText("RM " +price);
        textViewYear.setText(year);
        textViewMileage.setText(mile);
        textViewDesc.setText(desc);

        Glide.with(this).load(img).into(imageViewCars);


        setTitle(R.string.title_car_detail);

        btnMakeAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bookingIntent = new Intent(CarActivity.this, MakeAppointmentActivity.class);
                startActivity(bookingIntent);
            }
        });


    }
}
