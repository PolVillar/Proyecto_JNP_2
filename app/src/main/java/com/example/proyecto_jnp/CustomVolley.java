package com.example.proyecto_jnp;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class CustomVolley {
    /*public static RequestQueue newRequestQueue(Context context) {
        try {
            SSLUtils trustAllFactory = new SSLUtils();
            HurlStack stack = new HurlStack(null, trustAllFactory);
            return Volley.newRequestQueue(context, stack);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            return Volley.newRequestQueue(context);
        }
    }*/
}

