package com.wu.first;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.mob.cms.gui.CMSGUI;
import com.mob.cms.gui.themes.defaultt.DefaultTheme;

public class MainActivity extends AppCompatActivity {
    private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        text= (TextView) findViewById(R.id.textView);
        String name=getIntent().getStringExtra("username");
        String id=getIntent().getStringExtra("userid");
        CMSGUI.setTheme(DefaultTheme.class);
        CMSGUI.showNewsListPageWithCustomUser(id,name,null);

        //CMSGUI.showNewsListPageWithCustomUser(id,name,null);
       /* CMSSDK.getCategories(new Callback(){
            public void onSuccess(Object object) {
                ArrayList<Category> categories = (ArrayList<Category>) object;
                // 执行成功的操作

            }

            public void onCancel() {
                // 执行取消的操作
            }

            public void onFailed(Throwable t) {
                // 提示错误信息
            }
        });*/

    }
}
