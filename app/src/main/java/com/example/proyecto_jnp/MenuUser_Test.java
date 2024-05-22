package com.example.proyecto_jnp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import model.User;
import model.UserJwtInMemory;

public class MenuUser_Test extends AppCompatActivity {

    private TextView usernameTxt, passwordTxt, mailTxt, phoneTxt, fullNameTxt;
    private UserJwtInMemory userInMemory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_user_test);
        userInMemory = UserJwtInMemory.getInstance();
        charge();

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        String mail = intent.getStringExtra("mail");
        String phone = intent.getStringExtra("phone");
        String fullName = intent.getStringExtra("fullName");
        //Birthdate
        //ProfilePicture
        //Containers

        User user = userInMemory.getUser();
        usernameTxt.setText(user.getUsername());
        mailTxt.setText(user.getMail());
        phoneTxt.setText(user.getPhone());
        fullNameTxt.setText(user.getFullName());
    }

    private void charge(){
        usernameTxt = findViewById(R.id.usernameTXT);
        mailTxt = findViewById(R.id.mailTXT);
        phoneTxt = findViewById(R.id.phoneTXT);
        fullNameTxt = findViewById(R.id.fullNameTXT);
    }
}