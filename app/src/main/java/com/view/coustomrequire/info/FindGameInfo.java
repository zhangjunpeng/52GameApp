package com.view.coustomrequire.info;

import com.view.Identification.NameVal;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/4.
 */
public class FindGameInfo implements Serializable{
    private List<NameVal> region;
    private List<NameVal> issuecat;
    private List<NameVal> gamegrade;
    private List<NameVal> gametype;
    private List<NameVal> gamestage;

    public List<NameVal> getRegion() {
        return region;
    }

    public void setRegion(List<NameVal> region) {
        this.region = region;
    }

    public List<NameVal> getIssuecat() {
        return issuecat;
    }

    public void setIssuecat(List<NameVal> issuecat) {
        this.issuecat = issuecat;
    }

    public List<NameVal> getGamegrade() {
        return gamegrade;
    }

    public void setGamegrade(List<NameVal> gamegrade) {
        this.gamegrade = gamegrade;
    }

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
}
