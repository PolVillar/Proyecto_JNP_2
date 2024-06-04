package com.example.proyecto_jnp;

public class ConnectionManager {
    private static String url = "https://192.168.18.12:8443/users/authenticate";//"https://192.168.8.145/swagger-ui.html";

    public static String getUrl() {
        return url;
    }
}
