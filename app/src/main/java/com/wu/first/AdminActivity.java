package com.wu.first;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private ListView info_list;
    List<User> userList;
    private DButil dbutil=new DButil(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        info_list= (ListView) findViewById(R.id.info_list);
        String sql="select * from user_info";
        userList=dbutil.list_query(sql);

        UserAdapter adapter=new UserAdapter(userList,this);
        info_list.setAdapter(adapter);
        info_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(AdminActivity.this,UserInfoActivity.class);
                intent.putExtra("trans_name",userList.get(position).getName().toString());
                intent.putExtra("trans_pass",userList.get(position).getPassword().toString());
                intent.putExtra("trans_phone",userList.get(position).getUser_phone().toString());
                startActivity(intent);
            }
        });
    }
}
