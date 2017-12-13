package io.github.arecastudio.jniaga.srv;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import io.github.arecastudio.jniaga.MainActivity;
import io.github.arecastudio.jniaga.R;

/**
 * Created by android on 12/13/17.
 */

public class FirebaseMsg extends FirebaseMessagingService {
    private final String TAG = "FirebaseMsg";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            SendNotif(remoteMessage.getNotification().getBody());
        }

        //String msg=remoteMessage.getData().get("m");
    }

    private void SendNotif(String body) {
        Intent intent=new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("notif",body);

        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        String channelId = getString(R.string.default_notification_channel_id);

        Uri notifSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("JNiaga Info")
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notifSound)
                .setContentIntent(pendingIntent);

        NotificationManager manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());
    }
}
