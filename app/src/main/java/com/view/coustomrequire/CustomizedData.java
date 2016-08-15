package com.view.coustomrequire;

import com.app.tools.MyLog;
import com.test4s.account.MyAccount;
import com.test4s.net.BaseParams;
import com.view.Identification.NameVal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/29.
 */
public class CustomizedData {
    private static CustomizedData instance;

    private CustomizedData(){
    }

    public static CustomizedData getInstance(){
        if (instance==null){
            instance=new CustomizedData();
        }
        return instance;
    }


    private Map<String,List<NameVal>> map;

    public void setMap(Map<String, List<NameVal>> map) {
        this.map = map;
    }

    public Map<String, List<NameVal>> getMap() {
        return map;
    }



}
