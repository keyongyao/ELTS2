package com.kk.elts2.biz;


import com.kk.elts2.entity.ExamInfo;
import com.kk.elts2.entity.Question;
import com.kk.elts2.entity.User;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 逻辑业务的接口
 * Created by Administrator on 2016/9/2.
 */
public interface IExamBiz extends Serializable {
    /**
     * @param uid  用户ID
     * @param password  用户密码
     * @return  User 对象
     * @throws IdOrPasswordException  错误异常
     */
    User login(int uid, String password) throws IdOrPasswordException;

    User getUser();
    void loadQuestions();
    ExamInfo beginExam();
    Question getQuestion(int qid);
    void saveUserAnswer(int qid, ArrayList<String> userAnswers);
    int over();
}
