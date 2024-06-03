package com.example.proyecto_jnp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import model.Closet;
import model.Clothes;
import model.ClothesRecyclerViewAdapter;
import model.ConnectionConfig;
import model.Container;
import model.Suitcase;
import model.UserJwtInMemory;

public class InfoClothe extends AppCompatActivity {

    private Spinner spCategory, spCollection, spContainer;
    private EditText nameEdit, colorEdit, sizeEdit;
    private Button editBt;
    private ImageView image;
    private UserJwtInMemory userInMemory;
    ActivityResultLauncher<Intent> launcher;
    public List<String> categories, collections, containers;
    private List<Integer> ids;
    private Long id;
    private List<Container> containerList;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_clothe);
        charge();
        toolbar = findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ids = new ArrayList<>();
        containerList = new ArrayList<>();
        userInMemory = UserJwtInMemory.getInstance();
        disableSSLCertificateChecking();
        initializeLauncher();
        countSuitcasesQuery();
        setAdapters();

        Intent intent = getIntent();
        id = intent.getLongExtra("clothesId",0 );
        nameEdit.setText(intent.getStringExtra("clothesName"));
        colorEdit.setText(intent.getStringExtra("clothesColor"));
        sizeEdit.setText(intent.getStringExtra("clothesSize"));
        setSpinnerInitialValue(spCategory, categories, intent.getStringExtra("clothesCategory"));
        setSpinnerInitialValue(spCollection, collections, intent.getStringExtra("clothesCollection"));

        byte[] img = intent.getByteArrayExtra("clothesImg");
        if (img != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
            image.setImageDrawable(drawable);
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAddImage(view);
            }
        });
        editBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateClothe();
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
    private void setSpinnerInitialValue(Spinner spinner, List<String> items, String initialValue) {
        if (initialValue != null) {
            initialValue = initialValue.toLowerCase(); // Convertir el valor inicial a minúsculas
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).toLowerCase().equals(initialValue)) { // Comparar en minúsculas
                    spinner.setSelection(i);
                    break;
                }
            }
        }
    }

    private void countSuitcasesQuery() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String ownerUsername = userInMemory.getUser().getUsername();
        String url = ConnectionConfig.getIp(this) + "/containers/find/all?owner=" + ownerUsername;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        containers.clear();
                        ids.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject responseObj = response.getJSONObject(i);

                                if (responseObj.getString("type").equals("CLOSET")){
                                    Closet closet = new Closet();
                                    closet.setName(responseObj.getString("name"));
                                    closet.setId(responseObj.getLong("id"));
                                    closet.setType(Container.Type.CLOSET);
                                    containerList.add(closet);
                                    containers.add(responseObj.getString("name"));
                                }
                                else{
                                    Suitcase suitcase = new Suitcase();
                                    suitcase.setName(responseObj.getString("name"));
                                    suitcase.setId(responseObj.getLong("id"));
                                    suitcase.setType(Container.Type.SUITCASE);
                                    containerList.add(suitcase);
                                    containers.add(responseObj.getString("name"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter<String> containersAdapter = (ArrayAdapter<String>) spContainer.getAdapter();
                        containersAdapter.notifyDataSetChanged();
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

        queue.add(jsonArrayRequest);
    }
    private void updateClothe() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String ownerUsername = userInMemory.getUser().getUsername();
        String url = ConnectionConfig.getIp(this) + "/clothes/update";
        Log.d("id", id+"");

        try {
            // Recoger los datos de los campos de entrada

            String name = nameEdit.getText().toString();
            String color = colorEdit.getText().toString();
            String size = sizeEdit.getText().toString();
            String collection = spCollection.getSelectedItem().toString().toUpperCase();
            String category = spCategory.getSelectedItem().toString().toUpperCase();
            String containerSearch = spContainer.getSelectedItem().toString();
            Container container = null;
            for (Container cont : containerList) {
                if (cont.getName().equals(containerSearch)) {
                    container = cont;
                    break;
                }
            }
            String nombre = container.getName();
            String tipo = String.valueOf(container.getType());
            Log.d("tip",tipo);

            Long containerId = container.getId();
            Log.d("contId",containerId+"");

            Log.d("coll",collection);
            Log.d("cat",category);

            // Recoger la imagen como Base64
            Drawable drawable = image.getDrawable();
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

            // Construir el JSON con los datos recogidos
            JSONObject clotheJson = new JSONObject();
            clotheJson.put("id",id);
            clotheJson.put("name", name);
            clotheJson.put("color", color);
            clotheJson.put("size", size);
            clotheJson.put("picture", encodedImage);
            clotheJson.put("collection", collection);
            clotheJson.put("category", category);

            JSONObject containerJson = new JSONObject();
            containerJson.put("id", containerId);
            containerJson.put("type",tipo);
            clotheJson.put("container", containerJson);

            // Añadir los campos del propietario si es necesario
            JSONObject ownerJson = new JSONObject();
            ownerJson.put("username", userInMemory.getUser().getUsername());
            ownerJson.put("birthDate",userInMemory.getUser().getBirthDate());
            ownerJson.put("profilePicture",null);
            // Añadir otros campos necesarios del propietario aquí

            containerJson.put("owner", ownerJson);
            Date fechaActual = new Date();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            String fechaFormateada = formatoFecha.format(fechaActual);
            clotheJson.put("lastUse",fechaFormateada);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                clotheJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

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


    private void setAdapters() {
        ArrayAdapter<String> collectionsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, collections);
        collectionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCollection.setAdapter(collectionsAdapter);

        ArrayAdapter<String> categoriesAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(categoriesAdapter);

        ArrayAdapter<String> containersAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, containers);
        containersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spContainer.setAdapter(containersAdapter);
    }


    private void charge() {
        spCategory = findViewById(R.id.spCategoryEdit);
        spContainer = findViewById(R.id.spinner2);
        spCollection = findViewById(R.id.spCollectionEdit);
        nameEdit = findViewById(R.id.etNameEdit);
        colorEdit = findViewById(R.id.etColorEdit);
        sizeEdit = findViewById(R.id.etSizeEdit);
        editBt = findViewById(R.id.bEditClothe);
        image = findViewById(R.id.ivClotheEdit);
        categories = new ArrayList<>(Arrays.asList(getString(R.string.category_jacket),
                getString(R.string.category_shirt), getString(R.string.category_pants), getString(R.string.category_shoes),
                getString(R.string.category_underwear), getString(R.string.category_complement)));
        collections = new ArrayList<>(Arrays.asList(getString(R.string.collection_winter),
                getString(R.string.collection_spring), getString(R.string.collection_summer), getString(R.string.collection_autumn)));
        containers = new ArrayList<>(); // Inicializar containers
        ids = new ArrayList<>(); // Inicializar ids
    }

    public void disableSSLCertificateChecking() {
        @SuppressLint("CustomX509TrustManager") TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @SuppressLint("TrustAllX509TrustManager")
            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
                // Not implemented
            }

            @SuppressLint("TrustAllX509TrustManager")
            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
                // Not implemented
            }
        }};

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
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
                            image.setImageBitmap(photoBitmap);
                        }
                    } else {
                        Uri photoUri = intent.getData();
                        if (photoUri != null) {
                            try {
                                image.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        });
    }
}
