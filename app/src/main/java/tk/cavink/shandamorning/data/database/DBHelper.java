package tk.cavink.shandamorning.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cav on 06.08.21.
 */

public class DBHelper extends SQLiteOpenHelper{
    public static final String DBNAME = "alarm.db3";
    public static final String ALARM = "alarm";
    public static final int DATABASE_VERSION = 1;


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateDatabase(db,0,DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateDatabase(db,oldVersion,newVersion);
    }

    private void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("create table "+ALARM+"(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "hour integer ," + // часы
                "minute integer," + // минуты
                "volume integer default 100," + // громкость
                "vibro_flg integer default 0, " + // 0 - нет сигнала
                "action_flg integer default 1, " + // активный будильник
                "lang text default 'ru'," + // язык
                "days text," + // список дней в которые работает будильник
                "url_ringtone text" + // рингтон
                ")");
    }
}
