package my.edu.tarc.secondhandcar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewProfileActivity extends AppCompatActivity {

    private TextView textViewEditProf, textViewCustEmail, textViewCustID, textViewCustContactNo, textViewCustName;
    private Button buttonChangePw;
    private String  id,email,name,contactNo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.view_action_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        textViewCustID = (TextView) findViewById(R.id.textViewCustID);
        textViewCustName = (TextView) findViewById(R.id.textViewCustName);
        textViewCustContactNo = (TextView) findViewById(R.id.textViewCustContNo);
        textViewCustEmail = (TextView) findViewById(R.id.textViewCustEmail);
        buttonChangePw = (Button) findViewById(R.id.buttonChangePw);
        //in the bracket, the first(attribute), the second is (id), the third constant (getPackageName())
        textViewEditProf = (TextView) findViewById(getResources().getIdentifier("textViewEditProf", "id", getPackageName()));
        textViewEditProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editProfIntent = new Intent(ViewProfileActivity.this, EditProfActivity.class);
                startActivity(editProfIntent);
            }
        });

        buttonChangePw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changPwIntent = new Intent(ViewProfileActivity.this, ChangePasswordActivity.class);
                startActivity(changPwIntent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            //get share preference after login
            SharedPreferences sharePref = getSharedPreferences("My_Pref", Context.MODE_PRIVATE);

            id = sharePref.getString("custID", null);
            email = sharePref.getString("custEmail", null);
            name = sharePref.getString("custName", null);
            contactNo = sharePref.getString("custContactNo", null);

            textViewCustID.setText(id);
            textViewCustContactNo.setText(contactNo);
            textViewCustEmail.setText(email);
            textViewCustName.setText(name);


        } catch (Exception e) {
            checkError(e,ViewProfileActivity.this);
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
