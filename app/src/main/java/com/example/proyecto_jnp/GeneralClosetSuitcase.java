package com.example.proyecto_jnp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.RecyclerViewAdapter;

public class GeneralClosetSuitcase extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private List<Clothes> itemList;
    private List<Drawable> clothesImgs;
    private List<String> clothesNames;
    private List<Date> clothesDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suitcase);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Añadir items del JSON
        /*itemList = new ArrayList<>();
        itemList.add(new Clothes());
        itemList.add(new Clothes());
        itemList.add(new Clothes());*/

        clothesImgs = new ArrayList<>();
        clothesNames = new ArrayList<>();
        clothesDates = new ArrayList<>();



        adapter = new RecyclerViewAdapter(this,clothesImgs,clothesNames,clothesDates);
        recyclerView.setAdapter(adapter);
    }
}