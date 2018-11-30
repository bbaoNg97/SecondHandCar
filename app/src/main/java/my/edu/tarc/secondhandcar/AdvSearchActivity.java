package my.edu.tarc.secondhandcar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;


public class AdvSearchActivity extends AppCompatActivity {

    private String[] spinnerColorName;
    private int[] spinnerColor;

    NumberFormat formatter = NumberFormat.getCurrencyInstance();

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

    //store the default value
    private int minPrice;
    private int maxPrice;
    private int minMileage;
    private int maxMileage;
    private int minYear;
    private int maxYear;
    private String colorName, spPurpose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv_search);

        //link spinner UI
        mSpinnerPurpose = (Spinner) findViewById(R.id.spinnerPurpose);
        mSpinnerColor = (Spinner) findViewById(R.id.spinnerColor);
        setTitle(R.string.title_recommend_car);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinnerColorName = new String[]{"All", "White", "Black", "Silver", "Red", "Blue", "Brown",
                "Yellow", "Green", "Purple", "Other"};
        spinnerColor = new int[]{R.drawable.color_all, R.drawable.color_white
                , R.drawable.color_black
                , R.drawable.color_silver, R.drawable.color_red
                , R.drawable.color_blue, R.drawable.color_brown
                , R.drawable.color_yellow, R.drawable.color_green
                , R.drawable.color_purple, R.drawable.color_others};


        /*//TODO: will be deleted
        ArrayAdapter<CharSequence> purposeAdapter = ArrayAdapter.createFromResource(this, R.array.purpose, android.R.layout.simple_spinner_item);
        purposeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerPurpose.setAdapter(purposeAdapter);
        //mSpinnerPurpose.setOnItemSelectedListener(this);*/

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
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

                try {
                   minPrice=numberFormat.parse(textViewMinPrice.getText().toString()).intValue();
                } catch (Exception e) {
                    minPrice=0;
                }


                Intent advSearchCarIntent = new Intent(AdvSearchActivity.this, SearchCarResultActivity.class);
                //pass all selected criteria to searchResultAct
                advSearchCarIntent.putExtra(MIN_PRICE, minPrice);
                advSearchCarIntent.putExtra(Max_PRICE, maxPrice);
                advSearchCarIntent.putExtra(MIN_MILEAGE, minMileage);
                advSearchCarIntent.putExtra(Max_MILEAGE, maxMileage);
                advSearchCarIntent.putExtra(MIN_YEAR, minYear);
                advSearchCarIntent.putExtra(Max_YEAR, maxYear);
                //spPurpose = mSpinnerPurpose.getSelectedItem().toString();
                //advSearchCarIntent.putExtra("Purpose", spPurpose);
                advSearchCarIntent.putExtra("Color", colorName);
                advSearchCarIntent.putExtra("from", "recommendCar");


                startActivity(advSearchCarIntent);


            }
        });
    }

    private String getFormatedAmount(int amount) {
        return NumberFormat.getNumberInstance(Locale.ENGLISH).format(amount);
    }

    private int getActualAmount(int amount) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

        try {
            amount = numberFormat.parse(textViewMinPrice.getText().toString()).intValue();
        } catch (Exception e) {
            amount = 0;
        }

        return amount;
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
        //store the default value
        minPrice = 0;
        maxPrice = 5000000;
        minMileage = 0;
        maxMileage = 1000000;
        minYear = 1950;
        maxYear = 2018;
        colorName = "All";
        setSeekBar();
        buttonAdvSearchCar.setEnabled(true);
        AdapterCustomColor mAdapterCustomColor = new AdapterCustomColor(AdvSearchActivity.this, spinnerColorName, spinnerColor);
        mSpinnerColor.setAdapter(mAdapterCustomColor);

        mSpinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(AdvSearchActivity.this, spinnerColorName[i], Toast.LENGTH_SHORT).show();
                colorName = spinnerColorName[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        textViewMinPrice.setText("");
        textViewMinYear.setText("");
        textViewMinMileage.setText("");
        textViewMaxMileage.setText("");
        textViewMaxPrice.setText("");
        textViewMaxYear.setText("");

        seekBarMinPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                //increase by 5000
                i = i / 5000;
                i = i * 5000;
                String price;
                Double dPrice = Double.parseDouble(String.valueOf(i));
                price = formatter.format(dPrice);

                textViewMinPrice.setText(price);
                minPrice = i;


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                buttonAdvSearchCar.setEnabled(false);
                String price;
                Double dPrice = Double.parseDouble(String.valueOf(minPrice));
                price = formatter.format(dPrice);
                seekBarMaxPrice.setEnabled(true);
                seekBarMaxPrice.setMax(MAX_PRICE - minPrice);
                seekBarMaxPrice.setProgress(0);

                textViewMaxPrice.setText(price);
                seekBarMaxPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        i = i + minPrice;
                        i = i / 5000;
                        i = i * 5000;
                        maxPrice = i;
                        String price;
                        Double dPrice = Double.parseDouble(String.valueOf(i));
                        price = formatter.format(dPrice);
                        textViewMaxPrice.setText(price);

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        maxPrice = 5000000;
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        buttonAdvSearchCar.setEnabled(true);

                    }
                });


            }


        });

        seekBarMinMileage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i = i / 5000;
                i = i * 5000;
                String mileage = getFormatedAmount(i);
                String mileMsg = mileage + getString(R.string.km);
                textViewMinMileage.setText(mileMsg);
                minMileage = i;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                buttonAdvSearchCar.setEnabled(false);
                seekBarMaxMileage.setEnabled(true);
                seekBarMaxMileage.setMax(MAX_MILEAGE - minMileage);
                seekBarMaxMileage.setProgress(0);
                String mileage = getFormatedAmount(minMileage);
                String mileMsg = mileage + getString(R.string.km);
                textViewMaxMileage.setText(mileMsg);
                seekBarMaxMileage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        i = i + minMileage;
                        i = i / 5000;
                        i = i * 5000;
                        String mileage = getFormatedAmount(i);
                        String mileMsg = mileage + getString(R.string.km);
                        textViewMaxMileage.setText(mileMsg);
                        maxMileage = i;

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        maxMileage = 1000000;
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        buttonAdvSearchCar.setEnabled(true);
                    }
                });

            }
        });

        seekBarMinYear.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                i = i + 1950;

                textViewMinYear.setText(String.valueOf(i));
                minYear = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                buttonAdvSearchCar.setEnabled(false);
                seekBarMaxYear.setEnabled(true);
                textViewMaxYear.setText(String.valueOf(minYear));
                seekBarMaxYear.setMax(MAX_YEAR - minYear);
                seekBarMaxYear.setProgress(0);
                seekBarMaxYear.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                        i = i + minYear;
                        textViewMaxYear.setText(String.valueOf(i));
                        maxYear = 0;
                        maxYear = maxYear + i;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        maxYear = 2018;
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        buttonAdvSearchCar.setEnabled(true);
                    }
                });


            }
        });
    }

    private void setSeekBar() {
        //set maximum value for price, mileage, and year
        seekBarMinPrice.setProgress(0);
        seekBarMinPrice.setMax(5000000);
        seekBarMinPrice.incrementProgressBy(5000);

        seekBarMinMileage.setProgress(0);
        seekBarMinMileage.setMax(1000000);
        seekBarMinMileage.incrementProgressBy(5000);

        seekBarMinYear.setProgress(0);
        //2018-1950 is 68
        seekBarMinYear.setMax(68);

        //seek bar for max value change to initial point
        seekBarMaxPrice.setProgress(0);
        seekBarMaxMileage.setProgress(0);
        seekBarMaxYear.setProgress(0);

        //seek bar for max value cannot be triggered, must set the minimum value first
        seekBarMaxPrice.setEnabled(false);
        seekBarMaxMileage.setEnabled(false);
        seekBarMaxYear.setEnabled(false);

    }

}
