package my.edu.tarc.secondhandcar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class CarActivity extends AppCompatActivity {

    private ImageView imageViewCars;
    private Button btnMakeAppointment;
    private TextView textViewName, textViewDealerName, textViewDealerAddress, textViewDesc, textViewPrice, textViewColor, textViewMileage, textViewYear, textViewLocation, textViewOriPrice;
    private ProgressBar pbLoading;
    private String carID, name, price, color, desc, year, mile, status, carType, carPhoto, dealerID, dealerLoc, dealerAddress, dealerName;
    private String custID;
    private Double discount;
    private NumberFormat formatter = NumberFormat.getCurrencyInstance();

    DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###");
    SharedPreferences sharePref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        setTitle(R.string.title_car_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharePref = getSharedPreferences("My_Pref", Context.MODE_PRIVATE);
        custID = sharePref.getString("custID", null);

        imageViewCars = (ImageView) findViewById(R.id.imageViewCars);
        btnMakeAppointment = (Button) findViewById(R.id.buttonAppointment);
        textViewDesc = (TextView) findViewById(R.id.textViewDesc);
        textViewPrice = (TextView) findViewById(R.id.textViewPrice);
        textViewColor = (TextView) findViewById(R.id.textViewColor);
        textViewMileage = (TextView) findViewById(R.id.textViewMileage);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewYear = (TextView) findViewById(R.id.textViewResultYear);
        textViewLocation = (TextView) findViewById(R.id.textViewLocation);
        textViewDealerName = (TextView) findViewById(R.id.textViewDealerName);
        textViewDealerAddress = (TextView) findViewById(R.id.textViewDealerAddress);
        textViewOriPrice = (TextView) findViewById(R.id.textViewOriPrice);
        pbLoading = (ProgressBar) findViewById(R.id.pbLoading);

        Intent intent = getIntent();
        carID = intent.getStringExtra("carID");
        name = intent.getStringExtra("carName");
        carPhoto = intent.getStringExtra("carPhoto");
        color = intent.getStringExtra("carColor");
        desc = intent.getStringExtra("carDesc");
        mile = intent.getStringExtra("carMileage");
        mile = decimalFormat.format(Integer.parseInt(mile));
        carType = intent.getStringExtra("carType");
        year = intent.getStringExtra("carYear");
        price = intent.getStringExtra("carPrice");
        dealerID = intent.getStringExtra("dealerID");
        status = intent.getStringExtra("carStatus");

        getDealerLoc(getString(R.string.get_dealer_location_url), dealerID);


        btnMakeAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check connection
                if (!LoginActivity.isConnected(CarActivity.this)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CarActivity.this);
                    builder.setTitle("Connection Error");
                    builder.setMessage("No network.\nPlease try connect your network").setNegativeButton("Retry", null).create().show();
                }
                //if havent login yet
                else if (custID == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CarActivity.this);
                    builder.setMessage("Please login first").setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent loginIntent = new Intent(CarActivity.this, LoginActivity.class);
                            startActivity(loginIntent);
                        }
                    }).create().show();
                } else {
                    Intent bookingIntent = new Intent(CarActivity.this, MakeAppointmentActivity.class);
                    bookingIntent.putExtra("from", "booking");
                    bookingIntent.putExtra("CarName", name);
                    bookingIntent.putExtra("Price", price);
                    bookingIntent.putExtra("carID", carID);
                    bookingIntent.putExtra("dealerID", dealerID);
                    startActivity(bookingIntent);
                }

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

                        JSONObject object = jsonArray.getJSONObject(0);
                        dealerLoc = object.getString("dealerLoc").trim();
                        dealerAddress = object.getString("dealerAddress").trim();
                        dealerName = object.getString("dealerName").trim();
                        discount = Double.parseDouble(object.getString("discountRate"));


                        loadData();
                    } else {
                        Toast.makeText(CarActivity.this, message, Toast.LENGTH_LONG).show();
                        finish();
                    }
                } catch (JSONException e) {
                    checkError(e, CarActivity.this);
                    finish();
                }
                pbLoading.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                checkError(error, CarActivity.this);
                finish();
                pbLoading.setVisibility(View.GONE);
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("dealerID", dealerID);
                params.put("carID", carID);
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

    private void loadData() {
        int colorResId = getResources().getColor(getResources().getIdentifier(color, "color", getPackageName()));
        textViewColor.setTextColor(colorResId);
        if (color.equals("White"))
            textViewColor.setBackgroundResource(R.color.Black);
        textViewName.setText(name);
        Glide.with(CarActivity.this).load(carPhoto).into(imageViewCars);
        textViewColor.setText(color);
        textViewDesc.setText(desc);
        String mileMsg = mile + " KM";
        textViewMileage.setText(mileMsg);
        textViewYear.setText(year);

        Double Ori = Double.parseDouble(price);
        if (discount > 0) {
            textViewOriPrice.setVisibility(View.VISIBLE);
            textViewOriPrice.setText(formatter.format(Ori));
            Double newPrice = Ori - (Ori * discount / 100);
            textViewPrice.setText(formatter.format(newPrice));
            price=String.format("%.0f", newPrice);
        } else {
            textViewOriPrice.setVisibility(View.GONE);
            textViewPrice.setText(formatter.format(Ori));
        }

        textViewLocation.setText(dealerLoc);
        textViewDealerName.setText(dealerName);
        textViewDealerAddress.setText(dealerAddress);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
