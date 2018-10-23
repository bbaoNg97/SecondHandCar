package my.edu.tarc.secondhandcar;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class EditProfActivity extends AppCompatActivity {

    private TextView tvCustEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_prof);
        setTitle(R.string.title_edit_prof);

        SharedPreferences sharePref = getSharedPreferences("my_pref", Context.MODE_PRIVATE);
        String email = sharePref.getString("email", null);

        tvCustEmail=(TextView)findViewById(R.id.textViewCustEmail);
        tvCustEmail.setText(email.toString());
    }
}
