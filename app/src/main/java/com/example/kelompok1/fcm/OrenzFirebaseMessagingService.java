package com.example.kelompok1.fcm;

import android.content.Intent;
import android.util.Log;

import com.example.kelompok1.BerandaOrenz;
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

    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        storeToken(refreshedToken);
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

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
            } else {
                // Handle message within 10 seconds
//                handleNow();
            }

        }

        // Check if message contains a notification payload.
        remoteMessage.getData();
//            Log.d(TAG, "Message Notification Title: " + remoteMessage.getNotification().getTitle());
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

        OrenzNotificationManager mNotificationManager = new OrenzNotificationManager(getBaseContext());
        Intent intent;
//            String title = remoteMessage.getNotification().getTitle();
//            String body = remoteMessage.getNotification().getBody();
        Map<String, String> data = remoteMessage.getData();

        Log.d(TAG, data.get("data"));

        String tata = data.get("data");

        try {
            JSONObject json = new JSONObject(tata);
            String title = (String) json.get("title");
            String body = (String) json.get("message");

            String paramintent = "beranda";

            if (paramintent.equalsIgnoreCase("promo")){
                intent = new Intent(getApplicationContext(), PromosiFragment.class);
            }else{
                intent = new Intent(getApplicationContext(), BerandaOrenz.class);
            }

            mNotificationManager.showSmallNotif(title, body, intent);

        } catch (JSONException e) {
            e.printStackTrace();
        }



        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void storeToken(String token){
        StoredToken.getInstance(getBaseContext()).saveDeviceToken(token);
    }
}
