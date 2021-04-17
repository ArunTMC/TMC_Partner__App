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

import static com.meatchop.tmcpartner.R.mipmap.tmcicon_launcher;
import static com.meatchop.tmcpartner.R.mipmap.tmcicon_launcher_transperent;
import static com.meatchop.tmcpartner.R.mipmap.tmcicon_launchersmall;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    //extending the FirebaseMessagingService class in MyFirebaseMessagingService class to get all the  FirebaseMessagingService methods


    String TAG = "Tag";

    //this method is Called when a notification is received.
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //this is the general configuration for recieving the notification
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            //Setting the intent to open any activty from our appliction when user clicked the notification
            Intent intent = new Intent(this, MobileScreen_Dashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                    PendingIntent.FLAG_ONE_SHOT);
            String channelId  = "Notification";

            //special notification connfiguration for the oreo device
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)  {
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


                NotificationChannel channel = new NotificationChannel(channelId,
                        "Notification for device above Nought",
                        NotificationManager.IMPORTANCE_HIGH);
                channel.enableVibration(true);
                channel.setDescription("Description");
                channel.setVibrationPattern(new long[]{400, 400});
                channel.setShowBadge(true);

                channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    channel.setAllowBubbles(true);
                }
                channel.setImportance( NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);


                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();

                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(this, channelId)
                                .setSmallIcon(R.drawable.tmctransparent)
                                .setContentTitle(remoteMessage.getNotification().getTitle())
                                .setContentText(remoteMessage.getNotification().getBody())
                                .setSound(defaultSoundUri)
                                .setColor(990000)
                                .setPriority(Notification.PRIORITY_MAX)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                        tmcicon_launcher))
                                .setVibrate(new long[]{400, 400})
                                .setStyle(bigText)
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                                .setAutoCancel(true)
                                .setContentIntent(pendingIntent);




                notificationBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;

                notificationManager.notify(0, notificationBuilder.build());

            }
            else{

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
                Drawable drawable= ContextCompat.getDrawable(this, tmcicon_launcher);

                Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                bigPictureStyle.bigPicture(bitmap);
                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(this, channelId)
                                .setSmallIcon(R.drawable.tmctransparent)
                                .setContentTitle(remoteMessage.getNotification().getTitle())
                                .setContentText(remoteMessage.getNotification().getBody())
                                .setSound(defaultSoundUri)
                                .setColor(990000)
                                .setPriority(2)
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                        tmcicon_launcher))
                                .setVibrate(new long[]{400, 400})
                                .setStyle(bigPictureStyle)
                                .setPriority(Notification.PRIORITY_MAX)
                                .setAutoCancel(true)

                                .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                                .setContentIntent(pendingIntent);



                notificationBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;


                notificationManager.notify(0, notificationBuilder.build());

            }




        }

    }






    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

    }







}