package com.kk.elts2.biz;

import android.content.Context;


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
    Files mFiles;
    ArrayList<Question> mQuestionArrayList;
    ExamInfo mExamInfo=new ExamInfo();
    User mUser;
    IExamDAO examDAOInterface;
     public ExamBiz(Context  context){
       String parse_mode=context.getResources().getString(R.string.parse_mode);
         switch (parse_mode){
             case "txt":
                 examDAOInterface=new ExamDAO_Text(context);
                 break;
             case "pull_xml":
                 examDAOInterface=new ExamDAO_PullParser(context);
                 break;
             case "json":
                 examDAOInterface=new ExamDAO_Json(context);
                 break;
         }
    }
    @Override
    public User login(int uid, String password) throws IdOrPasswordException {
        User user=examDAOInterface.findUser(uid);
        if (user==null||!user.getPassword().equals(password))
            throw new IdOrPasswordException("用户名或密码有误！");
        mUser=user;
        return user;
    }

    @Override
    public User getUSer() {
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
        for (Question m:mQuestionArrayList
             ) {
            if (m.getAnswers().equals(m.getUserAnswers())){
                totle+=m.getScore();
            }
        }
        return totle;
    }
}
