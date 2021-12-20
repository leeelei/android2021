package com.example.listviewtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String[] data={"Apple","Banana","Orange","Watermelon","Pear","Grape","Pineapple","Strawberry","Cherry","Mango","Apple","Banana","Orange","Watermelon","Pear","Grape","Pineapple","Strawberry","Cherry","Mango"};
    private List<Fruit> fruitList=new ArrayList<>();
    private Button btnAnimal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAnimal=(Button)findViewById(R.id.btnAnimal);
        btnAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AnimalActivity.class);
                startActivity(intent);
            }
        });
        //ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_expandable_list_item_1,data);
        initFruits();
        FruitAdapter adapter=new FruitAdapter(MainActivity.this,R.layout.fruit_item,fruitList);
        ListView listView=(ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fruit fruit=fruitList.get(i);
                Toast.makeText(MainActivity.this,fruit.getName(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initFruits(){
        for(int i=0;i<3;i++){
            Fruit apple=new Fruit("apple",R.mipmap.apple_pic);
            fruitList.add(apple);
            Fruit banana=new Fruit("banana",R.mipmap.banana_pic);
            fruitList.add(banana);
            Fruit orange=new Fruit("orange",R.mipmap.orange_pic);
            fruitList.add(orange);
            Fruit watermelon=new Fruit("watermelon",R.mipmap.watermelon_pic);
            fruitList.add(watermelon);
            Fruit pear=new Fruit("pear",R.mipmap.pear_pic);
            fruitList.add(pear);
            Fruit grape=new Fruit("pear",R.mipmap.grape_pic);
            fruitList.add(grape);

            Fruit pineapple=new Fruit("pineapple",R.mipmap.pineapple_pic);
            fruitList.add(pineapple);
            Fruit strawberry=new Fruit("strawberry",R.mipmap.strawberry_pic);
            fruitList.add(strawberry);

            Fruit cherry=new Fruit("cherry",R.mipmap.cherry_pic);
            fruitList.add(cherry);
            Fruit mango=new Fruit("cherry",R.mipmap.mango_pic);
            fruitList.add(mango);


            /*
            Fruit apple=new Fruit("apple",R.drawable.apple_pic);
            fruitList.add(apple);
            Fruit banana=new Fruit("banana",R.drawable.banana_pic);
            fruitList.add(banana);
            Fruit orange=new Fruit("orange",R.drawable.orange_pic);
            fruitList.add(orange);
            Fruit watermelon=new Fruit("watermelon",R.drawable.watermelon_pic);
            fruitList.add(watermelon);
            Fruit pear=new Fruit("pear",R.drawable.pear_pic);
            fruitList.add(pear);
            Fruit grape=new Fruit("pear",R.drawable.grape_pic);
            fruitList.add(grape);

            Fruit pineapple=new Fruit("pineapple",R.drawable.pineapple_pic);
            fruitList.add(pineapple);
            Fruit strawberry=new Fruit("strawberry",R.drawable.strawberry_pic);
            fruitList.add(strawberry);

            Fruit cherry=new Fruit("cherry",R.drawable.cherry_pic);
            fruitList.add(cherry);
            Fruit mango=new Fruit("cherry",R.drawable.mango_pic);
            fruitList.add(mango);

             */
        }
    }
}