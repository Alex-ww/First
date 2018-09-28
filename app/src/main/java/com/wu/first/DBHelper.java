package com.wu.first;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Administrator on 2018/9/25.
 */
public class DBHelper extends SQLiteOpenHelper {
    private final static String DB_NAME ="user.db";//数据库名
    private final static int VERSION = 1;//版本号
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public DBHelper(Context cnx){
        this(cnx,DB_NAME,null,VERSION);
    }
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }
    public DBHelper(Context cxt,int version) {
        this(cxt,DB_NAME,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table user_info(" +
                "id integer primary key autoincrement," +
                "name varchar(20)," +
                "password varchar(20),"+
                "phone varchar(20))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql  = "update student ....";//自己的Update操作
        db.execSQL(sql);
    }
}
