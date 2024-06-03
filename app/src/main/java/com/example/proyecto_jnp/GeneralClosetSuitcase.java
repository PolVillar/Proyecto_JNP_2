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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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
import java.util.Arrays;
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
import model.ClothesRecyclerViewAdapter;
import model.UserJwtInMemory;

public class GeneralClosetSuitcase extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private RecyclerView recyclerView;
    private ClothesRecyclerViewAdapter adapter;
    private List<Clothes> clothesList;
    private List<Clothes> itemList;
    private List<Drawable> clothesImgs;
    private List<String> clothesNames;
    private List<String> clothesDates;
    public List<String> categories,collections;
    private Toolbar toolbar;
    private UserJwtInMemory userInMemory;
    private SSLUtils sslUtils;
    private Long id;
    private Button add;
    private Spinner collectionFilter, categoryFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suitcase);
        toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        collectionFilter= findViewById(R.id.spinnerColl);
        categoryFilter=findViewById(R.id.spinnerCat);
        charge();
        setAdapters();
        clothesImgs = new ArrayList<>();
        clothesNames = new ArrayList<>();
        clothesDates = new ArrayList<>();
        clothesList = new ArrayList<>();
        userInMemory = UserJwtInMemory.getInstance();
        sslUtils= new SSLUtils(this);
        add = findViewById(R.id.addClothe);
        Intent intent = getIntent();
        getSupportActionBar().setTitle(intent.getStringExtra("closetName"));
        String closetName = intent.getStringExtra("closetName");
        String type = intent.getStringExtra("closetType");
        id = intent.getLongExtra("closetId",0);
        Log.d("Id:",id.toString());
        disableSSLCertificateChecking();
        countSuitcasesQuery(collectionFilter.getSelectedItem().toString(),categoryFilter.getSelectedItem().toString());
        disableSSLCertificateChecking();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        collectionFilter.setOnItemSelectedListener(this);
        categoryFilter.setOnItemSelectedListener(this);

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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        countSuitcasesQuery(collectionFilter.getSelectedItem().toString(), categoryFilter.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
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

    private void setAdapters() {
        ArrayAdapter<String> collectionsAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, collections);
        collectionFilter.setAdapter(collectionsAdapter);
        ArrayAdapter<String> categoriesAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, categories);
        categoryFilter.setAdapter(categoriesAdapter);
    }

    private void countSuitcasesQuery(String collection, String category) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String ownerUsername = userInMemory.getUser().getUsername();
        String url = ConnectionConfig.getIp(this) + "/clothes/find/all?containerId=" + id+"&owner%20username="+ownerUsername+"&collection="+collection.toUpperCase()+"&category="+category.toUpperCase();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null, // No need for a request body
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        clothesList.clear();

                        for (int i=0;i<response.length();i++){
                            Clothes clothes= new Clothes();
                            try {
                                JSONObject responseObj = response.getJSONObject(i);

                                String pic = responseObj.getString("picture");
                                byte[] byteArray = Base64.decode(pic, Base64.DEFAULT);
                                clothes.setPicture(byteArray);
                                clothes.setId(responseObj.getLong("id"));
                                String name=responseObj.getString("name");
                                clothes.setName(name);
                                String lastUse = responseObj.getString("lastUse");
                                clothesDates.add(lastUse);
                                clothes.setColor(responseObj.getString("color"));
                                clothes.setSize(responseObj.getString("size"));
                                clothes.setLastUse(ClothesRecyclerViewAdapter.sdf.parse(lastUse));
                                clothes.setCategory(responseObj.getString("category"));
                                clothes.setCollection(responseObj.getString("collection"));
                                Log.d("estoy aqui:",name);
                                clothesList.add(clothes);
                                // Further code to handle the closets
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        adapter = new ClothesRecyclerViewAdapter(GeneralClosetSuitcase.this,clothesList);
                        recyclerView.setAdapter(adapter);

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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
            InputStream certificate = getResources().openRawResource(R.raw.marianaows);
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
        countSuitcasesQuery(collectionFilter.getSelectedItem().toString(),categoryFilter.getSelectedItem().toString());
    }
    private  void charge(){
        categories= new ArrayList<>(Arrays.asList(getString(R.string.category_jacket),
                getString(R.string.category_shirt),getString(R.string.category_pants),getString(R.string.category_shoes),
                getString(R.string.category_underwear),getString(R.string.category_complement)));
        collections= new ArrayList<>(Arrays.asList(getString(R.string.collection_winter),
                getString(R.string.collection_spring),getString(R.string.collection_summer),getString(R.string.collection_autumn)));
    }

}