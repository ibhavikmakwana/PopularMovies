package com.ibhavikmakwana.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Bhavik Makwana on 7/29/2017.
 */

public class Utils {
    /**
     * Checks whether network (WIFI/mobile) is available or not.
     *
     * @param context application context.
     * @return true if network available,false otherwise.
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    /**
     * Get the string from the input stream.
     *
     * @param inputStream {@link InputStream} to read.
     * @return String.
     * @throws IOException - If unable to read
     */
    public static String getStringFromStream(@NonNull InputStream inputStream) throws IOException {
        //noinspection ConstantConditions
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) total.append(line).append('\n');
        return total.toString();
    }
}
