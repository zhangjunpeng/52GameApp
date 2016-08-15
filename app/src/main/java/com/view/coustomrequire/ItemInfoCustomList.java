package com.view.coustomrequire;

import com.view.Identification.NameVal;

import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2016/8/4.
 */
public class ItemInfoCustomList implements Serializable{
    private String id;
    private String user_id;
    private String identity_cat;
    private Object info;
    private String apply;
    private NameVal servive_cat;
    private String custom_id;
    private String checked;
    private String appendix;
    private String note;
    private String reson;
    private String identity_cat_name;
    private String checked_name;
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Object getInfo() {
        return info;
    }

    public void setInfo(Object info) {
        this.info = info;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getApply() {
        return apply;
    }

    public void setApply(String apply) {
        this.apply = apply;
    }

    public NameVal getServive_cat() {
        return servive_cat;
    }

    public void setServive_cat(NameVal servive_cat) {
        this.servive_cat = servive_cat;
    }

    public String getCustom_id() {
        return custom_id;
    }

    public void setCustom_id(String custom_id) {
        this.custom_id = custom_id;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getAppendix() {
        return appendix;
    }

    public void setAppendix(String appendix) {
        this.appendix = appendix;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getReson() {
        return reson;
    }

    public void setReson(String reson) {
        this.reson = reson;
    }

    public String getIdentity_cat_name() {
        return identity_cat_name;
    }

    public void setIdentity_cat_name(String identity_cat_name) {
        this.identity_cat_name = identity_cat_name;
    }

    public String getChecked_name() {
        return checked_name;
    }

    public void setChecked_name(String checked_name) {
        this.checked_name = checked_name;
    }
}
