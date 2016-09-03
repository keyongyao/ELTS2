package com.kk.elts2.dao;

import android.content.Context;
import android.content.res.Resources;

import com.kk.elts2.R;
import com.kk.elts2.entity.Files;
import com.kk.elts2.entity.User;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/9/2.
 */
public  abstract class ExamDAOBase implements Serializable {
    public ExamDAOBase() {
        mUsers=new HashMap<>();
    }

    HashMap<Integer,User> mUsers;

    protected abstract void loadUser();

    protected Files getFiles(Context context) {
        Resources resources = context.getResources();
        String parse_mode=resources.getString(R.string.parse_mode);
        String rootUrl=resources.getString(R.string.root_url);
        String fnExaminfo=null;
        String fnQuestion=null;
        String fnUser=null;
        if ("txt".equals(parse_mode)){
            fnExaminfo=resources.getString(R.string.exam_info_txt);
            fnQuestion=resources.getString(R.string.question_txt);
            fnUser=resources.getString(R.string.user_txt);
        }else if ("pull_xml".equals(parse_mode) || "sax_xml".equals(parse_mode)){
            fnExaminfo=resources.getString(R.string.exam_info_xml);
            fnQuestion=resources.getString(R.string.question_xml);
            fnUser=resources.getString(R.string.user_xml);
        }else if ("json".equals(parse_mode)){
            fnExaminfo=resources.getString(R.string.exam_info_json);
            fnQuestion=resources.getString(R.string.question_json);
            fnUser=resources.getString(R.string.user_json);
        }
        Files  ff= new Files(rootUrl,fnExaminfo,fnQuestion,fnUser);
        return ff;
    }

    public User findUser(int id) {
        return mUsers.get(id);
    }
}
