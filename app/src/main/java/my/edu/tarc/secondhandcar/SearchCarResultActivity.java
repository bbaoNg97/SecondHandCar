package my.edu.tarc.secondhandcar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SearchCarResultActivity extends AppCompatActivity {
private TextView tvColor,tvPurpose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_car_result);


        String color=getIntent().getExtras().getString("Color");
        String purpose=getIntent().getExtras().getString("Purpose");

       tvPurpose=(TextView)findViewById(R.id.tvPurpose);
       tvPurpose.setText(String.valueOf(purpose));
        tvColor=(TextView)findViewById(R.id.tvColor);
        tvColor.setText(String.valueOf(color));
    }


}
