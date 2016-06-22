package com.test4s.jsonparser;

import com.test4s.net.Url;
import com.view.s4server.CPSimpleInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/2.
 */
public class CPJsonParser {
    private static CPJsonParser cpJsonParser;

    private List<CPSimpleInfo> cpSimpleInfos;

    private CPJsonParser(){
    }
    public static CPJsonParser getInstance(){
        if (cpJsonParser==null){
            cpJsonParser=new CPJsonParser();
        }
        return cpJsonParser;
    }

    public List<CPSimpleInfo> getcplistparser(String result){
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                cpSimpleInfos=new ArrayList<>();
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                Url.prePic=jsonObject1.getString("prefixPic");
                JSONArray ja=jsonObject1.getJSONArray("cpList");
                for (int i=0;i<ja.length();i++){
                    JSONObject jsonObject2=ja.getJSONObject(i);
                    CPSimpleInfo cpSimpleInfo=new CPSimpleInfo();
                    cpSimpleInfo.setUser_id(jsonObject2.getString("user_id"));
                    cpSimpleInfo.setIdentity_cat(jsonObject2.getString("identity_cat"));
                    cpSimpleInfo.setLogo(jsonObject2.getString("logo"));
                    cpSimpleInfo.setCompany_intro(jsonObject2.getString("company_intro"));
                    cpSimpleInfo.setCompany_name(jsonObject2.getString("company_name"));
                    cpSimpleInfos.add(cpSimpleInfo);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return cpSimpleInfos;
    }


}
