package com.view.Identification;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/30.
 */
public class NameVal implements Serializable {
    private String id;
    private String val;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
