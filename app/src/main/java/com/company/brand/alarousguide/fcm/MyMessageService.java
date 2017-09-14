package com.company.brand.alarousguide.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.company.brand.alarousguide.CustomerActivities.HomeActivity;
import com.company.brand.alarousguide.R;
import com.company.brand.alarousguide.TraderActivities.TraderOffersActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


public class MyMessageService extends FirebaseMessagingService {
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();

        handler = new Handler();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        sp = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sp.edit();
        // Check if message contains a data payload.
        Log.e("hiiii","notification");
        Log.e("data",remoteMessage.getData().toString());
        Map<String, String> data = remoteMessage.getData();

        if (data.size() > 0) {

            // Send a notification that you got a new message
            sendNotification(data);

        }
    }

    private void sendNotification(Map<String, String> data) {
        Intent intent;
        int role_id = sp.getInt("roleId",0);
        if (role_id == 1){

            intent= new Intent(this, HomeActivity.class);

        } else {

            intent= new Intent(this,  TraderOffersActivity.class);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        String message =  data.get("title");
        String message_title = data.get("message");

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo_app)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.drawable.logo_app))
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText(message))
                .setContentTitle(message_title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }
}
