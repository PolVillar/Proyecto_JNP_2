package com.example.proyecto_jnp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class LogIn_Screen extends AppCompatActivity {

    private ImageView closet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_screen);

        closet = findViewById(R.id.imageCloset);

        closet.setImageResource(R.drawable.closet_img);
    }
}