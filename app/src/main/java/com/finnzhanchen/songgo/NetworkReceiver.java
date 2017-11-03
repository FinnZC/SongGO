package com.finnzhanchen.songgo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

/**
 * Created by chen on 01/11/2017.
 */

public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        /*
        if (networkPref.equals(WIFI) && networkInfo != null
                && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            // Wi´Fi is connected, so use Wi´Fi
        } else if (networkPref.equals(ANY) && networkInfo != null) {
            // Have a network connection and permission, so use data
        } else {
            // No Wi´Fi and no permission, or no network connection
        }
        */
    }
}