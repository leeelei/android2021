package com.example.litepaltest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button createDatabase=(Button)findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connector.getDatabase();
            }
        });

        Button addData=(Button)findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book=new Book();
                book.setName("The Da Vinci Code");
                book.setAuthor("Dan Brown");
                book.setPages(454);
                book.setPrice(16.96);
                book.setPress("Unkonw");
                book.save();
            }
        });

        Button updateData=(Button)findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Book book=new Book();
               book.setName("The Lost Symbol");
               book.setAuthor("Dan Brown");
               book.setPages(510);
               book.setPrice(19.95);
               book.setPress("Unknow");
               book.save();
               book.setPrice(10.99);
               book.save();
            }
        });
        Button deleteButton = (Button)findViewById(R.id.delete_data);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LitePal.deleteAll(Book.class,"price < ?","15");

            }
        });

        Button queryButton=(Button)findViewById(R.id.query_data);
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Book> books= LitePal.findAll(Book.class);
                for(Book book:books){
                    Log.d("MainActiviy","book name is "+book.getName());
                    Log.d("MainActiviy","book author is "+book.getAuthor());
                    Log.d("MainActiviy","book pages is "+book.getPages());
                    Log.d("MainActiviy","book price is "+book.getPrice());
                    Log.d("MainActiviy","book press is "+book.getPress());
                }
            }
        });
    }
}