package com.view.Identification;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by Administrator on 2016/6/30.
 */
public class InvIdenFragment extends Fragment {

    private List<NameVal> investcatList;//投资类型
    private List<NameVal> coompstageList;//投资阶段

    private TextView invescat_text;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_ident_tz,null);

//        initData();
        return view;
    }
    private void initData() {
        BaseParams params=new BaseParams("identity/identityselect");
        params.addParams("type","stage");
        params.addSign();
        x.http().post(params.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("列表 result==="+result);
                parser1(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                initView();
//                initTextViewListener();
            }
        });
        BaseParams params2=new BaseParams("identity/identityselect");
        params2.addParams("type","investcat");
        params2.addSign();
        x.http().post(params2.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("列表 result==="+result);
                parser2(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                initView();
            }
        });
    }

    private void initView() {
        if (coompstageList==null||investcatList==null){
            return;
        }

    }
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void upView(List<NameVal> dataInfo,String type){
        switch (type){
            case "investcat":
                break;
            case "coompstage":
                break;
        }
        initList(dataInfo);
    }

    private void initList(List<NameVal> dataInfo) {

    }

    private void parser1(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            boolean su = jsonObject.getBoolean("success");
            int code = jsonObject.getInt("code");
            if (su && code == 200) {
                JSONObject data = jsonObject.getJSONObject("data");
                coompstageList=new ArrayList<>();
                JSONArray ostyleArr=data.getJSONArray("selectList");
                for (int i=0;i<ostyleArr.length();i++){
//                    Map<String,String> map=new HashMap<>();
                    NameVal nameVal=new NameVal();
                    JSONObject object=ostyleArr.getJSONObject(i);
                    if (object.getString("id").equals("0")){
                        continue;
                    }
                    nameVal.setId(object.getString("id"));
                    nameVal.setVal(object.getString("val"));
//                    map.put("id",);
//                    map.put("val",);
                    coompstageList.add(nameVal);
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void parser2(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            boolean su = jsonObject.getBoolean("success");
            int code = jsonObject.getInt("code");
            if (su && code == 200) {
                JSONObject data = jsonObject.getJSONObject("data");
                investcatList=new ArrayList<>();
                JSONArray ostyleArr=data.getJSONArray("selectList");
                for (int i=0;i<ostyleArr.length();i++){
//                    Map<String,String> map=new HashMap<>();
                    NameVal nameVal=new NameVal();
                    JSONObject object=ostyleArr.getJSONObject(i);
                    if (object.getString("id").equals("0")){
                        continue;
                    }
                    nameVal.setId(object.getString("id"));
                    nameVal.setVal(object.getString("val"));
//                    map.put("id",);
//                    map.put("val",);
                    investcatList.add(nameVal);
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
