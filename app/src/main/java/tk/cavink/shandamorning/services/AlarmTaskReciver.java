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
            intent.putExtra(ConstantManager.ALARM_ID,mId);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(ConstantManager.VIBRO_ENABLE, true);
            try {
                App.getContext().startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
