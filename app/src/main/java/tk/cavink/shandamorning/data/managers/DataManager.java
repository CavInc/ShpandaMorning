package tk.cavink.shandamorning.data.managers;

import android.content.Context;

import tk.cavink.shandamorning.App;
import tk.cavink.shandamorning.data.database.DBConnect;

/**
 * Created by cav on 06.08.21.
 */

public class DataManager {
    private static DataManager INSTANCE = null;

    private Context mContext;
    private PrefManager mPrefManager;
    private DBConnect mDBConnect;


    public static DataManager getInstance() {
        if (INSTANCE==null){
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public DataManager() {
        mContext = App.getContext();
        mPrefManager = new PrefManager();
        mDBConnect = new DBConnect(mContext);
    }

    public Context getContext() {
        return mContext;
    }

    public PrefManager getPrefManager() {
        return mPrefManager;
    }

    public DBConnect getDBConnect() {
        return mDBConnect;
    }
}
