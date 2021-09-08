package tk.cavink.shandamorning.data.managers;

import android.content.SharedPreferences;

import tk.cavink.shandamorning.App;
import tk.cavink.shandamorning.R;

/**
 * Created by cav on 05.08.21.
 */

public class PrefManager {
    private static final String THEME_ID = "THEME_ID";
    private static final String FIRST_START = "FIRST_START";
    private SharedPreferences mSharedPreferences;

    public PrefManager() {
        mSharedPreferences = App.getSharedPreferences();
    }

    public int getThemeId(){
        int themeId = mSharedPreferences.getInt(THEME_ID, R.style.GreenTheme);
        if (themeId <= 100 ){
            themeId = R.style.GreenTheme;
        }
        return themeId;
    }

    public void setThemeId(int themeId){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(THEME_ID,themeId);
        editor.apply();
    }

    public boolean isFirstStart(){
        return mSharedPreferences.getBoolean(FIRST_START,true);
    }

    public void setFirstStart(boolean flg){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(FIRST_START,flg);
        editor.apply();
    }
}
