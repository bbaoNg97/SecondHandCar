package my.edu.tarc.secondhandcar;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class SearchCarResultActivity extends AppCompatActivity {

    private ListView listViewCarResult;
    private Integer [] IMAGES={R.drawable.t1,R.drawable.t2,R.drawable.t3};
    private String[] NAMES ={"Proton Saga","Proton Vira","Honda Civic",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_car_result);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.car_result_action_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        AdapterCarResult adapterCarResult =new AdapterCarResult(this,IMAGES,NAMES);
        listViewCarResult=(ListView)findViewById(R.id.listViewCarResult);
        listViewCarResult.setAdapter(adapterCarResult);
        listViewCarResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent carIntent=new Intent(SearchCarResultActivity.this,CarActivity.class);
                startActivity(carIntent);
            }
        });

    }


}
