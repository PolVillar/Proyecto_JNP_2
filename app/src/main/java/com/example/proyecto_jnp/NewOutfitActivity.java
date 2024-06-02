package com.example.proyecto_jnp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

import model.Clothes;
import model.ClothesRecyclerViewAdapter;
import model.OnItemClickListener;
import model.Outfit;

public class NewOutfitActivity extends AppCompatActivity {

    private RecyclerView rvSelectedClothes;
    private RecyclerView rvClothes;
    private List<Clothes> clothes;
    private List<Clothes> selectedClothes;
    private ClothesRecyclerViewAdapter clothesAdapter,selectedClothesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_outfit);

        charge();
        clothesAdapter.setOnItemClickListener(pos -> {
            Clothes c = clothes.get(pos);
            if(!selectedClothes.contains(c)) selectedClothes.add(c);
        });
        selectedClothesAdapter.setOnItemClickListener(pos -> {
            selectedClothes.remove(pos);
        });
    }
    private void charge(){
        rvSelectedClothes=findViewById(R.id.rvSelectedClothes);
        rvClothes= findViewById(R.id.rvClothes);
        clothesAdapter= new ClothesRecyclerViewAdapter(this,clothes);
        selectedClothesAdapter= new ClothesRecyclerViewAdapter(this,selectedClothes);
    }

}