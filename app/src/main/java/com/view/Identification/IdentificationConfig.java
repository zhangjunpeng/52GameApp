package com.view.Identification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/29.
 */
public class IdentificationConfig {

    private static IdentificationConfig instance;

    private IdentificationConfig(){

    }

    public static IdentificationConfig getInstance(){
        if (instance==null){
            instance=new IdentificationConfig();
        }
        return instance;
    }

    private String uploadUrl;
    private String certificationUrl;
    private String uploadType;
    private String companyName;

    private List<NameVal> areaList;
    private List<NameVal> sizeList;
    private List<NameVal> osresList;
    private List<NameVal> stageList;
    private List<NameVal> isscatList;
    private List<NameVal> buscatList;
    private List<NameVal> invescatList;

    public List<NameVal> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<NameVal> areaList) {
        this.areaList = areaList;
    }

    public List<NameVal> getSizeList() {
        return sizeList;
    }

    public void setSizeList(List<NameVal> sizeList) {
        this.sizeList = sizeList;
    }

    public List<NameVal> getOsresList() {
        return osresList;
    }

    public void setOsresList(List<NameVal> osresList) {
        this.osresList = osresList;
    }

    public List<NameVal> getStageList() {
        return stageList;
    }

    public void setStageList(List<NameVal> stageList) {
        this.stageList = stageList;
    }

    public List<NameVal> getIsscatList() {
        return isscatList;
    }

    public void setIsscatList(List<NameVal> isscatList) {
        this.isscatList = isscatList;
    }

    public List<NameVal> getBuscatList() {
        return buscatList;
    }

    public void setBuscatList(List<NameVal> buscatList) {
        this.buscatList = buscatList;
    }

    public List<NameVal> getInvescatList() {
        return invescatList;
    }

    public void setInvescatList(List<NameVal> invescatList) {
        this.invescatList = invescatList;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getCertificationUrl() {
        return certificationUrl;
    }

    public void setCertificationUrl(String certificationUrl) {
        this.certificationUrl = certificationUrl;
    }

    public String getUploadType() {
        return uploadType;
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
