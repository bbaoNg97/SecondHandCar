package my.edu.tarc.secondhandcar;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by Alex on 11/27/2018.
 */

public class PromAdapter extends RecyclerView.Adapter<PromAdapter.ViewHolder> {
    private static final String TAG = "PromAdapter";
    private ArrayList<String> mCarName = new ArrayList<>();
    private ArrayList<String> mCarImage = new ArrayList<>();
    private ArrayList<String> mCarId = new ArrayList<>();
    private ArrayList<String> mCarBrand = new ArrayList<>();
    private ArrayList<String> mCarPrice = new ArrayList<>();
    private ArrayList<String> mCarColor = new ArrayList<>();
    private ArrayList<String> mCarDesc = new ArrayList<>();
    private ArrayList<String> mCarType = new ArrayList<>();
    private ArrayList<String> mDealerID = new ArrayList<>();
    private ArrayList<String> mStatus = new ArrayList<>();
    private ArrayList<String> mCarYear = new ArrayList<>();
    private ArrayList<String> mCarMile = new ArrayList<>();
    private ArrayList<String> mDiscount = new ArrayList<>();
    private Context mContext;
    private String price, Nprice;
    private Double dPrice, oPrice;
    NumberFormat formatter = NumberFormat.getCurrencyInstance();

    public PromAdapter(ArrayList<String> mCarName, ArrayList<String> mCarImage, ArrayList<String> mCarId, ArrayList<String> mCarBrand, ArrayList<String> mCarPrice, ArrayList<String> mCarColor, ArrayList<String> mCarDesc, ArrayList<String> mCarType, ArrayList<String> mDealerID, ArrayList<String> mStatus, ArrayList<String> mCarYear, ArrayList<String> mCarMile, ArrayList<String> mDiscount, Context mContext) {
        this.mCarName = mCarName;
        this.mCarImage = mCarImage;
        this.mCarId = mCarId;
        this.mCarBrand = mCarBrand;
        this.mCarPrice = mCarPrice;
        this.mCarColor = mCarColor;
        this.mCarDesc = mCarDesc;
        this.mCarType = mCarType;
        this.mDealerID = mDealerID;
        this.mStatus = mStatus;
        this.mCarYear = mCarYear;
        this.mCarMile = mCarMile;
        this.mDiscount = mDiscount;
        this.mContext = mContext;
    }


    @Override
    public PromAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prom_adap, parent, false);
        PromAdapter.ViewHolder holder = new PromAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PromAdapter.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext)
                .asBitmap()
                .load(mCarImage.get(position))
                .into(holder.imageViewShProCar);

        holder.textViewProCarName.setText(mCarBrand.get(position) + " " + mCarName.get(position));

        dPrice = Double.parseDouble(mCarPrice.get(position));
        price = formatter.format(dPrice);
        Double Rate = Double.parseDouble(mDiscount.get(position));

        holder.textViewNormalPrice.setText(price);

        oPrice = dPrice - (dPrice * Rate / 100);
        Nprice = formatter.format(oPrice);
        holder.textViewProPrice.setText(Nprice);

        holder.LayoutPromCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dPrice = Double.parseDouble(mCarPrice.get(position));
                price = formatter.format(dPrice);

                Intent intent = new Intent(mContext, CarActivity.class);
                intent.putExtra("carID", mCarId.get(position));
                intent.putExtra("carName", mCarBrand.get(position) + " " + mCarName.get(position));
                intent.putExtra("carPhoto", mCarImage.get(position));
                intent.putExtra("carPrice", Nprice);
                intent.putExtra("carColor", mCarColor.get(position));
                intent.putExtra("carDesc", mCarDesc.get(position));
                intent.putExtra("carYear", mCarYear.get(position));
                intent.putExtra("carMileage", mCarMile.get(position));
                intent.putExtra("dealerID", mDealerID.get(position));
                intent.putExtra("carType", mCarType.get(position));
                intent.putExtra("carStatus", mStatus.get(position));
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mCarId.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewProCarName, textViewNormalPrice, textViewProPrice;
        ImageView imageViewShProCar;
        ConstraintLayout LayoutPromCar;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewProCarName = (TextView) itemView.findViewById(R.id.textViewProCarName);
            textViewNormalPrice = (TextView) itemView.findViewById(R.id.textViewNormalPrice);
            textViewProPrice = (TextView) itemView.findViewById(R.id.textViewProPrice);
            imageViewShProCar = (ImageView) itemView.findViewById(R.id.imageViewShProCar);
            LayoutPromCar = (ConstraintLayout) itemView.findViewById(R.id.LayoutPromCar);
        }
    }

}