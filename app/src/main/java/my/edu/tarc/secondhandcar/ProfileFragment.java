package my.edu.tarc.secondhandcar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends Fragment {
    private TextView tvWelcome, tvAboutUs, tvRateUs, tvShareApp;
    private Button btnLogout;
    private TableLayout tlWelcome;
    private String name;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        btnLogout = (Button)v.findViewById(R.id.buttonLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor user=getActivity().getSharedPreferences("my_pref",Context.MODE_PRIVATE).edit();
                user.putString("email",null);
                user.apply();

                getActivity().finish();
            }
        });
        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        tvWelcome = (TextView) getView().findViewById(R.id.textViewWelcome);
        btnLogout = (Button) getView().findViewById(R.id.buttonLogout);
        tlWelcome = (TableLayout) getView().findViewById(R.id.tableLayoutWelcome);

        //to do layout setting
        boolean checkName = checkSharePref(tvWelcome, btnLogout, tlWelcome);

        //check if it has been logged in

        if (checkName == true) {
            inflater.inflate(R.menu.action_bar_welcome, menu);
        }
        //if havent login yet
        else {
            inflater.inflate(R.menu.action_bar_login, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //if it is showing login icon
        if (id == R.id.action_Login) {
            Intent loginIntent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
            startActivity(loginIntent);
        }
        //if it is showing my profile icon
        else {
            Intent myProfileIntent = new Intent(getActivity().getApplicationContext(), ViewProfileActivity.class);
            //myProfileIntent.putExtra("email",name);
            startActivity(myProfileIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean checkSharePref(TextView textView, Button button, TableLayout tableLayout) {
        try {
            SharedPreferences sharePref = getActivity().getSharedPreferences("my_pref", Context.MODE_PRIVATE);
            String welcome;
            name = sharePref.getString("email", null);
            if (name.equals("abc")) {
                button.setVisibility(View.VISIBLE);
                tableLayout.setVisibility(View.VISIBLE);
                welcome = "Welcome! " + name;
                textView.setText(welcome);
                return true;
            } else {
                button.setVisibility(View.INVISIBLE);
                tableLayout.setVisibility(View.INVISIBLE);
                return false;
            }
        } catch (Exception e) {
            //Toast.makeText(getActivity().getApplicationContext(), "Please login first", Toast.LENGTH_LONG).show();
            return false;
        }

    }


}
