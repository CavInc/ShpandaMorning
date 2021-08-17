package tk.cavink.shandamorning.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import tk.cavink.shandamorning.data.managers.DataManager;
import tk.cavink.shandamorning.data.models.AlarmData;
import tk.cavink.shandamorning.utils.Func;

/**
 * Created by cav on 17.08.21.
 */

public class AlarmBootReciver extends BroadcastReceiver {
    private DataManager mDataManager;

    public AlarmBootReciver(){
        mDataManager = DataManager.getInstance();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            ArrayList<AlarmData> dataAlarm = mDataManager.getDBConnect().getAlarm(true);
            for (AlarmData lx:dataAlarm){
                if (lx.isActive()) {
                    Func.setAlarmAM(mDataManager.getContext(),lx,true);
                }
            }
        }
    }
}
