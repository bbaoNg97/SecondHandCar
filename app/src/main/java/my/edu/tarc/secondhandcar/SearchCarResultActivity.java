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

import java.util.HashMap;
import java.util.Map;

public class SearchCarResultActivity extends AppCompatActivity {

    private ListView listViewCarResult;
    private Integer[] IMAGES = {R.drawable.t1, R.drawable.t2, R.drawable.t3};
    private String[] NAMES = {"Proton Saga", "Proton Vira", "Honda Civic",};
    private ProgressBar searchingResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_car_result);
        setTitle(R.string.title_search_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String carModel = getIntent().getStringExtra("carModel");
        String carBrand = getIntent().getStringExtra("carBrand");
        getSearchResult(getString(R.string.get_search_result_url), carModel, carBrand);
        AdapterCarResult adapterCarResult = new AdapterCarResult(SearchCarResultActivity.this, IMAGES, NAMES);
        searchingResult = (ProgressBar) findViewById(R.id.searchingResult);
        listViewCarResult = (ListView) findViewById(R.id.listViewCarResult);
        listViewCarResult.setAdapter(adapterCarResult);
        listViewCarResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent carIntent = new Intent(SearchCarResultActivity.this, CarActivity.class);
                startActivity(carIntent);
            }
        });

    }

    private void getSearchResult(String url, final String carModel, final String carBrand) {
        searchingResult.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    String success=jsonObject.getString("success");
                    JSONArray jsonArray=jsonObject.getJSONArray("SEARCH");
                    if(success.equals("1")){


                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObj=jsonArray.getJSONObject(i);
                            jsonObj.getString("");

                        }
                    }else{

                    }
                }catch (Exception e){
                    checkError(e, SearchCarResultActivity.this);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                checkError(error, SearchCarResultActivity.this);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("carBrand", carBrand);
                params.put("carModel", carModel);
                return super.getParams();
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
