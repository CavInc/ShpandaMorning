package tk.cavink.shandamorning.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import tk.cavink.shandamorning.R;
import tk.cavink.shandamorning.data.managers.DataManager;
import tk.cavink.shandamorning.data.models.AlarmData;
import tk.cavink.shandamorning.ui.activites.MainActivity;
import tk.cavink.shandamorning.ui.adapters.AlarmListAdapter;
import tk.cavink.shandamorning.ui.helpers.SelectAlarmListener;
import tk.cavink.shandamorning.utils.ConstantManager;

/**
 * Created by cav on 05.08.21.
 */

public class AlarmListFragment extends Fragment implements View.OnClickListener,SelectAlarmListener {
    private DataManager mDataManager;
    private RecyclerView mRecyclerView;
    private AlarmListAdapter mAdapter;

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

        rootView.findViewById(R.id.add_alarm_bt).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).changeVisibleThemeButton(true);
        updateUI();
    }

    private void updateUI(){
        try {
            ArrayList<AlarmData> data1 = mDataManager.getDBConnect().getAlarm(true);
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
        //TODO добавить сюда отключение будильника

        mDataManager.getDBConnect().setActiveAlarm(mAdapter.getItem(position).getId(),action);
    }
}
