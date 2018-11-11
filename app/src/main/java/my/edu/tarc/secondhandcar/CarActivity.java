package my.edu.tarc.secondhandcar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
    private TextView textViewName, textViewDesc, textViewPrice, textViewColor, textViewMileage, textViewYear, textViewLocation;
    private ProgressBar pbLoading;
    private String carID, name, price, color, desc, year, mile, status, carType, carPhoto, dealerID, dealerLoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        setTitle(R.string.title_car_detail);

        imageViewCars = (ImageView) findViewById(R.id.imageViewCars);
        btnMakeAppointment = (Button) findViewById(R.id.buttonAppointment);
        textViewDesc = (TextView) findViewById(R.id.textViewDesc);
        textViewPrice = (TextView) findViewById(R.id.textViewPrice);
        textViewColor = (TextView) findViewById(R.id.textViewColor);
        textViewMileage = (TextView) findViewById(R.id.textViewMileage);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewYear = (TextView) findViewById(R.id.textViewYear);
        textViewLocation = (TextView) findViewById(R.id.textViewLocation);

        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);

        Intent intent = getIntent();
        carID = intent.getStringExtra("carID");
        name = intent.getStringExtra("carName");
        carPhoto = intent.getStringExtra("carPhoto");
        color = intent.getStringExtra("carColor");
        desc = intent.getStringExtra("carDesc");
        mile = intent.getStringExtra("carMileage");
        carType = intent.getStringExtra("carType");
        year = intent.getStringExtra("carYear");
        price = intent.getStringExtra("carPrice");
        dealerID = intent.getStringExtra("dealerID");
        status = intent.getStringExtra("carStatus");
        pbLoading.setVisibility(View.VISIBLE);
        getDealerLoc(getString(R.string.get_dealer_location_url), dealerID);
        pbLoading.setVisibility(View.GONE);

        textViewName.setText(name);
        Glide.with(this).load(carPhoto).into(imageViewCars);
        textViewColor.setText(color);
        textViewDesc.setText(desc);
        textViewMileage.setText(mile);
        textViewYear.setText(year);
        textViewPrice.setText(price);
        textViewLocation.setText(dealerLoc);

        btnMakeAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent bookingIntent = new Intent(CarActivity.this, MakeAppointmentActivity.class);
                bookingIntent.putExtra("CarName", name);
                bookingIntent.putExtra("Price", price);
                bookingIntent.putExtra("carID", carID);
                startActivity(bookingIntent);
            }
        });

    }

    private void getDealerLoc(String url, final String dealerID) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String message = jsonObject.getString("message");
                    JSONArray jsonArray = jsonObject.getJSONArray("LOCATION");
                    if (success.equals("1")) {
                        String location = "";
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            location = object.getString("dealerLoc").trim();
                        }
                        dealerLoc = location;
                    } else {
                        Toast.makeText(CarActivity.this, message, Toast.LENGTH_LONG).show();
                        finish();
                    }
                } catch (JSONException e) {
                    checkError(e, CarActivity.this);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                checkError(error, CarActivity.this);
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
