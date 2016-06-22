package com.view.myreport;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/5.
 */
public class QuesPCInfo {
    private String name;
    private List<Map<String,String>> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<String, String>> getList() {
        return list;
    }

    public void setList(List<Map<String, String>> list) {
        this.list = list;
    }
}
