package com.view.index;

/**
 * Created by Administrator on 2016/7/22.
 */
public class ChangeEvent {
    public ChangeEvent(String data,int type){
        this.type=type;
        this.data=data;
    }

    public String data;
    public int type;
}
