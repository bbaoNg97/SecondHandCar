package my.edu.tarc.secondhandcar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SearchCarResultActivity extends AppCompatActivity {

    private ListView listViewCarResult;

    private ArrayList<String> NAMES = new ArrayList<>();
    private ArrayList<String> PRICES = new ArrayList<>();
    private ArrayList<String> COLORS = new ArrayList<>();
    private ArrayList<String> DESCS = new ArrayList<>();
    private ArrayList<String> YEARS = new ArrayList<>();
    private ArrayList<String> CAR_STATUS = new ArrayList<>();
    private ArrayList<String> CAR_TYPES = new ArrayList<>();
    private ArrayList<String> MILEAGES = new ArrayList<>();
    private ArrayList<String> CAR_PHOTOS = new ArrayList<>();
    private ArrayList<String> DEALER_ID = new ArrayList<>();
    private ArrayList<String> CAR_ID = new ArrayList<>();
    private ArrayList<Car> carArr = new ArrayList<>();
    private ProgressBar searchingResult;
    private String carBrand, carModel;

    private int minPrice = 0;
    private int maxPrice = 5000000;
    private int minMileage = 0;
    private int maxMileage = 1000000;
    private int minYear = 1950;
    private int maxYear = 2018;

    private String colorName, purpose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_car_result);
        setTitle(R.string.title_search_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchingResult = (ProgressBar) findViewById(R.id.searchingResult);
        listViewCarResult = (ListView) findViewById(R.id.listViewCarResult);


        Intent intent = getIntent();
        if (intent.getStringExtra("from").equals("searchCar")) {
            //just showing search result
            carBrand = intent.getStringExtra("carBrand");
            carModel = intent.getStringExtra("carModel");
            searchingResult.setVisibility(View.VISIBLE);
            clearList();
            getSearchResult(getString(R.string.get_search_result_url), carBrand, carModel);
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

            clearList();
            getRecommendedCar(SearchCarResultActivity.this, getString(R.string.get_recommended_car_url));
        }


    }

    private void getRecommendedCar(Context context, String url) {
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
                            //TODO: just show only 5 record is enough
                            jsonObj = jsonArr.getJSONObject(i);
                            String carName = jsonObj.getString("carName");
                            String price = jsonObj.getString("price");
                            String color = jsonObj.getString("color");
                            String desc = jsonObj.getString("desc");
                            String year = jsonObj.getString("year");
                            String carStatus = jsonObj.getString("carStatus");
                            String carType = jsonObj.getString("carType");
                            String mileage = jsonObj.getString("mileage");
                            String carPhoto = jsonObj.getString("car_photo");
                            String dealerID = jsonObj.getString("dealerID");
                            String carID = jsonObj.getString("carID");

                            Car car = new Car(carName, price, color, desc, year, carStatus, carType, mileage, carPhoto, dealerID, carID);

                            carArr.add(car);

                           /* NAMES.add(carName);
                            PRICES.add(price);
                            COLORS.add(color);
                            DESCS.add(desc);
                            YEARS.add(year);
                            CAR_STATUS.add(carStatus);
                            CAR_TYPES.add(carType);
                            MILEAGES.add(mileage);
                            CAR_PHOTOS.add(carPhoto);
                            DEALER_ID.add(dealerID);
                            CAR_ID.add(carID);*/


                        }
                        //AdapterCarResult adapterCarResult = new AdapterCarResult(SearchCarResultActivity.this, NAMES, PRICES, COLORS, DESCS, YEARS, CAR_STATUS, CAR_TYPES, MILEAGES, CAR_PHOTOS, DEALER_ID, CAR_ID);
                        AdapterCarResult adapterCarResult = new AdapterCarResult(SearchCarResultActivity.this, carArr);

                        listViewCarResult.setAdapter(adapterCarResult);
                    } else {
                        Toast.makeText(SearchCarResultActivity.this, message, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception error) {
                    checkError(error, SearchCarResultActivity.this);
                }
                searchingResult.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                checkError(error, SearchCarResultActivity.this);
                searchingResult.setVisibility(View.GONE);
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

    private void clearList() {
        carArr.clear();
      /*  NAMES.clear();
        PRICES.clear();
        COLORS.clear();
        DESCS.clear();
        YEARS.clear();
        CAR_STATUS.clear();
        CAR_TYPES.clear();
        MILEAGES.clear();
        CAR_PHOTOS.clear();
        DEALER_ID.clear();
        CAR_ID.clear(); */
    }


    private void getSearchResult(String url, final String brand, final String model) {

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
                            String price = jsonObj.getString("price");
                            String color = jsonObj.getString("color");
                            String desc = jsonObj.getString("desc");
                            String year = jsonObj.getString("year");
                            String carStatus = jsonObj.getString("carStatus");
                            String carType = jsonObj.getString("carType");
                            String mileage = jsonObj.getString("mileage");
                            String carPhoto = jsonObj.getString("car_photo");
                            String dealerID = jsonObj.getString("dealerID");
                            String carID = jsonObj.getString("carID");

                            Car car = new Car(carName, price, color, desc, year, carStatus, carType, mileage, carPhoto, dealerID, carID);

                            carArr.add(car);


                            /*NAMES.add(carName);
                            PRICES.add(price);
                            COLORS.add(color);
                            DESCS.add(desc);
                            YEARS.add(year);
                            CAR_STATUS.add(carStatus);
                            CAR_TYPES.add(carType);
                            MILEAGES.add(mileage);
                            CAR_PHOTOS.add(carPhoto);
                            DEALER_ID.add(dealerID);
                            CAR_ID.add(carID);*/

                        }
                        //AdapterCarResult adapterCarResult = new AdapterCarResult(SearchCarResultActivity.this, NAMES, PRICES, COLORS, DESCS, YEARS, CAR_STATUS, CAR_TYPES, MILEAGES, CAR_PHOTOS, DEALER_ID, CAR_ID);
                        AdapterCarResult adapterCarResult = new AdapterCarResult(SearchCarResultActivity.this, carArr);
                        listViewCarResult.setAdapter(adapterCarResult);
                    } else {
                        Toast.makeText(SearchCarResultActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    checkError(e, SearchCarResultActivity.this);
                }
                searchingResult.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                checkError(error, SearchCarResultActivity.this);
                searchingResult.setVisibility(View.GONE);
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
