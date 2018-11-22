package my.edu.tarc.secondhandcar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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


public class SearchCarResultActivity extends AppCompatActivity {

    private ListView listViewCarResult;
    private ArrayList<Car> carArr = new ArrayList<>();
    private ProgressBar searchingResult;
    private TabLayout tabLayout;
    private String carBrand, carModel;
    private Spinner spinnerSortBy;

    private int minPrice;
    private int maxPrice;
    private int minMileage;
    private int maxMileage;
    private int minYear;
    private int maxYear;
    private String colorName, purpose, temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_car_result);
        setTitle(R.string.title_search_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchingResult = (ProgressBar) findViewById(R.id.searchingResult);
        listViewCarResult = (ListView) findViewById(R.id.listViewCarResult);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        spinnerSortBy = (Spinner) findViewById(R.id.spinnerSortBy);

        Intent intent = getIntent();
        if (intent.getStringExtra("from").equals("searchCar")) {
            //just showing search result
            carBrand = intent.getStringExtra("carBrand");
            carModel = intent.getStringExtra("carModel");
            searchingResult.setVisibility(View.VISIBLE);
            //temp: use in spinner,and adapter
            temp = "SearchCar";
        } else {
            //show recommended car
            minPrice = intent.getIntExtra(AdvSearchActivity.MIN_PRICE, 0);
            minMileage = intent.getIntExtra(AdvSearchActivity.MIN_MILEAGE, 0);
            minYear = intent.getIntExtra(AdvSearchActivity.MIN_YEAR, 0);
            maxPrice = intent.getIntExtra(AdvSearchActivity.Max_PRICE, 0);
            maxMileage = intent.getIntExtra(AdvSearchActivity.Max_MILEAGE, 0);
            maxYear = intent.getIntExtra(AdvSearchActivity.Max_YEAR, 0);
            //   purpose = intent.getStringExtra("Purpose");
            colorName = intent.getStringExtra("Color");

            //temp: use in spinner,and adapter
            temp = "RecommendCar";
        }
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(this, R.array.sort_by, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSortBy.setAdapter(sortAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (temp.equals("SearchCar")) {
                    arrangeCar(tab);
                } else {
                    arrangeRecommendCar(tab);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (temp.equals("SearchCar")) {
                    arrangeCar(tab);
                } else {
                    arrangeRecommendCar(tab);
                }
            }
        });



        spinnerSortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                clearList();
                if (temp.equals("SearchCar")) {
                    //show search result
                    getSearchResult(getString(R.string.get_search_result_url), carBrand, carModel, i);
                } else {
                    //show recommended car
                    getRecommendedCar(SearchCarResultActivity.this, getString(R.string.get_recommended_car_url), i);
                }
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
                AdapterRecommendCar adapterRecommendCar = new AdapterRecommendCar(SearchCarResultActivity.this, carArr);
                Collections.sort(carArr, Car.PriceComparator);
                listViewCarResult.setAdapter(adapterRecommendCar);
            } else if (tabText.equals(getString(R.string.mileage))) {
                AdapterRecommendCar adapterRecommendCar = new AdapterRecommendCar(SearchCarResultActivity.this, carArr);
                Collections.sort(carArr, Car.MileageComparator);
                listViewCarResult.setAdapter(adapterRecommendCar);
            } else {
                AdapterRecommendCar adapterRecommendCar = new AdapterRecommendCar(SearchCarResultActivity.this, carArr);
                Collections.sort(carArr, Car.YearComparator);
                listViewCarResult.setAdapter(adapterRecommendCar);
            }

        } else {
            if (tabText.equals(getString(R.string.car_price))) {
                AdapterRecommendCar adapterRecommendCar = new AdapterRecommendCar(SearchCarResultActivity.this, carArr);
                Collections.sort(carArr, Collections.reverseOrder(Car.PriceComparator));
                listViewCarResult.setAdapter(adapterRecommendCar);
            } else if (tabText.equals(getString(R.string.mileage))) {
                AdapterRecommendCar adapterRecommendCar = new AdapterRecommendCar(SearchCarResultActivity.this, carArr);
                Collections.sort(carArr, Collections.reverseOrder(Car.MileageComparator));
                listViewCarResult.setAdapter(adapterRecommendCar);
            } else {
                AdapterRecommendCar adapterRecommendCar = new AdapterRecommendCar(SearchCarResultActivity.this, carArr);
                Collections.sort(carArr, Collections.reverseOrder(Car.YearComparator));
                listViewCarResult.setAdapter(adapterRecommendCar);
            }
        }
    }

    private void arrangeCar(TabLayout.Tab tab) {
        String tabText = tab.getText().toString();
        if (spinnerSortBy.getSelectedItemPosition() == 0) {
            if (tabText.equals(getString(R.string.car_price))) {
                AdapterSearchCarResult adapterRecommendCar = new AdapterSearchCarResult(SearchCarResultActivity.this, carArr);
                Collections.sort(carArr, Car.PriceComparator);
                listViewCarResult.setAdapter(adapterRecommendCar);
            } else if (tabText.equals(getString(R.string.mileage))) {
                AdapterSearchCarResult adapterRecommendCar = new AdapterSearchCarResult(SearchCarResultActivity.this, carArr);
                Collections.sort(carArr, Car.MileageComparator);
                listViewCarResult.setAdapter(adapterRecommendCar);
            } else {
                AdapterSearchCarResult adapterRecommendCar = new AdapterSearchCarResult(SearchCarResultActivity.this, carArr);
                Collections.sort(carArr, Car.YearComparator);
                listViewCarResult.setAdapter(adapterRecommendCar);
            }

        } else {
            if (tabText.equals(getString(R.string.car_price))) {
                AdapterSearchCarResult adapterRecommendCar = new AdapterSearchCarResult(SearchCarResultActivity.this, carArr);
                Collections.sort(carArr, Collections.reverseOrder(Car.PriceComparator));
                listViewCarResult.setAdapter(adapterRecommendCar);
            } else if (tabText.equals(getString(R.string.mileage))) {
                AdapterSearchCarResult adapterRecommendCar = new AdapterSearchCarResult(SearchCarResultActivity.this, carArr);
                Collections.sort(carArr, Collections.reverseOrder(Car.MileageComparator));
                listViewCarResult.setAdapter(adapterRecommendCar);
            } else {
                AdapterSearchCarResult adapterRecommendCar = new AdapterSearchCarResult(SearchCarResultActivity.this, carArr);
                Collections.sort(carArr, Collections.reverseOrder(Car.YearComparator));
                listViewCarResult.setAdapter(adapterRecommendCar);
            }
        }
    }


    private void getRecommendedCar(Context context, String url, final int i) {
        searchingResult.setVisibility(View.VISIBLE);
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

                            Car car = new Car(carName, price, color, desc, year, carStatus, carType, mileage, carPhoto, dealerID, carID);

                            carArr.add(car);

                        }
                        arrangeRecommededCar(i);
                    } else {
                        Toast.makeText(SearchCarResultActivity.this, message, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception error) {
                    checkError(error, SearchCarResultActivity.this);
                }
                searchingResult.setVisibility(View.GONE);
                listViewCarResult.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                checkError(error, SearchCarResultActivity.this);
                searchingResult.setVisibility(View.GONE);
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

    private void arrangeRecommededCar(int i) {
        if (i == 0) {
            if (tabLayout.getSelectedTabPosition() == 0) {
                AdapterRecommendCar adapterRecommendCar = new AdapterRecommendCar(SearchCarResultActivity.this, carArr);
                Collections.sort(carArr, Car.PriceComparator);
                listViewCarResult.setAdapter(adapterRecommendCar);
            } else if (tabLayout.getSelectedTabPosition() == 1) {
                AdapterRecommendCar adapterRecommendCar = new AdapterRecommendCar(SearchCarResultActivity.this, carArr);
                Collections.sort(carArr, Car.MileageComparator);
                listViewCarResult.setAdapter(adapterRecommendCar);
            } else {
                AdapterRecommendCar adapterRecommendCar = new AdapterRecommendCar(SearchCarResultActivity.this, carArr);
                Collections.sort(carArr, Car.YearComparator);
                listViewCarResult.setAdapter(adapterRecommendCar);
            }

        } else {
            if (tabLayout.getSelectedTabPosition() == 0) {
                AdapterRecommendCar adapterRecommendCar = new AdapterRecommendCar(SearchCarResultActivity.this, carArr);
                Collections.sort(carArr, Collections.reverseOrder(Car.PriceComparator));
                listViewCarResult.setAdapter(adapterRecommendCar);
            } else if (tabLayout.getSelectedTabPosition() == 1) {
                AdapterRecommendCar adapterRecommendCar = new AdapterRecommendCar(SearchCarResultActivity.this, carArr);
                Collections.sort(carArr, Collections.reverseOrder(Car.MileageComparator));
                listViewCarResult.setAdapter(adapterRecommendCar);
            } else {
                AdapterRecommendCar adapterRecommendCar = new AdapterRecommendCar(SearchCarResultActivity.this, carArr);
                Collections.sort(carArr, Collections.reverseOrder(Car.YearComparator));
                listViewCarResult.setAdapter(adapterRecommendCar);
            }

        }
    }

    private void clearList() {
        carArr.clear();

    }


    private void getSearchResult(String url, final String brand, final String model, final int i) {
        searchingResult.setVisibility(View.VISIBLE);
        listViewCarResult.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    JSONArray jsonArray = jsonObject.getJSONArray("SEARCH");
                    if (success.equals("1")) {
                        JSONObject jsonObj;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObj = jsonArray.getJSONObject(i);
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

                            Car car = new Car(carName, price, color, desc, year, carStatus, carType, mileage, carPhoto, dealerID, carID);

                            carArr.add(car);

                        }
                        arrangeDefaultCar(i);

                    } else {
                        Toast.makeText(SearchCarResultActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    checkError(e, SearchCarResultActivity.this);
                }
                searchingResult.setVisibility(View.GONE);
                listViewCarResult.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                checkError(error, SearchCarResultActivity.this);
                searchingResult.setVisibility(View.GONE);
                listViewCarResult.setVisibility(View.VISIBLE);
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("brandName", brand);
                params.put("name", model);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SearchCarResultActivity.this);
        requestQueue.add(stringRequest);

    }

    private void arrangeDefaultCar(int i) {
        if (i == 0) {
            if (tabLayout.getSelectedTabPosition() == 0) {
                AdapterSearchCarResult adapterRecommendCar = new AdapterSearchCarResult(SearchCarResultActivity.this, carArr);
                Collections.sort(carArr, Car.PriceComparator);
                listViewCarResult.setAdapter(adapterRecommendCar);
            } else if (tabLayout.getSelectedTabPosition() == 1) {
                AdapterSearchCarResult adapterRecommendCar = new AdapterSearchCarResult(SearchCarResultActivity.this, carArr);
                Collections.sort(carArr, Car.MileageComparator);
                listViewCarResult.setAdapter(adapterRecommendCar);
            } else {
                AdapterSearchCarResult adapterRecommendCar = new AdapterSearchCarResult(SearchCarResultActivity.this, carArr);
                Collections.sort(carArr, Car.YearComparator);
                listViewCarResult.setAdapter(adapterRecommendCar);
            }

        } else {
            if (tabLayout.getSelectedTabPosition() == 0) {
                AdapterSearchCarResult adapterRecommendCar = new AdapterSearchCarResult(SearchCarResultActivity.this, carArr);
                Collections.sort(carArr, Collections.reverseOrder(Car.PriceComparator));
                listViewCarResult.setAdapter(adapterRecommendCar);
            } else if (tabLayout.getSelectedTabPosition() == 1) {
                AdapterSearchCarResult adapterRecommendCar = new AdapterSearchCarResult(SearchCarResultActivity.this, carArr);
                Collections.sort(carArr, Collections.reverseOrder(Car.MileageComparator));
                listViewCarResult.setAdapter(adapterRecommendCar);
            } else {
                AdapterSearchCarResult adapterRecommendCar = new AdapterSearchCarResult(SearchCarResultActivity.this, carArr);
                Collections.sort(carArr, Collections.reverseOrder(Car.YearComparator));
                listViewCarResult.setAdapter(adapterRecommendCar);
            }

        }
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
            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

        } else {
            Toast.makeText(context, "No Record! \n" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

}
