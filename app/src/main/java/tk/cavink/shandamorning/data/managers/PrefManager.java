package tk.cavink.shandamorning.data.managers;

import android.content.SharedPreferences;

import tk.cavink.shandamorning.App;
import tk.cavink.shandamorning.R;

/**
 * Created by cav on 05.08.21.
 */

public class PrefManager {
    private static final String THEME_ID = "THEME_ID";
    private SharedPreferences mSharedPreferences;

    public PrefManager() {
        mSharedPreferences = App.getSharedPreferences();
    }

    public int getThemeId(){
        return mSharedPreferences.getInt(THEME_ID, R.style.GreenTheme);
    }

    public void setThemeId(int themeId){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(THEME_ID,themeId);
        editor.apply();
    }
}
