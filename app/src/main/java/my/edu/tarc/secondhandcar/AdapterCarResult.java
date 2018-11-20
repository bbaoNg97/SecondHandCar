package my.edu.tarc.secondhandcar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bbao on 17/10/2018.
 */

public class AdapterCarResult extends ArrayAdapter<Car> {

    private Context mContext;
    private List<Car> cars = new ArrayList<>();
    private ArrayList<String> carNames = new ArrayList<>();
    private ArrayList<String> carImages = new ArrayList<>();
    private ArrayList<String> car_prices = new ArrayList<>();
    private ArrayList<String> car_colors = new ArrayList<>();
    private ArrayList<String> car_descs = new ArrayList<>();
    private ArrayList<String> car_years = new ArrayList<>();
    private ArrayList<String> car_status = new ArrayList<>();
    private ArrayList<String> car_types = new ArrayList<>();
    private ArrayList<String> mileages = new ArrayList<>();
    private ArrayList<String> dealerID = new ArrayList<>();
    private ArrayList<String> carID = new ArrayList<>();
    private String strName, strImage, strPrice, strColor, strDesc, strYear, strCarStatus, strType, strMileage, strDealerID, strCarID;
    NumberFormat formatter = NumberFormat.getCurrencyInstance();

    public AdapterCarResult(Context context, ArrayList<Car> car) {
        super(context, R.layout.content_search_result);
        mContext = context;
        cars = car;
    }


    public AdapterCarResult(Context context, ArrayList<String> carNames, ArrayList<String> car_prices, ArrayList<String> car_colors, ArrayList<String> car_descs, ArrayList<String> car_years, ArrayList<String> car_status, ArrayList<String> car_types, ArrayList<String> mileages, ArrayList<String> carImages, ArrayList<String> dealerID, ArrayList<String> carID) {
        super(context, R.layout.content_search_result);
        mContext = context;
        this.carNames = carNames;
        this.carImages = carImages;
        this.car_prices = car_prices;
        this.car_colors = car_colors;
        this.car_descs = car_descs;
        this.car_years = car_years;
        this.car_status = car_status;
        this.car_types = car_types;
        this.mileages = mileages;
        this.dealerID = dealerID;
        this.carID = carID;
    }

    @Override
    public int getCount() {
        return cars.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.content_search_result, null, true);
        final TextView carResult = (TextView) v.findViewById(R.id.textViewCarResult);
        final ImageView imCarResult = (ImageView) v.findViewById(R.id.imageViewCarResult);

      final Car currentCar = cars.get(position);
        Glide.with(mContext)
                .asBitmap()
                .load( currentCar.getCAR_PHOTOS())
                .into(imCarResult);

        carResult.setText(currentCar.getNAMES());

        ConstraintLayout searchResultLayout = (ConstraintLayout) v.findViewById(R.id.searchResultLayout);
        searchResultLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strName = carResult.getText().toString();
                strImage = currentCar.getCAR_PHOTOS();
                Double dPrice = Double.parseDouble(currentCar.getPRICES());
                strPrice = formatter.format(dPrice);
                strColor = currentCar.getCOLORS();
                strMileage = currentCar.getMILEAGES();
                strYear = currentCar.getYEARS();
                strDealerID = currentCar.getDEALER_ID();
                strDesc = currentCar.getDESCS();
                strCarID = currentCar.getCAR_ID();
                strCarStatus = currentCar.getCAR_STATUS();

                strType = currentCar.getCAR_TYPES();

                Intent carIntent = new Intent(mContext, CarActivity.class);
                carIntent.putExtra("carName", strName);
                carIntent.putExtra("carPhoto", strImage);
                carIntent.putExtra("carColor", strColor);
                carIntent.putExtra("carDesc", strDesc);
                carIntent.putExtra("carMileage", strMileage);
                carIntent.putExtra("carType", strType);
                carIntent.putExtra("carYear", strYear);
                carIntent.putExtra("carPrice", strPrice);
                carIntent.putExtra("dealerID", strDealerID);
                carIntent.putExtra("carStatus", strCarStatus);
                carIntent.putExtra("carID", strCarID);
                mContext.startActivity(carIntent);
            }
        });
        return v;
    }


}
