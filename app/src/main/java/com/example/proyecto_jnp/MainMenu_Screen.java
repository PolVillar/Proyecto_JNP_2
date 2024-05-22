package com.example.proyecto_jnp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

import model.User;
import model.UserJwtInMemory;

public class MainMenu_Screen extends AppCompatActivity {

    private Toolbar toolbar;
    private Menu menu;
    private ImageView image;
    private UserJwtInMemory userInMemory;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_screen);

        charge();
        userInMemory = UserJwtInMemory.getInstance();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("App Name");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.burger_menu);

        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.userProfile) {
                return true;
            }

            return false;
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MainMenu_Screen.this, toolbar);
                popupMenu.getMenuInflater().inflate(R.menu.main_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Manejar el clic en los elementos del menú aquí
                        int id = menuItem.getItemId();
                        if (id == R.id.yourCloset) {
                            // Manejar el clic en la opción "Your closet"
                            return true;
                        } else if (id == R.id.marketplace) {
                            // Manejar el clic en la opción "Marketplace"
                            return true;
                        } else if (id == R.id.dailyOutfit) {
                            // Manejar el clic en la opción "Daily Outfit"
                            return true;
                        } else if (id == R.id.socialNetwork) {
                            // Manejar el clic en la opción "Social Network"
                            return true;
                        } else if (id == R.id.logOut) {
                            // Manejar el clic en la opción "Log Out"
                            return true;
                        }
                        return false;
                    }
                });

                // Mostrar el PopupMenu
                popupMenu.show();

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_user, menu);

        User user = userInMemory.getUser();
        byte[] byteArray = user.getProfilePicture();
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        MenuItem userPfp = menu.findItem(R.id.userProfile);
        Drawable iconDrawable = new BitmapDrawable(getResources(), bitmap);
        userPfp.setIcon(iconDrawable);
        return true;
    }
    private void charge(){
    }
}