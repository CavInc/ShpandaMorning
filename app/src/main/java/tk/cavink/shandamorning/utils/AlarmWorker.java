package tk.cavink.shandamorning.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import tk.cavink.shandamorning.ui.activites.AlarmActivity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by cav on 04.08.21.
 */

public class AlarmWorker extends Worker {


    private static final String TAG = "WRK";

    public AlarmWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG,"StartWork");
        Intent intent = new Intent(getApplicationContext(),AlarmActivity.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
        return Worker.Result.success();
    }
}
