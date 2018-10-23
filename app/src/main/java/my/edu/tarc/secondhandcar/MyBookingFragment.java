package my.edu.tarc.secondhandcar;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MyBookingFragment extends Fragment {

    private ListView listViewMyBooking;
    private Integer [] bookingStatus={R.drawable.ic_action_red_status,R.drawable.ic_action_red_status,R.drawable.ic_action_red_status,R.drawable.ic_action_green_status,R.drawable.ic_action_green_status};
    private String[] CarNAMES ={"Proton Saga","Proton Vira","Honda Civic", "Honda City","Myvi"};
    private String[] bookingDates = {"5/5/2018","6/6/2018","7/7/2018","4/5/4444","12/12/2222"};
    private String[] bookingTimes = {"9.00am","10.00am","11.00am","12.00pm","1.00pm"};



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
        View v= inflater.inflate(R.layout.fragment_my_booking, container, false);

        //**got unreachable statement, is return problem
        AdapterMyBooking myBookingAdapter=new AdapterMyBooking(getContext(),bookingStatus,CarNAMES,bookingDates,bookingTimes);
        listViewMyBooking=(ListView)v.findViewById(R.id.listViewBooking);
        listViewMyBooking.setAdapter(myBookingAdapter);
        listViewMyBooking.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent bookingDetailIntent=new Intent(getActivity(),BookingDetailActivity.class);
                startActivity(bookingDetailIntent);
            }
        });
        return v;


    }
//for the action bar, add a refresh icon
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.action_bar_refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(getActivity().getApplicationContext(),"Refreshing...",Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }
}
