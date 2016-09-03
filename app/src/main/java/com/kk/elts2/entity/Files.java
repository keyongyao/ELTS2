package com.kk.elts2.entity;

import java.io.Serializable;

/**
 *存放考试服务端的URL 考试信息 考试问题  考试用户
 * Created by Administrator on 2016/9/2.
 */
public class Files implements Serializable {
    private String rootUrl;
    private String fnExamInfo;
    private String fnQuestion;
    private String fnUser;

    @Override
    public String toString() {
        return "Files{" +
                "rootUrl='" + rootUrl + '\'' +
                ", fnExamInfo='" + fnExamInfo + '\'' +
                ", fnQuestion='" + fnQuestion + '\'' +
                ", fnUser='" + fnUser + '\'' +
                '}';
    }

    public Files() {
    }

    /**
     *
     * @param url  文件URL
     * @param fnExamInfo  文件信息
     * @param fnQuestion  问题文件
     * @param fnUser    用户
     */
    public Files(String url, String fnExamInfo, String fnQuestion, String fnUser) {
        this.rootUrl = url;
        this.fnExamInfo = fnExamInfo;
        this.fnQuestion = fnQuestion;
        this.fnUser = fnUser;
    }

    /**
     *
     * @return  文件的URL
     */
    public String getRootUrl() {
        return rootUrl;
    }

    /**
     *
     * @param rootUrl 设置文件的URL
     */
    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    /**
     *
     * @return 文件考试信息
     */
    public String getFnExamInfo() {
        return fnExamInfo;
    }

    public void setFnExamInfo(String fnExamInfo) {
        this.fnExamInfo = fnExamInfo;
    }

    public String getFnQuestion() {
        return fnQuestion;
    }

    public void setFnQuestion(String fnQuestion) {
        this.fnQuestion = fnQuestion;
    }

    public String getFnUser() {
        return fnUser;
    }

    public void setFnUser(String fnUser) {
        this.fnUser = fnUser;
    }
}
