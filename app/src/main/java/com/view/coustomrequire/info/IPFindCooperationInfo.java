package com.view.coustomrequire.info;

import com.test4s.net.BaseParams;
import com.view.Identification.NameVal;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/4.
 */
public class IPFindCooperationInfo implements Serializable{
    private List<NameVal> iputhority;
    private List<NameVal> coopip;

    public List<NameVal> getIputhority() {
        return iputhority;
    }

    public void setIputhority(List<NameVal> iputhority) {
        this.iputhority = iputhority;
    }

    public List<NameVal> getCoopip() {
        return coopip;
    }

    public void setCoopip(List<NameVal> coopip) {
        this.coopip = coopip;
    }
}
