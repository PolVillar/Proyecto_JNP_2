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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Clothes;
import model.ConnectionConfig;
import model.ClothesRecyclerViewAdapter;
import model.UserJwtInMemory;

public class GeneralClosetSuitcase extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ClothesRecyclerViewAdapter adapter;
    private List<Clothes> clothesList;
    private Toolbar toolbar;
    private UserJwtInMemory userInMemory;
    private SSLUtils sslUtils;
    private Long id;
    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suitcase);
        toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
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
        sslUtils.disableSSLCertificateChecking();
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
        RequestQueue queue = Volley.newRequestQueue(this,new HurlStack(null,sslUtils.newSSLSocketFactory()));
        String ownerUsername = userInMemory.getUser().getUsername();
        String url = ConnectionConfig.getIp(this) + "/clothes/find/all?containerId=" + id+"&owner%20username="+ownerUsername;

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
                                String name=responseObj.getString("name");
                                clothes.setName(name);
                                String lastUse = responseObj.getString("lastUse");
                                clothes.setLastUse(ClothesRecyclerViewAdapter.sdf.parse(lastUse));
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
    @Override
    protected void onResume() {
        super.onResume();
        countSuitcasesQuery();
    }

}