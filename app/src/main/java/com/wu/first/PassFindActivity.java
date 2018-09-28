package com.wu.first;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class PassFindActivity extends AppCompatActivity {
    private EditText pass_find;
    private Button btn_reset;

    private EditText phone;
    private EditText auto_code;
    private  Button get_code;
    EventHandler eHandler;
    private String phone_number;
    private String cord_number;
    private int Time=60000;
    private boolean flag=true;
    private CountDownTimer countDownTimer=new CountDownTimer(Time,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            get_code.setText("等待"+millisUntilFinished/1000+"s");
        }

        @Override
        public void onFinish() {
        get_code.setText("获取验证码");
        get_code.setEnabled(true);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_find);
        MobSDK.init(this);

        init();                     //初始化控件

        eHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eHandler);

       btnListen();

       /* btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DButil dButil = new DButil(PassFindActivity.this);
                String name = pass_find.getText().toString();
                String pass = "123456";
                int id = dButil.query(name);
                if (id == -1) {
                    Toast.makeText(PassFindActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
                } else {
                    dButil.update(name, pass, id);
                    Toast.makeText(PassFindActivity.this, "密码重置成功，为123456", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }

    private void btnListen() {
        get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(judPhone())//去掉左右空格获取字符串
                {
                    SMSSDK.getVerificationCode("86",phone_number);
                    auto_code.requestFocus();
                }
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(judCord())
                    SMSSDK.submitVerificationCode("86",phone_number,cord_number);
                flag=false;
            }
        });
    }

    /**
         * 使用Handler来分发Message对象到主线程中，处理事件
         */
        Handler handler=new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int event=msg.arg1;
                int result=msg.arg2;
                Object data=msg.obj;
                /*if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    if(result == SMSSDK.RESULT_COMPLETE) {
                        boolean smart = (Boolean)data;
                        if(smart) {
                            Toast.makeText(getApplicationContext(),"该手机号已经注册过，请重新输入",
                                    Toast.LENGTH_LONG).show();
                            phone.requestFocus();
                            return;
                        }
                    }
                }
                if(result==SMSSDK.RESULT_COMPLETE)
                {

                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "验证码输入正确",
                                Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    if(flag)
                    {
                        get_code.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(),"验证码获取失败请重新获取", Toast.LENGTH_LONG).show();
                        phone.requestFocus();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"验证码输入错误", Toast.LENGTH_LONG).show();
                    }
                }

            }*/
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 如果操作成功
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        // 校验验证码，返回校验的手机和国家代码
                        Toast.makeText(PassFindActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                        DButil dButil = new DButil(PassFindActivity.this);
                        String name = pass_find.getText().toString();
                        String pass = "123456";
                        int id = dButil.query(name);
                        if (id == -1) {
                            Toast.makeText(PassFindActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
                        } else {
                            dButil.update(name, pass, id);
                            Toast.makeText(PassFindActivity.this, "密码重置成功，为123456", Toast.LENGTH_SHORT).show();
                        }
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        // 获取验证码成功，true为智能验证，false为普通下发短信
                        Toast.makeText(PassFindActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                        countDownTimer.start();
                        get_code.setEnabled(false);
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        // 返回支持发送验证码的国家列表
                    }
                } else {
                    // 如果操作失败
                    if (flag) {
                        Toast.makeText(PassFindActivity.this, "验证码获取失败，请重新获取", Toast.LENGTH_SHORT).show();
                        phone.requestFocus();
                    } else {
                        ((Throwable) data).printStackTrace();
                        Toast.makeText(PassFindActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        };



    private void init() {
        pass_find= (EditText) findViewById(R.id.pass_find);
        btn_reset= (Button) findViewById(R.id.btn_reset);
        phone= (EditText) findViewById(R.id.phone);
        auto_code= (EditText) findViewById(R.id.auto_code);
        get_code= (Button) findViewById(R.id.get_code);
    }
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eHandler);
    }

    private boolean judPhone()
    {
        if(TextUtils.isEmpty(phone.getText().toString().trim()))
        {
            Toast.makeText(PassFindActivity.this,"请输入您的电话号码",Toast.LENGTH_LONG).show();
            phone.requestFocus();
            return false;
        }
        else if(phone.getText().toString().trim().length()!=11)
        {
            Toast.makeText(PassFindActivity.this,"您的电话号码位数不正确",Toast.LENGTH_LONG).show();
            phone.requestFocus();
            return false;
        }
        else
        {
            phone_number=phone.getText().toString().trim();
            String num="[1][358]\\d{9}";
            String name = pass_find.getText().toString();
            DButil dbutil=new DButil(PassFindActivity.this);
            String jug_phone=dbutil.phone_query(name);
            if(phone_number.matches(num)||jug_phone.equals(""))
                return true;
            else
            {
                Toast.makeText(PassFindActivity.this,"请输入注册时的手机号码",Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }

    private boolean judCord()
    {
        judPhone();
        if(TextUtils.isEmpty(auto_code.getText().toString().trim()))
        {
            Toast.makeText(PassFindActivity.this,"请输入您的验证码",Toast.LENGTH_LONG).show();
            auto_code.requestFocus();
            return false;
        }
        else if(auto_code.getText().toString().trim().length()!=4)
        {
            Toast.makeText(PassFindActivity.this,"您的验证码位数不正确",Toast.LENGTH_LONG).show();
            auto_code.requestFocus();

            return false;
        }
        else
        {
            cord_number=auto_code.getText().toString().trim();
            return true;
        }

    }
}
