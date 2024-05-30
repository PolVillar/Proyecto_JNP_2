package model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class ConnectionConfig {
    //private static String ip = "https://192.168.8.60:8443";

    public static String getIp(Context context) {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        String ip=sharedPreferences.getString("server_ip","192.168.8.60");
        //return "https://"+ip+":8443";
        return "https://192.168.1.39:8443";
    }
}
