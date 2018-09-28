package com.wu.first;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserInfoActivity extends AppCompatActivity {
    private EditText userInfo_name;
    private EditText userInfo_pass;
    private EditText userInfo_phone;
    private Button modification;
    private Button delete;

    private Button btn_ok;
    private Button cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        init();
        btnListen();
    }

    private void btnListen() {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(UserInfoActivity.this);
                builder.setTitle("删除页面");
                builder.setMessage("是否删除！");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DButil dbutil=new DButil(UserInfoActivity.this);
                        dbutil.delete(userInfo_name.getText().toString());
                        Intent intent =new Intent(UserInfoActivity.this,AdminActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });

        modification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfo_pass.setEnabled(true);

                modification.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);

                btn_ok.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DButil dbutil=new DButil(UserInfoActivity.this);
                String f_name=userInfo_name.getText().toString();
                String f_pass=userInfo_pass.getText().toString();
                int id=dbutil.query(f_name);
                dbutil.update(f_name,f_pass,id);
                userInfo_pass.setEnabled(false);

                modification.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);

                btn_ok.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfo_pass.setText(getIntent().getStringExtra("trans_pass"));
                userInfo_pass.setEnabled(false);

                modification.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);

                btn_ok.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
            }
        });
    }

    private void init() {
        userInfo_name= (EditText) findViewById(R.id.userInfo_name);
        userInfo_pass= (EditText) findViewById(R.id.userInfo_pass);
        userInfo_phone= (EditText) findViewById(R.id.userInfo_phone);
        modification= (Button) findViewById(R.id.modification);
        delete= (Button) findViewById(R.id.delete);
        btn_ok= (Button) findViewById(R.id.btn_ok);
        cancel= (Button) findViewById(R.id.cancel);

        userInfo_name.setText(getIntent().getStringExtra("trans_name"));
        userInfo_pass.setText(getIntent().getStringExtra("trans_pass"));
        userInfo_phone.setText(getIntent().getStringExtra("trans_phone"));
    }
}
