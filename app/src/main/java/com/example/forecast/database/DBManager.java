package com.example.forecast.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    public static SQLiteDatabase database;

    //初始化数据库信息
    public static void initDB(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    //查找数据库当中城市列表
    public static List<String> queryCity() {
        Cursor cursor = database.query("info", null, null, null, null, null, null);
        List<String> cityList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String city = cursor.getString(cursor.getColumnIndex("city"));
            cityList.add(city);
        }
        return cityList;
    }

    //根据城市名称替换信息内容
    public static int updateData(String city, String content) {
        ContentValues values = new ContentValues();
        values.put("content", content);
        return database.update("info", values, "city=?", new String[]{city});
    }

    //新增一条城市记录
    public static long addData(String city, String content) {
        ContentValues values = new ContentValues();
        values.put("content", content);
        values.put("city", city);
        return database.insert("info", null, values);
    }

    //根据城市名查询数据库中的内容
    public static String queryData(String city) {
        Cursor cursor = database.query("info", null, "city=?", new String[]{city}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String content = cursor.getString(cursor.getColumnIndex("content"));
            return content;
        }
        return null;

    }

    //存储城市天气，最多存储5个城市，一旦超过5个就不能存储了
    public static int getCityCount() {
        Cursor cursor = database.query("info", null, null, null, null, null, null);
        return cursor.getCount();
    }

    //查询数据库当中全部信息
    public static List<DatabaseBean> getAllInfo() {
        Cursor cursor = database.query("info", null, null, null, null, null, null, null);
        List<DatabaseBean> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String city = cursor.getString(cursor.getColumnIndex("city"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            DatabaseBean databaseBean = new DatabaseBean(id, city, content);
            list.add(databaseBean);
        }
        return list;
    }

    //根据城市名删除对应的数据
    public static int deleteData(String city) {
        int count = database.delete("info", "city=?", new String[]{city});
        return count;
    }
    //删除表中所有数据,清楚缓存
    public static void deleteAllData(){
        String sql="delete from info";
        database.execSQL(sql);
    }
}
