package com.test4s.net;

import com.test4s.gdb.Adverts;
import com.test4s.gdb.GameInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/1/19.
 */
public class GameIndexParser {
    boolean success;
    int code;
    String msg;
    String prefixPic;

    List<Adverts> advertsList;
    List<GameInfo> hotGameList;
    List<GameInfo> loveGameList;
    List<GameInfo> localGameList;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
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

    public String getPrefixPic() {
        return prefixPic;
    }

    public void setPrefixPic(String prefixPic) {
        this.prefixPic = prefixPic;
    }

    public List<Adverts> getAdvertsList() {
        return advertsList;
    }

    public void setAdvertsList(List<Adverts> advertsList) {
        this.advertsList = advertsList;
    }

    public List<GameInfo> getHotGameList() {
        return hotGameList;
    }

    public void setHotGameList(List<GameInfo> hotGameList) {
        this.hotGameList = hotGameList;
    }

    public List<GameInfo> getLoveGameList() {
        return loveGameList;
    }

    public void setLoveGameList(List<GameInfo> loveGameList) {
        this.loveGameList = loveGameList;
    }

    public List<GameInfo> getLocalGameList() {
        return localGameList;
    }

    public void setLocalGameList(List<GameInfo> localGameList) {
        this.localGameList = localGameList;
    }
}
