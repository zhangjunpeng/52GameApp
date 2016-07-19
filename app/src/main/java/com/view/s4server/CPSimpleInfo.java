package com.view.s4server;

/**
 * Created by Administrator on 2016/3/2.
 */
public class CPSimpleInfo {

    private String user_id;
    private String identity_cat;
    private String logo;
    private String company_intro;
    private String company_name;
    private String area;
    private String scale;
    private boolean iscare;

    public boolean iscare() {
        return iscare;
    }

    public void setIscare(boolean iscare) {
        this.iscare = iscare;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getIdentity_cat() {
        return identity_cat;
    }

    public void setIdentity_cat(String identity_cat) {
        this.identity_cat = identity_cat;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCompany_intro() {
        return company_intro;
    }

    public void setCompany_intro(String company_intro) {
        this.company_intro = company_intro;
    }
}
