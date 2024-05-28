package com.example.proyecto_jnp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import model.User;
import model.UserJwtInMemory;

public class LogInForm_Screen extends AppCompatActivity {

    EditText username, password;
    Button login,cancel;

    private static final String TAG = "LogInForm_Screen";
    private Boolean check=true;
    private UserJwtInMemory userInMemory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_form_screen);
        disableSSLCertificateChecking();
        charge();
        userInMemory = UserJwtInMemory.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().isEmpty()){
                    username.setError("Username cannot be blank");
                    check=false;
                }if(password.getText().toString().isEmpty()) {
                    password.setError("Password cannot be blank");
                    check=false;
                }
                if (check) {
                    authenticateUser(username.getText().toString().trim(), password.getText().toString().trim());
                }
                else{
                    check=true;
                    return;
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void charge(){
        username=findViewById(R.id.username_et);
        password=findViewById(R.id.password_et);
        login=findViewById(R.id.login_bt);
        cancel = findViewById(R.id.cancel_bt);
    }

    private SSLSocketFactory newSSLSocketFactory(){
        TrustManagerFactory tmf;

        try {
            tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init((KeyStore) null);

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
            return context.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    private void authenticateToken(final String username, final String password){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://192.168.8.145:8443/auth/login";
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
                        try {
                            String usernameJSON = response.getString("username");
                            String messageJSON =  response.getString("message");
                            String jwtJSON = response.getString("jwt");
                            Boolean statusJSON = response.getBoolean("status");

                            userInMemory.setToken(jwtJSON);
                            authenticateUser(usernameJSON, jwtJSON);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                    showAlertDialog(error.getMessage(), error.getMessage());
                } else if (error instanceof NoConnectionError) {
                    showAlertDialog("Connection error", "Error");
                } else if (error instanceof TimeoutError) {
                    showAlertDialog("Timeout error", "Error");
                } else if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                    showAlertDialog("The username or the password are incorrect, please try again", "Error");
                } else {
                    showAlertDialog("The log in credencials are not correct, try with another user or password", "Error");
                }
            }
        });
        queue.add(jsonObjectRequest);
    }


    private void authenticateUser(final String username, String token){

        //IMPORTANTE

        /*import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public void sendRequestWithToken(String url, JSONObject requestBody) {
    RequestQueue requestQueue = Volley.newRequestQueue(this);

    // Supongamos que el token está guardado en SharedPreferences
    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
    String token = sharedPreferences.getString("auth_token", "");

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.POST,
            url,
            requestBody,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // Manejar la respuesta
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Manejar el error
                }
            }
    ) {
        @Override
        public Map<String, String> getHeaders() {
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", "Bearer " + token);
            return headers;
        }
    };

    // Agregar la solicitud a la cola
    requestQueue.add(jsonObjectRequest);
}
*/



        //RequestQueue queue = CustomVolley.newRequestQueue(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        //, new HurlStack(null, newSSLSocketFactory())
        String url = "https://192.168.8.145:8443/users/get/by/username";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String usernameJSON = response.getString("username");
                            String passwordJSON = response.getString("password");
                            String mailJSON = response.getString("mail");
                            String phoneJSON = response.getString("phone");
                            String fullNameJSON = response.getString("fullName");
                            //Birthdate
                            String profilePictureJSON = response.getString("profilePicture");
                            //Containers

                            byte[] byteArray = Base64.decode(profilePictureJSON, Base64.DEFAULT);
                            User user = new User(usernameJSON,passwordJSON,mailJSON,phoneJSON,fullNameJSON,null,byteArray,null);
                            userInMemory.setUser(user);

                            Intent intent = new Intent(LogInForm_Screen.this, MainMenu_Screen.class);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                    showAlertDialog(error.getMessage(), error.getMessage());
                } else if (error instanceof NoConnectionError) {
                    showAlertDialog("Connection error", "Error");
                } else if (error instanceof TimeoutError) {
                    showAlertDialog("Timeout error", "Error");
                } else if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                    showAlertDialog("The username or the password are incorrect, please try again", "Error");
                } else {
                    showAlertDialog("The log in credencials are not correct, try with another user or password", "Error");
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
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