package com.test4s.jsonparser;

import com.test4s.net.Url;
import com.view.s4server.IPSimpleInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/3.
 */
public class IPJsonParser {
    List<IPSimpleInfo> ipSimpleInfos;

    private static IPJsonParser ipJsonParser;
    private IPJsonParser(){

    }
    public static IPJsonParser getIntance(){
        if (ipJsonParser==null){
            ipJsonParser=new IPJsonParser();
        }
        return ipJsonParser;
    }

    public List<IPSimpleInfo> ipListParser(String result){
        try {
             JSONObject jsonObject=new JSONObject(result);
             boolean su=jsonObject.getBoolean("success");
             int code=jsonObject.getInt("code");
            if (su&&code==200){
                ipSimpleInfos=new ArrayList<>();
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                Url.prePic=jsonObject1.getString("prefixPic");
                JSONArray jsonArray=jsonObject1.getJSONArray("ipList");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject2=jsonArray.getJSONObject(i);
                    IPSimpleInfo ipSimpleInfo=new IPSimpleInfo();
                    ipSimpleInfo.setId(jsonObject2.getString("id"));
                    ipSimpleInfo.setLogo(jsonObject2.getString("ip_logo"));
                    ipSimpleInfo.setIp_name(jsonObject2.getString("ip_name"));
                    ipSimpleInfo.setIp_cat(jsonObject2.getString("ip_cat"));
                    ipSimpleInfo.setCompany_name(jsonObject2.getString("company_name"));
                    ipSimpleInfo.setIp_style(jsonObject2.getString("ip_style"));
                    ipSimpleInfo.setUthority(jsonObject2.getString("uthority"));
                    ipSimpleInfos.add(ipSimpleInfo);

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ipSimpleInfos;
    }


}
