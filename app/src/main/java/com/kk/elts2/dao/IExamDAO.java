package com.kk.elts2.dao;

import com.kk.elts2.entity.ExamInfo;
import com.kk.elts2.entity.Question;
import com.kk.elts2.entity.User;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/2.
 */
public interface IExamDAO {
    ArrayList<Question> loadQuestions();
    ExamInfo loadExamInfo();
    User findUser(int id);

}
