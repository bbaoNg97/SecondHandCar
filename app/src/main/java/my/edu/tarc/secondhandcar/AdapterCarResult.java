package my.edu.tarc.secondhandcar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Bbao on 17/10/2018.
 */

public class AdapterCarResult extends ArrayAdapter<String>{

    private final Context context;
    private final String[] carNames;
    private final Integer[] carImages;


    public AdapterCarResult(@NonNull Context context, Integer[] IMAGES, String[] NAMES) {
        super(context, R.layout.content_search_result);

        this.carImages=IMAGES;
        this.carNames=NAMES;
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
        View v=inflater.inflate(R.layout.content_search_result,null,true);
        TextView carResult=(TextView)v.findViewById(R.id.textViewCarResult);
        ImageView imCarResult=(ImageView)v.findViewById(R.id.imageViewCarResult);

        imCarResult.setImageResource(carImages[position]);
        carResult.setText(carNames[position]);

        return v;
    }




}
