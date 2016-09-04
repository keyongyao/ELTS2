package com.kk.elts2.dao;

import android.content.Context;
import android.util.Log;

import com.kk.elts2.entity.ExamInfo;
import com.kk.elts2.entity.Files;
import com.kk.elts2.entity.Question;
import com.kk.elts2.entity.User;
import com.kk.elts2.utils.HttpUtils;
import com.kk.elts2.utils.IOClose;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/2.
 */
public class ExamDAO_PullParser extends ExamDAOBase implements IExamDAO {
    Files mFiles;
    ExamInfo examInfo;
    public ExamDAO_PullParser(Context context) {
        mFiles=getFiles(context);
        Log.e("main", "ExamDAO_PullParser: " + "已获取文件配置URL信息", null);
        loadUser();
        Log.e("main", "ExamDAO_PullParser: " + "已获取用户信息表", null);
    }

    @Override
    protected void loadUser() {
        String url=mFiles.getRootUrl()+mFiles.getFnUser();
        Log.e("main", "loadUser: " + url);
        InputStream inputStream=null;
        try {
            Log.e("main", "loadUser: " + "开始获取到输入流", null);
            inputStream= HttpUtils.getInputStream(url,null,HttpUtils.RequestMethod.GET);

            XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
            xmlPullParser.setInput(inputStream, "UTF-8");
            for(int eventType=XmlPullParser.START_DOCUMENT;eventType!=XmlPullParser.END_DOCUMENT;
                    eventType=xmlPullParser.next()){
                if (eventType==XmlPullParser.START_TAG){
                    if ("user".equals(xmlPullParser.getName())){
                        User user=new User();
                        user.setId(Integer.parseInt(xmlPullParser.getAttributeValue(null,"id")));
                        user.setName(xmlPullParser.getAttributeValue(null,"name"));
                        user.setPassword(xmlPullParser.getAttributeValue(null,"password"));
                        user.setPhone(xmlPullParser.getAttributeValue(null,"phone"));
                        user.setEmail(xmlPullParser.getAttributeValue(null,"email"));
                        mUsers.put(user.getId(),user);
                    }
                }
            }
            Log.e("main", "loadUser: " + mUsers.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            IOClose.closeAll(inputStream);
            HttpUtils.closeClient();
        }
    }

    @Override
    public ArrayList<Question> loadQuestions() {
        String url=mFiles.getRootUrl()+mFiles.getFnQuestion();
        Log.e("main", "loadQuestions: " + url, null);
        InputStream inputStream=null;
        ArrayList<Question> questionList=new ArrayList<>();
        try {
            inputStream=HttpUtils.getInputStream(url, null, HttpUtils.RequestMethod.GET);
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(inputStream, "UTF-8");
            for(int eventType=XmlPullParser.START_DOCUMENT;eventType!=XmlPullParser.END_DOCUMENT;
                    eventType=xmlPullParser.next()){
                String tagName=xmlPullParser.getName();
                if (eventType==XmlPullParser.START_TAG){
                    if (tagName.equals("question")){
                        String answers=xmlPullParser.getAttributeValue(null,"answer");
                        ArrayList<String> answerList=new ArrayList<>(); // for multi answer of one question
                        for (int i = 0; i <answers.length() ; i++) {
                            answerList.add(answers.charAt(i)+"");
                        }
                        int score=Integer.parseInt(xmlPullParser.getAttributeValue(null,"score"));
                        int level= Integer.parseInt(xmlPullParser.getAttributeValue(null,"level"));
                        String title=xmlPullParser.getAttributeValue(null,"title");
                        StringBuilder options=new StringBuilder();
                        for (int i = 4; i <xmlPullParser.getAttributeCount() ; i++) {
                            options.append(xmlPullParser.getAttributeValue(i) + "\n");
                        }
                        Question bean=new Question(answerList,level,score,title,options.toString());
                        questionList.add(bean);
                    }
                }
            }
            Log.e("main", "loadQuestions: " + questionList.get(3).toString(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            IOClose.closeAll(inputStream);
            HttpUtils.closeClient();
        }
        return questionList;
    }

    @Override
    public ExamInfo loadExamInfo() {
        String url=mFiles.getRootUrl()+mFiles.getFnExamInfo();
        Log.e("main", "loadExamInfo: " + url, null);
        InputStream inputStream=null;
        examInfo = new ExamInfo();
        try {
            inputStream = HttpUtils.getInputStream(url, null, HttpUtils.RequestMethod.GET);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();//创建用于pull解析的解析器
            parser.setInput(inputStream, "utf-8");
            ExamInfo examInfo=new ExamInfo();
            for (int eventType = XmlPullParser.START_DOCUMENT;
                 eventType != XmlPullParser.END_DOCUMENT; eventType = parser.next()) {
                if (eventType == XmlPullParser.START_TAG) {
                    String tagName = parser.getName();// 获取标签名称
                    if ("exam_info".equals(tagName)) {
                        examInfo.setSubjectTitle(parser.getAttributeValue(0));
                        examInfo.setLimitTime(Integer.parseInt(parser.getAttributeValue(1)));
                        examInfo.setQuestionCount(Integer.parseInt(parser.getAttributeValue(2)));
                        return examInfo;
                    }
                }
            }

            Log.e("main", "loadExamInfo: " + examInfo.toString(), null);
            return examInfo;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("main", "loadExamInfo: " + "获取ExamInfo出错!!!!!进入紧急手动设置 ", null);
            // TODO 暂时应付获取不到Examinfo输入流 的问题
            examInfo.setSubjectTitle("core java");
            examInfo.setQuestionCount(20);
            examInfo.setLimitTime(10);

            Log.e("main", "loadExamInfo: " + examInfo.toString(), null);
            return examInfo;

        }finally {
            IOClose.closeAll(inputStream);
            HttpUtils.closeClient();
        }

    }
}
