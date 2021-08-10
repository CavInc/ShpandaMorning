package tk.cavink.shandamorning.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import tk.cavink.shandamorning.data.models.AlarmData;

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

    private String arrayDayToStr(ArrayList<Boolean> day) {
        StringBuilder rec = new StringBuilder();
        for (Boolean l:day){
            if (l){
                rec.append("T");
            } else {
                rec.append("F");
            }
            rec.append(",");
        }
        rec.delete(rec.length()-1,rec.length());
        return rec.toString();
    }

    // добавим будильник
    public long addAlarm(AlarmData alarmData){
        open();
        ContentValues values = new ContentValues();
        values.put("hour",alarmData.getH());
        values.put("minute",alarmData.getM());
        values.put("volume",alarmData.getVolume());
        values.put("lang",alarmData.getLang());
        values.put("url_ringtone",alarmData.getRingtone());
        values.put("vibro_flg",alarmData.isVibro() ? 1 : 0);
        values.put("days",arrayDayToStr(alarmData.getDays()));
        long recid = database.insert(DBHelper.ALARM,null,values);

        close();
        return recid;
    }

    // Редактируем будильник
    public void editAlarm(AlarmData alarmData){
        open();
        ContentValues values = new ContentValues();
        values.put("hour",alarmData.getH());
        values.put("minute",alarmData.getM());
        values.put("volume",alarmData.getVolume());
        values.put("lang",alarmData.getLang());
        values.put("url_ringtone",alarmData.getRingtone());
        values.put("vibro_flg",alarmData.isVibro() ? 1 : 0);
        values.put("days",arrayDayToStr(alarmData.getDays()));

        database.update(DBHelper.ALARM,values,"id=?",new String[]{String.valueOf(alarmData.getId())});

        close();
    }

    public void setActiveAlarm(int id,boolean active){
        open();
        ContentValues values = new ContentValues();
        values.put("action_flg",active ? 1 : 0);
        database.update(DBHelper.ALARM,values,"id=?",new String[]{String.valueOf(id)});
        close();
    }

    public void deleteAlarm(int id){
        open();
        database.delete(DBHelper.ALARM,"id=?",new String[]{String.valueOf(id)});
        close();
    }

    private ArrayList<Boolean> strToList(String days){
        ArrayList<Boolean> rec = new ArrayList<>();
        String[] lx = days.split(",");
        for (String l:lx){
            if (l.equals("T")) {
                rec.add(true);
            } else {
                rec.add(false);
            }
        }
        return rec;
    }

    public ArrayList<AlarmData> getAlarm(boolean active) {
        ArrayList<AlarmData> rec = new ArrayList<>();
        open();
        Cursor cursor = database.query(DBHelper.ALARM,
                new String[]{"id","hour","minute","volume","lang","vibro_flg","action_flg","url_ringtone","days"},
                null,null,null,null,null);
        while (cursor.moveToNext()) {
            ArrayList<Boolean> days = strToList(cursor.getString(cursor.getColumnIndex("days")));
            rec.add(new AlarmData(cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getInt(cursor.getColumnIndex("hour")),
                    cursor.getInt(cursor.getColumnIndex("minute")),
                    cursor.getInt(cursor.getColumnIndex("volume")),
                    (cursor.getInt(cursor.getColumnIndex("vibro_flg")) == 1 ? true : false),
                    (cursor.getInt(cursor.getColumnIndex("action_flg")) == 1 ? true : false),
                    days,
                    cursor.getString(cursor.getColumnIndex("lang")),
                    cursor.getString(cursor.getColumnIndex("url_ringtone"))
                    ));
        }
        close();
        return rec;
    }

    public AlarmData getAlarmOne(int id){
        AlarmData rec = null;
        open();
        Cursor cursor = database.query(DBHelper.ALARM,
                new String[]{"id","hour","minute","volume","lang","vibro_flg","action_flg","url_ringtone"},
                null,null,null,null,null);
        while (cursor.moveToNext()) {
            ArrayList<Boolean> days = strToList(cursor.getString(cursor.getColumnIndex("days")));
            rec = new AlarmData(cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getInt(cursor.getColumnIndex("hour")),
                    cursor.getInt(cursor.getColumnIndex("minute")),
                    cursor.getInt(cursor.getColumnIndex("volume")),
                    (cursor.getInt(cursor.getColumnIndex("vibro_flg")) == 1 ? true : false),
                    (cursor.getInt(cursor.getColumnIndex("action_flg")) == 1 ? true : false),
                    days,
                    cursor.getString(cursor.getColumnIndex("lang")),
                    cursor.getString(cursor.getColumnIndex("url_ringtone")));
        }
        close();
        return rec;
    }
}
