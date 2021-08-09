package tk.cavink.shandamorning.ui.fragments;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import tk.cavink.shandamorning.R;
import tk.cavink.shandamorning.data.managers.DataManager;
import tk.cavink.shandamorning.data.models.AlarmData;
import tk.cavink.shandamorning.ui.activites.MainActivity;
import tk.cavink.shandamorning.utils.ConstantManager;
import tk.cavink.shandamorning.utils.Func;

/**
 * Created by cav on 05.08.21.
 */

public class SetAlarmFragment extends Fragment implements View.OnClickListener{
    private static final String MODE = "MODE";
    private static final int REQUEST_RINGTONE = 234;
    private static final String TAG = "SAF";
    private DataManager mDataManager;

    private NumberPicker np1;
    private NumberPicker np2;
    private SwitchCompat mVibroSet;
    private SeekBar mVolume;

    private TextView mLangTV;
    private TextView mRingtoneName;

    private int mode = ConstantManager.ADD_ALARM;
    private int mAlarmID;

    private String mRingtoneUri;

    private boolean[] mDay = {false,false,false,false,false,false,false};

    public static SetAlarmFragment newInstance(int mode){
        Bundle args = new Bundle();
        args.putInt(MODE,mode);
        SetAlarmFragment fragment = new SetAlarmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
        if (getArguments() != null) {
            mode = getArguments().getInt(MODE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.setalarm_fragment, container, false);

        mVibroSet = rootView.findViewById(R.id.vibro_set);
        mLangTV = rootView.findViewById(R.id.lang_tv);
        mVolume = rootView.findViewById(R.id.volume);
        mRingtoneName = rootView.findViewById(R.id.add_rington_name);

        np1 = rootView.findViewById(R.id.numberPicker1);
        np1.setMinValue(0);
        np1.setMaxValue(23);
        np2 = rootView.findViewById(R.id.numberPicker2);
        np2.setMinValue(0);
        np2.setMaxValue(59);

        Calendar c = Calendar.getInstance();
        np1.setValue(c.get(Calendar.HOUR_OF_DAY));
        np2.setValue(c.get(Calendar.MINUTE));

        ((TextView) getActivity().findViewById(R.id.tv_head_2)).setText("Создать будильник");

        rootView.findViewById(R.id.back_bt).setOnClickListener(this);
        rootView.findViewById(R.id.success_bt).setOnClickListener(this);
        rootView.findViewById(R.id.lv_ring).setOnClickListener(this);
        rootView.findViewById(R.id.delete_bt).setOnClickListener(this);

        if (mode == ConstantManager.ADD_ALARM) {
            mLangTV.setText("Русский");
        } else {
            AlarmData data = mDataManager.getAlarmData();
            mAlarmID = data.getId();
            np1.setValue(data.getH());
            np2.setValue(data.getM());
            mVolume.setProgress(data.getVolume());
            //mDay = data.getDays();

            mVibroSet.setChecked(data.isVibro());
            if (data.getRingtone() != null) {
                setRingtoneName(Uri.parse(data.getRingtone()));
            }
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_bt) {
            ((MainActivity) getActivity()).viewFragment(new AlarmListFragment(),"ALARM_LIST");
        }
        if (v.getId() == R.id.success_bt){
            int h = np1.getValue();
            int m = np2.getValue();
            boolean vibro =  mVibroSet.isChecked();
            int volume = mVolume.getProgress();

            if (mode == ConstantManager.ADD_ALARM) {
                long id = mDataManager.getDBConnect().addAlarm(new AlarmData(h, m, volume, vibro, true, null, "ru",mRingtoneUri));
            } else {
                mDataManager.getDBConnect().editAlarm(new AlarmData(mAlarmID,h, m, volume, vibro, true, null, "ru",mRingtoneUri));
            }

            //TODO установка будильника
            Func.setAlarmAM(getActivity(),new AlarmData(mAlarmID,h, m, volume, vibro, true, null, "ru",mRingtoneUri),true);

            ((MainActivity) getActivity()).viewFragment(new AlarmListFragment(),"ALARM_LIST");
        }
        if (v.getId() == R.id.lv_ring) {
            Intent intent=new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            startActivityForResult(intent,REQUEST_RINGTONE);
        }
        if (v.getId() == R.id.delete_bt) {
            //TODO задать вопрос ?
            mDataManager.getDBConnect().deleteAlarm(mAlarmID);
            // TODO сброс будильника

            ((MainActivity) getActivity()).viewFragment(new AlarmListFragment(),"ALARM_LIST");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_RINGTONE:
                if (resultCode == getActivity().RESULT_OK) {
                    Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    mRingtoneUri = uri.toString();
                    setRingtoneName(uri);
                }
                break;
            default:
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setRingtoneName(Uri uri) {
        Ringtone r=RingtoneManager.getRingtone(getActivity(), uri);
        Log.d(TAG,uri.toString()+" "+r.getTitle(getActivity()));
        mRingtoneName.setText(r.getTitle(getActivity()));
    }

    // https://android.googlesource.com/platform/packages/apps/DeskClock/
    // https://github.com/LineageOS/android_packages_apps_DeskClock
    // https://github.com/CyanogenMod/android_packages_apps_DeskClock
    // https://github.com/lithium/alarming/blob/master/src/com/hlidskialf/android/alarmclock/SetAlarm.java
    // https://code.google.com/archive/p/kraigsandroid/source/default/source
    // https://russianblogs.com/article/4668157306/



    // https://startandroid.ru/ru/uroki/vse-uroki-spiskom/377-urok-162-grafika-drawable-shape-gradient.html
    // https://stackoverflow.com/questions/13929877/how-to-make-gradient-background-in-android

    // https://github.com/StephenVinouze/MaterialNumberPicker
}
