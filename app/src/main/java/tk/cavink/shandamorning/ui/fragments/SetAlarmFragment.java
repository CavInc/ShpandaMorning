package tk.cavink.shandamorning.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Calendar;

import tk.cavink.shandamorning.R;
import tk.cavink.shandamorning.data.managers.DataManager;
import tk.cavink.shandamorning.data.models.AlarmData;
import tk.cavink.shandamorning.ui.activites.MainActivity;

/**
 * Created by cav on 05.08.21.
 */

public class SetAlarmFragment extends Fragment implements View.OnClickListener{
    private DataManager mDataManager;

    private NumberPicker np1;
    private NumberPicker np2;
    private SwitchCompat mVibroSet;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.setalarm_fragment, container, false);

        mVibroSet = rootView.findViewById(R.id.vibro_set);

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

            long id = mDataManager.getDBConnect().addAlarm(new AlarmData(h,m,0,vibro,true,null,"ru"));

        }
        if (v.getId() == R.id.lv_ring) {

        }
    }

    // https://android.googlesource.com/platform/packages/apps/DeskClock/
    // https://github.com/LineageOS/android_packages_apps_DeskClock
    // https://github.com/CyanogenMod/android_packages_apps_DeskClock

    // https://startandroid.ru/ru/uroki/vse-uroki-spiskom/377-urok-162-grafika-drawable-shape-gradient.html
    // https://stackoverflow.com/questions/13929877/how-to-make-gradient-background-in-android

    // https://github.com/StephenVinouze/MaterialNumberPicker
}
