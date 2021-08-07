package tk.cavink.shandamorning.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by cav on 05.08.21.
 */

public class DBConnect {
    private SQLiteDatabase database;
    private DBHelper mDBHelper;
    private Context mContext;


    public DBConnect(Context context){
        mContext = context;
        mDBHelper = new DBHelper(context,DBHelper.DBNAME,null,DBHelper.DATABASE_VERSION);
    }

    public void open(){
        database = mDBHelper.getWritableDatabase();
    }
    public  void close(){
        if (database!=null) {
            database.close();
        }
    }

    // удаляем базу данных
    public void dropDB(){
        mContext.deleteDatabase(DBHelper.DBNAME);
    }

}
