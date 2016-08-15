package com.view.coustomrequire.info;

import com.view.Identification.NameVal;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/4.
 */
public class FindIssueInfo implements Serializable{
    private List<NameVal> region;
    private List<NameVal> issuecat;
    private List<NameVal> issuegame;

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

    public List<NameVal> getIssuegame() {
        return issuegame;
    }

    public void setIssuegame(List<NameVal> issuegame) {
        this.issuegame = issuegame;
    }
}
