package my.edu.tarc.secondhandcar;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Bbao on 19/10/2018.
 */

public class AdapterMyBooking extends ArrayAdapter<String> {

    private final Context context;
    private final Integer[] bookingStatus;
    private final String[] carNames,dates,times;

    public AdapterMyBooking(@NonNull Context context,  Integer[] bookingStatus, String[] carNames, String[] dates, String[] times) {
        super(context, R.layout.content_my_booking);
        this.bookingStatus = bookingStatus;
        this.carNames = carNames;
        this.dates = dates;
        this.times = times;
        this.context=context;
    }

    @Override
    public int getCount() {
        return carNames.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v=inflater.inflate(R.layout.content_my_booking,null,true);
        TextView carName=(TextView)v.findViewById(R.id.textViewBookingCarName);
        ImageView imBookingStatus=(ImageView)v.findViewById(R.id.imageViewBookingStatus);
        TextView bookingDate=(TextView)v.findViewById(R.id.textViewBookingDate);
        TextView bookingTime=(TextView)v.findViewById(R.id.textViewBookingTime);

        imBookingStatus.setImageResource(bookingStatus[position]);
        carName.setText(carNames[position]);
        bookingDate.setText(dates[position]);
        bookingTime.setText(times[position]);


        return v;
    }
}
