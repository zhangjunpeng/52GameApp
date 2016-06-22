package com.test4s.net;

import com.test4s.gdb.GameInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/1/20.
 */
public class GameListParser {
    List<GameInfo> gameInfoList;
    String p;
    public static String staticUrl;
    public static String webUrl;

    public List<GameInfo> getGameInfoList() {
        return gameInfoList;
    }

    public void setGameInfoList(List<GameInfo> gameInfoList) {
        this.gameInfoList = gameInfoList;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getStaticUrl() {
        return staticUrl;
    }

    public void setStaticUrl(String staticUrl) {
        this.staticUrl = staticUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}
