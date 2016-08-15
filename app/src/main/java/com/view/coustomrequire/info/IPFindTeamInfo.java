package com.view.coustomrequire.info;

import com.test4s.net.BaseParams;
import com.view.Identification.NameVal;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/4.
 */
public class IPFindTeamInfo implements Serializable{
    private List<NameVal> ipdevelopcat;
    private List<NameVal> coopip;

    public List<NameVal> getIpdevelopcat() {
        return ipdevelopcat;
    }

    public void setIpdevelopcat(List<NameVal> ipdevelopcat) {
        this.ipdevelopcat = ipdevelopcat;
    }

    public List<NameVal> getCoopip() {
        return coopip;
    }

    public void setCoopip(List<NameVal> coopip) {
        this.coopip = coopip;
    }
}
