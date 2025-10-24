package com.studypact.studypact.activity;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.studypact.studypact.R;
import com.studypact.studypact.adapter.AppGridAdapter;

public class ChooseAppsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_apps);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.app_grid);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        recyclerView.setAdapter(new AppGridAdapter(fab));


    }

}
