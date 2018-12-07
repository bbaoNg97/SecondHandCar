package my.edu.tarc.secondhandcar;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MyBookingFragment extends Fragment {
    private ListView listViewMyBooking;
    private ProgressBar downloading;
    private TextView tvCaption, tvCaption1;
    private ArrayList<String> arrBookingStatus = new ArrayList<>();
    private ArrayList<String> arrCarNAMES = new ArrayList<>();
    private ArrayList<String> arrBookingDates = new ArrayList<>();
    private ArrayList<String> arrBookingTimes = new ArrayList<>();
    private ArrayList<String> arrPrice = new ArrayList<>();
    private ArrayList<String> arrCarPhoto = new ArrayList<>();
    private ArrayList<String> arrAgentID = new ArrayList<>();
    private ArrayList<String> arrAcceptDate = new ArrayList<>();
    private ArrayList<String> arrAcceptTime = new ArrayList<>();
    private ArrayList<String> arrAppID = new ArrayList<>();
    private ArrayList<String> arrDealerLocation = new ArrayList<>();
    private ArrayList<String> arrDealerID = new ArrayList<>();
    private TextView tvTips1Met, tvTips2Booked, tvTips3Cancelled, tvTips4Pending;
    private ImageView iv1Met, iv2Booked, iv3Cancelled, iv4Pending;

    private Button btnSearch;

    SharedPreferences sharePref;

    private String custID;

    public MyBookingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_booking, container, false);
        downloading = (ProgressBar) v.findViewById(R.id.downloadBooking);
        btnSearch = (Button) v.findViewById(R.id.btnSearch);
        tvCaption = (TextView) v.findViewById(R.id.tvNoBooking1);
        tvCaption1 = (TextView) v.findViewById(R.id.tvNoBooking2);
        tvTips1Met = (TextView) v.findViewById(R.id.textViewTips1);
        tvTips2Booked = (TextView) v.findViewById(R.id.textViewTips2);
        tvTips3Cancelled = (TextView) v.findViewById(R.id.textViewTips3);
        tvTips4Pending = (TextView) v.findViewById(R.id.textViewTips4);
        iv1Met = (ImageView) v.findViewById(R.id.imageViewG);
        iv2Booked = (ImageView) v.findViewById(R.id.imageViewR);
        iv3Cancelled = (ImageView) v.findViewById(R.id.imageViewCr);
        iv4Pending = (ImageView) v.findViewById(R.id.imageViewP);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), searchCarActivity.class);
                startActivity(intent);
            }
        });

        listViewMyBooking = (ListView) v.findViewById(R.id.listViewBooking);
        return v;

    }

    private void getAppointment(final Context context, final String url) {
        //TODO: pending also needed
        clearTips();

        clearView();
        downloading.setVisibility(View.VISIBLE);
        btnSearch.setEnabled(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            //String message = jsonObject.getString("message");
                            JSONArray jsonArray = jsonObject.getJSONArray("BOOKING");
                            //if HAVE RECORD
                            if (success.equals("1")) {
                                //retrive the record
                                tvCaption.setVisibility(View.INVISIBLE);
                                tvCaption1.setVisibility(View.INVISIBLE);
                                btnSearch.setVisibility(View.INVISIBLE);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject userResponse = jsonArray.getJSONObject(i);

                                    String carName = userResponse.getString("carName");
                                    String price = userResponse.getString("price");
                                    String appID = userResponse.getString("appID");
                                    String carID = userResponse.getString("carID");
                                    String agentID = userResponse.getString("agentID");
                                    String appDate = userResponse.getString("appDate");
                                    String appTime = userResponse.getString("appTime");
                                    String appStatus = userResponse.getString("appStatus");
                                    String carPhoto = userResponse.getString("car_photo");
                                    String acceptDate = userResponse.getString("acceptDate");
                                    String accptTime = userResponse.getString("acceptTime");
                                    String dealerLocation = userResponse.getString("dealerLocation");
                                    String dealerID = userResponse.getString("dealerID");
                                    arrCarNAMES.add(carName);
                                    arrBookingDates.add(appDate);
                                    arrBookingTimes.add(appTime);
                                    arrBookingStatus.add(appStatus);
                                    arrPrice.add(price);
                                    arrCarPhoto.add(carPhoto);
                                    arrAgentID.add(agentID);
                                    arrAcceptDate.add(acceptDate);
                                    arrAcceptTime.add(accptTime);
                                    arrAppID.add(appID);
                                    arrDealerLocation.add(dealerLocation);
                                    arrDealerID.add(dealerID);

                                }
                                initListVIew(context);
                                showTips();

                                downloading.setVisibility(View.GONE);
                            } else {
                                tvCaption.setVisibility(View.VISIBLE);
                                tvCaption1.setVisibility(View.VISIBLE);
                                btnSearch.setVisibility(View.VISIBLE);
                                btnSearch.setEnabled(true);
                                downloading.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            checkError(e,getContext());
                            downloading.setVisibility(View.GONE);
                            e.printStackTrace();
                            btnSearch.setEnabled(true);

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        checkError(error,getContext());
                        downloading.setVisibility(View.GONE);
                        btnSearch.setEnabled(true);
                        showTips();

                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //LHS is from php, RHS is getText there

                params.put("custID", custID);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);


    }

    private void initListVIew(Context context) {
        AdapterMyBooking myBookingAdapter = new AdapterMyBooking(context, arrBookingStatus, arrCarNAMES, arrBookingDates, arrBookingTimes, arrPrice, arrAcceptDate, arrAcceptTime, arrCarPhoto, arrAgentID, arrAppID, arrDealerLocation, arrDealerID);
        listViewMyBooking.setAdapter(myBookingAdapter);
    }

    //for the action bar, add a refresh icon
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.action_bar_refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (custID == null) {
            tvCaption.setVisibility(View.VISIBLE);
            tvCaption1.setVisibility(View.VISIBLE);
            btnSearch.setEnabled(true);
        } else {
            getAppointment(getActivity(), getString(R.string.get_my_booking_url));
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearView() {
        arrBookingStatus.clear();
        arrBookingTimes.clear();
        arrBookingDates.clear();
        arrCarNAMES.clear();
        arrPrice.clear();
        arrAcceptDate.clear();
        arrAcceptTime.clear();
        arrCarPhoto.clear();
        arrAgentID.clear();
        arrAppID.clear();
    }

    @Override
    public void onResume() {
        super.onResume();

        sharePref = getActivity().getSharedPreferences("My_Pref", Context.MODE_PRIVATE);
        custID = sharePref.getString("custID", null);

        if (custID == null) {
            downloading.setVisibility(View.GONE);
            tvCaption.setVisibility(View.VISIBLE);
            tvCaption1.setVisibility(View.VISIBLE);
            btnSearch.setEnabled(true);
        } else {
            getAppointment(getActivity(), getString(R.string.get_my_booking_url));
        }
    }

    private void showTips() {
        tvTips1Met.setVisibility(View.VISIBLE);
        tvTips2Booked.setVisibility(View.VISIBLE);
        tvTips3Cancelled.setVisibility(View.VISIBLE);
        tvTips4Pending.setVisibility(View.VISIBLE);
        iv1Met.setVisibility(View.VISIBLE);
        iv2Booked.setVisibility(View.VISIBLE);
        iv3Cancelled.setVisibility(View.VISIBLE);
        iv4Pending.setVisibility(View.VISIBLE);
    }

    private void clearTips() {
        tvTips1Met.setVisibility(View.GONE);
        tvTips2Booked.setVisibility(View.GONE);
        tvTips3Cancelled.setVisibility(View.GONE);
        tvTips4Pending.setVisibility(View.GONE);
        iv1Met.setVisibility(View.GONE);
        iv2Booked.setVisibility(View.GONE);
        iv3Cancelled.setVisibility(View.GONE);
        iv4Pending.setVisibility(View.GONE);
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
