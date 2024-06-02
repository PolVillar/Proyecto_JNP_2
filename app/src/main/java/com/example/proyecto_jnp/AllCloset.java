package com.example.proyecto_jnp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
import model.ConnectionConfig;
import model.Container;
import model.Suitcase;
import model.User;
import model.UserJwtInMemory;

public class AllCloset extends AppCompatActivity {

    private Toolbar toolbar;
    private static int MAX_CLOSETS =6, MAX_SUITCASES=2;
    private UserJwtInMemory userInMemory;
    private SSLUtils sslUtils;
    private int countClosets,countSuitcases;
    private List<Closet> closetList;
    private List<ImageView> closetListImgs;
    private List<Suitcase> suitcaseList;
    private List<ImageView> suitcaseListImgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_closet);
        userInMemory = UserJwtInMemory.getInstance();
        User checkUser = userInMemory.getUser();
        if (checkUser==null)finish();
        disableSSLCertificateChecking();
        charge();
        closetList= new ArrayList<>();
        closetListImgs= new ArrayList<>();
        suitcaseList = new ArrayList<>();
        suitcaseListImgs = new ArrayList<>();
        countClosetsQuery();
        countSuitcasesQuery();

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
            finish();
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
                        if (countClosets<MAX_CLOSETS){
                            ImageView closetLast =closetListImgs.get(countClosets);
                            closetLast.setVisibility(View.VISIBLE);
                            closetLast.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    showUsernameInputDialog(Container.Type.CLOSET);
                                }
                            });
                            closetLast.setImageResource(R.drawable.plus);
                        }else{
                            ImageView closetLast = findViewById(R.id.ivCloset6);
                            closetLast.setImageResource(R.drawable.closet_but);
                        }
                        for (int i =0;i<countClosets; i++){
                            ImageView closet = closetListImgs.get(i);
                            closet.setVisibility(View.VISIBLE);
                            String closetName = closetList.get(i).getName();
                            Long closetId = closetList.get(i).getId();
                            String closetType = closetList.get(i).getType().toString();
                            closet.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(AllCloset.this,GeneralClosetSuitcase.class);
                                    intent.putExtra("closetName",closetName);
                                    intent.putExtra("closetId",closetId);
                                    intent.putExtra("closetType",closetType);
                                    startActivity(intent);
                                }
                            });
                        }
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
    private void showUsernameInputDialog(Container.Type type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AllCloset.this);
        builder.setTitle("Name");

        final EditText input = new EditText(AllCloset.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString().trim();
                if (type.equals(Container.Type.CLOSET)){
                    createClosets(userInMemory.getUser(),name);
                    recreate();
                }else{
                    createSuitcases(userInMemory.getUser(),name);
                    recreate();
                }

            }
        });
        builder.setNegativeButton(getText(R.string.cancel).toString(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    private void createClosets(User user, String name){
        RequestQueue queue = Volley.newRequestQueue(this,new HurlStack(null,sslUtils.newSSLSocketFactory()));
        String url = ConnectionConfig.getIp(this) + "/containers/save";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", 0);
            jsonBody.put("name", name);
            jsonBody.put("type", Container.Type.CLOSET);

            JSONObject ownerObj = new JSONObject();
            ownerObj.put("username", user.getUsername());
            ownerObj.put("password", user.getPassword());
            ownerObj.put("mail", user.getMail());
            ownerObj.put("phone", user.getPhone());
            ownerObj.put("fullName", user.getFullName());
            ownerObj.put("birthDate", user.getBirthDate());
            ownerObj.put("profilePicture", null);

            jsonBody.put("owner", ownerObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("CreateClosetsResponse", "Closet created successfully: " + response.toString());
                        try {
                            countClosetsQuery();
                        } catch (Exception e) {
                            e.printStackTrace();
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
    private void createSuitcases(User user, String name){
        RequestQueue queue = Volley.newRequestQueue(this,new HurlStack(null,sslUtils.newSSLSocketFactory()));
        String url = ConnectionConfig.getIp(this) + "/containers/save";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", 0);
            jsonBody.put("name", name);
            jsonBody.put("type", Container.Type.SUITCASE);

            JSONObject ownerObj = new JSONObject();
            ownerObj.put("username", user.getUsername());
            ownerObj.put("password", user.getPassword());
            ownerObj.put("mail", user.getMail());
            ownerObj.put("phone", user.getPhone());
            ownerObj.put("fullName", user.getFullName());
            ownerObj.put("birthDate", user.getBirthDate());
            ownerObj.put("profilePicture", null);

            jsonBody.put("owner", ownerObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("CreateClosetsResponse", "Closet created successfully: " + response.toString());
                        try {
                            countSuitcasesQuery();
                        } catch (Exception e) {
                            e.printStackTrace();
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

    private void countSuitcasesQuery() {
        RequestQueue queue = Volley.newRequestQueue(this, new HurlStack(null, newSSLSocketFactory()));
        String ownerUsername = userInMemory.getUser().getUsername();
        String url = ConnectionConfig.getIp(this) + "/containers/find/all/SUITCASES?owner=" + ownerUsername;

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
                                Long suitcaseId = responseObj.getLong("id");
                                String name = responseObj.getString("name");
                                Suitcase suitcase = new Suitcase(suitcaseId,  name, Container.Type.SUITCASE, userInMemory.getUser(), null);
                                suitcaseList.add(suitcase);

                                // Further code to handle the closets
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        countSuitcases=suitcaseList.size();
                        suitcaseListImgs.add(findViewById(R.id.ivSuitcase1));
                        suitcaseListImgs.add(findViewById(R.id.ivSuitcase2));

                        if (countSuitcases<MAX_SUITCASES){
                            ImageView lastSuitcase = findViewById(R.id.ivSuitcase2);
                            lastSuitcase.setVisibility(View.VISIBLE);
                            lastSuitcase.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    showUsernameInputDialog(Container.Type.SUITCASE);
                                }
                            });
                            lastSuitcase.setImageResource(R.drawable.plus);
                        }else{
                            ImageView lastSuitcase = findViewById(R.id.ivSuitcase2);
                            lastSuitcase.setImageResource(R.drawable.suitcase_but);
                        }

                        for (int i =0;i<countSuitcases; i++){
                            ImageView closet = suitcaseListImgs.get(i);
                            closet.setVisibility(View.VISIBLE);
                            String suitcaseName = suitcaseList.get(i).getName();
                            Long suitcaseId = suitcaseList.get(i).getId();
                            String closetType = suitcaseList.get(i).getType().toString();
                            closet.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(AllCloset.this,GeneralClosetSuitcase.class);
                                    intent.putExtra("closetName",suitcaseName);
                                    intent.putExtra("closetId",suitcaseId);
                                    intent.putExtra("closetType",closetType);
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
    private void charge() {
        sslUtils= new SSLUtils(this);
    }
}
