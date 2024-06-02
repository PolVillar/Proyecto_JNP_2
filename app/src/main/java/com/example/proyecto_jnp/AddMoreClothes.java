package com.example.proyecto_jnp;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import model.Clothes;
import model.ConnectionConfig;
import model.Container;
import model.EnumerationMaps;
import model.User;
import model.UserJwtInMemory;

public class AddMoreClothes extends AppCompatActivity {

    public List<String> categories,collections;
    private EditText etName,etColor,etSize;
    private ImageView ivNewClothe;
    private Spinner spCategory,spCollection;
    private String type,name;
    private Long id;
    private Clothes clothes;
    private EnumerationMaps maps;
    private UserJwtInMemory userInMemory;
    private SSLUtils sslUtils;
    private Container container;
    private Toolbar toolbar;
    ActivityResultLauncher<Intent> launcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_clothes);
        charge();
        setAdapters();
        disableSSLCertificateChecking();
        Intent intent = getIntent();
        type = intent.getStringExtra("containerType");
        id = intent.getLongExtra("containerId",0);
        name = intent.getStringExtra("containerName");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(intent.getStringExtra(""));
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setBackCallback();
    }

    private void setBackCallback() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
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
    private void charge() {
        etName = findViewById(R.id.etNameEdit);
        etColor = findViewById(R.id.etColorEdit);
        etSize = findViewById(R.id.etSizeEdit);
        ivNewClothe = findViewById(R.id.ivClotheEdit);
        spCategory = findViewById(R.id.spCategoryEdit);
        spCollection = findViewById(R.id.spCollectionEdit);

        categories= new ArrayList<>(Arrays.asList(getString(R.string.category_jacket),
                getString(R.string.category_shirt),getString(R.string.category_pants),getString(R.string.category_shoes),
                getString(R.string.category_underwear),getString(R.string.category_complement)));
        collections= new ArrayList<>(Arrays.asList(getString(R.string.collection_winter),
                getString(R.string.collection_spring),getString(R.string.collection_summer),getString(R.string.collection_autumn)));
        maps= new EnumerationMaps(this);
        initializeLauncher();
    }


    private void setAdapters() {
        ArrayAdapter<String> collectionsAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, collections);
        spCollection.setAdapter(collectionsAdapter);
        ArrayAdapter<String> categoriesAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, categories);
        spCategory.setAdapter(categoriesAdapter);
    }

    private void initializeLauncher(){
        launcher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode()== Activity.RESULT_OK){
                Intent intent= result.getData();
                if(intent!=null){
                    Bundle bundle=intent.getExtras();
                    if(bundle!=null){
                        Bitmap photoBitmap= (Bitmap) bundle.get("data");
                        if(photoBitmap!=null) ivNewClothe.setImageBitmap(photoBitmap);
                    }
                    else{
                        Uri photoUri = intent.getData();
                        if(photoUri!=null) {
                            try {
                                ivNewClothe.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        });
    }
    public void onClickCreate(View view) {
        if (etName.getText().toString().isEmpty()) {
            etName.setError(getString(R.string.edit_text_empty));
        } else if (ivNewClothe.getDrawable() == null) {
            return;
        } else if (!(ivNewClothe.getDrawable() instanceof BitmapDrawable)) {
            return;
        } else {
            Bitmap bitmap = ((BitmapDrawable) ivNewClothe.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            // Obtén las selecciones actuales de los Spinners
            String selectedCollection = spCollection.getSelectedItem().toString();
            String selectedCategory = spCategory.getSelectedItem().toString();

            // Crea el objeto Clothes con las selecciones correctas
            clothes= new Clothes(null,etName.getText().toString(),
                    etColor.getText().toString(),etSize.getText().toString(),
                    byteArray, maps.getCollections().get(spCollection.getSelectedItem().toString()).toString(),
                    maps.getCategories().get(spCategory.getSelectedItem().toString()).toString(),null ,null);

            createClothe(userInMemory.getUser(), clothes);
        }
    }

    private void createClothe(User user,Clothes clothe){
        RequestQueue queue = Volley.newRequestQueue(this,new HurlStack(null,newSSLSocketFactory()));
        String url = ConnectionConfig.getIp(this) + "/clothes/save";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", 0);
            jsonBody.put("name", clothe.getName());
            jsonBody.put("color", clothe.getColor());
            jsonBody.put("size", clothe.getSize());
            // Si picture es un array, puedes pasar un JSONArray
            byte[] byteArray = clothe.getPicture();
            String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
            jsonBody.put("picture", base64Image);
            jsonBody.put("collection", clothe.getCollection());
            jsonBody.put("category", clothe.getCategory());
            // Agregar el objeto Container
            JSONObject containerObj = new JSONObject();
            containerObj.put("id", id.toString());
            containerObj.put("name", name);
            containerObj.put("type", type);
            // Configurar los datos del Propietario
            JSONObject ownerObj = new JSONObject();
            ownerObj.put("username", user.getUsername());
            ownerObj.put("password", user.getPassword());
            ownerObj.put("mail", user.getMail());
            ownerObj.put("phone", user.getPhone());
            ownerObj.put("fullName", user.getFullName());
            ownerObj.put("birthDate", user.getBirthDate());
            ownerObj.put("profilePicture", null); // Aquí podrías poner la lógica para agregar imágenes
            containerObj.put("owner", ownerObj);
            jsonBody.put("container", containerObj);
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(date);
            jsonBody.put("lastUse", formattedDate);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            finish();
                        } catch (Exception e) {
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            Log.e("CreateClosetsError", "Error: " + new String(error.networkResponse.data));
                        } else {
                            Log.e("CreateClosetsError", "Error: " + error.getMessage());
                        }
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
    }


    public void onClickAddImage(View view){
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        chooser.putExtra(Intent.EXTRA_INTENT, galleryIntent);
        chooser.putExtra(Intent.EXTRA_TITLE, getString(R.string.select_image_title));
        Intent[] intentArray = { cameraIntent };
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
        launcher.launch(chooser);
    }
    public SSLSocketFactory newSSLSocketFactory(){
        try (InputStream certificate =getResources().openRawResource(R.raw.marianaows);
             InputStream is= getAssets().open("config.properties")){
            Properties props= new Properties();
            props.load(is);
            String keyStorePassword= props.getProperty("keystore_password");
            Log.i("password",keyStorePassword);
            KeyStore keyStore = KeyStore.getInstance("BKS");
            keyStore.load(certificate, keyStorePassword.toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());

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
    public void disableSSLCertificateChecking() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
                // Not implemented
            }

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
            Log.i("holassl","ey");
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}