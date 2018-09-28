package com.wu.first;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/9/25.
 */
public class DButil {
    private DBHelper helper=null;
    public DButil(Context context){
        helper=new DBHelper(context);
    }

    public DButil(Context context,int version){
        helper=new DBHelper(context,version);
    }

    public void info_insert(User user){                       //用户注册添加数据
        String sql="insert into user_info(name,password,phone)values(?,?,?)";
        SQLiteDatabase db=helper.getWritableDatabase();
        db.execSQL(sql,new Object[]{user.getName(),user.getPassword(),user.getUser_phone()});
    }
    //根据姓名查询id
    public int query(String name){
        int flag=-1;
        String sql="select * from user_info where name=?";
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cr=db.rawQuery(sql,new String[]{name});
        if(cr.moveToNext()){
            flag=cr.getInt(0);
            return flag;
        }else {
            return flag;
        }
    }
    public List<User> info_query(User user,String sql){                     //查找数据库中是否有某个用户
        List<User> userList=new ArrayList<>();
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cr=db.rawQuery(sql,new String[]{user.getName().toString(),user.getPassword().toString()});
        if(cr.moveToNext()){
            userList.add(user);
        }
        return userList;
    }
    public void delete(String name){
        String sql="delete from user_info where name=?";
        SQLiteDatabase db=helper.getWritableDatabase();
        db.execSQL(sql,new String[]{name});
    }
    public List<User> list_query(String sql){                                //查找数据库中的所有用户
        List<User> userList=new ArrayList<>();
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cr=db.rawQuery(sql,null);
        while(cr.moveToNext()){
            User user=new User(cr.getString(1).toString(),cr.getString(2).toString(),cr.getString(3).toString());
            userList.add(user);
        }
        return userList;
    }
    public void update(String name,String pass,int id){                        //修改用户信息
        SQLiteDatabase db=helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("name",name);
        values.put("password",pass);
        db.update("user_info",values,"id=?",new String[]{id+""});
    }
   public String phone_query(String name){
       String flag="";
       String sql="select * from user_info where name=?";
       SQLiteDatabase db=helper.getWritableDatabase();
       Cursor cr=db.rawQuery(sql,new String[]{name});
       if(cr.moveToNext()){
           flag=cr.getString(3);
           return flag;
       }else {
           return flag;
       }
   }

}
