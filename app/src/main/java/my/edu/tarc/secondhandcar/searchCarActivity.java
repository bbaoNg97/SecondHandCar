package my.edu.tarc.secondhandcar;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

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

public class searchCarActivity extends AppCompatActivity {
    private Button buttonAdvSearch, buttonSearch;
    private Spinner spBrand, spModel;
    private ArrayList<String> brand = new ArrayList<>();
    private ProgressBar pbRetrievingData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_car);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.search_action_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonSearch = (Button) findViewById(R.id.buttonSearchCar);
        spBrand = (Spinner) findViewById(R.id.spinnerCarBrand);
        spModel = (Spinner) findViewById(R.id.spinnerCarModel);
        pbRetrievingData = (ProgressBar) findViewById(R.id.retrievingCarData);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchResultIntent = new Intent(searchCarActivity.this, SearchCarResultActivity.class);
                startActivity(searchResultIntent);
            }
        });
        buttonAdvSearch = (Button) findViewById(R.id.buttonAdvSearch);
        buttonAdvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent advSearchIntent = new Intent(searchCarActivity.this, AdvSearchActivity.class);
                startActivity(advSearchIntent);

            }
        });
        pbRetrievingData.setVisibility(View.VISIBLE);
        getBrand(getString(R.string.get_car_brand_url));
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
                        pbRetrievingData.setVisibility(View.GONE);
                        buttonSearch.setEnabled(true);
                        ArrayAdapter<String> brandAdap = new ArrayAdapter<>(searchCarActivity.this, android.R.layout.simple_spinner_dropdown_item, brand);
                        spBrand.setAdapter(brandAdap);

                    } else {
                        pbRetrievingData.setVisibility(View.GONE);
                        buttonSearch.setEnabled(true);
                        Toast.makeText(searchCarActivity.this, "Error", Toast.LENGTH_LONG).show();
                        finish();
                    }
                } catch (JSONException e) {
                    if (!LoginActivity.isConnected(searchCarActivity.this)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(searchCarActivity.this);
                        builder.setTitle("Connection Error");
                        builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

                    } else
                        Toast.makeText(searchCarActivity.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                    pbRetrievingData.setVisibility(View.GONE);
                    buttonSearch.setEnabled(true);
                    finish();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (!LoginActivity.isConnected(searchCarActivity.this)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(searchCarActivity.this);
                            builder.setTitle("Connection Error");
                            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

                        } else
                            Toast.makeText(searchCarActivity.this, "Error " + error.toString(), Toast.LENGTH_LONG).show();
                        pbRetrievingData.setVisibility(View.GONE);
                        buttonSearch.setEnabled(true);
                        finish();
                    }
                }) {

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
}
