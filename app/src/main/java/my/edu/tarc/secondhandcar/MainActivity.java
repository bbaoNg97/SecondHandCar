package my.edu.tarc.secondhandcar;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;

    private HomeFragment homeFragment;
    private BookingFragment bookingFragment;
    private ProfileFragment profileFragment;
    private SearchFragment searchFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainFrame=(FrameLayout)findViewById(R.id.main_fram);
        mMainNav=(BottomNavigationView)findViewById(R.id.main_nav);

        homeFragment= new HomeFragment();
        bookingFragment = new BookingFragment();
        profileFragment= new ProfileFragment();
        searchFragment= new SearchFragment();
        setFragment(homeFragment);


        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:

                        //to show the home title
                        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
                        setFragment(homeFragment);
                        return true;
                    case R.id.nav_booking:
                        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
                        setFragment(bookingFragment);
                        return true;
                    case R.id.nav_profile:
                        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
                        setFragment(profileFragment);
                        return true;
                    case R.id.nav_search:
                        //to show the custom title
                        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                        getSupportActionBar().setCustomView(R.layout.search_action_bar);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                        setFragment(searchFragment);
                        return true;
                    default:
                        return false;


                }
            }
        });
    }

    private void setFragment(Fragment fragement) {
        FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_fram,fragement);
        fragmentTransaction.commit();
    }
}
