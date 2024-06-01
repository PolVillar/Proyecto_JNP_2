package com.example.proyecto_jnp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import model.Clothes;
import model.ConnectionConfig;
import model.Container;
import model.RecyclerViewAdapter;
import model.Suitcase;
import model.User;
import model.UserJwtInMemory;

public class GeneralClosetSuitcase extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private List<Clothes> itemList;
    private List<Drawable> clothesImgs;
    private List<String> clothesNames;
    private List<String> clothesDates;
    private Toolbar toolbar;
    private UserJwtInMemory userInMemory;
    private Long id;
    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suitcase);
        toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        clothesImgs = new ArrayList<>();
        clothesNames = new ArrayList<>();
        clothesDates = new ArrayList<>();
        userInMemory = UserJwtInMemory.getInstance();
        add = findViewById(R.id.addClothe);
        Intent intent = getIntent();
        getSupportActionBar().setTitle(intent.getStringExtra("closetName"));
        String closetName = intent.getStringExtra("closetName");
        String type = intent.getStringExtra("closetType");
        id = intent.getLongExtra("closetId",0);
        Log.d("Id:",id.toString());
        disableSSLCertificateChecking();
        countSuitcasesQuery();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GeneralClosetSuitcase.this, AddMoreClothes.class);
                intent.putExtra("containerId",id);
                intent.putExtra("containerName", closetName);
                intent.putExtra("containerType",type);
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Maneja el evento de clic en el botón de retroceso
            finish(); // Cierra la actividad actual y regresa a la anterior
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void countSuitcasesQuery() {
        RequestQueue queue = Volley.newRequestQueue(this, new HurlStack(null, newSSLSocketFactory()));
        String ownerUsername = userInMemory.getUser().getUsername();
        String url = ConnectionConfig.getIp(this) + "/clothes/find/all?containerId=" + id+"&owner%20username="+ownerUsername;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null, // No need for a request body
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        clothesImgs.clear();
                        clothesNames.clear();
                        clothesDates.clear();
                        for (int i=0;i<response.length();i++){

                            try {
                                JSONObject responseObj = response.getJSONObject(i);

                                String pic = responseObj.getString("picture");
                                byte[] byteArray = Base64.decode(pic, Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                                Drawable imgDrawable = new BitmapDrawable(getResources(), bitmap);
                                clothesImgs.add(imgDrawable);
                                String name = responseObj.getString("name");
                                clothesNames.add(name);
                                String lastUse = responseObj.getString("lastUse");
                                clothesDates.add(lastUse);
                                Log.d("estoy aqui:",name);
                                // Further code to handle the closets
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        adapter = new RecyclerViewAdapter(GeneralClosetSuitcase.this,clothesImgs,clothesNames,clothesDates);
                        recyclerView.setAdapter(adapter);

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("aiuda", error.getMessage());
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

        queue.add(jsonArrayRequest);
    }
    private SSLSocketFactory newSSLSocketFactory() {
        try {
            InputStream certificate = getResources().openRawResource(R.raw.marianaows2);
            KeyStore keyStore = KeyStore.getInstance("BKS");
            keyStore.load(certificate, "marianaoclosetpoljuan".toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "marianaoclosetpoljuan".toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException |
                 UnrecoverableKeyException | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
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
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        countSuitcasesQuery();
    }

}