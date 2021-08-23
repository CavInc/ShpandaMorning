package tk.cavink.shandamorning.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.rm.rmswitch.RMSwitch;

import java.util.ArrayList;

import tk.cavink.shandamorning.R;
import tk.cavink.shandamorning.data.models.AlarmData;
import tk.cavink.shandamorning.ui.helpers.SelectAlarmListener;

/**
 * Created by cav on 05.08.21.
 */

public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.ViewHolder> {
    private ArrayList<AlarmData> mData;
    private Context mContext;
    private SelectAlarmListener mSelectAlarmListener;

    public AlarmListAdapter(Context context, ArrayList<AlarmData> data,SelectAlarmListener listener) {
        mContext = context;
        mData = data;
        mSelectAlarmListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.alarm_item,viewGroup,false);
        return new ViewHolder(v,mSelectAlarmListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        AlarmData rec = mData.get(position);
        viewHolder.mHour.setText(String.format("%02d",rec.getH())+":"+String.format("%02d",rec.getM()));
        viewHolder.mSwitch.setChecked(rec.isActive());
        viewHolder.mDay.setText(converDay(rec.getDays()));
    }

    private String converDay(ArrayList<Boolean> days){
        int workDay = 0;
        int i = 0;
        String outDay = "";
        String [] sd = mContext.getResources().getStringArray(R.array.name_day);
        for (Boolean l:days){
            if (l) {
                workDay +=1;
                outDay = outDay+sd[i]+",";
            }
            i+=1;
        }
        if (workDay == 0) {
            return "Сегодня";
        } else if (workDay == 7) {
            return "Каждый день";
        } else {
            return outDay;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public AlarmData getItem(int position){
        return mData.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {
        public TextView mHour;
        public TextView mDay;
        public RMSwitch mSwitch;

        private SelectAlarmListener mSelectAlarmListener;

        public ViewHolder(@NonNull View itemView,SelectAlarmListener listener) {
            super(itemView);
            mSelectAlarmListener = listener;
            mHour = itemView.findViewById(R.id.item_time);
            mDay = itemView.findViewById(R.id.item_period);
            mSwitch = itemView.findViewById(R.id.item_activity);
            //mSwitch.setOnCheckedChangeListener(this);
            mSwitch.addSwitchObserver(mRMSwitchObserver);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mSelectAlarmListener != null) {
                mSelectAlarmListener.selectItem(getAdapterPosition());
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (mSelectAlarmListener != null) {
                mSelectAlarmListener.onChangeAction(getAdapterPosition(),b);
            }
        }

        RMSwitch.RMSwitchObserver mRMSwitchObserver = new RMSwitch.RMSwitchObserver() {
            @Override
            public void onCheckStateChange(RMSwitch switchView, boolean isChecked) {
                if (mSelectAlarmListener != null) {
                    mSelectAlarmListener.onChangeAction(getAdapterPosition(),isChecked);
                }
            }
        };
    }
}
