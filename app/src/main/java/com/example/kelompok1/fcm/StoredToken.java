package com.example.kelompok1.fcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class StoredToken {

    private static final String SHARED_PREF_NAME = "FCMSharedPref";
    static final String TAG_TOKEN = "tag_token";

    private static StoredToken mInstance;
    private static Context mCtx;

    private StoredToken(Context context){
        mCtx = context;
    }

    public static synchronized StoredToken getInstance(Context context){
        if (mInstance == null){
            mInstance = new StoredToken(context);
        }

        return mInstance;
    }

    public boolean saveDeviceToken(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
        return true;
    }

    public String getDeviceToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TAG_TOKEN, null);
    }

}
