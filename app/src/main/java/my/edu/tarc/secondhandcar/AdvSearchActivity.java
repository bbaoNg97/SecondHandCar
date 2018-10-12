package my.edu.tarc.secondhandcar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.florescu.android.rangeseekbar.RangeSeekBar;

public class AdvSearchActivity extends AppCompatActivity {
private RangeSeekBar rangeSeekBarPriceRange,rangeSeekBarMileAge,rangeSeekBarYear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv_search);

        rangeSeekBarPriceRange=(RangeSeekBar)findViewById(R.id.rangeSeekBarPriceRange);
        rangeSeekBarMileAge=(RangeSeekBar)findViewById(R.id.rangeSeekBarMileage);
        rangeSeekBarYear=(RangeSeekBar)findViewById(R.id.rangeSeekBarYear);

        rangeSeekBarPriceRange.setRangeValues(1000,9999999);
        rangeSeekBarMileAge.setRangeValues(0,9999999);
        rangeSeekBarYear.setRangeValues(1950,2018);
    }
}
