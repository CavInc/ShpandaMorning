package tk.cavink.shandamorning.ui.activites;

import android.app.AlarmManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;

import tk.cavink.shandamorning.R;
import tk.cavink.shandamorning.data.managers.DataManager;
import tk.cavink.shandamorning.data.models.AlarmData;
import tk.cavink.shandamorning.utils.ConstantManager;
import tk.cavink.shandamorning.utils.Func;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener {
    private static final double MAX_VOLUME = 100;
    private static final String TAG = "AA";
    private MediaPlayer mMediaPlayer;

    private DataManager mDataManager;

    private String urlSound;

    private boolean vibrate;

    private AlarmData mAlarmData;
    private TextView mTime;
    private int alarm_volume;

    private ImageButton mAlarmStop; // кнопа гашения
    private Button mLong10;
    private float storeX;
    private AudioManager amanager;
    private int volOld;
    private float storeY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataManager dataManager = DataManager.getInstance();
        setTheme(dataManager.getPrefManager().getThemeId());

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

        mAlarmStop =  findViewById(R.id.stop_alarm);
        //mAlarmStop.setOnClickListener(this);
        mAlarmStop.setOnTouchListener(this);

        mLong10 =  findViewById(R.id.long_10);
        mLong10.setOnClickListener(this);

        final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.rotate);

        animRotate.setRepeatCount(Animation.INFINITE);
        mAlarmStop.startAnimation(animRotate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // перезапускаем будильник

        int countDay = Func.countDay(mAlarmData.getDays());
        if (countDay !=0 ) {
            Func.setAlarmAM(this, mAlarmData, true);
        }

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
            amanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
            volOld = amanager.getStreamVolume(AudioManager.STREAM_MUSIC);

            Log.d("ALARMA",urlSound);
            try {
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(this, Uri.parse(urlSound));
                mMediaPlayer.prepare();

                mMediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build());

                final float volume = (float) (1 - (Math.log(MAX_VOLUME - alarm_volume) / Math.log(MAX_VOLUME)));

                //int volune = amanager.getStreamVolume(AudioManager.STREAM_ALARM);
                //Log.d("ALARMA","ALARM VOLUME: "+volune);
                //amanager.setStreamVolume(AudioManager.STREAM_ALARM,alarm_volume,AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);

                //Log.d("ALARMA","MUSIC VOLUME:" + volOld+" MAX:"+amanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));

                //amanager.setStreamVolume(AudioManager.STREAM_MUSIC,  alarm_volume,AudioManager.FLAG_SHOW_UI+AudioManager.FLAG_PLAY_SOUND);
                amanager.setStreamVolume(AudioManager.STREAM_MUSIC,  alarm_volume,AudioManager.FLAG_PLAY_SOUND);

                //mMediaPlayer.setVolume(alarm_volume/100.0f, alarm_volume/100.0f);
                mMediaPlayer.setVolume(alarm_volume/15.0f,alarm_volume/15.0f);


               // mMediaPlayer.setVolume(volume,volume);
                mMediaPlayer.setScreenOnWhilePlaying(true); // не дает уснуть во премя воспроизведениея ?
                mMediaPlayer.setLooping(true); // зациклим до окончания работы активности
                int duration = mMediaPlayer.getDuration();
                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                amanager.setStreamVolume(AudioManager.STREAM_MUSIC,volOld,AudioManager.FLAG_PLAY_SOUND);
            } finally {
                //amanager.setStreamVolume(AudioManager.STREAM_MUSIC,vol,AudioManager.FLAG_SHOW_UI+AudioManager.FLAG_PLAY_SOUND);
            }
        }

    }

    private void stopMusic(){
        if (mMediaPlayer != null) {
            amanager.setStreamVolume(AudioManager.STREAM_MUSIC,volOld,AudioManager.FLAG_PLAY_SOUND);
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(v.getId()){
            case R.id.stop_alarm:
                switch(event.getAction()){
                    case MotionEvent.ACTION_MOVE:
                        //v.setX(event.getRawX() - v.getWidth()/2);
                        v.setY(event.getRawY() - v.getHeight()/2);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG,"EVENT UP");
                        v.setX(storeX);
                        v.setY(storeY);
                        mLong10.setVisibility(View.VISIBLE);
                        stopMusic();
                        finish();
                        break;
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG,"EVENT DOWN");
                        storeX = v.getX();
                        storeY = v.getY();
                        mLong10.setVisibility(View.GONE);
                        v.clearAnimation();
                        break;
                }
                break;
        }

        return true;
    }
}
