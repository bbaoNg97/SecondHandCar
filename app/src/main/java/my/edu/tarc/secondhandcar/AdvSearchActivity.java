package my.edu.tarc.secondhandcar;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import java.util.concurrent.ExecutionException;


public class AdvSearchActivity extends AppCompatActivity {

    private String[] spinnerColorName;
    private int[] spinnerColor;

    NumberFormat formatter = NumberFormat.getCurrencyInstance();

    private Spinner mSpinnerColor, mSpinnerPurpose;
    private SeekBar seekBarMinPrice, seekBarMaxPrice, seekBarMinMileage, seekBarMaxMileage, seekBarMinYear, seekBarMaxYear;
    private TextView textViewMinPrice, textViewMaxPrice, textViewMinMileage, textViewMaxMileage, textViewMinYear, textViewMaxYear;
    private Button buttonAdvSearchCar, buttonResetRec;

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
    private int minPrice = 0;
    private int maxPrice = MAX_PRICE;
    private int minMileage = 0;
    private int maxMileage = MAX_MILEAGE;
    private int minYear = 1950;
    private int maxYear = MAX_YEAR;
    private String colorName = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv_search);

        //link spinner UI
        mSpinnerPurpose = (Spinner) findViewById(R.id.spinnerPurpose);
        mSpinnerColor = (Spinner) findViewById(R.id.spinnerColor);
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
        buttonResetRec = (Button) findViewById(R.id.buttonResetRec);

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

        setSeekBar();
        buttonAdvSearchCar.setEnabled(true);
        //spinner color is custom spinner, so need adapterCustom
        AdapterCustomColor mAdapterCustomColor = new AdapterCustomColor(AdvSearchActivity.this, spinnerColorName, spinnerColor);
        mSpinnerColor.setAdapter(mAdapterCustomColor);

        /*//TODO: will be deleted
        ArrayAdapter<CharSequence> purposeAdapter = ArrayAdapter.createFromResource(this, R.array.purpose, android.R.layout.simple_spinner_item);
        purposeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerPurpose.setAdapter(purposeAdapter);
        //mSpinnerPurpose.setOnItemSelectedListener(this);*/

        mSpinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                colorName = spinnerColorName[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        seekBarMinPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                if (fromUser) {
                    i = rangeSpinner(i);
                    textViewMinPrice.setText(getFormattedValue(String.valueOf(i)));

                    //minPrice use for displaying initial max Price purpose
                    minPrice = i;
                } else {
                    textViewMinPrice.setText("");
                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                maxPrice = 5000000;
                //let the min price become starting value of max price
                textViewMaxPrice.setText(getFormattedValue(String.valueOf(minPrice)));

                seekBarMaxPrice.setEnabled(true);
                seekBarMaxPrice.setProgress(0);
                seekBarMaxPrice.setMax(MAX_PRICE - minPrice);

                seekBarMaxPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                        if (fromUser) {
                            //if drag back to initial point, the default value should be minPrice
                            i = i + minPrice;

                            i = rangeSpinner(i);

                            textViewMaxPrice.setText(getFormattedValue(String.valueOf(i)));
                            maxPrice = i;
                        } else
                            textViewMaxPrice.setText("");

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        //if user does not drag seekBarMaxPrice, the maxPrice should be 5000000
                        maxPrice = 5000000;
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });


            }


        });

        seekBarMinMileage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                if (fromUser) {
                    i = rangeSpinner(i);

                    textViewMinMileage.setText(getFormatedAmount(i));
                    minMileage = i;
                } else textViewMinMileage.setText("");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                seekBarMaxMileage.setEnabled(true);
                seekBarMaxMileage.setMax(MAX_MILEAGE - minMileage);
                seekBarMaxMileage.setProgress(0);

                textViewMaxMileage.setText(getFormatedAmount(minMileage));
                seekBarMaxMileage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                        if (fromUser) {
                            i = i + minMileage;
                            i = rangeSpinner(i);

                            textViewMaxMileage.setText(getFormatedAmount(i));
                            maxMileage = i;
                        } else textViewMaxMileage.setText("");

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        maxMileage = 1000000;
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

            }
        });

        seekBarMinYear.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                if (fromUser) {
                    i = i + 1950;

                    textViewMinYear.setText(String.valueOf(i));
                    minYear = i;
                } else textViewMinYear.setText("");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarMaxYear.setEnabled(true);
                textViewMaxYear.setText(String.valueOf(minYear));
                seekBarMaxYear.setMax(MAX_YEAR - minYear);
                seekBarMaxYear.setProgress(0);
                seekBarMaxYear.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                        if (fromUser) {
                            i = i + minYear;
                            textViewMaxYear.setText(String.valueOf(i));
                            //maxYear = 0;
                            maxYear = maxYear + i;
                        } else textViewMaxYear.setText("");

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        maxYear = 2018;
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });


            }
        });

        buttonAdvSearchCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

                //if the value didnt change
                try {
                    minPrice = numberFormat.parse(textViewMinPrice.getText().toString()).intValue();
                } catch (Exception e) {
                    minPrice = 0;
                }
                //if the value didnt change
                try {
                    maxPrice = numberFormat.parse(textViewMaxPrice.getText().toString()).intValue();
                } catch (Exception e) {
                    maxPrice = 5000000;
                }
                //if the value didnt change
                try {
                    String msg = textViewMinMileage.getText().toString();
                    msg = msg.replaceAll("[km]", "");
                    msg = msg.replaceAll("[,]", "");
                    minMileage = Integer.parseInt(msg);
                } catch (Exception e) {
                    minMileage = 0;
                }
                //if the value didnt change
                try {
                    String msg = textViewMaxMileage.getText().toString();
                    msg = msg.replaceAll("[km]", "");
                    msg = msg.replaceAll("[,]", "");
                    maxMileage = Integer.parseInt(msg);
                } catch (Exception e) {
                    maxMileage = 5000000;
                }
                //if the value didnt change
                try {
                    minYear = Integer.parseInt(textViewMinYear.getText().toString());
                } catch (Exception e) {
                    minYear = 1950;
                }
                //if the value didnt change
                try {
                    maxYear = Integer.parseInt(textViewMaxYear.getText().toString());
                } catch (Exception e) {
                    maxYear = 2018;
                }

                if (maxMileage == 0 || maxPrice == 0) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(AdvSearchActivity.this);
                    alert.setTitle("Invalid criteria");
                    alert.setMessage("Maximum price and maximum mileage must be more than 0.\nPlease try again").setNegativeButton("Retry", null);
                    alert.create().show();
                } else {
                    Intent advSearchCarIntent = new Intent(AdvSearchActivity.this, RecommededCarsActivity.class);
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
            }
        });
    }

    private int rangeSpinner(int i) {
        //increase by 5000
        i = i / 5000;
        i = i * 5000;
        return i;
    }

    private String getFormatedAmount(int amount) {
        return NumberFormat.getNumberInstance(Locale.ENGLISH).format(amount) + getString(R.string.km);
    }

    private String getFormattedValue(String value) {
        Double dPrice = Double.parseDouble(value);
        return formatter.format(dPrice);
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


    private void setSeekBar() {
        //set maximum value for price, mileage, and year
        seekBarMinPrice.setProgress(0);
        seekBarMinPrice.setMax(MAX_PRICE);
        seekBarMinPrice.incrementProgressBy(5000);

        seekBarMinMileage.setProgress(0);
        seekBarMinMileage.setMax(MAX_MILEAGE);
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

    public void onReset(View view) {
        textViewMinYear.setText("");
        textViewMinPrice.setText("");
        textViewMinMileage.setText("");
        textViewMaxYear.setText("");
        textViewMaxPrice.setText("");
        textViewMaxMileage.setText("");
        minPrice = 0;
        maxPrice = MAX_PRICE;
        minMileage = 0;
        maxMileage = MAX_MILEAGE;
        minYear = 1950;
        maxYear = MAX_YEAR;
        colorName = "All";
        setSeekBar();
        AdapterCustomColor mAdapterCustomColor = new AdapterCustomColor(AdvSearchActivity.this, spinnerColorName, spinnerColor);
        mSpinnerColor.setAdapter(mAdapterCustomColor);

    }
}
