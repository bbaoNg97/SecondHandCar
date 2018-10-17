package my.edu.tarc.secondhandcar;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class SearchCarResultActivity extends AppCompatActivity {

    private ListView listViewCarResult;
    private Integer [] IMAGES={R.drawable.test1,R.drawable.test3,R.drawable.test4,R.drawable.test5,R.drawable.test6};
    private String[] NAMES ={"Proton Saga","Proton Vira","Honda Civic", "Honda City","Myvi"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_car_result);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.car_result_action_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CarResultAdapter carResultAdapter=new CarResultAdapter(this,IMAGES,NAMES);
        listViewCarResult=(ListView)findViewById(R.id.listViewCarResult);
        listViewCarResult.setAdapter(carResultAdapter);


    }


}
