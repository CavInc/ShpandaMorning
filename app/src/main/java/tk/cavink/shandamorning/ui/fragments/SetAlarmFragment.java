package tk.cavink.shandamorning.ui.fragments;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.rm.rmswitch.RMSwitch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tk.cavink.shandamorning.R;
import tk.cavink.shandamorning.data.managers.DataManager;
import tk.cavink.shandamorning.data.models.AlarmData;
import tk.cavink.shandamorning.data.models.AlarmDaySetData;
import tk.cavink.shandamorning.ui.activites.MainActivity;
import tk.cavink.shandamorning.ui.adapters.DaysAdapter;
import tk.cavink.shandamorning.ui.helpers.SelectDayListener;
import tk.cavink.shandamorning.utils.ConstantManager;
import tk.cavink.shandamorning.utils.Func;

/**
 * Created by cav on 05.08.21.
 */

public class SetAlarmFragment extends Fragment implements View.OnClickListener,SelectDayListener,View.OnTouchListener{
    private static final String MODE = "MODE";
    private static final int REQUEST_RINGTONE = 234;
    private static final String TAG = "SAF";
    private DataManager mDataManager;

    private com.shawnlin.numberpicker.NumberPicker np1;
    private com.shawnlin.numberpicker.NumberPicker np2;
    private RMSwitch mVibroSet;
    private SeekBar mVolume;

    private TextView mLangTV;
    private ImageView mLangFlag;
    private TextView mRingtoneName;

    private int mode = ConstantManager.ADD_ALARM;
    private int mAlarmID;

    private String mRingtoneUri;

    private String[] mDay = {"F","F","F","F","F","F","F"};

    private DaysAdapter mDaysAdapter;
    private RecyclerView mRecyclerView;

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
        mLangFlag = rootView.findViewById(R.id.lang_flag);

        np1 = rootView.findViewById(R.id.numberPicker1);
        np1.setMinValue(0);
        np1.setMaxValue(23);
        np2 = rootView.findViewById(R.id.numberPicker2);
        np2.setMinValue(0);
        np2.setMaxValue(59);

        Calendar c = Calendar.getInstance();
        np1.setValue(c.get(Calendar.HOUR_OF_DAY));
        np2.setValue(c.get(Calendar.MINUTE));



        rootView.findViewById(R.id.back_bt).setOnClickListener(this);
        rootView.findViewById(R.id.success_bt).setOnClickListener(this);
        rootView.findViewById(R.id.lv_ring).setOnClickListener(this);
        rootView.findViewById(R.id.delete_bt).setOnClickListener(this);

        mRecyclerView = rootView.findViewById(R.id.select_day);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        ArrayList<AlarmDaySetData> mDays = new ArrayList<>();

        mDays.add(new AlarmDaySetData(1,"Пн",false));
        mDays.add(new AlarmDaySetData(2,"Вт",false));
        mDays.add(new AlarmDaySetData(3,"Ср",false));
        mDays.add(new AlarmDaySetData(4,"Чт",false));
        mDays.add(new AlarmDaySetData(5,"Пт",false));
        mDays.add(new AlarmDaySetData(6,"Сб",false));
        mDays.add(new AlarmDaySetData(7,"Вс",false));

        if (mode == ConstantManager.ADD_ALARM) {
            mLangTV.setText(R.string.str_lang_ru);
            mLangFlag.setImageResource(R.drawable.ic_russia);
            ((TextView) getActivity().findViewById(R.id.tv_head_2)).setText("Создать будильник");
            mRingtoneName.setText("Не выбрано");
        } else {
            ((TextView) getActivity().findViewById(R.id.tv_head_2)).setText("Изменить будильник");
            AlarmData data = mDataManager.getAlarmData();
            mAlarmID = data.getId();
            np1.setValue(data.getH());
            np2.setValue(data.getM());
            mVolume.setProgress(data.getVolume());
            //mDay = data.getDays();
            if (data.getLang().equals("ru")) {
                mLangTV.setText(R.string.str_lang_ru);
                mLangFlag.setImageResource(R.drawable.ic_russia);
            } else if (data.getLang().equals("en")) {
                mLangFlag.setImageResource(R.drawable.ic_english);
                mLangTV.setText(R.string.str_lang_en);
            } else if (data.getLang().equals("de")) {
                mLangFlag.setImageResource(R.drawable.ic_german);
                mLangTV.setText(R.string.str_lang_de);
            }

            mVibroSet.setChecked(data.isVibro());
            if (data.getRingtone() != null) {
                setRingtoneName(Uri.parse(data.getRingtone()));
                mRingtoneUri = data.getRingtone();
            } else {
                mRingtoneName.setText("Не выбрано");
            }
            int i = 0;
            for (Boolean l:data.getDays()) {
                mDays.get(i).setAction(l);
                i +=1;
            }
        }

        mDaysAdapter = new DaysAdapter(getActivity(),mDays,this);
        mRecyclerView.setAdapter(mDaysAdapter);

        rootView.findViewById(R.id.lang_panel).setOnTouchListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).changeVisibleThemeButton(false);
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

            List<AlarmDaySetData> days = mDaysAdapter.getItems();
            ArrayList<Boolean> days_out = new ArrayList<>();
            for (AlarmDaySetData l:days) {
                days_out.add(l.isAction());
            }

            String lang = "";
            if (mLangTV.getText().equals(getResources().getString(R.string.str_lang_ru))) {
                lang = "ru";
            } else if (mLangTV.getText().equals(getResources().getString(R.string.str_lang_en))) {
                lang = "en";
            } else {
                lang = "de";
            }

            if (mode == ConstantManager.ADD_ALARM) {
                long id = mDataManager.getDBConnect().addAlarm(new AlarmData(h, m, volume, vibro, true, days_out, lang,mRingtoneUri));
                mAlarmID = (int) id;
            } else {
                mDataManager.getDBConnect().editAlarm(new AlarmData(mAlarmID,h, m, volume, vibro, true, days_out, lang,mRingtoneUri));
            }

            if (mDataManager.getDBConnect().getAlarmOne(mAlarmID).isActive()) {
                Func.setAlarmAM(getActivity(), new AlarmData(mAlarmID, h, m, volume, vibro, true, days_out, lang, mRingtoneUri), true);
            }

            ((MainActivity) getActivity()).viewFragment(new AlarmListFragment(),"ALARM_LIST");
        }
        if (v.getId() == R.id.lv_ring) {
            Intent intent=new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            startActivityForResult(intent,REQUEST_RINGTONE);
        }
        if (v.getId() == R.id.delete_bt) {
            //TODO задать вопрос ?
            //Func.setAlarmAM(getActivity(),,false);
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

    @Override
    public void onChange(int position) {
        mDaysAdapter.getItem(position).setAction(!mDaysAdapter.getItem(position).isAction());
        mDaysAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.lang_panel:
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG,"EVENT UP");
                        Log.d(TAG,mLangTV.getText().toString());
                        if (mLangTV.getText().equals(getResources().getString(R.string.str_lang_ru))) {
                            mLangTV.setText(R.string.str_lang_en);
                            mLangFlag.setImageResource(R.drawable.ic_english);
                        } else if (mLangTV.getText().equals(getResources().getString(R.string.str_lang_en))){
                            mLangTV.setText(R.string.str_lang_de);
                            mLangFlag.setImageResource(R.drawable.ic_german);
                        } else {
                            mLangTV.setText(R.string.str_lang_ru);
                            mLangFlag.setImageResource(R.drawable.ic_russia);
                        }

                        break;
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG,"EVENT DOWN");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float x = event.getRawX();

                        break;
                }
                break;
        }
        return true;
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
