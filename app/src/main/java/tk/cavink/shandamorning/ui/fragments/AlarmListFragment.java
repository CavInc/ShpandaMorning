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

/**
 * Created by cav on 05.08.21.
 */

public class AlarmListFragment extends Fragment implements View.OnClickListener {
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
        updateUI();
    }

    private void updateUI(){
        ArrayList<AlarmData> data = new ArrayList<>();

        data.add(new AlarmData(12,45,100,false,true,null,"ru"));
        data.add(new AlarmData(02,55,100,false,true,null,"ru"));
        data.add(new AlarmData(12,17,100,false,true,null,"ru"));
        data.add(new AlarmData(18,45,100,false,true,null,"ru"));

        if (mAdapter == null) {
            mAdapter = new AlarmListAdapter(getActivity(),data);
            mRecyclerView.setAdapter(mAdapter);
        } else {

        }
    }

    @Override
    public void onClick(View v) {
        ((MainActivity) getActivity()).viewFragment(new SetAlarmFragment(),"SET_ALARM");
    }


}
