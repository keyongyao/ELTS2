package com.kk.elts2.dao;

import android.content.Context;

import com.kk.elts2.entity.ExamInfo;
import com.kk.elts2.entity.Files;
import com.kk.elts2.entity.Question;

import java.util.ArrayList;


/**
 * TODO  实现这个解析json的实现类
 * Created by Administrator on 2016/9/2.
 */
public class ExamDAO_Json extends ExamDAOBase implements IExamDAO {
    Files mFiles;
    public ExamDAO_Json(Context context){
        mFiles=getFiles(context);
    }
    @Override
    protected void loadUser() {

    }

    @Override
    public ArrayList<Question> loadQuestions() {
        return null;
    }

    @Override
    public ExamInfo loadExamInfo() {
        return null;
    }


}
