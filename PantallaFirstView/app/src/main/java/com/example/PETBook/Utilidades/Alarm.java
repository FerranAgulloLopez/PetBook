package com.example.PETBook.Utilidades;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.pantallafirstview.R;

public class Alarm extends BroadcastReceiver {

    NotificationCompat.Builder mybuilder;
    int myNofiticationId = 1;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("ALARM", "Alarm fired!!");

        mybuilder =  new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_menu_send)
                .setContentTitle("PeetBook")
                .setContentText("Evento próximo")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                //.setContentIntent(pendingIntent)
                .setAutoCancel(true);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(myNofiticationId, mybuilder.build());

    }
}
