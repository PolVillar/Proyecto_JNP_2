package com.example.proyecto_jnp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class SummerCloset extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summer_closet);

        ImageButton ibArrow = findViewById(R.id.ibArrowAut);

        ibArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SummerCloset.this, AllCloset.class);
                startActivity(intent);
            }
        });
    }
}