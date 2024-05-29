package com.example.proyecto_jnp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class AllCertificatesAndHostsTruster implements TrustManager, X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
    @Nullable
    public static SSLContext getSSLContext(){
        final TrustManager[] trustAllCerts= new TrustManager[]{new AllCertificatesAndHostsTruster()};
        try{
            final SSLContext context= SSLContext.getInstance("SSL");
            context.init(null,trustAllCerts, new SecureRandom());
            return context;
        } catch (Exception e) {
            return null;
        }
    }
    @NonNull
    @Contract(pure = true)
    public static HostnameVerifier getAllHostnamesVerifier(){
        return (s, sslSession) -> true;
    }
    public static void apply(){
        try {
            final SSLContext context= getSSLContext();
            assert context != null;
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(getAllHostnamesVerifier());
        }catch (Exception e){
            Log.e("CertHostTruster","Unable to initialize");
        }
    }
}
