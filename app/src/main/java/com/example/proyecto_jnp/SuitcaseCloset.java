package com.example.proyecto_jnp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class SuitcaseCloset extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suitcase);

        ImageView ivJacket = findViewById(R.id.ivJacket);

        ivJacket.setImageResource(R.drawable.jacket);

        ImageButton ibArrow = findViewById(R.id.ibArrow);

        ibArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuitcaseCloset.this, AllCloset.class);
                startActivity(intent);
            }
        });
    }
}