package com.example.proyecto_jnp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class WinterCloset extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winter_closet);

        ImageButton ibArrow = findViewById(R.id.ibArrow);
        Button btnJacket = findViewById(R.id.btnJackets);

        btnJacket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(WinterCloset.this, JacketsCloset.class);
                startActivity(intent2);
            }
        });

        ibArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WinterCloset.this, AllCloset.class);
                startActivity(intent);
            }
        });
    }
}