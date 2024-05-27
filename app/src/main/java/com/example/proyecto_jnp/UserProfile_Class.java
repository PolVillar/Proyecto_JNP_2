package com.example.proyecto_jnp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import model.User;
import model.UserJwtInMemory;

public class UserProfile_Class extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView pfp;
    private TextView usernameTxt,mailTxt,phoneTxt,birthdayTxt;
    private UserJwtInMemory userInMemory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_class);
        toolbar = findViewById(R.id.toolbar6);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Your Profile");
        userInMemory = UserJwtInMemory.getInstance();

        charge();
        User user = userInMemory.getUser();
        byte[] byteArray = user.getProfilePicture();
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        Drawable iconDrawable = new BitmapDrawable(getResources(), bitmap);
        pfp.setImageDrawable(iconDrawable);
        usernameTxt.setText("Username: "+user.getUsername());
        mailTxt.setText("E-mail: "+user.getMail());
        phoneTxt.setText("Phone: "+user.getPhone());
        birthdayTxt.setText("Birthday: "+user.getBirthDate());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void charge(){
        pfp= findViewById(R.id.pfp_iv);
        usernameTxt=findViewById(R.id.username_tv);
        mailTxt=findViewById(R.id.mail_tv);
        phoneTxt=findViewById(R.id.phone_tv);
        birthdayTxt=findViewById(R.id.birthday_tv);
    }
}