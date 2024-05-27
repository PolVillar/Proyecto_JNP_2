package com.example.proyecto_jnp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class JacketsCloset extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jackets_closet);

        ImageButton ibArrow = findViewById(R.id.ibArrowAut);
        ImageView ivJacket = findViewById(R.id.ivJacket);
        ImageButton ibAddInfo = findViewById(R.id.ibAddInfo);

        ivJacket.setImageResource(R.drawable.jacket);

        ivJacket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(JacketsCloset.this, DataJacket.class);
                startActivity(intent2);
            }
        });

        ibArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JacketsCloset.this, WinterCloset.class);
                startActivity(intent);
            }
        });

        ibAddInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JacketsCloset.this, AddMoreClothes.class);
                startActivity(intent);
            }
        });
    }
}