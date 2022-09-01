package com.practice.myapplication;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {

    public final String CHANNEL_ID = "1";
    private String title;

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "NotebrainerNotify",
                NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        title = intent.getStringExtra("title");

        Notification.Builder builder = new Notification.Builder(context, CHANNEL_ID);
        builder.setContentTitle("NoteBrainer")
                .setSmallIcon(R.drawable.ic_notifyon)
                .setContentText("Heyyo, It's time for " + title)
                .setColor(Color.BLACK);

        NotificationManagerCompat compat = NotificationManagerCompat.from(context);
        compat.notify(1, builder.build());

    }
}
