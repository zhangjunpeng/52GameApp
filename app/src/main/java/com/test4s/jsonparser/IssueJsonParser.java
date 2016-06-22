package com.test4s.jsonparser;

import com.test4s.net.Url;
import com.view.s4server.IssueSimpleInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/3.
 */
public class IssueJsonParser {

    List<IssueSimpleInfo> issueSimpleInfos;

    private static IssueJsonParser issueJsonParser;
    private IssueJsonParser(){

    }
    public static IssueJsonParser getInstance(){
        if (issueJsonParser==null){
            issueJsonParser=new IssueJsonParser();
        }
        return issueJsonParser;
    }

    public List<IssueSimpleInfo> getIssueSimpleInfos(String result){

        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                issueSimpleInfos=new ArrayList<>();
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                Url.prePic=jsonObject1.getString("prefixPic");
                JSONArray issues=jsonObject1.getJSONArray("issueList");
                for (int i=0;i<issues.length();i++){
                    JSONObject jsonObject2=issues.getJSONObject(i);
                    IssueSimpleInfo issueSimpleInfo=new IssueSimpleInfo();
                    issueSimpleInfo.setUser_id(jsonObject2.getString("user_id"));
                    issueSimpleInfo.setLogo(jsonObject2.getString("logo"));
                    issueSimpleInfo.setIdentity_cat(jsonObject2.getString("identity_cat"));
                    issueSimpleInfo.setCompany_name(jsonObject2.getString("company_name"));
                    issueSimpleInfo.setCompany_intro(jsonObject2.getString("company_intro"));
                    issueSimpleInfos.add(issueSimpleInfo);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return issueSimpleInfos;
    }

}
