package com.view.myreport;

/**
 * Created by Administrator on 2016/6/23.
 */
public class GameScoreEvent {
    public final String grade;
    public final String score;


    public GameScoreEvent(String grade,String score) {
        this.grade = grade;
        this.score=score;
    }
}
