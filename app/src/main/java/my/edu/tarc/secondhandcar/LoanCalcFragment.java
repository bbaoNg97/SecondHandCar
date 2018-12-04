package my.edu.tarc.secondhandcar;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


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

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_loan_calc, container, false);
        buttonCal = (Button) v.findViewById(R.id.buttonCal);
        buttonRepayment = (Button) v.findViewById(R.id.buttonRepayment);
        buttonReset = (Button) v.findViewById(R.id.buttonPwRec);
        editTextPrice = (EditText) v.findViewById(R.id.editTextPrice);
        editTextDownpay = (EditText) v.findViewById(R.id.editTextDownpay);
        editTextLoan = (EditText) v.findViewById(R.id.editTextLoan);
        editTextRate = (EditText) v.findViewById(R.id.editTextRate);


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

        editTextRate.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId== EditorInfo.IME_ACTION_GO){
                    buttonClick();
                }
                return false;
            }
        });

        buttonCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick();
            }
        });
        return v;
    }

    public void buttonClick() {
        String downPay = editTextDownpay.getText().toString();
        String Price = editTextPrice.getText().toString();
        String Loan = editTextLoan.getText().toString();
        String Rate = editTextRate.getText().toString();

        if (downPay.matches("") || Price.matches("") || Loan.matches("") || Rate.matches("")) {
            Toast.makeText(getActivity(), "Please fill in all the field to proceed!",
                    Toast.LENGTH_LONG).show();

        } else {
            double price = Double.parseDouble(Price);
            double downpay = Double.parseDouble(downPay);
            int loan = Integer.parseInt(Loan);
            double rate = Double.parseDouble(Rate);

            double actualLoan = price - downpay;
            double totalInterest = rate / 100 * actualLoan * loan;
            double monthlypay = (actualLoan + totalInterest) / (loan * 12);

            String s = String.format("Monthly Repayment: \n RM%.2f", monthlypay);
            buttonRepayment.setText(s);
            buttonRepayment.setVisibility(View.VISIBLE);
            buttonReset.setVisibility(View.VISIBLE);

            hideSoftKeyboard(getActivity());

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
