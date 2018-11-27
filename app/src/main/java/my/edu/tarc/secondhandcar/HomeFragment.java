package my.edu.tarc.secondhandcar;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private Button buttonSearch;
    private ProgressBar loadMain;
    private RecyclerView recyclerViewMainCar, recyclerViewReco;
    private ArrayList<String> mCarName = new ArrayList<>();
    private ArrayList<String> mCarImage = new ArrayList<>();
    private ArrayList<String> mCarId = new ArrayList<>();
    private ArrayList<String> mCarBrand = new ArrayList<>();
    private ArrayList<String> mCarPrice = new ArrayList<>();
    private ArrayList<String> mCarColor = new ArrayList<>();
    private ArrayList<String> mCarDesc = new ArrayList<>();
    private ArrayList<String> mCarYear = new ArrayList<>();
    private ArrayList<String> mCarMile = new ArrayList<>();
    private ArrayList<String> mCarType = new ArrayList<>();
    private ArrayList<String> mDealerID = new ArrayList<>();
    private ArrayList<String> mStatus = new ArrayList<>();


    private ArrayList<String> mCarName1 = new ArrayList<>();
    private ArrayList<String> mCarImage1 = new ArrayList<>();
    private ArrayList<String> mCarId1 = new ArrayList<>();
    private ArrayList<String> mCarBrand1 = new ArrayList<>();
    private ArrayList<String> mCarPrice1 = new ArrayList<>();
    private ArrayList<String> mCarColor1 = new ArrayList<>();
    private ArrayList<String> mCarDesc1 = new ArrayList<>();
    private ArrayList<String> mCarYear1 = new ArrayList<>();
    private ArrayList<String> mCarMile1 = new ArrayList<>();
    private ArrayList<String> mCarType1 = new ArrayList<>();
    private ArrayList<String> mDealerID1 = new ArrayList<>();
    private ArrayList<String> mStatus1 = new ArrayList<>();
    private ArrayList<String> mDiscount1 = new ArrayList<>();
    private String Url = "https://dewy-minuses.000webhostapp.com/CustGetCar.php";


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        buttonSearch = (Button) v.findViewById(R.id.buttonSearch);
        recyclerViewMainCar = (RecyclerView) v.findViewById(R.id.recyclerViewMainCar);
        recyclerViewReco = (RecyclerView) v.findViewById(R.id.recyclerViewReco);
        loadMain = (ProgressBar) v.findViewById(R.id.loadMain);


        clearAll();
        LoadPic(getView());

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(getActivity(), searchCarActivity.class);
                startActivity(searchIntent);
            }
        });
        return v;
    }

    private void clearAll() {
        mCarName.clear();
        mCarImage.clear();
        mCarId.clear();
        mCarBrand.clear();
        mCarPrice.clear();
        mCarColor.clear();
        mCarDesc.clear();
        mCarYear.clear();
        mCarMile.clear();
        mStatus.clear();
        mCarType.clear();
        mDealerID.clear();

        mCarName1.clear();
        mCarImage1.clear();
        mCarId1.clear();
        mCarBrand1.clear();
        mCarPrice1.clear();
        mCarColor1.clear();
        mCarDesc1.clear();
        mCarYear1.clear();
        mCarMile1.clear();
        mStatus1.clear();
        mCarType1.clear();
        mDealerID1.clear();
        mDiscount1.clear();
    }

    private void initRecyclerView(View v) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        CarAdapter adapter = new CarAdapter(mCarName, mCarImage, mCarId, mCarBrand, mCarPrice, mCarColor, mCarDesc, mCarType, mDealerID, mStatus, mCarYear, mCarMile, getActivity());
        PromAdapter adapter1=new PromAdapter(mCarName1, mCarImage1, mCarId1, mCarBrand1, mCarPrice1, mCarColor1, mCarDesc1, mCarType1, mDealerID1, mStatus1, mCarYear1, mCarMile1,mDiscount1, getActivity());
        recyclerViewMainCar.setLayoutManager(layoutManager);
        recyclerViewMainCar.setAdapter(adapter);
        recyclerViewReco.setAdapter(adapter1);


    }

    private void LoadPic(final View v) {

        loadMain.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("car");
                    JSONArray jsonArray1 = jsonObject.getJSONArray("promotion");
                    if (success.equals("1")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            //follow index
                            String carID = object.getString("id");
                            String name = object.getString("name");
                            String image_data = object.getString("imageUrl");
                            String brand = object.getString("brand");
                            String price = object.getString("price");
                            String color = object.getString("color");
                            String desc = object.getString("desc");
                            String year = object.getString("year");
                            String mileage = object.getString("mileage");
                            String carStatus = object.getString("status");
                            String carType = object.getString("type");
                            String dealerID = object.getString("dealerID");


                            mStatus.add(carStatus);
                            mCarType.add(carType);
                            mDealerID.add(dealerID);
                            mCarName.add(name);
                            mCarImage.add(image_data);
                            mCarId.add(carID);
                            mCarBrand.add(brand);
                            mCarPrice.add(price);
                            mCarColor.add(color);
                            mCarDesc.add(desc);
                            mCarYear.add(year);
                            mCarMile.add(mileage);

                        }

                        for (int i = 0; i < jsonArray1.length(); i++) {
                            JSONObject object = jsonArray1.getJSONObject(i);

                            //follow index
                            String carID = object.getString("id");
                            String name = object.getString("name");
                            String image_data = object.getString("imageUrl");
                            String brand = object.getString("brand");
                            String price = object.getString("price");
                            String color = object.getString("color");
                            String desc = object.getString("desc");
                            String year = object.getString("year");
                            String mileage = object.getString("mileage");
                            String carStatus = object.getString("status");
                            String carType = object.getString("type");
                            String dealerID = object.getString("dealerID");
                            String discountRate = object.getString("discountRate");
                            String endDate = object.getString("endDate");

                            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date date = formatter1.parse(endDate);
                                Date today = Calendar.getInstance().getTime();
                                if (date.after(today)) {
                                    mStatus1.add(carStatus);
                                    mCarType1.add(carType);
                                    mDealerID1.add(dealerID);
                                    mCarName1.add(name);
                                    mCarImage1.add(image_data);
                                    mCarId1.add(carID);
                                    mCarBrand1.add(brand);
                                    mCarPrice1.add(price);
                                    mCarColor1.add(color);
                                    mCarDesc1.add(desc);
                                    mCarYear1.add(year);
                                    mCarMile1.add(mileage);
                                    mDiscount1.add(discountRate);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }


                        loadMain.setVisibility(View.GONE);
                        initRecyclerView(v);

                    } else {
                        Toast.makeText(getActivity(), "No car in your list", Toast.LENGTH_LONG).show();
                        loadMain.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                    loadMain.setVisibility(View.GONE);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error " + error.toString(), Toast.LENGTH_LONG).show();
                        loadMain.setVisibility(View.GONE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

}
