package tk.cavink.shandamorning.data.models;

/**
 * Created by cav on 09.08.21.
 */

public class AlarmDaySetData {
    private int mDayId;
    private String mName;
    private boolean mAction;

    public AlarmDaySetData(int dayId, boolean action) {
        mDayId = dayId;
        mAction = action;
    }

    public AlarmDaySetData(int dayId, String name, boolean action) {
        mDayId = dayId;
        mName = name;
        mAction = action;
    }

    public int getDayId() {
        return mDayId;
    }

    public String getName() {
        return mName;
    }

    public boolean isAction() {
        return mAction;
    }

    public void setAction(boolean action) {
        mAction = action;
    }
}
