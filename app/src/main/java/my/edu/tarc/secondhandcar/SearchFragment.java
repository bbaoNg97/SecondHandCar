package my.edu.tarc.secondhandcar;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;


public class SearchFragment extends Fragment {

Toolbar toolbar;

    public SearchFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v=inflater.inflate(R.layout.fragment_search, container, false);
        //toolbar=(Toolbar)v.findViewById(R.id.toolbar1);


        //setHasOptionsMenu(true);
        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


        return v;

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.nav_item,menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle item selection
        switch (item.getItemId()){
            case R.id.nav_home:

                //do stg
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
