package com.view.coustomrequire.info;

import com.view.Identification.NameVal;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/4.
 */
public class FindTeamInfo implements Serializable{
    private List<NameVal> teamtype;
    private List<NameVal> starge;

    public List<NameVal> getTeamtype() {
        return teamtype;
    }

    public void setTeamtype(List<NameVal> teamtype) {
        this.teamtype = teamtype;
    }

    public List<NameVal> getStarge() {
        return starge;
    }

    public void setStarge(List<NameVal> starge) {
        this.starge = starge;
    }
}
