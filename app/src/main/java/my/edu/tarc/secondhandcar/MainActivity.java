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
    private LoanCalcFragment loanCalcFragment;
    private ProfileFragment profileFragment;
    private SavedCarFragment savedCarFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainFrame=(FrameLayout)findViewById(R.id.main_fram);
        mMainNav=(BottomNavigationView)findViewById(R.id.main_nav);

        homeFragment= new HomeFragment();
        loanCalcFragment=new LoanCalcFragment();
        profileFragment= new ProfileFragment();
        savedCarFragment=new SavedCarFragment();


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
                    case R.id.nav_loanCalc:
                        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
                        setFragment(loanCalcFragment);
                        return true;
                    case R.id.nav_favourite:
                        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
                        setFragment(savedCarFragment);
                        return true;
                    case R.id.nav_profile:
                        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
                        setFragment(profileFragment);
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
