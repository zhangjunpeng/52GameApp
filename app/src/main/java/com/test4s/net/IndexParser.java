package com.test4s.net;

import com.test4s.gdb.Investment;
import com.test4s.gdb.OutSource;
import com.view.s4server.CPSimpleInfo;
import com.view.s4server.IPSimpleInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/1/14.
 */
public class IndexParser {
    private Boolean success;
    private int code;
    private String msg;
    private  data data;





    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public data getData() {
        return data;
    }

    public void setData(data data) {
        this.data = data;
    }

    public class data{
        List<CPSimpleInfo> cpList;
        List<Investment> investorList;
        List<OutSource> outSourceList;
        List<IPSimpleInfo> ipList;
        private String prefixPic;
        public String getPrefixPic() {
            return prefixPic;
        }

        public void setPrefixPic(String prefixPic) {
            this.prefixPic = prefixPic;
        }

        public List<CPSimpleInfo> getCpList() {
            return cpList;
        }

        public void setCpList(List<CPSimpleInfo> cpList) {
            this.cpList = cpList;
        }

        public List<Investment> getInvestorList() {
            return investorList;
        }

        public void setInvestorList(List<Investment> investorList) {
            this.investorList = investorList;
        }

        public List<OutSource> getOutSourceList() {
            return outSourceList;
        }

        public void setOutSourceList(List<OutSource> outSourceList) {
            this.outSourceList = outSourceList;
        }

        public List<IPSimpleInfo> getIpList() {
            return ipList;
        }

        public void setIpList(List<IPSimpleInfo> ipList) {
            this.ipList = ipList;
        }
    }

}
