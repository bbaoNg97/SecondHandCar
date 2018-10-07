package my.edu.tarc.secondhandcar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPw;
    private Button buttonLogin;
    private TextView textViewSignUp,textViewReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail=(EditText)findViewById(R.id.editTextEmail);
        editTextPw=(EditText)findViewById(R.id.editTextPw);
        buttonLogin=(Button)findViewById(R.id.buttonLogin);
        textViewSignUp=(TextView)findViewById(R.id.textViewSignUp);
        textViewReset=(TextView)findViewById(R.id.textViewResetPw);

        textViewReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resetPwIntent=new Intent(LoginActivity.this, ResetPwActivity.class);
                startActivity(resetPwIntent);
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regIntent=new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(regIntent);
            }
        });

    }
}
