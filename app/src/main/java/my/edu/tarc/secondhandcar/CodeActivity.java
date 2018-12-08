package my.edu.tarc.secondhandcar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class CodeActivity extends AppCompatActivity {
    private ProgressBar checkingCode;
    private Button btnConfCode;
    private SharedPreferences sharePref;
    private String code, intentCode;
    private EditText etPwCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        setTitle(getString(R.string.title_reset_pw));
        checkingCode = (ProgressBar) findViewById(R.id.checkingCode);
        btnConfCode = (Button) findViewById(R.id.buttonConfCode);
        etPwCode = (EditText) findViewById(R.id.etCode);
        checkingCode.setVisibility(View.GONE);

        Intent intent = getIntent();
        intentCode = intent.getStringExtra("code");
    }

    public void OnConfCode(View view) {

        code = etPwCode.getText().toString();
        if(code.equals("")){
            etPwCode.setError("Please enter code");
        }
        else if (code.equals(intentCode)) {
           Intent intent=new Intent(CodeActivity.this,PwRecoveryActivity.class);
            startActivity(intent);
        }
        else{
            AlertDialog.Builder builder=new AlertDialog.Builder(CodeActivity.this);
            builder.setTitle("Incorrect code");
            builder.setMessage("Incorrect code, please enter again").setNegativeButton("Retry",null);
            builder.create().show();
        }
    }
}
