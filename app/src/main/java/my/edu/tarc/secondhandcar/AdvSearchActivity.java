package my.edu.tarc.secondhandcar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;


public class AdvSearchActivity extends AppCompatActivity {

    private String[] spinnerColorName;
    private int[] spinnerColor;

    private Spinner mSpinnerColor;



//private RangeSeekBar rangeSeekBarPriceRange,rangeSeekBarMileAge,rangeSeekBarYear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv_search);
        mSpinnerColor=(Spinner)findViewById(R.id.spinnerColor);

        spinnerColorName=new String[]{"All","White","Black","Silver","Red","Blue","Brown",
                "Yellow","Green","Purple","Others"};
        spinnerColor= new int[]{R.drawable.color_all,R.drawable.color_white
                ,R.drawable.color_black
                ,R.drawable.color_silver,R.drawable.color_red
                ,R.drawable.color_blue,R.drawable.color_brown
                ,R.drawable.color_yellow,R.drawable.color_green
                ,R.drawable.color_purple,R.drawable.color_others};


        CustomColorAdapter mCustomColorAdapter= new CustomColorAdapter(AdvSearchActivity.this,spinnerColorName,spinnerColor);
        mSpinnerColor.setAdapter(mCustomColorAdapter);

        //rangeSeekBarPriceRange=(RangeSeekBar)findViewById(R.id.rangeSeekBarPriceRange);
        //rangeSeekBarMileAge=(RangeSeekBar)findViewById(R.id.rangeSeekBarMileage);
        //rangeSeekBarYear=(RangeSeekBar)findViewById(R.id.rangeSeekBarYear);

        //rangeSeekBarPriceRange.setRangeValues(1000,9999999);
        //rangeSeekBarMileAge.setRangeValues(0,9999999);
        //rangeSeekBarYear.setRangeValues(1950,2018);
    }
}
