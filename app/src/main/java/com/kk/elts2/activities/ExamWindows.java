package com.kk.elts2.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk.elts2.R;
import com.kk.elts2.biz.IExamBiz;
import com.kk.elts2.entity.ExamInfo;
import com.kk.elts2.entity.Question;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ExamWindows extends BaseActivity {
    IExamBiz biz;
    TextView mExamInfo, mLeftTime;
    EditText mQuestion;
    CheckBox mCbA, mCbB, mCbC, mCbD;
    Gallery gallery;
    int oldQuestionIndex;
    Timer leftTime;
    FloatingActionButton mFbtn;
    ArrayList<Integer> finishedQuestions;
    volatile ExamInfo examInfo;
    ArrayList<Question> questions;
    QuestionsAdapter questionsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_windows);
        initData();
        initView();
        setListenr();
        begineExam();
    }

    private void setListenr() {
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 如果选择了 就说明做题了
                biz.saveUserAnswer(oldQuestionIndex, getUserAnswersFromCheckBox());
                // 通知适配器  用户做了选择
                if (mCbA.isChecked() || mCbB.isChecked() || mCbC.isChecked() || mCbD.isChecked()) {
                    finishedQuestions.add(oldQuestionIndex);
                    questionsAdapter.notifyDataSetChanged();
                }
                // 用户把 上一个答案清空了
                if (biz.getQuestion(oldQuestionIndex).getUserAnswers().isEmpty()) {
                    if (finishedQuestions.contains(oldQuestionIndex)) {
                        finishedQuestions.remove(oldQuestionIndex);
                        questionsAdapter.notifyDataSetChanged();
                    }
                }
                // 跳题后的前一个题的位置。
                oldQuestionIndex = position;

                // 刷新题目  清空checkbox的选择  如果是做过的题 则显示用户的答案
                reflashQuestion(position);

            }
        });


    }

    //刷新题目的选项
    private void reflashQuestion(int nowPosition) {
        mCbA.setChecked(false);
        mCbB.setChecked(false);
        mCbC.setChecked(false);
        mCbD.setChecked(false);
        Question question = biz.getQuestion(nowPosition);
        if (finishedQuestions.contains(nowPosition)) {
            ArrayList<String> userAnswers = question.getUserAnswers();
            for (String aa : userAnswers
                    ) {
                switch (aa) {
                    case "A":
                        mCbA.setChecked(true);
                        break;
                    case "B":
                        mCbB.setChecked(true);
                        break;
                    case "C":
                        mCbC.setChecked(true);
                        break;
                    case "D":
                        mCbD.setChecked(true);
                        break;
                }
            }


        }
        String questTitle = question.getTitle() + "\n";
        String questBody = question.getOptions();
        mQuestion.setText(questTitle + questBody);


    }

    private ArrayList<String> getUserAnswersFromCheckBox() {
        ArrayList<String> userAnswers = new ArrayList<>();
        if (mCbA.isChecked())
            userAnswers.add("A");
        if (mCbB.isChecked())
            userAnswers.add("B");
        if (mCbC.isChecked())
            userAnswers.add("C");
        if (mCbD.isChecked())
            userAnswers.add("D");

        return userAnswers;
    }

    protected void startTime() {
        long startTime = System.currentTimeMillis();
        final long endTime = startTime + examInfo.getLimitTime() * 60 * 1000;
        final Timer mTimer = new Timer();
        Timer mTimer2 = new Timer();
        mTimer.schedule(new TimerTask() {
            long minute, second;

            @Override
            public void run() {
                long leftTime = endTime - System.currentTimeMillis();
                minute = leftTime / 1000 / 60;
                second = leftTime / 1000 % 60;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLeftTime.setText("剩余时间：" + minute + ":" + second);
                    }
                });
            }
        }, 0, 1000);

        mTimer2.schedule(new TimerTask() {
            @Override
            public void run() {
                mTimer.cancel();
                Log.e("main", "run: " + "已终止了任务", null);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("main", "run: 提交计算分数", null);
                        examCommit();

                    }
                });

            }
        }, examInfo.getLimitTime() * 60 * 1000);
    }

    private void examCommit() {
        int score = biz.over();
        Log.e("main", "examCommit: 获取返回分数了", null);
        AlertDialog.Builder builder = new AlertDialog.Builder(ExamWindows.this);
        builder.setTitle("交卷");
        builder.setIcon(R.mipmap.exam_commit32x32);
        builder.setMessage("时间到了,系统算的成绩为：" + score + "分.");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();
    }

    private void begineExam() {
        new Thread() {
            @Override
            public void run() {
                examInfo = biz.beginExam();
                // 只有创建View的线程能对UI进行修改
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String info = examInfo.toString();   // 因为这个是全局变量，要和获取ExamInfo的线程放在一起
                        mExamInfo.setText(info);

                        questionsAdapter = new QuestionsAdapter();  // 依赖 examInfo
                        gallery.setAdapter(questionsAdapter);
                    }
                });
                startTime();
            }
        }.start();

    }

    private void initData() {
        biz = (IExamBiz) getIntent().getSerializableExtra("Biz");
        finishedQuestions = new ArrayList<>();

    }


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
        if (id == R.id.commit) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    examCommit();

                }
            });

            return true;
        }
        if (id == R.id.reset) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initView() {

        mExamInfo = findViewById_(R.id.tvExaminfo);
        mLeftTime = findViewById_(R.id.tvleftTime);
        mQuestion = findViewById_(R.id.etQuestions);
        mCbA = findViewById_(R.id.cbA);
        mCbB = findViewById_(R.id.cbB);
        mCbC = findViewById_(R.id.cbC);
        mCbD = findViewById_(R.id.cbD);
        gallery = findViewById_(R.id.gallery);


    }

    // 由于是内部类，及可以直接读取外部成员变量
    class QuestionsAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return examInfo.getQuestionCount();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //TODO  使用优化
            View layout = View.inflate(ExamWindows.this, R.layout.item_question, null);
            ImageView imageView = (ImageView) layout.findViewById(R.id.ivItem);
            TextView textView = (TextView) layout.findViewById(R.id.tvQueNum);
            textView.setText("题" + (position + 1));
            if (finishedQuestions.contains(position)) {
                imageView.setImageResource(R.mipmap.answer24x24);

            } else {
                imageView.setImageResource(R.mipmap.ques24x24);
            }
            return layout;
        }
    }

}
