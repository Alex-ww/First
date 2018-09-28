package com.wu.first;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText new_name;
    private EditText new_pass1;
    private EditText new_pass2;
    private EditText register_phone;
    private EditText register_code;

    private Button btn_register1;
    private Button read_code;
    private DButil dbutil=new DButil(this);

    EventHandler eventHandler;

    private String phoneNumber;     // 电话号码
    private String verificationCode;  // 验证码

    private boolean flag;  // 操作是否成功
    private int Time=60000;
    private CountDownTimer countDownTimer=new CountDownTimer(Time,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            read_code.setText("等待"+millisUntilFinished/1000+"s");
        }

        @Override
        public void onFinish() {
            read_code.setText("获取验证码");
            read_code.setEnabled(true);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Toolbar mToolbarTb = (Toolbar) findViewById(R.id.register_toolBar);
        setSupportActionBar(mToolbarTb);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        SMSSDK.setAskPermisionOnReadContact(true);

        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // afterEvent会在子线程被调用，因此如果后续有UI相关操作，需要将数据发送到UI线程
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
        btn_register1.setOnClickListener(this);
        read_code.setOnClickListener(this);
        mToolbarTb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

  /*      btn_register1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=new_name.getText().toString();
                String pass1=new_pass1.getText().toString();
                String pass2=new_pass2.getText().toString();
                if(name.equals("")||pass1.equals("")||pass2.equals("")){
                    Toast.makeText(RegisterActivity.this,"账号或密码不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    if(dbutil.query(name)!=-1){
                        Toast.makeText(RegisterActivity.this,"账号已经存在",Toast.LENGTH_SHORT).show();
                    }else{
                        if(pass1.equals(pass2)){
                            User user=new User(name,pass1);
                            dbutil.info_insert(user);
                            new AlertDialog.Builder(RegisterActivity.this).setTitle("注册信息").setMessage("注册成功").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                }
                            }).show();
                        }else{
                            Toast.makeText(RegisterActivity.this,"两次密码不一样",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });*/
    }

    private void init() {
        new_name= (EditText) findViewById(R.id.new_name);
        new_pass1= (EditText) findViewById(R.id.new_pass1);
        new_pass2= (EditText) findViewById(R.id.new_pass2);
        btn_register1= (Button) findViewById(R.id.btn_register1);

        register_phone= (EditText) findViewById(R.id.register_phone);
        register_code= (EditText) findViewById(R.id.re_code);
        read_code= (Button) findViewById(R.id.read_code);
    }
    // 使用完EventHandler需注销，否则可能出现内存泄漏
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event=msg.arg1;
            int result=msg.arg2;
            Object data=msg.obj;
            if (result == SMSSDK.RESULT_COMPLETE) {
                // 如果操作成功
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    // 校验验证码，返回校验的手机和国家代码
                    Toast.makeText(RegisterActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                    String name=new_name.getText().toString();
                    String pass1=new_pass1.getText().toString();
                    String pass2=new_pass2.getText().toString();

                    if(name.equals("")||pass1.equals("")||pass2.equals("")){
                        Toast.makeText(RegisterActivity.this,"账号或密码不能为空",Toast.LENGTH_SHORT).show();
                    }else{
                        if(dbutil.query(name)!=-1){
                            Toast.makeText(RegisterActivity.this,"账号已经存在",Toast.LENGTH_SHORT).show();
                        }else{
                            if(pass1.equals(pass2)){
                                User user=new User(name,pass1,phoneNumber);
                                dbutil.info_insert(user);
                                new AlertDialog.Builder(RegisterActivity.this).setTitle("注册信息").setMessage("注册成功").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                    }
                                }).show();
                            }else{
                                Toast.makeText(RegisterActivity.this,"两次密码不一样",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    // 获取验证码成功，true为智能验证，false为普通下发短信
                    Toast.makeText(RegisterActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    countDownTimer.start();
                    read_code.setEnabled(false);
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    // 返回支持发送验证码的国家列表
                }
            } else {
                // 如果操作失败
                if (flag) {
                    Toast.makeText(RegisterActivity.this, "验证码获取失败，请重新获取", Toast.LENGTH_SHORT).show();
                    register_phone.requestFocus();
                } else {
                    ((Throwable) data).printStackTrace();
                    Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.read_code:
                if (!TextUtils.isEmpty(register_phone.getText())) {
                    phoneNumber = register_phone.getText().toString();
                    String num="[1][358]\\d{9}";
                    if (register_phone.getText().length() == 11||phoneNumber.matches(num)) {
                        SMSSDK.getVerificationCode("86", phoneNumber); // 发送验证码给号码的 phoneNumber 的手机
                        register_code.requestFocus();
                    }
                    else {
                        Toast.makeText(this, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
                        register_phone.requestFocus();
                    }
                } else {
                    Toast.makeText(this, "请输入电话号码", Toast.LENGTH_SHORT).show();
                    register_phone.requestFocus();
                }
                break;

            case R.id.btn_register1:
                if (!TextUtils.isEmpty(register_code.getText())) {
                    if (register_code.getText().length() == 4) {
                        verificationCode = register_code.getText().toString();
                        SMSSDK.submitVerificationCode("86", phoneNumber, verificationCode);
                        flag = false;
                    } else {
                        Toast.makeText(this, "请输入完整的验证码", Toast.LENGTH_SHORT).show();
                        register_code.requestFocus();
                    }
                } else {
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    register_code.requestFocus();
                }
                break;

            default:
                break;
        }
    }
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }


    /*public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.home){
            finish();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }*/

}
