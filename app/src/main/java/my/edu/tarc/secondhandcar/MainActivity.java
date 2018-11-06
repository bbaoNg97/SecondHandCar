package my.edu.tarc.secondhandcar;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;

    private HomeFragment homeFragment;
    private LoanCalcFragment loanCalcFragment;
    private ProfileFragment profileFragment;
    private MyBookingFragment myBookingFragment;

    private String name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharePref = this.getSharedPreferences("My_Pref", Context.MODE_PRIVATE);
        name = sharePref.getString("custName", null);

        mMainFrame=(FrameLayout)findViewById(R.id.main_fram);
        mMainNav=(BottomNavigationView)findViewById(R.id.main_nav);

        homeFragment= new HomeFragment();
        loanCalcFragment=new LoanCalcFragment();
        profileFragment= new ProfileFragment();
        myBookingFragment=new MyBookingFragment();


        setFragment(homeFragment);
        setTitle(R.string.title_home);

        if(!isConnected()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Connection Error");
            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();
        }

        mMainNav.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

            }
        });
        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        setTitle(R.string.title_home);
                        setFragment(homeFragment);
                        return true;

                    case R.id.nav_loanCalc:
                        setTitle(R.string.title_loanCalc);
                        setFragment(loanCalcFragment);
                        return true;

                    case R.id.nav_appointment:
                        setTitle(R.string.title_appointment);
                        setFragment( myBookingFragment);
                        return true;

                    case R.id.nav_profile:
                        setTitle(R.string.title_profile);
                        setFragment(profileFragment);
                        return true;

                    default:
                        return false;


                }
            }
        });
    }
    //to check network connectivity
    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

    private void setFragment(Fragment fragement) {
        FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_fram,fragement);
        fragmentTransaction.commit();
    }
    private void switchFrag(MenuItem item){


    }
}
