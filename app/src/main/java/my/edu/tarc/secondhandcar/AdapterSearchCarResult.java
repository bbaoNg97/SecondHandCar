package my.edu.tarc.secondhandcar;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bbao on 17/10/2018.
 */

public class AdapterSearchCarResult extends ArrayAdapter<Car> {

    private Context mContext;
    private List<Car> cars = new ArrayList<>();
    private String strName, strImage, strPrice, strColor, strDesc, strYear, strCarStatus, strType, strMileage, strDealerID, strCarID;
    NumberFormat formatter = NumberFormat.getCurrencyInstance();


    public AdapterSearchCarResult(Context context, ArrayList<Car> car) {
        super(context, R.layout.content_search_result);
        mContext = context;
        cars = car;
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
        final ImageView imageViewdis = (ImageView) v.findViewById(R.id.imageViewdis);
        final TextView tvResultPrice = (TextView) v.findViewById(R.id.textViewResultPrice);
        final TextView tvResultYear = (TextView) v.findViewById(R.id.textViewResultYear);
        final ImageView imageViewTop = (ImageView) v.findViewById(R.id.imageViewTop);
        final TextView tvMileage = (TextView) v.findViewById(R.id.textViewSearchResultM);
        final Car currentCar = cars.get(position);
        double dis = Double.parseDouble(currentCar.getDISCOUNT());
        Double dPrice = (double) currentCar.getPRICES();
        Glide.with(mContext)
                .asBitmap()
                .load(currentCar.getCAR_PHOTOS())
                .into(imCarResult);

        if (dis > 0) {
            dPrice = dPrice - (dPrice * dis / 100);
            imageViewdis.setVisibility(View.VISIBLE);
            tvResultPrice.setTextColor(ContextCompat.getColor(mContext, R.color.Red));
        }else {
            imageViewdis.setVisibility(View.GONE);
            tvResultPrice.setTextColor(ContextCompat.getColor(mContext, R.color.Black));
        }


        carResult.setText(currentCar.getNAMES());
        imageViewTop.setVisibility(View.GONE);

        strYear = currentCar.getYEARS() + "";
        tvResultYear.setText(strYear);
        strMileage = String.valueOf(currentCar.getMILEAGES());
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###");
        strMileage=decimalFormat.format(Double.parseDouble(strMileage));
        strPrice = formatter.format(dPrice);
        tvResultPrice.setText(strPrice);


        tvMileage.setText(strMileage+" KM");
        ConstraintLayout searchResultLayout = (ConstraintLayout) v.findViewById(R.id.searchResultLayout);
        searchResultLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strName = carResult.getText().toString();
                strImage = currentCar.getCAR_PHOTOS();
                strPrice = currentCar.getPRICES() + "";
                strColor = currentCar.getCOLORS();
                strMileage = String.valueOf(currentCar.getMILEAGES());
                strYear = currentCar.getYEARS() + "";
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
