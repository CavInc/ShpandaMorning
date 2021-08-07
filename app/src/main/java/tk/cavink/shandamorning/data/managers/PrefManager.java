package tk.cavink.shandamorning.data.managers;

import android.content.SharedPreferences;

import tk.cavink.shandamorning.App;

/**
 * Created by cav on 05.08.21.
 */

public class PrefManager {
    private SharedPreferences mSharedPreferences;

    public PrefManager() {
        mSharedPreferences = App.getSharedPreferences();
    }
}
