package com.kk.elts2.entity;

import java.io.Serializable;

/**
 *
 * Created by Administrator on 2016/9/2.
 */
public class ExamInfo  implements Serializable {
    private String subjectTitle;
    private int limitTime;
    private int questionCount;
    private int  uid;

    public ExamInfo(String subjectTitle, int limitTime, int questionCount, int uid) {
        this.subjectTitle = subjectTitle;
        this.limitTime = limitTime;
        this.questionCount = questionCount;
        this.uid = uid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public ExamInfo() {
    }


    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public int getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(int limitTime) {
        this.limitTime = limitTime;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    @Override
    public String toString() {
        return "考试科目："+subjectTitle+"  考试用户："+uid+"\n"
                +"考试时间："+limitTime+"   考试题数："+questionCount;
    }
}
