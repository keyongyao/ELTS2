package com.kk.elts2.activities;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * 包装一下方法，避免每次都转型
 * Created by Administrator on 2016/9/3.
 */
public class BaseActivity extends AppCompatActivity {
    public <T extends View> T findViewById_(int id){
        return (T) findViewById(id);
    }
}
