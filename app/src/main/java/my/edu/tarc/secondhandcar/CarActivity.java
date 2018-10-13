package my.edu.tarc.secondhandcar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class CarActivity extends Activity {

    private ViewPager viewPager1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        viewPager1=(ViewPager)findViewById(R.id.viewPager1);

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(this);

        viewPager1.setAdapter(viewPagerAdapter);

    }
}
