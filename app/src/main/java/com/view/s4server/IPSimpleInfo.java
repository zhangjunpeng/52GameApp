package com.view.s4server;

/**
 * Created by Administrator on 2016/3/3.
 */
public class IPSimpleInfo {
    private String id;
    private String ip_name;
    private String logo;
    private String ip_style;
    private String ip_cat;
    private String uthority;
    private String company_name;

    private String ip_info;
    private String over_time;

    private boolean iscare;

    public boolean iscare() {
        return iscare;
    }

    public void setIscare(boolean iscare) {
        this.iscare = iscare;
    }

    public String getOver_time() {
        return over_time;
    }

    public void setOver_time(String over_time) {
        this.over_time = over_time;
    }

    public String getIp_info() {
        return ip_info;
    }

    public void setIp_info(String ip_info) {
        this.ip_info = ip_info;
    }

    public String getIp_name() {
        return ip_name;
    }

    public void setIp_name(String ip_name) {
        this.ip_name = ip_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getIp_style() {
        return ip_style;
    }

    public void setIp_style(String ip_style) {
        this.ip_style = ip_style;
    }

    public String getIp_cat() {
        return ip_cat;
    }

    public void setIp_cat(String ip_cat) {
        this.ip_cat = ip_cat;
    }

    public String getUthority() {
        return uthority;
    }

    public void setUthority(String uthority) {
        this.uthority = uthority;
    }
}
