package com.example.forecast.base;

import android.app.Application;

import com.example.forecast.database.DBHelper;
import com.example.forecast.database.DBManager;

import org.xutils.x;

public class UniteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        //数据库跟随项目一并初始化
        DBManager.initDB(this);
    }
}
