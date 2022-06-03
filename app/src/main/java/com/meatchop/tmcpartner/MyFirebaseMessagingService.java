package com.meatchop.tmcpartner;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.OtherClasses.MobileScreen_Dashboard;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static com.meatchop.tmcpartner.R.mipmap.tmcicon_launcher;
import static com.meatchop.tmcpartner.R.mipmap.tmcicon_launcher_transperent;
import static com.meatchop.tmcpartner.R.mipmap.tmcicon_launchersmall;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    //extending the FirebaseMessagingService class in MyFirebaseMessagingService class to get all the  FirebaseMessagingService methods


    String TAG = "Tag";
    String tmcctgyname ;
    String tmcSubctgyname ;
    String tmcSubctgykey ;

    //this method is Called when a notification is received.
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //this is the general configuration for recieving the notification
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getData());

            try {
                Map<String, String> map = remoteMessage.getData();

                 tmcSubctgykey = map.get("tmcsubctgykey");
                Log.d(TAG, "Message Notification Body:+tmcSubctgykey " + tmcSubctgykey);

                tmcSubctgyname = map.get("tmcsubctgyname");
                Log.d(TAG, "Message Notification Body:+tmcSubctgyname " + tmcSubctgyname);

                tmcctgyname = map.get("tmcctgyname");
                Log.d(TAG, "Message Notification Body:+tmcctgyname " + tmcctgyname);

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
            //Setting the intent to open any activty from our appliction when user clicked the notification
            Intent intent = new Intent(this, MobileScreen_Dashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


            intent.putExtra("tmcSubctgykey",tmcSubctgykey);
            intent.putExtra("tmcSubctgyname",tmcSubctgyname);
            intent.putExtra("tmcctgyname",tmcctgyname);



                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                    PendingIntent.FLAG_IMMUTABLE);
            String channelId  = "Notification";
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                NotificationChannel channel = new NotificationChannel(channelId,
                        "Notification for device above Nought",
                        NotificationManager.IMPORTANCE_HIGH);
                channel.enableVibration(true);
                channel.setDescription("Description");
                channel.setVibrationPattern(new long[]{400, 400});
                channel.setShowBadge(true);

              //  channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);

                channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);


            }


            if(remoteMessage.getNotification().getImageUrl()!=null){

                String imageUrl = remoteMessage.getNotification().getImageUrl().toString();
                Bitmap bitmap = getBitmapfromUrl(imageUrl);
                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(this, channelId)
                                .setSmallIcon(R.drawable.tmctransparent)
                                .setContentTitle(remoteMessage.getNotification().getTitle())
                                .setContentText(remoteMessage.getNotification().getBody())
                                .setSound(defaultSoundUri)
                                .setColor(990000)
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                        tmcicon_launcher))
                                .setVibrate(new long[]{400, 400})
                                .setPriority(Notification.PRIORITY_MAX)
                                .setAutoCancel(true)
                                .setStyle(new NotificationCompat.BigPictureStyle()
                                        .bigPicture(bitmap)
                                        .bigLargeIcon(BitmapFactory.decodeResource(getResources(),
                                                R.mipmap.tmcicon_launcher))
                                )
                               .setContentIntent(pendingIntent);


                notificationBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;


                notificationManager.notify(0, notificationBuilder.build());

            }
            else {

                   NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(this, channelId)
                                .setSmallIcon(R.drawable.tmctransparent)
                                .setContentTitle(remoteMessage.getNotification().getTitle())
                                .setContentText(remoteMessage.getNotification().getBody())
                                .setSound(defaultSoundUri)
                                .setColor(990000)
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                        tmcicon_launcher))
                                .setVibrate(new long[]{400, 400})
                                .setPriority(Notification.PRIORITY_MAX)
                                .setAutoCancel(true)

                                .setContentIntent(pendingIntent);


                notificationBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;


                notificationManager.notify(0, notificationBuilder.build());


            }

        }

    }






    @Override
    public void onNewToken(String token) {
        //Log.d(TAG, "Refreshed token: " + token);

    }






    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            Log.e("awesome", "Error in getting notification image: " + e.getLocalizedMessage());
            return null;
        }
    }



}