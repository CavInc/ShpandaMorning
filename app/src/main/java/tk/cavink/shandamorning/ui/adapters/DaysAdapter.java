package tk.cavink.shandamorning.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tk.cavink.shandamorning.R;
import tk.cavink.shandamorning.data.models.AlarmDaySetData;
import tk.cavink.shandamorning.ui.helpers.SelectDayListener;

/**
 * Created by cav on 10.08.21.
 */

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.ViewHolder>{
    private Context mContext;
    private List<AlarmDaySetData> mData;

    private SelectDayListener mSelectDayListener;

    public DaysAdapter(Context context, List<AlarmDaySetData> data,SelectDayListener listener) {
        mContext = context;
        mData = data;
        mSelectDayListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.button_day_item,viewGroup,false);
        return new ViewHolder(v,mSelectDayListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        AlarmDaySetData rec = mData.get(position);
        viewHolder.mDayItem.setText(rec.getName());
        if (rec.isAction()) {
            viewHolder.mView.setBackground(mContext.getResources().getDrawable(R.drawable.day_bt_green,null));
            viewHolder.mDayItem.setTextColor(mContext.getResources().getColor(android.R.color.white));
        } else {
            viewHolder.mView.setBackground(mContext.getResources().getDrawable(R.drawable.day_bt,null));
            viewHolder.mDayItem.setTextColor(mContext.getResources().getColor(android.R.color.black));
        }
    }

    public AlarmDaySetData getItem(int position){
        return mData.get(position);
    }

    public List<AlarmDaySetData> getItems() {
        return mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mDayItem;
        private View mView;
        private SelectDayListener mSelectDayListener;

        public ViewHolder(@NonNull View itemView,SelectDayListener listener) {
            super(itemView);
            mDayItem = itemView.findViewById(R.id.day_name);
            mView = itemView.findViewById(R.id.day_item);
            mSelectDayListener = listener;
            mView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mSelectDayListener != null) {
                mSelectDayListener.onChange(getAdapterPosition());
            }
        }
    }
}
