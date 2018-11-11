package my.edu.tarc.secondhandcar;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;

/**
 * Created by Alex on 11/2/2018.
 */

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {
    private static final String TAG = "CarAdapter";
    private ArrayList<String> mCarName = new ArrayList<>();
    private ArrayList<String> mCarImage = new ArrayList<>();
    private ArrayList<String> mCarId = new ArrayList<>();
    private ArrayList<String> mCarBrand = new ArrayList<>();
    private ArrayList<String> mCarPrice = new ArrayList<>();
    private ArrayList<String> mCarColor = new ArrayList<>();
    private ArrayList<String> mCarDesc = new ArrayList<>();
    private ArrayList<String> mCarYear = new ArrayList<>();
    private ArrayList<String> mCarMile = new ArrayList<>();
    private Context mContext;
    private String price;
    private Double dPrice;
    NumberFormat formatter = NumberFormat.getCurrencyInstance();

    public CarAdapter(ArrayList<String> mCarName, ArrayList<String> mCarImage, ArrayList<String> mCarId, ArrayList<String> mCarBrand, ArrayList<String> mCarPrice, ArrayList<String> mCarColor, ArrayList<String> mCarDesc, ArrayList<String> mCarYear, ArrayList<String> mCarMile, Context mContext) {
        this.mCarName = mCarName;
        this.mCarImage = mCarImage;
        this.mCarId = mCarId;
        this.mCarBrand = mCarBrand;
        this.mCarPrice = mCarPrice;
        this.mCarColor = mCarColor;
        this.mCarDesc = mCarDesc;
        this.mCarYear = mCarYear;
        this.mCarMile = mCarMile;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_adap, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext)
                .asBitmap()
                .load(mCarImage.get(position))
                .into(holder.imageViewShCar);

        holder.textViewCarName.setText(mCarBrand.get(position) + " " + mCarName.get(position));

        dPrice = Double.parseDouble(mCarPrice.get(position));
        price = formatter.format(dPrice);

        holder.textViewCarPrice.setText(price);


        holder.LayoutCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick:Clicked on: " + mCarName.get(position));
                Toast.makeText(mContext, mCarName.get(position), Toast.LENGTH_LONG).show();

                dPrice = Double.parseDouble(mCarPrice.get(position));
                price = formatter.format(dPrice);

                Intent intent = new Intent(mContext, CarActivity.class);
                intent.putExtra("carID", mCarId.get(position));
                intent.putExtra("carName", mCarBrand.get(position) + " " + mCarName.get(position));
                intent.putExtra("carPhoto", mCarImage.get(position));;
                intent.putExtra("carPrice", price);
                intent.putExtra("carColor", mCarColor.get(position));
                intent.putExtra("carDesc", mCarDesc.get(position));
                intent.putExtra("carYear", mCarYear.get(position));
                intent.putExtra("carMileage", mCarMile.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCarId.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCarName, textViewCarPrice;
        ImageView imageViewShCar;
        ConstraintLayout LayoutCar;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewCarName = itemView.findViewById(R.id.textViewCarName);
            textViewCarPrice = itemView.findViewById(R.id.textViewCarPrice);
            imageViewShCar = itemView.findViewById(R.id.imageViewShCar);
            LayoutCar = itemView.findViewById(R.id.LayoutCar);
        }
    }

}
