package com.example.proyecto_jnp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import model.ConnectionConfig;
import model.User;
import model.UserJwtInMemory;

public class UserProfile_Class extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView pfp;
    private Calendar calendar;
    private TextView usernameTxt,mailTxt,phoneTxt,birthdayTxt;
    private UserJwtInMemory userInMemory;
    ActivityResultLauncher<Intent> launcher;
    private Button update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_class);
        toolbar = findViewById(R.id.toolbar6);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.yprofile));
        userInMemory = UserJwtInMemory.getInstance();
        initializeLauncher();

        charge();
        User user = userInMemory.getUser();
        byte[] byteArray = user.getProfilePicture();
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        Drawable iconDrawable = new BitmapDrawable(getResources(), bitmap);
        pfp.setImageDrawable(iconDrawable);
        usernameTxt.setText(getString(R.string.username)+": "+user.getUsername());
        mailTxt.setText("E-mail: "+user.getMail());
        phoneTxt.setText(user.getPhone());
        birthdayTxt.setText(user.getBirthDate());
        pfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAddImage(view);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateClothe();
            }
        });
        birthdayTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }
    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Aquí puedes manejar la fecha seleccionada
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        birthdayTxt.setText(selectedDate);
                    }
                },
                year,
                month,
                dayOfMonth);

        datePickerDialog.show();
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
        calendar = Calendar.getInstance();
        update= findViewById(R.id.button4);
    }
    public void onClickAddImage(View view) {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        chooser.putExtra(Intent.EXTRA_INTENT, galleryIntent);
        chooser.putExtra(Intent.EXTRA_TITLE, getString(R.string.select_image_title));
        Intent[] intentArray = {cameraIntent};
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
        launcher.launch(chooser);
    }
    private void initializeLauncher() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent intent = result.getData();
                if (intent != null) {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        Bitmap photoBitmap = (Bitmap) bundle.get("data");
                        if (photoBitmap != null) {
                            pfp.setImageBitmap(photoBitmap);
                        }
                    } else {
                        Uri photoUri = intent.getData();
                        if (photoUri != null) {
                            try {
                                pfp.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        });
    }
    private void updateClothe() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ConnectionConfig.getIp(this) + "/users/update";

        try {
            Drawable drawable = pfp.getDrawable();
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

            JSONObject ownerJson = new JSONObject();
            ownerJson.put("username",userInMemory.getUser().getUsername());
            ownerJson.put("password",userInMemory.getUser().getPassword());
            Log.d("pass",userInMemory.getUser().getPassword());
            ownerJson.put("mail", userInMemory.getUser().getMail());
            ownerJson.put("phone",phoneTxt.getText().toString());
            ownerJson.put("fullName",userInMemory.getUser().getFullName());
            ownerJson.put("birthDate",birthdayTxt.getText().toString());
            ownerJson.put("profilePicture",encodedImage);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.PUT,
                    url,
                    ownerJson,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            User user = userInMemory.getUser();
                            user.setBirthDate(birthdayTxt.getText().toString());
                            user.setPhone(phoneTxt.getText().toString());
                            user.setProfilePicture(byteArray);
                            userInMemory.setUser(user);
                           finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + userInMemory.getToken());
                    return headers;
                }
            };

            queue.add(jsonObjectRequest);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}