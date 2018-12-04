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
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class searchCarActivity extends AppCompatActivity {
    private Button buttonAdvSearch, buttonSearch;
    private Spinner spBrand, spModel;
    private ArrayList<String> brand = new ArrayList<>();
    private ArrayList<String> cModel = new ArrayList<>();
    private ProgressBar pbRetrievingData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_car);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.title_search_car));

        buttonSearch = (Button) findViewById(R.id.buttonSearchCar);
        spBrand = (Spinner) findViewById(R.id.spinnerCarBrand);
        spModel = (Spinner) findViewById(R.id.spinnerCarModel);
        pbRetrievingData = (ProgressBar) findViewById(R.id.retrievingCarData);
        buttonAdvSearch = (Button) findViewById(R.id.buttonAdvSearch);
        buttonSearch.setEnabled(false);
        pbRetrievingData.setVisibility(View.VISIBLE);

        getBrand(getString(R.string.get_car_brand_url));

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String carBrand = spBrand.getSelectedItem().toString();
                String carModel = spModel.getSelectedItem().toString();

                Intent searchResultIntent = new Intent(searchCarActivity.this, SearchCarResultActivity.class);
                searchResultIntent.putExtra("carBrand", carBrand);
                searchResultIntent.putExtra("carModel", carModel);
                searchResultIntent.putExtra("from","searchCar");

                startActivity(searchResultIntent);
            }
        });

        buttonAdvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent advSearchIntent = new Intent(searchCarActivity.this, AdvSearchActivity.class);
                startActivity(advSearchIntent);

            }
        });


        spBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String brand = spBrand.getSelectedItem().toString();
                getModel(getString(R.string.get_car_model_url), brand);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getBrand(String url) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("brand");
                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            //follow index
                            String bid = object.getString("id").trim();
                            String bname = object.getString("name").trim();
                            String bdesc = object.getString("desc").trim();
                            String byear = object.getString("year").trim();
                            brand.add(bname);
                        }
                        ArrayAdapter<String> brandAdap = new ArrayAdapter<>(searchCarActivity.this, android.R.layout.simple_spinner_dropdown_item, brand);
                        spBrand.setAdapter(brandAdap);

                    } else {
                        Toast.makeText(searchCarActivity.this, "Error", Toast.LENGTH_LONG).show();
                        finish();
                    }

                } catch (JSONException e) {
                    checkError(e,searchCarActivity.this);
                    finish();
                }
                pbRetrievingData.setVisibility(View.GONE);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        checkError(error,searchCarActivity.this);
                        pbRetrievingData.setVisibility(View.GONE);
                        buttonSearch.setEnabled(true);
                        finish();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void getModel(String url, final String carBrand) {
        buttonSearch.setEnabled(false);
        pbRetrievingData.setVisibility(View.VISIBLE);
        cModel.clear();
        //pbRetrievingData.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("MODEL");
                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            //follow index
                            String model = object.getString("name").trim();
                            cModel.add(model);
                        }

                        ArrayAdapter<String> modelAdapt = new ArrayAdapter<>(searchCarActivity.this, android.R.layout.simple_spinner_dropdown_item, cModel);
                        spModel.setAdapter(modelAdapt);

                    } else {

                        Toast.makeText(searchCarActivity.this, "Error", Toast.LENGTH_LONG).show();
                        finish();
                    }
                } catch (JSONException e) {
                    checkError(e,searchCarActivity.this);
                    finish();
                }
                pbRetrievingData.setVisibility(View.GONE);
                buttonSearch.setEnabled(true);
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        checkError(error,searchCarActivity.this);
                        pbRetrievingData.setVisibility(View.GONE);
                        finish();
                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("brandName", carBrand);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
