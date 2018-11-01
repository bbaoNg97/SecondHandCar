package my.edu.tarc.secondhandcar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class BookingDetailActivity extends AppCompatActivity {
    private Button btnBackMyBooking;
    private TextView tvCarName,tvAppDate,tvAppTime,tvPrice;
    private String carName,appDate,appTime,price,carPhoto;
    private ImageView ivCarPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
        setTitle(R.string.title_booking_detail);


        tvCarName=(TextView)findViewById(R.id.textViewCarName);
        tvAppDate=(TextView)findViewById(R.id.textViewAppDate);
        tvAppTime=(TextView)findViewById(R.id.textViewAppTime);
        tvPrice=(TextView)findViewById(R.id.textViewPrice);
        ivCarPhoto=(ImageView)findViewById(R.id.imageViewCarPhoto);
        btnBackMyBooking = (Button) findViewById(R.id.buttonBackMyBooking);

        Intent intent=getIntent();
        carName=intent.getStringExtra("CarName");
        appDate=intent.getStringExtra("appDate");
        appTime=intent.getStringExtra("appTime");
        price=intent.getStringExtra("price");
        carPhoto=intent.getStringExtra("carPhoto");

        tvCarName.setText(carName.toString());
        tvAppDate.setText(appDate.toString());
        tvAppTime.setText(appTime.toString());
        tvPrice.setText("RM "+ price.toString());
        Glide.with(getApplicationContext()).asBitmap().load(carPhoto).into(ivCarPhoto);

        btnBackMyBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
