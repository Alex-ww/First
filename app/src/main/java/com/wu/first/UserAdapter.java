package com.wu.first;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2018/9/25.
 */
public class UserAdapter extends BaseAdapter {
    private List<User> userList;
    private LayoutInflater inflater;

    private ImageView item_icon;
    private TextView item_name;
    private TextView item_pass;

    public UserAdapter(List<User> userList, Context context){
        this.userList=userList;
        this.inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public User getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.list_item,null);
        User user=getItem(position);
        item_icon= (ImageView) view.findViewById(R.id.item_icon);
        item_name= (TextView) view.findViewById(R.id.item_name);
        item_pass= (TextView) view.findViewById(R.id.item_pass);
        item_icon.setImageResource(R.drawable.user_info);

        item_name.setText("账号："+user.getName());
        item_pass.setText("密码："+user.getPassword());
        return view;
    }
}
