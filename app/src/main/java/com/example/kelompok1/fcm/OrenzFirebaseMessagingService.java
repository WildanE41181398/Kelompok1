package com.example.kelompok1.fcm;

import android.content.Intent;
import android.util.Log;

import com.example.kelompok1.Beranda;
import com.example.kelompok1.BerandaOrenz;
import com.example.kelompok1.Helper.SessionManager;
import com.example.kelompok1.ui.promosi.PromosiFragment;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrenzFirebaseMessagingService extends FirebaseMessagingService {


    private static final String TAG = "OrenzFirebaseIIDService";
    private static final String MYTAG = "MyFirebaseMsgService";
    SessionManager sessionManager;

    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        storeToken(refreshedToken);
    }

    public String onTokenSend(){
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        return refreshedToken;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

                remoteMessage.getData();

                OrenzNotificationManager mNotificationManager = new OrenzNotificationManager(getBaseContext());
                Map<String, String> data = remoteMessage.getData();

                Log.d(TAG, data.get("data"));

                String tata = data.get("data");

                try {
                    JSONObject json = new JSONObject(tata);
                    String title = json.getString("title");
                    String body = json.getString("message");
                    String payload = json.getString("payload");

                    JSONObject object = new JSONObject(payload);
                    String paramintent = object.getString("intent");

                    Intent intent = null;

                    switch (paramintent){
                        case "home":
                            intent = new Intent(getApplicationContext(), BerandaOrenz.class);
                            intent.putExtra("NAVIGATION", "HOME");
                            break;

                        case "history":
                            intent = new Intent(getApplicationContext(), BerandaOrenz.class);
                            intent.putExtra("NAVIGATION", "HISTORY");
                            break;

                        case "promo":
                            intent = new Intent(getApplicationContext(), BerandaOrenz.class);
                            intent.putExtra("NAVIGATION", "PROMOSI");
                            break;

                        case "notifikasi":
                            intent = new Intent(getApplicationContext(), BerandaOrenz.class);
                            intent.putExtra("NAVIGATION", "NOTIFIKASI");
                            break;

                        case "akun":
                            intent = new Intent(getApplicationContext(), BerandaOrenz.class);
                            intent.putExtra("NAVIGATION", "AKUN");
                            break;
                    }

                    mNotificationManager.showSmallNotif(title, body, intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

        }

    }

    private void storeToken(String token){
        StoredToken.getInstance(getBaseContext()).saveDeviceToken(token);
    }
}
