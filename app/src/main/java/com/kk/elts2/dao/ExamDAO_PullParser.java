package com.kk.elts2.dao;

import android.content.Context;


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
    public ExamDAO_PullParser(Context context) {
        mFiles=getFiles(context);
        loadUser();
    }

    @Override
    protected void loadUser() {
        String url=mFiles.getRootUrl()+mFiles.getFnUser();
        InputStream inputStream=null;
        try {
            inputStream= HttpUtils.getInputStream(url,null,HttpUtils.RequestMethod.GET);
            XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
            xmlPullParser.setInput(inputStream,"UTF8");
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
        InputStream inputStream=null;
        ArrayList<Question> questionList=new ArrayList<>();
        try {
            inputStream=HttpUtils.getInputStream(url, null, HttpUtils.RequestMethod.GET);
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(inputStream,"UTF8");
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
                            options.append(xmlPullParser.getAttributeName(i)+"\n");
                        }
                        Question bean=new Question(answerList,level,score,title,options.toString());
                        questionList.add(bean);
                    }
                }
            }
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
        InputStream inputStream=null;
        try {

            inputStream = HttpUtils.getInputStream(url, null, HttpUtils.RequestMethod.GET);
            XmlPullParserFactory parserFactory=XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = parserFactory.newPullParser();
            xmlPullParser.setInput(inputStream,"UTF8");
            ExamInfo examInfo=new ExamInfo();
            for (int eventType=XmlPullParser.START_DOCUMENT;eventType!=XmlPullParser.END_DOCUMENT;
                 eventType=xmlPullParser.next()){
                if (eventType==XmlPullParser.START_TAG){
                    if ("exam_info".equals(xmlPullParser.getName())){
                        examInfo.setSubjectTitle(xmlPullParser.getAttributeName(0));
                        examInfo.setLimitTime(Integer.parseInt(xmlPullParser.getAttributeName(1)));
                        examInfo.setQuestionCount(Integer.parseInt(xmlPullParser.getAttributeName(2)));
                    }
                    return examInfo;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            IOClose.closeAll(inputStream);
            HttpUtils.closeClient();
        }
        return null;
    }
}
