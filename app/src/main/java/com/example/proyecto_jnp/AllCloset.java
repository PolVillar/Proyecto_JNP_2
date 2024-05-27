package com.example.proyecto_jnp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;

public class AllCloset extends AppCompatActivity {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_closet);

        Button btnWinter = findViewById(R.id.btnWinter);

        Button btnSummer = findViewById(R.id.btnSummer);

        Button btnAutum = findViewById(R.id.btnAutum);

        Button btnSpring = findViewById(R.id.btnSpring);

        Button btnSuitcase = findViewById(R.id.btnSuitcase);
        toolbar=findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Your Closet");



        btnWinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllCloset.this, WinterCloset.class);
                startActivity(intent);
            }
        });

        btnSummer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllCloset.this, SummerCloset.class);
                startActivity(intent);
            }
        });

        btnAutum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllCloset.this, AutumCloset.class);
                startActivity(intent);
            }
        });

        btnSpring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllCloset.this, SpringCloset.class);
                startActivity(intent);
            }
        });

        btnSuitcase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllCloset.this, SuitcaseCloset.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Finalizar la actividad actual y regresar a la anterior
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}