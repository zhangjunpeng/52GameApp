package com.view.myreport;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/5.
 */
public class GamePCInfo {
    private String name;
    private String score;
    private List<Map<String,String>> num;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public List<Map<String, String>> getNum() {
        return num;
    }

    public void setNum(List<Map<String, String>> num) {
        this.num = num;
    }
}
