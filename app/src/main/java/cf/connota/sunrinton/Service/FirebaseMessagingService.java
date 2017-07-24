package cf.connota.sunrinton.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import cf.connota.sunrinton.R;
import cf.connota.sunrinton.Activity.ResponsePlz;

/**
 * Created by Conota on 2017-07-24.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";
    private String msg;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        msg = remoteMessage.getData().get("title");

        Intent intent = new Intent(this, ResponsePlz.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);

        showNoti();
    }

    public void showNoti() {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, ResponsePlz.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(msg)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{1, 1000});

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mBuilder.build());

        mBuilder.setContentIntent(contentIntent);
    }
}
