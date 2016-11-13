package com.studypact.studypact.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.studypact.studypact.R;
import com.studypact.studypact.adapter.PactListAdapter;
import com.studypact.studypact.util.Util;

import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        JSONObject pacts = Util.getJSONFromStore("pacts");
        if(pacts.length() > 0){
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.pact_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(new PactListAdapter(pacts));
        } else {
            findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
        }
    }

}
