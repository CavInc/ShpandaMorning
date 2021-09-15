package tk.cavink.shandamorning.services;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.util.Log;



import tk.cavink.shandamorning.App;

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
            Intent intent = new Intent(App.getContext(), AlarmActivity.class);
            intent.putExtra(ConstantManager.ALARM_ID, mId);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(ConstantManager.VIBRO_ENABLE, true);
            App.getContext().startActivity(intent);

        }
    }


}
