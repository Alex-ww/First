package com.wu.first;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText user_name;
    private EditText user_password;

    private Button btn_login;
    private Button btn_regiter;

    private RadioButton redio_user,redio_admin;
    private TextView forget_pass;

    String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION};
    List<String> mPermissions=new ArrayList<>();    //储存未通过的权限
    private final int mRequestCode = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Toolbar mToolbarTb = (Toolbar) findViewById(R.id.login_toolBar);
        setSupportActionBar(mToolbarTb);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //initPermission();
        init();
        btn_Listener();
    }

    private void btn_Listener() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=user_name.getText().toString();
                String password=user_password.getText().toString();

                if(name.equals("")||password.equals("")){
                    Toast.makeText(LoginActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    if (redio_user.isChecked()) {
                        List<User> lists = new ArrayList<User>();
                        DButil dbutil = new DButil(LoginActivity.this);
                        String sql = "select * from user_info where name=? and password=?";
                        User user = new User(name, password);
                        lists = dbutil.info_query(user, sql);
                        Toast.makeText(LoginActivity.this, "" + lists.size(), Toast.LENGTH_LONG).show();
                        if (lists.size() == 0) {
                            Toast.makeText(LoginActivity.this, "用户名或密码出错", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("username",name);
                            intent.putExtra("userid",password);
                            startActivity(intent);
                            lists.clear();
                        }
                    }
                    if(redio_admin.isChecked()){
                        if(name.equals("admin")&&password.equals("123456")){
                            Toast.makeText(LoginActivity.this, "管理员登陆成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginActivity.this, "用户名或密码出错", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        btn_regiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent=new Intent(LoginActivity.this,PassFindActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init() {
        user_name= (EditText) findViewById(R.id.user_name);
        user_password= (EditText) findViewById(R.id.user_password);

        btn_login= (Button) findViewById(R.id.btn_login);
        btn_regiter= (Button) findViewById(R.id.btn_register);

        redio_user= (RadioButton) findViewById(R.id.user_redio);
        redio_admin= (RadioButton) findViewById(R.id.admin_redio);

        forget_pass= (TextView) findViewById(R.id.forget_pass);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
     //权限判断和申请
   /* private void initPermission() {
        mPermissions.clear();  //清空未通过的权限
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissions.add(permissions[i]);
            }
        }
        if(mPermissions.size()>0){
            ActivityCompat.requestPermissions(this,permissions,mRequestCode);
        }else {
            init();
        }
    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;//有权限没有通过
        if (mRequestCode == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    Log.i("信息:",""+grantResults.length);
                    hasPermissionDismiss = true;
                }
            }
            //如果有权限没有被允许
            if (hasPermissionDismiss) {
                showPermissionDialog();//跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问
            }else{
                //全部权限通过，可以进行下一步操作。。。
               init();
            }
        }
    }

    AlertDialog mPermissionDialog;
    String mPackName = "com.wu.first.LoginActivity";

    private void showPermissionDialog() {
        if (mPermissionDialog == null) {
            mPermissionDialog = new AlertDialog.Builder(this)
                    .setMessage("已禁用权限，请手动授予")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();

                            Uri packageURI = Uri.parse("package:" + mPackName);
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭页面或者做其他操作
                            cancelPermissionDialog();
                            init();
                        }
                    })
                    .create();
        }
        mPermissionDialog.show();
    }

    //关闭对话框
    private void cancelPermissionDialog() {
        mPermissionDialog.cancel();
    }*/

}
