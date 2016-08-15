package com.view.coustomrequire.info;

import com.view.Identification.NameVal;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/4.
 */
public class IPFindUniteInfo implements Serializable{
    private List<NameVal> ipcoopcat;
    private List<NameVal> coopip;

    public List<NameVal> getIpcoopcat() {
        return ipcoopcat;
    }

    public void setIpcoopcat(List<NameVal> ipcoopcat) {
        this.ipcoopcat = ipcoopcat;
    }

    public List<NameVal> getCoopip() {
        return coopip;
    }

    public void setCoopip(List<NameVal> coopip) {
        this.coopip = coopip;
    }
}
