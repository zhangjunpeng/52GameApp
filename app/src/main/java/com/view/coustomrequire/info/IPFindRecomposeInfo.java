package com.view.coustomrequire.info;

import com.view.Identification.NameVal;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/4.
 */
public class IPFindRecomposeInfo implements Serializable{
    private List<NameVal> gametype;
    private List<NameVal> gamestage;
    private List<NameVal> coopip;

    public List<NameVal> getGametype() {
        return gametype;
    }

    public void setGametype(List<NameVal> gametype) {
        this.gametype = gametype;
    }

    public List<NameVal> getGamestage() {
        return gamestage;
    }

    public void setGamestage(List<NameVal> gamestage) {
        this.gamestage = gamestage;
    }

    public List<NameVal> getCoopip() {
        return coopip;
    }

    public void setCoopip(List<NameVal> coopip) {
        this.coopip = coopip;
    }
}
