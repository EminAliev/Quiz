package com.example.aliev.quiz;

import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.app.ActionBar;

import com.example.aliev.quiz.datebases.DataBase;
import com.example.aliev.quiz.decoration.Decorator;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("QUIZ");
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycle_category);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        Adapter adapter = new Adapter(MainActivity.this, DataBase.getInst(this).getCategoriesFromDataBase());
        recyclerView.addItemDecoration(new Decorator(4));
        recyclerView.setAdapter(adapter);

    }
}
