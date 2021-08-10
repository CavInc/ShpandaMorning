package tk.cavink.shandamorning.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkerParameters;
import tk.cavink.shandamorning.data.models.AlarmData;
import tk.cavink.shandamorning.services.AlarmTaskReciver;
import tk.cavink.shandamorning.ui.activites.AlarmActivity;

/**
 * Created by cav on 04.08.21.
 */

public class Func {

    public static  boolean ALARM_START = true;
    private static boolean ALARM_CANCEL = false;

    public static String dateToStr(String mask,Date date){
        SimpleDateFormat format = new SimpleDateFormat(mask);
        return format.format(date);
    }

    public static Date strToDate(String mask,String date){
        SimpleDateFormat format = new SimpleDateFormat(mask);
        try {
            return  format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void setAlarm(Date date){
        Log.d("FUNC",dateToStr("yyyy-MM-dd HH:mm:ss",date));

        Calendar cl = Calendar.getInstance();

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(false)
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //constraints.setRequiresDeviceIdle(false);
        }

        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(AlarmWorker.class)
                .setConstraints(constraints)
                .setInitialDelay(c.getTimeInMillis()-cl.getTimeInMillis(), TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance().enqueue(oneTimeWorkRequest);
    }

    public static void setAlarmAM(Context context, AlarmData date, boolean mode){
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(context, AlarmTaskReciver.class);
        intent.putExtra(ConstantManager.ALARM_ID,date.getId());
        // добавить констану ?
        PendingIntent pi= PendingIntent.getBroadcast(context,date.getId(), intent,PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cl = Calendar.getInstance();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,date.getH());
        c.set(Calendar.MINUTE,date.getM());
        c.set(Calendar.SECOND,0);

        // проверка по времени в текущей дате
        if (cl.after(c)){
            // время до то кореектируем на день вперед
            Log.d("FNC",dateToStr("yyyy-MM-dd HH:mm:ss",cl.getTime()));
            c.add(Calendar.DAY_OF_MONTH,1);
        } else {
            Log.d("FNC","BEFORE");
        }

        Log.d("FUNC",dateToStr("yyyy-MM-dd HH:mm:ss",c.getTime()));

        if (mode) {
            am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
        } else {
            am.cancel(pi);
        }
    }

    // проиграем звук
    public static  void playSound(Context context){
        try {
            Uri notify = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notify);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // вибрация
    public static void playMessage(Context context){
        long mills = 300L;
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(mills);
    }
}
