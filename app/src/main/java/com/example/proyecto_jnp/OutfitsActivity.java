package com.example.proyecto_jnp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.List;

import model.OnItemClickListener;
import model.Outfit;
import model.OutfitsRecylecViewAdapter;

public class OutfitsActivity extends AppCompatActivity {
    private List<Outfit> outfits;
    private OutfitsRecylecViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfits);

        charge();
        adapter.setOnItemClickListener(pos -> {
            Outfit outfit= outfits.get(pos);
        });
    }
    private void charge(){
        adapter= new OutfitsRecylecViewAdapter(this,outfits);
    }
}