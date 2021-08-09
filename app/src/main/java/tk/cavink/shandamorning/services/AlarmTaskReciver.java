package tk.cavink.shandamorning.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
        mContext = context;
        mId = intent.getIntExtra(ConstantManager.ALARM_ID,-1);
        startAlarm();
    }

    private void startAlarm(){
        /*
        Intent intent = new Intent(mContext, AlarmSignalActivity.class);
        intent.putExtra("URL_SOUND",mModel.getAlarmUrlMelodu());
        intent.putExtra("TYPE_ALARM",mModel.getAlarmStopType());
        intent.putExtra("SIZE_ALARM",mModel.getAlarmSize());
        intent.putExtra("ALARM_ID",mModel.getId());
        intent.putExtra("ALARM_VOLUME",mModel.getAlarmVolume());
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        */
        if (mId != -1) {
            Intent intent = new Intent(mContext, AlarmActivity.class);
            intent.putExtra(ConstantManager.ALARM_ID,mId);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(ConstantManager.VIBRO_ENABLE, true);
            mContext.startActivity(intent);
        }
    }
}
