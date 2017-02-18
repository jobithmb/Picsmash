package com.three38inc.app.picsmash;

/**
 * Created by Jobith on 8/10/2015.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by Windows on 25-02-2015.
 */
public class PixieDB {

    private PixieHelper mHelper;
    private SQLiteDatabase mDatabase;

    public PixieDB(Context context) {
        mHelper = new PixieHelper(context);
        mDatabase = mHelper.getWritableDatabase();
        Log.d("ABC", "getWritable Called!");
    }

    public void add(String name, String category, String url, String courtesy) {
        //create a sql prepared statement
        String sql = "INSERT INTO PIXIE VALUES (?,?,?,?,?);";
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        statement.clearBindings();
        //for a given column index, simply bind the data to be put inside that index
        statement.bindString(2, name);
        statement.bindString(3, category);
        statement.bindString(4, url);
        statement.bindString(5, courtesy);
        statement.execute();


        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public boolean match(String name, String category, String url, String courtesy) {
        //create a sql prepared statement
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + PixieHelper.TABLE_PIXIE + " ORDER BY SLNO DESC",null);
        if (cursor != null && cursor.moveToFirst()) {
//            Toast.makeText(MyApplication.getAppContext(),"not null",Toast.LENGTH_SHORT).show();
            do{
            if(
            cursor.getString(cursor.getColumnIndex(PixieHelper.COLUMN_NAME)).equals(name) &&
            cursor.getString(cursor.getColumnIndex(PixieHelper.COLUMN_CATEGORY)).equals(category) &&
            cursor.getString(cursor.getColumnIndex(PixieHelper.COLUMN_URL)).equals(url) &&
            cursor.getString(cursor.getColumnIndex(PixieHelper.COLUMN_COURTESY)).equals(courtesy))
                return true;

            }while(cursor.moveToNext());

            return false;
        }
        else
            return false;
    }

    public void del(String name, String category, String url, String courtesy) {
        //create a sql prepared statement
        String sql = "DELETE FROM PIXIE WHERE "+PixieHelper.COLUMN_NAME+" =? AND "+PixieHelper.COLUMN_CATEGORY+" =? AND "+PixieHelper.COLUMN_URL+" =? AND "+PixieHelper.COLUMN_COURTESY+" =?";
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        statement.clearBindings();
        //for a given column index, simply bind the data to be put inside that index
        statement.bindString(1, name);
        statement.bindString(2, category);
        statement.bindString(3, url);
        statement.bindString(4, courtesy);
        statement.execute();


        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public void insertPixie(ArrayList<Pixie> listPixies, boolean clearPrevious) {
        if (clearPrevious) {
            deletePixie();
        }


        //create a sql prepared statement
        String sql = "INSERT INTO PIXIE VALUES (?,?,?,?,?);";
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        int i;
        for (i = 0; i < listPixies.size(); i++) {
            Pixie currentPixie = listPixies.get(i);
            statement.clearBindings();
            //for a given column index, simply bind the data to be put inside that index
            statement.bindString(2, currentPixie.getImgName());
            statement.bindString(3, currentPixie.getImgCategory());
            statement.bindString(4, currentPixie.getImgUrl());
            statement.bindString(5, currentPixie.getImgCourtesy());


            statement.execute();
        }
        Log.d("ABC","Rows inserted = "+i);
        //set the transaction as successful and end the transaction
        //L.m("inserting entries " + listMovies.size() + new Date(System.currentTimeMillis()));
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public ArrayList<Pixie> readPixie() {
        ArrayList<Pixie> listPixie = new ArrayList<>();
        int num =0;
        //get a list of columns to be retrieved, we need all of them
//        String[] columns = {PixieHelper.COLUMN_UID,
//                PixieHelper.COLUMN_NAME,
//                PixieHelper.COLUMN_CATEGORY,
//                PixieHelper.COLUMN_URL
//        };
        //Cursor cursor = mDatabase.query(PixieHelper.TABLE_PIXIE, columns, null, null, null, null, null);
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + PixieHelper.TABLE_PIXIE + " ORDER BY SLNO DESC",null);
        if (cursor != null && cursor.moveToFirst()) {
            //L.m("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {

                //create a new movie object and retrieve the data from the cursor to be stored in this movie object

//                if(cat.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(PixieHelper.COLUMN_CATEGORY)))) {
                    Pixie pixie = new Pixie();
                    //each step is a 2 part process, find the index of the column first, find the data of that column using
                    //that index and finally set our blank movie object to contain our data
                    pixie.setImgName(cursor.getString(cursor.getColumnIndex(PixieHelper.COLUMN_NAME)));
                    pixie.setImgCategory(cursor.getString(cursor.getColumnIndex(PixieHelper.COLUMN_CATEGORY)));
                    pixie.setImgUrl(cursor.getString(cursor.getColumnIndex(PixieHelper.COLUMN_URL)));
                    pixie.setImgCourtesy(cursor.getString(cursor.getColumnIndex(PixieHelper.COLUMN_COURTESY)));
//                    Log.d("ABC","PC_read = "+cursor.getString(cursor.getColumnIndex(PixieHelper.COLUMN_COURTESY)));
                    //add the movie to the list of movie objects which we plan to return
                    listPixie.add(pixie);
                    num++;
//                }
            }
            while (cursor.moveToNext());
        }
        Log.d("ABC", "Rows Read = " + num);
        return listPixie;
    }

    public ArrayList<Pixie> searchPixie(String cat) {
        ArrayList<Pixie> listPixie = new ArrayList<>();
        int num =0;
        //get a list of columns to be retrieved, we need all of them
//        String[] columns = {PixieHelper.COLUMN_UID,
//                PixieHelper.COLUMN_NAME,
//                PixieHelper.COLUMN_CATEGORY,
//                PixieHelper.COLUMN_URL
//        };
        //Cursor cursor = mDatabase.query(PixieHelper.TABLE_PIXIE, columns, null, null, null, null, null);
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + PixieHelper.TABLE_PIXIE + " ORDER BY SLNO DESC",null);
        if (cursor != null && cursor.moveToFirst()) {
            //L.m("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {

                //create a new movie object and retrieve the data from the cursor to be stored in this movie object

                if(cursor.getString(cursor.getColumnIndex(PixieHelper.COLUMN_NAME)).contains(cat)) {
                    Pixie pixie = new Pixie();
                    //each step is a 2 part process, find the index of the column first, find the data of that column using
                    //that index and finally set our blank movie object to contain our data
                    pixie.setImgName(cursor.getString(cursor.getColumnIndex(PixieHelper.COLUMN_NAME)));
                    pixie.setImgCategory(cursor.getString(cursor.getColumnIndex(PixieHelper.COLUMN_CATEGORY)));
                    pixie.setImgUrl(cursor.getString(cursor.getColumnIndex(PixieHelper.COLUMN_URL)));
                    pixie.setImgCourtesy(cursor.getString(cursor.getColumnIndex(PixieHelper.COLUMN_COURTESY)));
//                    Log.d("ABC","PC_read = "+cursor.getString(cursor.getColumnIndex(PixieHelper.COLUMN_COURTESY)));
                    //add the movie to the list of movie objects which we plan to return
                    listPixie.add(pixie);
                    num++;
                }
            }
            while (cursor.moveToNext());
        }
        Log.d("ABC","Rows Read = "+num+" for category = "+cat);
        return listPixie;
    }

    public void deletePixie() {
        mDatabase.delete(PixieHelper.TABLE_PIXIE, null, null);
    }

    private static class PixieHelper extends SQLiteOpenHelper {
        public static final String TABLE_PIXIE = "PIXIE";
        public static final String COLUMN_UID = "SLNO";
        public static final String COLUMN_NAME = "IMG_NAME";
        public static final String COLUMN_CATEGORY = "IMG_CATEGORY";
        public static final String COLUMN_COURTESY = "IMG_COURTESY";
        public static final String COLUMN_URL = "IMG_URL";

        private static final String CREATE_TABLE_PIXIE = "CREATE TABLE " + TABLE_PIXIE + " (" +
                COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT," +
                COLUMN_CATEGORY + " TEXT," +
                COLUMN_URL + " TEXT,"+
                COLUMN_COURTESY + " TEXT"+
                ");";

        private static final String DB_NAME = "PIXIE_DB";
        private static final int DB_VERSION = 1;
        private Context mContext;

        public PixieHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("ABC","Try create Table !");
            try {
                db.execSQL(CREATE_TABLE_PIXIE);
                //Toast.makeText(MyApplication.getAppContext(),"table created!",Toast.LENGTH_SHORT).show();
                Log.d("ABC","Table Created");
                //L.m("create table box office executed");
            } catch (SQLiteException exception) {
                Log.d("ABC", "SQL = " + exception.getMessage());
                //Toast.makeText(MyApplication.getAppContext(), exception.getMessage(),Toast.LENGTH_SHORT).show();
                //L.t(mContext, exception + "");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                //L.m("upgrade table box office executed");
                db.execSQL(" DROP TABLE " + TABLE_PIXIE + " IF EXISTS;");
                onCreate(db);
            } catch (SQLiteException exception) {
                //L.t(mContext, exception + "");
            }
        }
    }
}
