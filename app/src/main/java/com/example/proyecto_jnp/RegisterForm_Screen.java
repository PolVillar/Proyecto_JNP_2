package com.example.proyecto_jnp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import model.User;

public class RegisterForm_Screen extends AppCompatActivity {

    private EditText firstName, lastName,date,phone,address,password,conf_password,username,email;
    private Calendar calendar;
    private Button createAccount, cancel;
    private Boolean contin = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form_screen);

        charge();

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText[] editTexts = new EditText[]{firstName, lastName,date,phone,password,conf_password,username,email};
                contin = true;
                for (EditText editText: editTexts) {
                    if (editText.getText().toString().isEmpty()){
                        editText.setError("This field cannot be blank");
                        contin = false;
                    }
                }
                if (contin){
                    if (!password.getText().toString().trim().equals(conf_password.getText().toString().trim())){
                        contin = false;
                        conf_password.setError("The passwords do not match");
                    }
                }
                if (contin){
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                    String usernameVal = String.valueOf(username.getText());
                    String passwordVal = password.getText().toString().trim();
                    String mailVal = email.getText().toString().trim();
                    String phoneVal = phone.getText().toString().trim();
                    String fullnameVal = String.valueOf(firstName.getText()) + " " + String.valueOf(lastName.getText()).trim();
                    Date birthdateVal;
                    try {
                        birthdateVal = dateFormat.parse(date.getText().toString().trim());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return;
                    }
                    String profilePictureVal = "null";

                }
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
                        date.setText(selectedDate);
                    }
                },
                year,
                month,
                dayOfMonth);

        datePickerDialog.show();
    }
    private void charge(){
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        date = findViewById(R.id.bday_et);
        phone = findViewById(R.id.phone_et);
        email = findViewById(R.id.email_et);
        conf_password = findViewById(R.id.conf_password_et);
        password = findViewById(R.id.password_reg_et);
        address = findViewById(R.id.address_et);
        username = findViewById(R.id.username_reg_et);
        cancel = findViewById(R.id.cancel_reg_bt);
        createAccount = findViewById(R.id.create_acc_bt);
        calendar = Calendar.getInstance();
    }
    private void registerUser(final String username, final String password, final String mail, final String phone, final String fullname, final Date birthdate, final String profilePicture){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://192.168.18.12:8443/users/save";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // La solicitud fue exitosa, manejar la respuesta aquí
                        try {
                            String usernameJSON = response.getString("username");
                            String passwordJSON = response.getString("password");
                            String mailJSON = response.getString("mail");
                            String phoneJSON = response.getString("phone");
                            String fullNameJSON = response.getString("fullName");
                            //Birthdate
                            //ProfilePicture
                            //Containers

                            Intent intent = new Intent(RegisterForm_Screen.this, MenuUser_Test.class);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // La solicitud falló, manejar el error aquí
                if (error instanceof NetworkError) {
                    showAlertDialog("Connection error", "Error");
                    // Error de red (sin conexión, tiempo de espera, etc.)
                } else if (error instanceof NoConnectionError) {
                    showAlertDialog("Connection error", "Error");
                    // No hay conexión a Internet
                } else if (error instanceof TimeoutError) {
                    showAlertDialog("Connection error", "Error");
                    // Tiempo de espera agotado
                } else if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                    showAlertDialog("The username or the password are incorrect, please try again", "Error");
                    // Código de respuesta 404 - Recurso no encontrado
                } else {
                    // Otro tipo de error no reconocido
                    showAlertDialog("Error", "Error");
                }
            }
        }){
    };
        queue.add(jsonObjectRequest);
    }
    private void showAlertDialog(String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}