package my.edu.tarc.secondhandcar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoanCalcFragment extends Fragment {
    private Button buttonCal;
    private Button buttonRepayment;
    private Button buttonReset;

    private EditText editTextPrice;
    private EditText editTextDownpay;
    private EditText editTextLoan;
    private EditText editTextRate;



    public LoanCalcFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_loan_calc, container, false);
        buttonCal= (Button)v.findViewById(R.id.buttonCal);
        buttonRepayment= (Button)v.findViewById(R.id.buttonRepayment);
        buttonReset= (Button)v.findViewById(R.id.buttonReset);
        editTextPrice=(EditText)v.findViewById(R.id.editTextPrice);
        editTextDownpay=(EditText)v.findViewById(R.id.editTextDownpay);
        editTextLoan=(EditText)v.findViewById(R.id.editTextLoan);
        editTextRate=(EditText)v.findViewById(R.id.editTextRate);


buttonReset.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        editTextPrice.setText("");
        editTextDownpay.setText("");
        editTextLoan.setText("");
        editTextRate.setText("");
        buttonReset.setVisibility(View.INVISIBLE);
        buttonRepayment.setVisibility(View.INVISIBLE);
        editTextPrice.requestFocus();
    }
});


        buttonCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double price=Double.parseDouble(editTextPrice.getText().toString());
                double downpay=Double.parseDouble(editTextDownpay.getText().toString());
                int loan=Integer.parseInt(editTextLoan.getText().toString());
                double rate=Double.parseDouble(editTextRate.getText().toString());

                double actualLoan=price-downpay;
                double totalInterest= rate/100*actualLoan*loan;
                double monthlypay= (actualLoan+totalInterest)/(loan*12);

                String s=String.format("Monthly Repayment: \n RM%.2f",monthlypay);
                buttonRepayment.setText(s);
                buttonRepayment.setVisibility(View.VISIBLE);
                buttonReset.setVisibility(View.VISIBLE);

            }
        });
        return v;
    }

}
