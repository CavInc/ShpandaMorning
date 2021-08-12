package tk.cavink.shandamorning.ui.activites;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.IOException;
import java.util.Calendar;

import tk.cavink.shandamorning.R;
import tk.cavink.shandamorning.data.managers.DataManager;
import tk.cavink.shandamorning.data.models.AlarmData;
import tk.cavink.shandamorning.utils.ConstantManager;
import tk.cavink.shandamorning.utils.Func;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener {
    private static final double MAX_VOLUME = 100;
    private static final String TAG = "AA";
    private MediaPlayer mMediaPlayer;

    private DataManager mDataManager;

    private String urlSound;
    private int alarm_id;
    private int alarm_size;
    private int alarm_type;
    private boolean vibrate;

    private AlarmData mAlarmData;
    private TextView mTime;
    private int alarm_volume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_alarm);

        mDataManager = DataManager.getInstance();

        mTime = findViewById(R.id.timeTv);

        int mID = getIntent().getIntExtra(ConstantManager.ALARM_ID,-1);

        Log.d(TAG,"ALARM ID : "+mID);

        if (mID != -1){
            mAlarmData = mDataManager.getDBConnect().getAlarmOne(mID);
            mTime.setText(String.format("%02d",mAlarmData.getH())+" : "+String.format("%02d",mAlarmData.getM()));
            urlSound = mAlarmData.getRingtone();
            alarm_volume = mAlarmData.getVolume();
            vibrate = mAlarmData.isVibro();
        }

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(mCompletionListener);

        findViewById(R.id.stop_alarm).setOnClickListener(this);
        findViewById(R.id.long_10).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // перезапускаем будильник
        Func.setAlarmAM(this,mAlarmData,true);

        if (vibrate) {
            Func.playMessage(this);
        }
        startMusic();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void startMusic(){

        if (urlSound!=null && urlSound.length()!=0) {
            Log.d("ALARMA",urlSound);
            try {
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(this, Uri.parse(urlSound));
                mMediaPlayer.prepare();
                final float volume = (float) (1 - (Math.log(MAX_VOLUME - alarm_volume) / Math.log(MAX_VOLUME)));

                mMediaPlayer.setVolume(alarm_volume/100.0f, alarm_volume/100.0f);
               // mMediaPlayer.setVolume(volume,volume);
                mMediaPlayer.setScreenOnWhilePlaying(true); // не дает уснуть во премя воспроизведениея ?
                mMediaPlayer.setLooping(true); // зациклим до окончания работы активности
                int duration = mMediaPlayer.getDuration();
                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }

    }

    private void stopMusic(){
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.release();
                mMediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            if (mediaPlayer.isPlaying()) {

            }
        }
    };

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.stop_alarm) {
            stopMusic();
            finish();
        }
        if (view.getId() == R.id.long_10) {
            // запускаем на 10 минут познее
            stopMusic();
            Calendar c = Calendar.getInstance();
            c.set(Calendar.MINUTE,mAlarmData.getM());
            c.set(Calendar.HOUR_OF_DAY,mAlarmData.getH());
            c.add(Calendar.MINUTE,10);
            mAlarmData.setH(c.get(Calendar.HOUR_OF_DAY));
            mAlarmData.setM(c.get(Calendar.MINUTE));
            Func.setAlarmAM(this,mAlarmData,true);
            finish();
        }
    }
}
