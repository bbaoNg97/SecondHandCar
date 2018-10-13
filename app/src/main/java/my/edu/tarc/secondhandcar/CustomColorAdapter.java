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



public class CustomColorAdapter extends ArrayAdapter<String> {

    String[] spinnerColorName;
    int[] spinnerColor;

    Context mContext;

    public CustomColorAdapter(@NonNull Context context, String[] titles, int[] images) {
        super(context, R.layout.custom_spinner_color);
        this.spinnerColorName = titles;
        this.spinnerColor = images;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return spinnerColorName.length;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder= new ViewHolder();
        if (convertView == null ){
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.custom_spinner_color,parent,false);
            mViewHolder.mColor=(ImageView)convertView.findViewById(R.id.ivColor);
            mViewHolder.mColorName=(TextView)convertView.findViewById(R.id.tvColorName);
            convertView.setTag(mViewHolder);
        }else {
            mViewHolder=(ViewHolder)convertView.getTag();
        }
        mViewHolder.mColor.setImageResource(spinnerColor[position]);
        mViewHolder.mColorName.setText(spinnerColorName[position]);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    private static class ViewHolder {
        ImageView mColor;
        TextView mColorName;

    }
}
