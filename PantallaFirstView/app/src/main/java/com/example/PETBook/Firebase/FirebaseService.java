package com.example.PETBook.Firebase;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;


import com.example.PETBook.Conexion;
import com.example.PETBook.Controllers.AsyncResult;
import com.example.PETBook.MainActivity;
import com.example.PETBook.PantallaLogSign;
import com.example.PETBook.SingletonUsuario;
import com.example.pantallafirstview.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;


public class FirebaseService extends FirebaseMessagingService implements AsyncResult {

    String TAG = "Mensajes Firebase: ";
    NotificationCompat.Builder mybuilder;
    int myNofiticationId = 1;



    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        System.out.println("!!!!!!!!!!!! TOKEN FCM !!!!!!!!!!!!!!!");
        Log.d("Firebase", "token " + FirebaseInstanceId.getInstance().getToken());
        sendRegistrationToServer(token);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }



        // Notifications
        // Create an explicit intent for an Activity in your app

        SharedPreferences sharedPreferences = getSharedPreferences("credenciales", MODE_PRIVATE);
        String login = sharedPreferences.getString("login",null);
        Intent intent;

        if (login != null) {
             intent = new Intent(this, MainActivity.class);
        }
        else {
            intent = new Intent(this, PantallaLogSign.class);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        mybuilder =  new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_menu_send)
                .setContentTitle("PeetBook")
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(myNofiticationId, mybuilder.build());

    }


    public void sendRegistrationToServer(String token) {


        SingletonUsuario user = SingletonUsuario.getInstance();

        Conexion con = new Conexion(this);
        JSONObject jsonToSend = new JSONObject();
        try {
            jsonToSend.accumulate("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        con.execute("http://10.4.41.146:9999/ServerRESTAPI/token/" + user.getEmail(), "POST", jsonToSend.toString());

    }

    @Override
    public void OnprocessFinish(JSONObject output) {

        try {
            if(output.getInt("code")==200) {
                System.out.println("New token to server");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            try {
                System.out.println(output.getInt("code") +"\n\n\n");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }

    }
}
