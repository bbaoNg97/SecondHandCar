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

import java.util.ArrayList;
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

    private void initRecyclerView(View v) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        CarAdapter adapter = new CarAdapter(mCarName, mCarImage, mCarId, mCarBrand, mCarPrice, mCarColor, mCarDesc, mCarType,mDealerID,mStatus,mCarYear, mCarMile, getActivity());
        recyclerViewMainCar.setLayoutManager(layoutManager);
        recyclerViewMainCar.setAdapter(adapter);
        recyclerViewReco.setAdapter(adapter);


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
                            String carStatus=object.getString("status");
                            String carType=object.getString("type");
                            String dealerID=object.getString("dealerID");



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
