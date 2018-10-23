package my.edu.tarc.secondhandcar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewProfileActivity extends AppCompatActivity {

    TextView textViewEditProf,textViewCustEmail;
    Button buttonChangePw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        SharedPreferences sharePref = getSharedPreferences("my_pref", Context.MODE_PRIVATE);
        String email = sharePref.getString("email", null);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.view_action_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewCustEmail=(TextView)findViewById(R.id.textViewCustEmail);
        textViewCustEmail.setText(email.toString());

        buttonChangePw=(Button)findViewById(R.id.buttonChangePw);
        //in the bracket, the first(attribute), the second is (id), the third constant (getPackageName())
        textViewEditProf = (TextView)findViewById(getResources().getIdentifier("textViewEditProf","id",getPackageName()));
        textViewEditProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editProfIntent=new Intent(ViewProfileActivity.this,EditProfActivity.class);
                startActivity(editProfIntent);
            }
        });

        buttonChangePw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changPwIntent= new Intent(ViewProfileActivity.this,ChangePasswordActivity.class);
                startActivity(changPwIntent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
