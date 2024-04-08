package com.example.proyecto_jnp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class LogIn_Screen extends AppCompatActivity {

    private ImageView closet;
    private Button logIn,register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_screen);
        carga();

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogIn_Screen.this, LogInForm_Screen.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogIn_Screen.this, RegisterForm_Screen.class);
                startActivity(intent);
            }
        });
        closet.setImageResource(R.drawable.closet_img);
    }
    private void carga(){
        closet = findViewById(R.id.imageCloset);
        logIn=findViewById(R.id.logIn_bt);
        register=findViewById(R.id.register_bt);
    }
}