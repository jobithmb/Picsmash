package com.three38inc.app.picsmash;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Jobith on 8/5/2015.
 */
public class MyApplication extends Application {

    private static MyApplication sInstance;
    private static PixieDB mDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mDatabase = new PixieDB(this);
    }

    public static MyApplication getInstance(){
        return sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }

    public synchronized static PixieDB getWritableDatabase(){
        if(mDatabase == null){
            mDatabase = new PixieDB(getAppContext());
        }
        return mDatabase;
    }
}
