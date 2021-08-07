package tk.cavink.shandamorning;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by cav on 04.08.21.
 */

public class App extends Application {
    private static Context sContext;
    public static SharedPreferences sSharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this.getBaseContext();
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(sContext);
    }

    public static Context getContext() {
        return sContext;
    }

    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }
}
