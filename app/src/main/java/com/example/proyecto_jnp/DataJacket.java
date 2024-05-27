package com.example.proyecto_jnp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class DataJacket extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_jacket);

        ImageView ivPhotoJacket = findViewById(R.id.ivJacket);
        ivPhotoJacket.setImageResource(R.drawable.jacket);

        ImageButton ibArrow = findViewById(R.id.ibArrowAut);

        ibArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DataJacket.this, JacketsCloset.class);
                startActivity(intent);
            }
        });

    }
}