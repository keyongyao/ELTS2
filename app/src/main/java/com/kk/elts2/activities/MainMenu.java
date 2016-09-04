package com.kk.elts2.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.elts2.R;
import com.kk.elts2.biz.IExamBiz;

public class MainMenu extends BaseActivity {
    Button mExamStart, mExamExit, mExamResult, mExamRuler;
    TextView mWelcome;
    IExamBiz mBiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        mBiz = (IExamBiz) getIntent().getSerializableExtra("Biz");
        initView();
        initListener();
    }

    private void initListener() {
        findViewById(R.id.exam_result).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainMenu.this, "ceshi", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.exam_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(MainMenu.this);
                progressDialog.setTitle("载入试题");
                progressDialog.setMessage("请稍后....");
                progressDialog.show();
                new Thread() {
                    @Override
                    public void run() {
                        mBiz.loadQuestions();  //下载 试题

                        progressDialog.dismiss();
                        Intent intent = new Intent(MainMenu.this, ExamWindows.class);
                        intent.putExtra("Biz", mBiz);
                        startActivity(intent);
                    }
                }.start();
            }
        });
        findViewById(R.id.exam_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View layout = View.inflate(MainMenu.this, R.layout.exit_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
                builder.setTitle("退出");
                builder.setView(layout);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                builder.create().show();
            }
        });
        findViewById(R.id.exam_rule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainMenu.this);
                View layout = View.inflate(MainMenu.this, R.layout.exam_ruler, null);
                dialog.setContentView(layout);
                dialog.setTitle("考试规则");
                dialog.show();
            }
        });
    }

    private void initView() {
        mExamStart = findViewById_(R.id.exam_start);
        mExamExit = findViewById_(R.id.exam_exit);
        mExamRuler = findViewById_(R.id.exam_rule);
        mExamResult = findViewById_(R.id.exam_result);
        mWelcome = findViewById_(R.id.welcome);
        mWelcome.setText("欢迎" + mBiz.getUser().getName() + "考试！");
    }
}
