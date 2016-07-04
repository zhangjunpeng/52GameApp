package com.view.myreport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/22.
 */
public class ExpertEvaConInfo {
    private String name;
    private String data;
    private ArrayList<String> gameshot;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ArrayList<String> getGameshot() {
        return gameshot;
    }

    public void setGameshot(ArrayList<String> gameshot) {
        this.gameshot = gameshot;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
