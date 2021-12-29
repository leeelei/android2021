package com.coolweather.databasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;
    //@BindView(R.id.create_database)
    //Button createDatabase;
    /*
    @OnClick(R.id.create_database)
    public void onViewClicked(View view){
        dbHelper.getWritableDatabase();
    }
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper=new MyDatabaseHelper(this,"BookStore.db",null,2);
        Button createDatabase=(Button)findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.getWritableDatabase();
            }
        });

    }

}