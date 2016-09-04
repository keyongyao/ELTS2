package com.kk.elts2.biz;

import android.content.Context;
import android.util.Log;

import com.kk.elts2.R;
import com.kk.elts2.dao.ExamDAO_Json;
import com.kk.elts2.dao.ExamDAO_PullParser;
import com.kk.elts2.dao.ExamDAO_Text;
import com.kk.elts2.dao.IExamDAO;
import com.kk.elts2.entity.ExamInfo;
import com.kk.elts2.entity.Files;
import com.kk.elts2.entity.Question;
import com.kk.elts2.entity.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * 实现业务层的操作
 * Created by Administrator on 2016/9/2.
 */
public class ExamBiz implements IExamBiz {
    public IExamDAO examDAOInterface;
    Files mFiles;
    ArrayList<Question> mQuestionArrayList;
    ExamInfo mExamInfo;
    User mUser;
     public ExamBiz(Context  context){
       String parse_mode=context.getResources().getString(R.string.parse_mode);
         switch (parse_mode){
             case "txt":
                 examDAOInterface=new ExamDAO_Text(context);
                 Log.e("main", "ExamBiz: " + "ExamDAO_Text", null);
                 return;
             case "pull_xml":
                 examDAOInterface=new ExamDAO_PullParser(context);
                 Log.e("main", "ExamBiz: " + "ExamDAO_PullParser", null);
                 return;
             case "json":
                 examDAOInterface=new ExamDAO_Json(context);
                 Log.e("main", "ExamBiz: " + "ExamDAO_Json", null);
                 return;
         }
    }
    @Override
    public User login(int uid, String password) throws IdOrPasswordException {
        mUser = examDAOInterface.findUser(uid);
        if (mUser == null || !mUser.getPassword().equals(password))
            throw new IdOrPasswordException("用户名或密码有误!");
        return mUser;
    }

    @Override
    public User getUser() {
        return mUser;
    }

    @Override
    public void loadQuestions() {
        mQuestionArrayList=examDAOInterface.loadQuestions();
    }

    @Override
    public ExamInfo beginExam() {
        Collections.shuffle(mQuestionArrayList, new Random());
        for (int i = 0; i <mQuestionArrayList.size(); i++) {
            Question m=mQuestionArrayList.get(i);
            String title=m.getTitle();
            title=(i+1)+title.substring(title.indexOf("."));
            m.setTitle(title);
        }
        mExamInfo = examDAOInterface.loadExamInfo();
        Log.e("main", "beginExam: " + mExamInfo.toString(), null);
        mExamInfo.setUid(mUser.getId());
        return mExamInfo;
    }

    @Override
    public Question getQuestion(int qid) {
        return mQuestionArrayList.get(qid);
    }

    @Override
    public void saveUserAnswer(int qid, ArrayList<String> userAnswers) {
        Question question=mQuestionArrayList.get(qid);
        question.setUserAnswers(userAnswers);
    }

    @Override
    public int over() {
        int totle=0;
        Log.e("mail", "over: 开始进入记分", null);
        for (Question m:mQuestionArrayList
             ) {
            Log.e("main", "over: 题目：" + m.getTitle(), null);
            Log.e("main", "over: 进入记分体", null);
            Log.e("main", "over: 正确答案：" + m.getAnswers(), null);
            Log.e("main", "over: 用户答案：" + m.getUserAnswers(), null);
            if (m.getAnswers().equals(m.getUserAnswers())){

                totle+=m.getScore();
            }
        }
        Log.e("main", "over: 返回记分", null);
        return totle;
    }
}
