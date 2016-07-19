package com.view.game;

import com.app.tools.MyLog;
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
 * Created by Administrator on 2016/7/13.
 */
public class FiltParamsData {

    private static FiltParamsData instance;

    private FiltParamsData(){
        initPopList();
    }

    public static FiltParamsData getInstance(){
        if (instance==null){
            instance=new FiltParamsData();
        }
        return instance;
    }
    private void initPopList() {
        BaseParams baseParams=new BaseParams("api/selecttype");
        baseParams.addParams("type","all");
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("popdata=="+result);
                jsonparser(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

   private Map<String,List<NameVal>> map;

    public Map<String, List<NameVal>> getMap() {
        return map;
    }


    private void jsonparser(String result) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            boolean su=jsonObject.getBoolean("success");
            int code=jsonObject.getInt("code");
            if (su&&code==200){
                JSONObject data=jsonObject.getJSONObject("data");
                map=new HashMap<>();
                JSONObject selectList=data.getJSONObject("selectList");
                Iterator<String> iterator= selectList.keys();
                while (iterator.hasNext()){
                    String key=iterator.next();
                    JSONArray array=selectList.getJSONArray(key);
                    List<NameVal> nameValList=new ArrayList<>();
                    for (int i=0;i<array.length();i++){
                        JSONObject nameJson=array.getJSONObject(i);
                        NameVal val=new NameVal();
                        val.setId(nameJson.getString("id"));
                        if (nameJson.getString("id").equals("0")) {
                            val.setVal("全部");

                        }else {
                            if (nameJson.has("name")) {
                                val.setVal(nameJson.getString("name"));
                            } else if (nameJson.has("val")) {
                                val.setVal(nameJson.getString("val"));
                            }
                        }
                        nameValList.add(val);
                    }
                    MyLog.i("key==="+key);
                    map.put(key,nameValList);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
