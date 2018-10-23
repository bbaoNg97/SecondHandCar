package my.edu.tarc.secondhandcar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPw;
    private String email, pw;
    private Button buttonLogin;
    private TextView textViewSignUp, textViewReset;
    final String MY_PREF = "my_pref";
    final String EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPw = (EditText) findViewById(R.id.editTextPw);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);
        textViewReset = (TextView) findViewById(R.id.textViewResetPw);

        SharedPreferences chkuser = getSharedPreferences(MY_PREF, MODE_PRIVATE);
        String chkUsername = chkuser.getString(EMAIL, null);

        textViewReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resetPwIntent = new Intent(LoginActivity.this, ResetPwActivity.class);
                startActivity(resetPwIntent);
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(regIntent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw = editTextPw.getText().toString();
                email = editTextEmail.getText().toString();

                SharedPreferences.Editor user = getSharedPreferences(MY_PREF, MODE_PRIVATE).edit();
                user.putString(EMAIL, email);
                user.apply();
                if (email.matches("abc") && pw.matches("123")) {
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        });

    }
}
