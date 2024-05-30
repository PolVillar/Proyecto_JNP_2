package com.example.proyecto_jnp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.acl.Owner;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
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

import model.Closet;
import model.Clothes;
import model.ConnectionConfig;
import model.Container;
import model.User;
import model.UserJwtInMemory;

public class AllCloset extends AppCompatActivity {

    private Toolbar toolbar;
    private UserJwtInMemory userInMemory;
    private int countClosets;
    private List<Closet> closetList;
    private List<ImageView> closetListImgs;
    private List<ImageView> suitcaseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_closet);
        userInMemory = UserJwtInMemory.getInstance();
        disableSSLCertificateChecking();
        charge();
        closetList= new ArrayList<>();
        closetListImgs= new ArrayList<>();
        countClosetsQuery();

        // Configura la Toolbar
        toolbar = findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_your_closet);

        // Habilita el botón de retroceso
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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

    private void countClosetsQuery() {
        RequestQueue queue = Volley.newRequestQueue(this, new HurlStack(null, newSSLSocketFactory()));
        String ownerUsername = userInMemory.getUser().getUsername();
        String url = ConnectionConfig.getIp(this) + "/containers/find/all/CLOSETS?owner=" + ownerUsername;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null, // No need for a request body
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i=0;i<response.length();i++){

                            try {
                                JSONObject responseObj = response.getJSONObject(i);

                                // now we get our response from API in json object format.
                                // in below line we are extracting a string with
                                // its key value from our json object.
                                // similarly we are extracting all the strings from our json object.
                                Long closetId = responseObj.getLong("id");
                                String name = responseObj.getString("name");
                                Closet closet = new Closet(closetId,  name, Container.Type.CLOSET, userInMemory.getUser(), null);
                                closetList.add(closet);

                                // Further code to handle the closets
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        countClosets=closetList.size();
                        closetListImgs.add(findViewById(R.id.ivCloset1));
                        closetListImgs.add(findViewById(R.id.ivCloset2));
                        closetListImgs.add(findViewById(R.id.ivCloset3));
                        closetListImgs.add(findViewById(R.id.ivCloset4));
                        closetListImgs.add(findViewById(R.id.ivCloset5));
                        closetListImgs.add(findViewById(R.id.ivCloset6));
                        for (int i =0;i<countClosets; i++){
                            ImageView closet = closetListImgs.get(i);
                            closet.setVisibility(View.VISIBLE);
                            String closetName = closetList.get(i).getName();
                            closet.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(AllCloset.this,GeneralClosetSuitcase.class);
                                    intent.putExtra("closetName",closetName);
                                    startActivity(intent);
                                }
                            });
                        }
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

    private void charge() {
        // Aquí puedes inicializar los ImageView
        // No necesitas agregar a la lista en este punto
    }
}
