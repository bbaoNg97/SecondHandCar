package my.edu.tarc.secondhandcar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CarActivity extends AppCompatActivity {

    private ImageView imageViewCars;
    private Button btnMakeAppointment;
    private TextView textViewDesc, textViewName, textViewPrice, textViewColor, textViewMileage, textViewYear, textViewLocation;
    private String id, name,  price, color, desc, year, mile, status, carType, carPhoto, dealerID, dealerLoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        imageViewCars = (ImageView) findViewById(R.id.imageViewCars);
        btnMakeAppointment = (Button) findViewById(R.id.buttonAppointment);
        textViewDesc = (TextView) findViewById(R.id.textViewDesc);
        textViewPrice = (TextView) findViewById(R.id.textViewPrice);
        textViewColor = (TextView) findViewById(R.id.textViewColor);
        textViewMileage = (TextView) findViewById(R.id.textViewMileage);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewYear = (TextView) findViewById(R.id.textViewYear);
        textViewLocation = (TextView) findViewById(R.id.textViewLocation);


        Intent intent = getIntent();
        id = intent.getStringExtra("CarID");
        name = intent.getStringExtra("carName");
        carPhoto = intent.getStringExtra("CarImg");
        price = intent.getStringExtra("carPrice");
        color = intent.getStringExtra("carColor");
        desc = intent.getStringExtra("carDesc");
        year = intent.getStringExtra("carYear");
        mile = intent.getStringExtra("carMileage");
        status = intent.getStringExtra("carStatus");
        carType = intent.getStringExtra("carType");
        carPhoto = intent.getStringExtra("carPhoto");
        dealerID = intent.getStringExtra("dealerID");

        getDealerLoc(getString(R.string.get_dealer_location_url), dealerID);
        textViewName.setText(name);
        textViewColor.setText(color);
        textViewPrice.setText(price);
        textViewYear.setText(year);
        textViewMileage.setText(mile);
        textViewDesc.setText(desc);
        textViewLocation.setText(dealerLoc);

        Glide.with(this).load(carPhoto).into(imageViewCars);


        setTitle(R.string.title_car_detail);

        btnMakeAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent bookingIntent = new Intent(CarActivity.this, MakeAppointmentActivity.class);
                bookingIntent.putExtra("CarName", name);
                bookingIntent.putExtra("Price", price);
                startActivity(bookingIntent);
            }
        });


    }

    //TODO: try this
    private void getDealerLoc(String url, final String dealerID) {
        //pbRetrievingData.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("LOCATION");
                    if (success.equals("1")) {
                        String location = "";
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            //follow index
                            location = object.getString("dealerLoc").trim();

                        }
                        dealerLoc = location;

                    } else {

                        Toast.makeText(CarActivity.this, "Error", Toast.LENGTH_LONG).show();
                        finish();
                    }
                } catch (JSONException e) {
                    if (!LoginActivity.isConnected(CarActivity.this)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CarActivity.this);
                        builder.setTitle("Connection Error");
                        builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

                    } else
                        Toast.makeText(CarActivity.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();

                    finish();
                }

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (!LoginActivity.isConnected(CarActivity.this)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CarActivity.this);
                            builder.setTitle("Connection Error");
                            builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();

                        } else
                            Toast.makeText(CarActivity.this, "Error " + error.toString(), Toast.LENGTH_LONG).show();
                        finish();
                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("dealerID", dealerID);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
