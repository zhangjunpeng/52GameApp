package com.view.Identification;

import com.test4s.net.BaseParams;

import java.util.List;

/**
 * Created by Administrator on 2016/7/7.
 */
public class IdentParams  {
    private static IdentParams instance;

    private IdentParams(){}

    public static IdentParams getInstance(){
        if (instance==null){
            instance=new IdentParams();
        }
        return instance;
    }

    //认证需要参数，（ip特殊处理）
    private String comppanyName;
    private String iconUrl;
    private String companySize;
    private String area;
    private String companyInfo;

    //外包认证参数
    private List<NameVal> osresSelected;

    //投资认证参数
    private String invescat;
    private List<NameVal> invesStageSelected;

    //发行参数
    private List<NameVal> buscatSelected;
    private List<NameVal> isscatSelected;

    public String getComppanyName() {
        return comppanyName;
    }

    public void setComppanyName(String comppanyName) {
        this.comppanyName = comppanyName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getCompanySize() {
        return companySize;
    }

    public void setCompanySize(String companySize) {
        this.companySize = companySize;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(String companyInfo) {
        this.companyInfo = companyInfo;
    }

    public List<NameVal> getOsresSelected() {
        return osresSelected;
    }

    public void setOsresSelected(List<NameVal> osresSelected) {
        this.osresSelected = osresSelected;
    }

    public String getInvescat() {
        return invescat;
    }

    public void setInvescat(String invescat) {
        this.invescat = invescat;
    }

    public List<NameVal> getInvesStageSelected() {
        return invesStageSelected;
    }

    public void setInvesStageSelected(List<NameVal> invesStageSelected) {
        this.invesStageSelected = invesStageSelected;
    }

    public List<NameVal> getBuscatSelected() {
        return buscatSelected;
    }

    public void setBuscatSelected(List<NameVal> buscatSelected) {
        this.buscatSelected = buscatSelected;
    }

    public List<NameVal> getIsscatSelected() {
        return isscatSelected;
    }

    public void setIsscatSelected(List<NameVal> isscatSelected) {
        this.isscatSelected = isscatSelected;
    }
}
