package com.example.proyecto_jnp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.EditText;

public class RegisterForm_Screen extends AppCompatActivity {

    private EditText firstName, lastName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form_screen);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);



    }

}