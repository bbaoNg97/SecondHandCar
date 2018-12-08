package my.edu.tarc.secondhandcar;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RecommededCarsActivity extends AppCompatActivity {

    private int minPrice;
    private int maxPrice;
    private int minMileage;
    private int maxMileage;
    private int minYear;
    private int maxYear;
    private String colorName;

    private ListView listViewCarResult;
    private ArrayList<Car> carArr = new ArrayList<>();
    private ProgressBar recommendingResult;
    private TabLayout tabLayout;
    private Spinner spinnerSortBy;
    private TextView tvMinMile, tvMaxMile, tvMinPrice, tvMaxPrice, tvMinYear, tvMaxYear, tvColor;
    private double discRate = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommeded_cars);
        setTitle(R.string.title_recommended_car);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recommendingResult = (ProgressBar) findViewById(R.id.recommendingResult);
        listViewCarResult = (ListView) findViewById(R.id.listViewRecCarResult);
        tabLayout = (TabLayout) findViewById(R.id.tabLayoutRec);
        spinnerSortBy = (Spinner) findViewById(R.id.spinnerSortByRec);
        tvMinMile = (TextView) findViewById(R.id.textViewShMinMileage);
        tvMaxMile = (TextView) findViewById(R.id.textViewShMaxMileage);
        tvMinPrice = (TextView) findViewById(R.id.textViewShMinPrice);
        tvMaxPrice = (TextView) findViewById(R.id.textViewShMaxPrice);
        tvMinYear = (TextView) findViewById(R.id.textViewShMinYear);
        tvMaxYear = (TextView) findViewById(R.id.textViewShMaxYear);
        tvColor = (TextView) findViewById(R.id.textViewShColor);
        Intent intent = getIntent();
        //show recommended car
        minPrice = intent.getIntExtra(AdvSearchActivity.Min_PRICE, 0);
        minMileage = intent.getIntExtra(AdvSearchActivity.Min_MILEAGE, 0);
        minYear = intent.getIntExtra(AdvSearchActivity.Min_YEAR, 0);
        maxPrice = intent.getIntExtra(AdvSearchActivity.Max_PRICE, 0);
        maxMileage = intent.getIntExtra(AdvSearchActivity.Max_MILEAGE, 0);
        maxYear = intent.getIntExtra(AdvSearchActivity.Max_YEAR, 0);
        //   purpose = intent.getStringExtra("Purpose");
        colorName = intent.getStringExtra("Color");

        tvMaxPrice.setText(maxPrice + "");
        tvMinPrice.setText(minPrice + "");
        tvMinMile.setText(minMileage + "");
        tvMaxMile.setText(maxMileage + "");
        tvMaxYear.setText(maxYear + "");
        tvMinYear.setText(minYear + "");

        tvColor.setText(colorName + "");

        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(this, R.array.sort_by, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSortBy.setAdapter(sortAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                arrangeRecommendCar(tab);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                arrangeRecommendCar(tab);

            }
        });
        spinnerSortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                clearList();

                //show recommended car
                getRecommendedCar(RecommededCarsActivity.this, getString(R.string.get_recommended_car_url), i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void arrangeRecommendCar(TabLayout.Tab tab) {
        String tabText = tab.getText().toString();
        if (spinnerSortBy.getSelectedItemPosition() == 0) {
            if (tabText.equals(getString(R.string.car_price))) {
                AdapterRecommendCar adapterRecommendCar = new AdapterRecommendCar(RecommededCarsActivity.this, carArr);
                Collections.sort(carArr, Car.PriceComparator);
                listViewCarResult.setAdapter(adapterRecommendCar);
            } else if (tabText.equals(getString(R.string.mileage))) {
                AdapterRecommendCar adapterRecommendCar = new AdapterRecommendCar(RecommededCarsActivity.this, carArr);
                Collections.sort(carArr, Car.MileageComparator);
                listViewCarResult.setAdapter(adapterRecommendCar);
            } else {
                AdapterRecommendCar adapterRecommendCar = new AdapterRecommendCar(RecommededCarsActivity.this, carArr);
                Collections.sort(carArr, Car.YearComparator);
                listViewCarResult.setAdapter(adapterRecommendCar);
            }

        } else {
            if (tabText.equals(getString(R.string.car_price))) {
                AdapterRecommendCar adapterRecommendCar = new AdapterRecommendCar(RecommededCarsActivity.this, carArr);
                Collections.sort(carArr, Collections.reverseOrder(Car.PriceComparator));
                listViewCarResult.setAdapter(adapterRecommendCar);
            } else if (tabText.equals(getString(R.string.mileage))) {
                AdapterRecommendCar adapterRecommendCar = new AdapterRecommendCar(RecommededCarsActivity.this, carArr);
                Collections.sort(carArr, Collections.reverseOrder(Car.MileageComparator));
                listViewCarResult.setAdapter(adapterRecommendCar);
            } else {
                AdapterRecommendCar adapterRecommendCar = new AdapterRecommendCar(RecommededCarsActivity.this, carArr);
                Collections.sort(carArr, Collections.reverseOrder(Car.YearComparator));
                listViewCarResult.setAdapter(adapterRecommendCar);
            }
        }
    }

    private void getRecommendedCar(Context context, String url, final int i) {
        recommendingResult.setVisibility(View.VISIBLE);
        listViewCarResult.setVisibility(View.GONE);
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String success = jsonObject.getString("success");
                    JSONArray jsonArr = jsonObject.getJSONArray("RECOMMEND");
                    if (success.equals("1")) {
                        JSONObject jsonObj;

                        for (int i = 0; i < jsonArr.length(); i++) {

                            jsonObj = jsonArr.getJSONObject(i);
                            String carName = jsonObj.getString("carName");
                            int price = jsonObj.getInt("price");
                            String color = jsonObj.getString("color");
                            String desc = jsonObj.getString("desc");
                            int year = jsonObj.getInt("year");
                            String carStatus = jsonObj.getString("carStatus");
                            String carType = jsonObj.getString("carType");
                            int mileage = jsonObj.getInt("mileage");
                            String carPhoto = jsonObj.getString("car_photo");
                            String dealerID = jsonObj.getString("dealerID");
                            String carID = jsonObj.getString("carID");

                            String EndDate = jsonObj.getString("endDate");
                            String Discount = jsonObj.getString("discount");

                            Car car = new Car(carName, price, color, desc, year, carStatus, carType, mileage, carPhoto, dealerID, carID,Discount,EndDate);

                            carArr.add(car);

                        }
                        arrangeRecommendedCar(i);
                    } else {
                        Toast.makeText(RecommededCarsActivity.this, message, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception error) {
                    checkError(error, RecommededCarsActivity.this);
                }
                recommendingResult.setVisibility(View.GONE);
                listViewCarResult.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                checkError(error, RecommededCarsActivity.this);
                recommendingResult.setVisibility(View.GONE);
                listViewCarResult.setVisibility(View.VISIBLE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("minPrice", minPrice + "");
                params.put("maxPrice", maxPrice + "");
                params.put("minMileage", minMileage + "");
                params.put("maxMileage", maxMileage + "");
                params.put("minYear", minYear + "");
                params.put("maxYear", maxYear + "");
                params.put("color", colorName);
                //   params.put("carType",purpose+"");

                return params;
            }
        };
        RequestQueue reqQueue = Volley.newRequestQueue(context);
        reqQueue.add(strReq);
    }


    private void arrangeRecommendedCar(int i) {
        if (i == 0) {
            if (tabLayout.getSelectedTabPosition() == 0) {
                AdapterRecommendCar adapterRecommendCar = new AdapterRecommendCar(RecommededCarsActivity.this, carArr);
                Collections.sort(carArr, Car.PriceComparator);
                listViewCarResult.setAdapter(adapterRecommendCar);
            } else if (tabLayout.getSelectedTabPosition() == 1) {
                AdapterRecommendCar adapterRecommendCar = new AdapterRecommendCar(RecommededCarsActivity.this, carArr);
                Collections.sort(carArr, Car.MileageComparator);
                listViewCarResult.setAdapter(adapterRecommendCar);
            } else {
                AdapterRecommendCar adapterRecommendCar = new AdapterRecommendCar(RecommededCarsActivity.this, carArr);
                Collections.sort(carArr, Car.YearComparator);
                listViewCarResult.setAdapter(adapterRecommendCar);
            }

        } else {
            if (tabLayout.getSelectedTabPosition() == 0) {
                AdapterRecommendCar adapterRecommendCar = new AdapterRecommendCar(RecommededCarsActivity.this, carArr);
                Collections.sort(carArr, Collections.reverseOrder(Car.PriceComparator));
                listViewCarResult.setAdapter(adapterRecommendCar);
            } else if (tabLayout.getSelectedTabPosition() == 1) {
                AdapterRecommendCar adapterRecommendCar = new AdapterRecommendCar(RecommededCarsActivity.this, carArr);
                Collections.sort(carArr, Collections.reverseOrder(Car.MileageComparator));
                listViewCarResult.setAdapter(adapterRecommendCar);
            } else {
                AdapterRecommendCar adapterRecommendCar = new AdapterRecommendCar(RecommededCarsActivity.this, carArr);
                Collections.sort(carArr, Collections.reverseOrder(Car.YearComparator));
                listViewCarResult.setAdapter(adapterRecommendCar);
            }

        }
    }

    private void clearList() {
        carArr.clear();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void checkError(Exception e, Context context) {
        if (!LoginActivity.isConnected(context)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Connection Error");
            builder.setIcon(R.drawable.ic_action_info);
            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

        } else {
            Toast.makeText(context, "No Record! \n" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
