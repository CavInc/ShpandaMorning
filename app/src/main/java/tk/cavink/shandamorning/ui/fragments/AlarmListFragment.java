package tk.cavink.shandamorning.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import tk.cavink.shandamorning.R;
import tk.cavink.shandamorning.data.managers.DataManager;
import tk.cavink.shandamorning.data.models.AlarmData;
import tk.cavink.shandamorning.ui.activites.MainActivity;
import tk.cavink.shandamorning.ui.adapters.AlarmListAdapter;
import tk.cavink.shandamorning.ui.helpers.SelectAlarmListener;
import tk.cavink.shandamorning.utils.ConstantManager;
import tk.cavink.shandamorning.utils.Func;

/**
 * Created by cav on 05.08.21.
 */

public class AlarmListFragment extends Fragment implements View.OnClickListener,SelectAlarmListener {
    private DataManager mDataManager;
    private RecyclerView mRecyclerView;
    private AlarmListAdapter mAdapter;

    private ImageButton mAddAlarm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.alarmlist_fragment, container, false);

        ((TextView) getActivity().findViewById(R.id.tv_head_2)).setText("Ваши будильники");

        mRecyclerView = rootView.findViewById(R.id.alarm_list_lv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAddAlarm = rootView.findViewById(R.id.add_alarm_bt);
        mAddAlarm.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).changeVisibleThemeButton(true);
        updateUI();
        setDemo();
    }

    //Демо режим
    private void setDemo(){
        Calendar c = Calendar.getInstance();
        c.set(2021,8,30);
        Date ls = c.getTime();
        Date currentDate = new Date();
        Log.d("ALF",Func.dateToStr("yyyy-MM-dd",ls));
        Log.d("ALF",Func.dateToStr("yyyy-MM-dd",currentDate));
        if (currentDate.getTime() > ls.getTime()) {
            mAddAlarm.setEnabled(false);
            mAddAlarm.setVisibility(View.GONE);
            Toast.makeText(getActivity(),"Конец пробной версии",Toast.LENGTH_LONG).show();
        }
    }

    private void updateUI(){
        try {
            ArrayList<AlarmData> data1 = mDataManager.getDBConnect().getAlarm(true);
            if (data1.size() != 0 ){
                getActivity().findViewById(R.id.start_message_lv).setVisibility(View.GONE);
            }
            if (mAdapter == null) {
                mAdapter = new AlarmListAdapter(getActivity(),data1,this);
                mRecyclerView.setAdapter(mAdapter);
            }
        } catch (Exception e){
            mDataManager.getDBConnect().dropDB();
            updateUI();
        }
    }

    @Override
    public void onClick(View v) {
        ((MainActivity) getActivity()).viewFragment(SetAlarmFragment.newInstance(ConstantManager.ADD_ALARM),"SET_ALARM");
    }


    @Override
    public void selectItem(int position) {
        AlarmData data = mAdapter.getItem(position);
        mDataManager.setAlarmData(data);
        ((MainActivity) getActivity()).viewFragment(SetAlarmFragment.newInstance(ConstantManager.EDIT_ALARM),"SET_ALARM");
    }

    @Override
    public void onChangeAction(int position, boolean action) {

        AlarmData data = mAdapter.getItem(position);
        Func.setAlarmAM(getActivity(),data,action);

        mDataManager.getDBConnect().setActiveAlarm(mAdapter.getItem(position).getId(),action);
    }
}
