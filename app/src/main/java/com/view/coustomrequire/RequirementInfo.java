package com.view.coustomrequire;

import com.view.Identification.NameVal;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/7/27.
 */
public class RequirementInfo {
    private String ident_cat;
    private String ident_name;
    private List<NameVal> findservice;
    private String apply;
    private String company_phone;

    public String getApply() {
        return apply;
    }

    public void setApply(String apply) {
        this.apply = apply;
    }

    public String getCompany_phone() {
        return company_phone;
    }

    public void setCompany_phone(String company_phone) {
        this.company_phone = company_phone;
    }

    public List<NameVal> getFindservice() {
        return findservice;
    }

    public void setFindservice(List<NameVal> findservice) {
        this.findservice = findservice;
    }

    public String getIdent_cat() {
        return ident_cat;
    }

    public void setIdent_cat(String ident_cat) {
        this.ident_cat = ident_cat;
    }

    public String getIdent_name() {
        return ident_name;
    }

    public void setIdent_name(String ident_name) {
        this.ident_name = ident_name;
    }

}
