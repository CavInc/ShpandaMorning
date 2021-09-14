package tk.cavink.shandamorning.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Objects;

import tk.cavink.shandamorning.App;
import tk.cavink.shandamorning.R;
import tk.cavink.shandamorning.ui.activites.AlarmActivity;
import tk.cavink.shandamorning.utils.ConstantManager;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by cav on 06.08.21.
 */

public class AlarmTaskReciver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "SPANDA_MORNING";
    private static final String TAG_NOTIFICATION = "NOTIFICATION_MESSAGE";
    private static final int NOTIFICATION_ID = 435;
    private Context mContext;
    private int mId;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ATR","Start Reciver");
        mContext = context;
        mId = intent.getIntExtra(ConstantManager.ALARM_ID,-1);
        Log.d("ATR","ID: "+mId);
        startAlarm();
    }

    private void startAlarm(){
        if (mId != -1) {
            /*
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                startActivityNotification();
            } else {
                Intent intent = new Intent(App.getContext(), AlarmActivity.class);
                intent.putExtra(ConstantManager.ALARM_ID, mId);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(ConstantManager.VIBRO_ENABLE, true);
                App.getContext().startActivity(intent);
            }
            */

            Intent intent = new Intent(App.getContext(), AlarmActivity.class);
            intent.putExtra(ConstantManager.ALARM_ID, mId);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(ConstantManager.VIBRO_ENABLE, true);
            App.getContext().startActivity(intent);

        }
    }

    private void startActivityNotification(){
        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder;

        Intent intent = new Intent(App.getContext(), AlarmActivity.class);
        intent.putExtra(ConstantManager.ALARM_ID,mId);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(ConstantManager.VIBRO_ENABLE, true);

        PendingIntent ContentPendingIntent = PendingIntent.getActivity(mContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Alarm")
                .setContentText("alarm")
                .setColor(mContext.getResources().getColor(R.color.colorPrimaryDark))
                .setAutoCancel(true)
                .setContentIntent(ContentPendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID,
                    "Activity Opening Notification",
                    NotificationManager.IMPORTANCE_HIGH);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setDescription("Activity opening notification");

            mBuilder.setChannelId(CHANNEL_ID);

            Objects.requireNonNull(mNotificationManager).createNotificationChannel(mChannel);
        }
        Objects.requireNonNull(mNotificationManager)
                .notify(TAG_NOTIFICATION,NOTIFICATION_ID,mBuilder.build());

    }
}
