package com.example.proyecto_jnp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button pruebaLogIn, pruebaRegister,pruebaloginForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        carga();

        pruebaLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirLogIn();
            }
        });
        pruebaRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirRegisterForm();
            }
        });
        pruebaloginForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {abrirLoginForm();}
        });
    }

    private void abrirLogIn() {
        Intent intent = new Intent(this, LogIn_Screen.class);
        startActivity(intent);
    }
    private void abrirRegisterForm() {
        Intent intent = new Intent(this, RegisterForm_Screen.class);
        startActivity(intent);
    }
    private void abrirLoginForm() {
        Intent intent = new Intent(this, LogInForm_Screen.class);
        startActivity(intent);
    }
    private void carga(){
        pruebaLogIn = findViewById(R.id.pruebamain);
        pruebaRegister = findViewById(R.id.pruebaregister);
        pruebaloginForm = findViewById(R.id.pruebaloginform);
    }

    User user = new User();


}