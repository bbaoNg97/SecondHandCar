package my.edu.tarc.secondhandcar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Bbao on 19/10/2018.
 */

public class AdapterMyBooking extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> bookingStatus = new ArrayList<>();
    private ArrayList<String> carNames = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();
    private ArrayList<String> times = new ArrayList<>();
    private ArrayList<String> price = new ArrayList<>();
    private ArrayList<String> carPhoto = new ArrayList<>();
    private ArrayList<String> agentID = new ArrayList<>();
    private ArrayList<String> acceptDate = new ArrayList<>();
    private ArrayList<String> acceptTime = new ArrayList<>();
    private ArrayList<String> appID = new ArrayList<>();
    private String strPrice, strCarPhoto, strAgentID, strBookStatus, strAcceptDate, strAcceptTime,strAppID;

    public AdapterMyBooking(Context context, ArrayList<String> bookingStatus, ArrayList<String> carNames, ArrayList<String> dates, ArrayList<String> times, ArrayList<String> price, ArrayList<String> acceptDate, ArrayList<String> acceptTime, ArrayList<String> carPhoto, ArrayList<String> agentID,ArrayList<String> appID) {
        super(context, R.layout.content_my_booking);
        this.bookingStatus = bookingStatus;
        this.carNames = carNames;
        this.dates = dates;
        this.times = times;
        this.price = price;
        this.carPhoto = carPhoto;
        this.agentID = agentID;
        this.acceptDate = acceptDate;
        this.acceptTime = acceptTime;
        this.appID=appID;
        this.context = context;

    }

    @Override
    public int getCount() {
        return carNames.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.content_my_booking, null, true);
        final TextView carName = (TextView) v.findViewById(R.id.textViewBookingCarName);
        ImageView imBookingStatus = (ImageView) v.findViewById(R.id.imageViewBookingStatus);
        final TextView bookingDate = (TextView) v.findViewById(R.id.textViewBookingDate);
        final TextView bookingTime = (TextView) v.findViewById(R.id.textViewBookingTime);

        ConstraintLayout bookingLayout = (ConstraintLayout) v.findViewById(R.id.bookingLayout);


        carName.setText(carNames.get(position));
        bookingDate.setText(dates.get(position));
        bookingTime.setText(times.get(position));


        if (bookingStatus.get(position).equals("Met")) {
            imBookingStatus.setImageResource(R.drawable.ic_action_green_status);
        } else if (bookingStatus.get(position).equals("Booked")) {
            imBookingStatus.setImageResource(R.drawable.ic_action_red_status);
        } else {
            imBookingStatus.setImageResource(R.drawable.ic_action_cancel_status);
        }

        bookingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strPrice = price.get(position);
                strCarPhoto = carPhoto.get(position);
                strAgentID = agentID.get(position);
                strBookStatus = bookingStatus.get(position);
                strAcceptDate = acceptDate.get(position);
                strAcceptTime = acceptTime.get(position);
                strAppID=appID.get(position);
                Intent bookingDetailIntent = new Intent(context, BookingDetailActivity.class);
                bookingDetailIntent.putExtra("CarName", carName.getText().toString());
                bookingDetailIntent.putExtra("appDate", bookingDate.getText().toString());
                bookingDetailIntent.putExtra("appTime", bookingTime.getText().toString());
                bookingDetailIntent.putExtra("price", strPrice);
                bookingDetailIntent.putExtra("carPhoto", strCarPhoto);
                bookingDetailIntent.putExtra("agentID", strAgentID);
                bookingDetailIntent.putExtra("bookStatus", strBookStatus);
                bookingDetailIntent.putExtra("acceptDate", strAcceptDate);
                bookingDetailIntent.putExtra("acceptTime", strAcceptTime);
                bookingDetailIntent.putExtra("appID",strAppID);

                context.startActivity(bookingDetailIntent);
            }
        });


        return v;
    }
}
