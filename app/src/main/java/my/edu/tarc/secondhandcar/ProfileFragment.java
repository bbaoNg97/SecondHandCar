package my.edu.tarc.secondhandcar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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

import java.util.zip.Inflater;

public class ProfileFragment extends Fragment {
    private TextView tvWelcome, tvAboutUs, tvRateUs, tvShareApp;
    private Button btnLogout;
    private TableLayout tlWelcome;
    private String name, custID;
    SharedPreferences sharePref;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        tvWelcome = (TextView) v.findViewById(R.id.textViewWelcome);
        btnLogout = (Button) v.findViewById(R.id.buttonLogout);
        tlWelcome = (TableLayout) v.findViewById(R.id.tableLayoutWelcome);
        btnLogout = (Button) v.findViewById(R.id.buttonLogout);
        sharePref = getActivity().getSharedPreferences("My_Pref", Context.MODE_PRIVATE);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor user = getActivity().getSharedPreferences("My_Pref", Context.MODE_PRIVATE).edit();
                user.putString("custName", null);
                user.putString("custID", null);
                user.putString("custEmail", null);
                user.putString("password", null);
                user.putString("custContactNo", null);

                user.apply();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                Toast.makeText(getActivity(), "Logout Success", Toast.LENGTH_SHORT).show();
                intent.putExtra("from","Profile Fragment");
                startActivity(intent);
            }
        });

        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //to do layout setting
        boolean isLogin = checkCustomer(tvWelcome, btnLogout, tlWelcome);
        //check if it has been logged in

        if (isLogin) {
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
            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
            loginIntent.putExtra("from","ProfileFragment");
            startActivity(loginIntent);
        }
        //if it is showing my profile icon
        else {
            Intent myProfileIntent = new Intent(getActivity(), ViewProfileActivity.class);
            startActivity(myProfileIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean checkCustomer(TextView textView, Button button, TableLayout tableLayout) {
        try {
            SharedPreferences sharePref = getActivity().getSharedPreferences("My_Pref", Context.MODE_PRIVATE);
            String welcome;
            name = sharePref.getString("custName", null);
            //is has name(means is logged in)
            if (!name.isEmpty()) {
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
            return false;
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        sharePref = getActivity().getSharedPreferences("My_Pref", Context.MODE_PRIVATE);
        //success case
        getActivity().invalidateOptionsMenu();

        custID = sharePref.getString("custID", null);
        //if no customer login
        if (custID == null) {
            btnLogout.setVisibility(View.GONE);
            tvWelcome.setVisibility(View.GONE);
        } else {
            btnLogout.setVisibility(View.VISIBLE);
            tvWelcome.setVisibility(View.VISIBLE);
        }

    }


    private void checkError(Exception e, Context context) {
        if (!LoginActivity.isConnected(context)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Connection Error");
            builder.setIcon(R.drawable.ic_action_info);
            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

        } else {
            Toast.makeText(context, "Error:  \n" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

}
