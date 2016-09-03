package com.kk.elts2.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/2.
 */
public class Question implements Serializable {
    private ArrayList<String> answers;
    private ArrayList<String> userAnswers;
    private int level;
    private int score;
    private  String title;
    private String options;

    public Question() {
    }

    public Question(ArrayList<String> answers, int level, int score, String title, String options) {
        this.answers = answers;
        this.userAnswers = new ArrayList<>();
        this.level = level;
        this.score = score;
        this.title = title;
        this.options = options;
    }

    @Override
    public String toString() {
        return this.title+"\n"+this.options;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public ArrayList<String> getUserAnswers() {
        return userAnswers;
    }

    public void setUserAnswers(ArrayList<String> userAnswers) {
        this.userAnswers = userAnswers;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }
}
