package com.view.coustomrequire.info;

import com.view.Identification.NameVal;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/4.
 */
public class FindIPInfo implements Serializable{
    private List<NameVal> ipcoopcat;
    private List<NameVal> ipcat;
    private List<NameVal> ipstyle;

    public List<NameVal> getIpcoopcat() {
        return ipcoopcat;
    }

    public void setIpcoopcat(List<NameVal> ipcoopcat) {
        this.ipcoopcat = ipcoopcat;
    }

    public List<NameVal> getIpcat() {
        return ipcat;
    }

    public void setIpcat(List<NameVal> ipcat) {
        this.ipcat = ipcat;
    }

    public List<NameVal> getIpstyle() {
        return ipstyle;
    }

    public void setIpstyle(List<NameVal> ipstyle) {
        this.ipstyle = ipstyle;
    }
}
