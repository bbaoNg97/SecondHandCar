package my.edu.tarc.secondhandcar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;


public class AdvSearchActivity extends AppCompatActivity  {

    private String[] spinnerColorName;
    private int[] spinnerColor;



    private Spinner mSpinnerColor, mSpinnerPurpose;
    private SeekBar seekBarMinPrice, seekBarMaxPrice, seekBarMinMileage, seekBarMaxMileage, seekBarMinYear, seekBarMaxYear;
    private TextView textViewMinPrice, textViewMaxPrice, textViewMinMileage, textViewMaxMileage, textViewMinYear, textViewMaxYear;
    private Button buttonAdvSearchCar;

    public static final int MAX_PRICE = 5000000;
    public static final int MAX_MILEAGE = 1000000;
    public static final int MAX_YEAR = 2018;

    public static final String MIN_PRICE = "minimum price";
    public static final String Max_PRICE = "maximum price";
    public static final String MIN_MILEAGE = "minimum mileage";
    public static final String Max_MILEAGE = "maximum mileage";
    public static final String MIN_YEAR = "minimum year";
    public static final String Max_YEAR = "maximum year ";

    //store the last value
    private int minPrice;
    private int maxPrice;
    private int minMileage;
    private int maxMileage;
    private int minYear;
    private int maxYear;
    String colorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv_search);

        //link spinner UI
        mSpinnerColor = (Spinner) findViewById(R.id.spinnerColor);
        mSpinnerPurpose = (Spinner) findViewById(R.id.spinnerPurpose);

        setTitle(R.string.title_adv_search);


        spinnerColorName = new String[]{"All", "White", "Black", "Silver", "Red", "Blue", "Brown",
                "Yellow", "Green", "Purple", "Others"};
        spinnerColor = new int[]{R.drawable.color_all, R.drawable.color_white
                , R.drawable.color_black
                , R.drawable.color_silver, R.drawable.color_red
                , R.drawable.color_blue, R.drawable.color_brown
                , R.drawable.color_yellow, R.drawable.color_green
                , R.drawable.color_purple, R.drawable.color_others};


        CustomColorAdapter mCustomColorAdapter = new CustomColorAdapter(AdvSearchActivity.this, spinnerColorName, spinnerColor);
        mSpinnerColor.setAdapter(mCustomColorAdapter);


        mSpinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(AdvSearchActivity.this, spinnerColorName[i], Toast.LENGTH_SHORT).show();
                colorName=spinnerColorName[i].toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> purposeAdapter = ArrayAdapter.createFromResource(this, R.array.purpose, android.R.layout.simple_spinner_item);
        purposeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerPurpose.setAdapter(purposeAdapter);
        //mSpinnerPurpose.setOnItemSelectedListener(this);

        //link UI
        seekBarMinPrice = (SeekBar) findViewById(R.id.seekBarMinPrice);
        seekBarMaxPrice = (SeekBar) findViewById(R.id.seekBarMaxPrice);
        seekBarMinYear = (SeekBar) findViewById(R.id.seekBarMinYear);
        seekBarMaxYear = (SeekBar) findViewById(R.id.seekBarMaxYear);
        seekBarMinMileage = (SeekBar) findViewById(R.id.seekBarMinMileage);
        seekBarMaxMileage = (SeekBar) findViewById(R.id.seekBarMaxMileage);

        textViewMinPrice = (TextView) findViewById(R.id.textViewMinPrice);
        textViewMaxPrice = (TextView) findViewById(R.id.textViewMaxPrice);
        textViewMinMileage = (TextView) findViewById(R.id.textViewMinMileage);
        textViewMaxMileage = (TextView) findViewById(R.id.textViewMaxMileage);
        textViewMinYear = (TextView) findViewById(R.id.textViewMinYear);
        textViewMaxYear = (TextView) findViewById(R.id.textViewMaxYear);

        buttonAdvSearchCar = (Button) findViewById(R.id.buttonAdvSearchCar);

        buttonAdvSearchCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent advSearchCarIntent = new Intent(AdvSearchActivity.this, SearchCarResultActivity.class);

                advSearchCarIntent.putExtra(MIN_PRICE, minPrice);
                advSearchCarIntent.putExtra(Max_PRICE, maxPrice);
                advSearchCarIntent.putExtra(MIN_MILEAGE, minMileage);
                advSearchCarIntent.putExtra(Max_MILEAGE, maxMileage);
                advSearchCarIntent.putExtra(MIN_YEAR, minYear);
                advSearchCarIntent.putExtra(Max_YEAR, maxYear);
                final String spPurpose= mSpinnerPurpose.getSelectedItem().toString();
                advSearchCarIntent.putExtra("Purpose",spPurpose.toString());
                advSearchCarIntent.putExtra("Color",colorName.toString());


                startActivity(advSearchCarIntent);


            }
        });


        //set maximum value for price, mileage, and year
        seekBarMinPrice.setProgress(0);
        seekBarMinPrice.setMax(4995000);
        seekBarMinPrice.incrementProgressBy(5000);

        seekBarMinMileage.setProgress(0);
        seekBarMinMileage.setMax(995000);
        seekBarMinMileage.incrementProgressBy(5000);

        seekBarMinYear.setProgress(0);
        //2018-1950 is 68
        seekBarMinYear.setMax(68);


        //seek bar for max value cannot be triggered, must set the minimum value first
        seekBarMaxPrice.setEnabled(false);
        seekBarMaxMileage.setEnabled(false);
        seekBarMaxYear.setEnabled(false);

        seekBarMinPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {


                //start from 5000
                i = i + 5000;
                //increase by 5000
                i = i / 5000;
                i = i * 5000;
                textViewMinPrice.setText("Min Price: RM " + String.valueOf(i));
                minPrice = i;


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarMaxPrice.setEnabled(true);
                seekBarMaxPrice.setMax(MAX_PRICE - minPrice);
                seekBarMaxPrice.setProgress(0);
                textViewMaxPrice.setText("Max Price: RM " + String.valueOf(minPrice));
                seekBarMaxPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        i = i + minPrice;
                        i = i / 5000;
                        i = i * 5000;
                        maxPrice = i;
                        textViewMaxPrice.setText("Max Price: RM " + String.valueOf(maxPrice));

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {


                    }
                });


            }


        });

        seekBarMinMileage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i = i + 5000;
                i = i / 5000;
                i = i * 5000;
                textViewMinMileage.setText("Min Mileage: " + String.valueOf(i) + " km");
                minMileage = i;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarMaxMileage.setEnabled(true);
                seekBarMaxMileage.setMax(MAX_MILEAGE - minMileage);
                seekBarMaxMileage.setProgress(0);
                textViewMaxMileage.setText("Max Mileage: " + String.valueOf(minMileage) + " km");
                seekBarMaxMileage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        i = i + minMileage;
                        i = i / 5000;
                        i = i * 5000;
                        textViewMaxMileage.setText("Max Mileage: " + String.valueOf(i) + " km");
                        maxMileage = i;

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

            }
        });

        seekBarMinYear.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                i = i + 1950;
                textViewMinYear.setText("Min Year: " + String.valueOf(i));
                minYear = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarMaxYear.setEnabled(true);
                textViewMaxYear.setText("Max Year: " + String.valueOf(minYear));
                seekBarMaxYear.setMax(MAX_YEAR - minYear);
                seekBarMaxYear.setProgress(0);
                seekBarMaxYear.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                        i = i + minYear;
                        textViewMaxYear.setText("Max Year: " + String.valueOf(i));
                        maxYear = maxYear + i;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });


            }
        });

    }


}
