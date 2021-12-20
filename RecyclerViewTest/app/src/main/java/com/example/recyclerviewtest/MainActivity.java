package com.example.recyclerviewtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Fruit> fruitList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFruits();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recyler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        //layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        FruitAdapter adapter=new FruitAdapter(fruitList);
        recyclerView.setAdapter(adapter);
    }
    private void initFruits(){
        for(int i=0;i<2;i++){
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