package com.test4s.jsonparser;

import com.test4s.gdb.IndexAdvert;
import com.test4s.gdb.IndexItemInfo;
import com.test4s.net.Url;
import com.view.s4server.IPSimpleInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/11.
 */
public class IndexJsonParser {

    public Map<String,List> map;
    public List<String> order;
    public List<String> names;
    public  List<IndexAdvert> indexAdvertses;

    private static IndexJsonParser indexJsonParser;

    private IndexJsonParser(){}


    public static IndexJsonParser getInstance(){
        if (indexJsonParser==null) {
            indexJsonParser = new IndexJsonParser();
        }
        return indexJsonParser;
    }

    public void jsonParser(String res){
        try {
            JSONObject jsonObect=new JSONObject(res);
            boolean su=jsonObect.getBoolean("success");
            int code=jsonObect.getInt("code");
            if (su&&code==200){
                map=new HashMap<>();
                names=new ArrayList<>();
                order=new ArrayList<>();
                JSONObject data=jsonObect.getJSONObject("data");
                Url.prePic=data.getString("prefixPic");
                JSONArray reList=data.getJSONArray("reList");
                for (int i=0;i<reList.length();i++){
                    JSONObject info=reList.getJSONObject(i);
                    String method_name=info.getString("method_name");
                    order.add(method_name);
                    String name=info.getString("name");
                    names.add(name);
                    JSONArray array=info.getJSONArray(method_name+"list");
                    if (method_name.equals("ip")){
                        ArrayList<IPSimpleInfo> list=new ArrayList<>();
                        for (int j=0;j<array.length();j++){
                            JSONObject item=array.getJSONObject(j);
                            IPSimpleInfo ipsimpleInfo=new IPSimpleInfo();
                            ipsimpleInfo.setIp_name(item.getString("ip_name"));
                            ipsimpleInfo.setLogo(item.getString("ip_logo"));
                            ipsimpleInfo.setId(item.getString("id"));
                            list.add(ipsimpleInfo);
                        }
                        map.put(method_name,list);
                    }else {
                        ArrayList<IndexItemInfo> list=new ArrayList<>();
                        for (int j=0;j<array.length();j++){
                            JSONObject item=array.getJSONObject(j);
                            IndexItemInfo simpleInfo=new IndexItemInfo();
                            simpleInfo.setUser_id(item.getString("user_id"));
                            simpleInfo.setLogo(item.getString("logo"));
                            simpleInfo.setIdentity_cat(item.getString("identity_cat"));
                            simpleInfo.setCompany_name(item.getString("company_name"));
                            list.add(simpleInfo);
                        }
                        map.put(method_name,list);
                    }
                }
                JSONArray adverts=data.getJSONArray("adverts");
                indexAdvertses=new ArrayList<>();
                for (int i=0;i<adverts.length();i++){
                    IndexAdvert advert=new IndexAdvert();
                    JSONObject jadvert=adverts.getJSONObject(i);
                    advert.setUser_id(jadvert.getString("id"));
                    advert.setAdvert_name(jadvert.getString("advert_name"));
                    advert.setAdvert_pic(jadvert.getString("advert_pic"));
                    advert.setAdvert_url(jadvert.getString("advert_url"));
                    indexAdvertses.add(advert);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

