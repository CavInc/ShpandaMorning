package tk.cavink.shandamorning.data.models;

import java.util.ArrayList;

/**
 * Created by cav on 05.08.21.
 */

public class AlarmData {
    private int mId; // id будильника
    private int mH; // часы
    private int mM; // минуты
    private int mVolume; // громкость
    private boolean mVibro; // использовать вибро сигнал
    private boolean mActive; // активынй будильник
    private String mRingtone; // рингтон
    private ArrayList<Boolean> mDays; // список дней
    private String mLang; // язык

    public AlarmData(int id, int h, int m, int volume, boolean vibro, boolean active,
                     ArrayList<Boolean> days, String lang,String ringtone) {
        mId = id;
        mH = h;
        mM = m;
        mVolume = volume;
        mVibro = vibro;
        mActive = active;
        mDays = days;
        mLang = lang;
        mRingtone = ringtone;
    }

    public AlarmData(int h, int m, int volume, boolean vibro, boolean active,
                     ArrayList<Boolean> days, String lang,String ringtone) {
        mH = h;
        mM = m;
        mVolume = volume;
        mVibro = vibro;
        mActive = active;
        mDays = days;
        mLang = lang;
        mRingtone = ringtone;
    }

    public int getId() {
        return mId;
    }

    public int getH() {
        return mH;
    }

    public int getM() {
        return mM;
    }

    public void setH(int h) {
        mH = h;
    }

    public void setM(int m) {
        mM = m;
    }

    public int getVolume() {
        return mVolume;
    }

    public boolean isVibro() {
        return mVibro;
    }

    public boolean isActive() {
        return mActive;
    }

    public String getLang() {
        return mLang;
    }

    public ArrayList<Boolean> getDays() {
        return mDays;
    }

    public String getRingtone() {
        return mRingtone;
    }
}
