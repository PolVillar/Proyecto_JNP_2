package com.example.proyecto_jnp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import model.ConnectionConfig;
import model.User;
import model.UserJwtInMemory;

public class RegisterForm_Screen extends AppCompatActivity {

    private EditText firstName, lastName,date,phone,password,conf_password,username,email;
    private Calendar calendar;
    private Button createAccount, cancel;
    private Boolean contin = true;
    private UserJwtInMemory userInMemory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form_screen);
        userInMemory = UserJwtInMemory.getInstance();
        charge();
        disableSSLCertificateChecking();

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

                    registerUser(usernameVal, passwordVal, mailVal, phoneVal, fullnameVal, birthdateVal, profilePictureVal);
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
        username = findViewById(R.id.username_reg_et);
        cancel = findViewById(R.id.cancel_reg_bt);
        createAccount = findViewById(R.id.create_acc_bt);
        calendar = Calendar.getInstance();
    }
    private void registerUser(final String username, final String password, final String mail, final String phone, final String fullname, final Date birthdate, final String profilePicture){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = ConnectionConfig.getIp()+"/auth/signup";

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String birthdateValFormatted = dateFormat.format(birthdate);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("password", password);
            jsonBody.put("mail",mail);
            jsonBody.put("phone",phone);
            jsonBody.put("fullName", fullname);
            jsonBody.put("birthDate",birthdateValFormatted);

            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.profile_blank);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            String pfp = Base64.encodeToString(byteArray, Base64.DEFAULT);

            jsonBody.put("profilePicture", pfp);
            //jsonBody.put("profilePicture", null);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Drawable drawable = ContextCompat.getDrawable(RegisterForm_Screen.this, R.drawable.profile_blank);
                            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            String pfp = Base64.encodeToString(byteArray, Base64.DEFAULT);

                            User user = new User(username,password,mail,phone,fullname,birthdateValFormatted,byteArray,null);
                            userInMemory.setUser(user);

                            Intent intent = new Intent(RegisterForm_Screen.this, MainMenu_Screen.class);
                            startActivity(intent);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                    showAlertDialog(error.getMessage(), "Error");
                } else if (error instanceof NoConnectionError) {
                    showAlertDialog("Connection error", "Error");
                } else if (error instanceof TimeoutError) {
                    showAlertDialog("Connection error", "Error");
                } else if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                    showAlertDialog("The username or the password are incorrect, please try again", "Error");
                } else {
                    showAlertDialog("The mail or username are already in use", "Error");
                }
            }
        });

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
    public static void disableSSLCertificateChecking() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                // Not implemented
            }
        } };

        try {
            SSLContext sc = SSLContext.getInstance("TLS");

            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() { @Override public boolean verify(String hostname, SSLSession session) { return true; } });
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}