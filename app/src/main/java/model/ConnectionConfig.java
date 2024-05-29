package model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class ConnectionConfig {
    //private static String ip = "https://192.168.8.61:8443";

    public static String getIp(Context context) {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        String ip=sharedPreferences.getString("server_ip","192.168.8.61");
        return "https://"+ip+":8443";
    }
}
