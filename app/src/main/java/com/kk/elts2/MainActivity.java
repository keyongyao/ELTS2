package com.kk.elts2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.kk.elts2.activities.BaseActivity;
import com.kk.elts2.activities.MainMenu;
import com.kk.elts2.biz.ExamBiz;
import com.kk.elts2.biz.IExamBiz;
import com.kk.elts2.biz.IdOrPasswordException;
import com.kk.elts2.entity.User;

public class MainActivity extends BaseActivity {
    RadioButton mSaveAll,mSaveID,mSaveNull ;
    EditText mEtID,mEtPWD;
    IExamBiz mBiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        initView();
        initData();

        setBtnListener();
    }

    private void initData() {
        // 从本地读取用户登录的信息
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        int id = sp.getInt("uid", -1);
        if (id != -1) {
            mEtID.setText(id + "");
        }
        String pass = sp.getString("pass", "");
        mEtPWD.setText(pass);


        new Thread() {
            @Override
            public void run() {
                mBiz = new ExamBiz(MainActivity.this);  //加载用户数据
            }
        }.start();
    }

    /**
     * s设置登陆和退出按钮的事件
     */
    private void setBtnListener() {
        //设置退出按钮事件
        findViewById(R.id.btnExit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View layout = View.inflate(MainActivity.this, R.layout.exit_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("退出考试？");
                builder.setView(layout);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();

            }
        });
        //设置登陆按钮事件
        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = mEtID.getText().toString();
                if (TextUtils.isEmpty(uid)) {
                    mEtID.setError("ID不能为空");
                    return;
                }
                int id;
                try {
                    id = Integer.parseInt(uid);
                } catch (Exception e) {
                    mEtID.setError("ID格式不正确");
                    return;
                }

                String pass = mEtPWD.getText().toString();
                if (TextUtils.isEmpty(pass)) {
                    mEtPWD.setError("密码不能为空");
                }
                try {
                    User login = mBiz.login(id, pass);
                    saveLoginInfo(id, pass);
                    Intent intent = new Intent(MainActivity.this, MainMenu.class);
                    intent.putExtra("Biz", mBiz);
                    startActivity(intent);

                } catch (IdOrPasswordException e) {
                    if (e.getMessage().equals("用户名或密码有误!")) {
                        Toast.makeText(MainActivity.this, "用户名或密码有误!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


            }
        });
    }

    private void saveLoginInfo(int id, String pass) {
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE); //保存到data/apckage/loginInfo.xml 文件
        SharedPreferences.Editor edit = sp.edit();
        edit.clear(); //清楚以前的信息
        edit.commit();
        if (mSaveAll.isChecked()) {
            edit.putInt("uid", id);
            edit.putString("pass", pass);
            edit.commit();
        } else if (mSaveID.isChecked()) {
            edit.putInt("uid", id);
            edit.commit();
        }
        Toast.makeText(MainActivity.this, "save login info", Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        mSaveAll= findViewById_(R.id.saveAll);
        mSaveID= findViewById_(R.id.saveID);
        mSaveNull= findViewById_(R.id.saveNull);
        mEtID= findViewById_(R.id.etID);
        mEtPWD= findViewById_(R.id.etPwd);

    }

    /**
     * 右上角菜单
     * @param menu  菜单XML
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
