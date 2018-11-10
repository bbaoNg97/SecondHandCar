package my.edu.tarc.secondhandcar;

import android.content.Context;
import android.content.Intent;
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

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by Bbao on 17/10/2018.
 */

public class AdapterCarResult extends ArrayAdapter<String>{

    private Context context;
    private ArrayList<String> carNames=new ArrayList<>();
    private ArrayList<String> carImages=new ArrayList<>();
    private ArrayList<String> car_prices = new ArrayList<>();
    private ArrayList<String> car_colors = new ArrayList<>();
    private ArrayList<String> car_descs = new ArrayList<>();
    private ArrayList<String> car_years = new ArrayList<>();
    private ArrayList<String> car_status = new ArrayList<>();
    private ArrayList<String> car_types = new ArrayList<>();
    private ArrayList<String> mileages = new ArrayList<>();
    private ArrayList<String> dealerID = new ArrayList<>();
    private String strPrice,strColor,strDesc,strYear,strCarStatus,strType,strMileage,strDealerID;
    NumberFormat formatter = NumberFormat.getCurrencyInstance();

    public AdapterCarResult(Context context,  ArrayList<String> carNames, ArrayList<String> carImages, ArrayList<String> car_prices, ArrayList<String> car_colors, ArrayList<String> car_descs, ArrayList<String> car_years, ArrayList<String> car_status, ArrayList<String> car_types, ArrayList<String> mileages,ArrayList<String> dealerID) {
        super(context, R.layout.content_search_result);
        this.context = context;
        this.carNames = carNames;
        this.carImages = carImages;
        this.car_prices = car_prices;
        this.car_colors = car_colors;
        this.car_descs = car_descs;
        this.car_years = car_years;
        this.car_status = car_status;
        this.car_types = car_types;
        this.mileages = mileages;
        this.dealerID=dealerID;
    }

    @Override
    public int getCount() {
        return carNames.size();
   }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v=inflater.inflate(R.layout.content_search_result,null,true);
        final TextView carResult=(TextView)v.findViewById(R.id.textViewCarResult);
        final ImageView imCarResult=(ImageView)v.findViewById(R.id.imageViewCarResult);

        Glide.with(context)
                .asBitmap()
                .load(carImages.get(position))
                .into(imCarResult);

        carResult.setText(carNames.get(position));

        ConstraintLayout searchResultLayout=(ConstraintLayout)v.findViewById(R.id.searchResultLayout);
        searchResultLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strCarStatus=car_status.get(position);
                strColor=car_colors.get(position);
                strDesc=car_descs.get(position);
                strMileage=mileages.get(position);
                strType=car_types.get(position);
                strYear=car_years.get(position);
                strDealerID=dealerID.get(position);

                Double dPrice = Double.parseDouble(car_prices.get(position));
                strPrice=formatter.format(dPrice);

                Intent carIntent = new Intent(context, CarActivity.class);
                carIntent.putExtra("carName",carResult.getText().toString());
                //carIntent.putExtra("carPhoto",imCarResult.toString());
                carIntent.putExtra("carStatus",strCarStatus);
                carIntent.putExtra("carColor",strColor);
                carIntent.putExtra("carDesc",strDesc);
                carIntent.putExtra("carMileage",strMileage);
                carIntent.putExtra("carType",strType);
                carIntent.putExtra("carYear",strYear);
                carIntent.putExtra("carPrice",strPrice);
                carIntent.putExtra("dealerID",strDealerID);
                context.startActivity(carIntent);
            }
        });
        return v;
    }




}
