package my.edu.tarc.secondhandcar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.HashMap;
import java.util.Map;

public class SearchCarResultActivity extends AppCompatActivity {

    private ListView listViewCarResult;
    //private Integer[] IMAGES = {R.drawable.t1, R.drawable.t2, R.drawable.t3};
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
    private ProgressBar searchingResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_car_result);
        setTitle(R.string.title_search_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchingResult = (ProgressBar) findViewById(R.id.searchingResult);
        listViewCarResult = (ListView) findViewById(R.id.listViewCarResult);

        Intent intent = getIntent();
        String carBrand = intent.getStringExtra("carBrand");
        String carModel = intent.getStringExtra("carModel");

        searchingResult.setVisibility(View.VISIBLE);
        clearList();
        getSearchResult(getString(R.string.get_search_result_url), carBrand, carModel);

    }

    private void clearList() {
        NAMES.clear();
        PRICES.clear();
        COLORS.clear();
        DESCS.clear();
        YEARS.clear();
        CAR_STATUS.clear();
        CAR_TYPES.clear();
        MILEAGES.clear();
        CAR_PHOTOS.clear();
        DEALER_ID.clear();
        CAR_ID.clear();
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

                            NAMES.add(carName);
                            PRICES.add(price);
                            COLORS.add(color);
                            DESCS.add(desc);
                            YEARS.add(year);
                            CAR_STATUS.add(carStatus);
                            CAR_TYPES.add(carType);
                            MILEAGES.add(mileage);
                            CAR_PHOTOS.add(carPhoto);
                            DEALER_ID.add(dealerID);
                            CAR_ID.add(carID);

                        }
                        AdapterCarResult adapterCarResult = new AdapterCarResult(SearchCarResultActivity.this, NAMES, PRICES, COLORS, DESCS, YEARS, CAR_STATUS, CAR_TYPES, MILEAGES, CAR_PHOTOS, DEALER_ID, CAR_ID);
                        listViewCarResult.setAdapter(adapterCarResult);
                    } else {
                        Toast.makeText(SearchCarResultActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    checkError(e, SearchCarResultActivity.this);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                checkError(error, SearchCarResultActivity.this);

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
        searchingResult.setVisibility(View.GONE);
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
