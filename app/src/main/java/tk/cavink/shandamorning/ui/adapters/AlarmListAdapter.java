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
        viewHolder.mHour.setText(String.valueOf(rec.getH())+":"+String.valueOf(rec.getM()));
        viewHolder.mSwitch.setChecked(rec.isActive());
        viewHolder.mDay.setText("Каждый день");

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
        public SwitchCompat mSwitch;

        private SelectAlarmListener mSelectAlarmListener;

        public ViewHolder(@NonNull View itemView,SelectAlarmListener listener) {
            super(itemView);
            mSelectAlarmListener = listener;
            mHour = itemView.findViewById(R.id.item_time);
            mDay = itemView.findViewById(R.id.item_period);
            mSwitch = itemView.findViewById(R.id.item_activity);
            mSwitch.setOnCheckedChangeListener(this);
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
    }
}
