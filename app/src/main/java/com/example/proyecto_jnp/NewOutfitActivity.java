package com.example.proyecto_jnp;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import model.Clothes;
import model.ClothesRecyclerViewAdapter;
import model.OnItemClickListener;
import model.Outfit;

public class NewOutfitActivity extends AppCompatActivity {

    private RecyclerView rvSelectedClothes,rvClothes;
    private List<Clothes> clothes,selectedClothes;
    private EditText etOutfitName;
    private ClothesRecyclerViewAdapter clothesAdapter,selectedClothesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_outfit);
        setBackCallback();
        charge();
        clothesAdapter.setOnItemClickListener(pos -> {
            Clothes c = clothes.get(pos);
            if(!selectedClothes.contains(c)) selectedClothes.add(c);
        });
        selectedClothesAdapter.setOnItemClickListener(pos -> {
            selectedClothes.remove(pos);
        });
    }
    private void setBackCallback() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }
    private void charge(){
        etOutfitName= findViewById(R.id.etOutfitName);
        rvSelectedClothes=findViewById(R.id.rvSelectedClothes);
        rvClothes= findViewById(R.id.rvClothes);
        clothesAdapter= new ClothesRecyclerViewAdapter(this,clothes);
        selectedClothesAdapter= new ClothesRecyclerViewAdapter(this,selectedClothes);
    }
    public void onClickCreate(View view){
        String name= etOutfitName.getText().toString().trim();
    }
    public void onClickCancel(View view){
        finish();
    }

}