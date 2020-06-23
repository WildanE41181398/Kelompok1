package com.example.kelompok1.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.kelompok1.R;

public class OrenzNotificationManager {

    public static final int ID_BIG_NOTIFICATION = 234;
    public static final int ID_SMALL_NOTIFICATION = 235;

    private Context mCtx;

    public OrenzNotificationManager(Context mCtx){
        this.mCtx = mCtx;
    }

    public void showSmallNotif(String title, String message, Intent intent){
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mCtx,
                        0,
                        intent,
                        PendingIntent.FLAG_ONE_SHOT
                );

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx, "Default");
        Notification notif;
        notif = mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_notifikasi_kecil)
                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.ic_ol_logosquare))
                .setContentText(message)
                .build();

        notif.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("Default", "Default Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notif);

    }

}
