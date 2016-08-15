package com.view.coustomrequire.info;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/4.
 */
public class FindInvestInfo implements Serializable{
    private String starge;
    private String money;
    private String minstock;
    private String maxstock;

    public String getStarge() {
        return starge;
    }

    public void setStarge(String starge) {
        this.starge = starge;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMinstock() {
        return minstock;
    }

    public void setMinstock(String minstock) {
        this.minstock = minstock;
    }

    public String getMaxstock() {
        return maxstock;
    }

    public void setMaxstock(String maxstock) {
        this.maxstock = maxstock;
    }
}
