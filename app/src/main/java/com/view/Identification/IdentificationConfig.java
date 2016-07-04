package com.view.Identification;

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
