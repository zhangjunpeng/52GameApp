package com.view.Identification;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

/**
 * Created by Administrator on 2016/6/30.
 */
public class OSIdenFragment extends Fragment {
    private List<Map<String,String>> outsorcestyleList;

    private List<TextView> textViewList;
    private LinearLayout line1;
    private LinearLayout line2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_ident_wb,null);

        line1= (LinearLayout) view.findViewById(R.id.linear1_os);
        line2= (LinearLayout) view.findViewById(R.id.linear2_os);

        initData();


        textViewList=new ArrayList<>();
        for (int i=0;i<line1.getChildCount();i++){
            TextView textView= (TextView) line1.getChildAt(i);
            textViewList.add(textView);

        }
        for (int i=0;i<line2.getChildCount();i++){
            TextView textView= (TextView) line2.getChildAt(i);
            textViewList.add(textView);
        }

        return view;
    }
    private void initData() {
        BaseParams params=new BaseParams("identity/identityselect");
        params.addParams("type","outsource");
        params.addSign();
        x.http().post(params.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("列表 result==="+result);
                parser(result);
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
                initTextViewListener();
            }
        });
    }

    private void initTextViewListener() {
        for (int i=0;i<textViewList.size();i++){
            TextView textView=textViewList.get(i);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView view= (TextView) v;
                    if (view.isSelected()){
                        view.setTextColor(Color.rgb(124,124,124));
                    }else {
                        view.setTextColor(Color.WHITE);
                    }
                    view.setSelected(!view.isSelected());
                }
            });
        }
    }

    private void initView() {
        for (int i=0;i<textViewList.size();i++){
            if (i>=outsorcestyleList.size()){
                textViewList.get(i).setVisibility(View.INVISIBLE);
            }else {
                Map<String,String> map=outsorcestyleList.get(i);
                textViewList.get(i).setText(map.get("val"));
            }
        }
    }


    private void parser(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            boolean su = jsonObject.getBoolean("success");
            int code = jsonObject.getInt("code");
            if (su && code == 200) {
                JSONObject data = jsonObject.getJSONObject("data");
                outsorcestyleList=new ArrayList<>();
                JSONArray ostyleArr=data.getJSONArray("selectList");
                for (int i=0;i<ostyleArr.length();i++){
                    Map<String,String> map=new HashMap<>();
                    JSONObject object=ostyleArr.getJSONObject(i);
                    if (object.getString("id").equals("0")){
                        continue;
                    }
                    map.put("id",object.getString("id"));
                    map.put("val",object.getString("val"));
                    outsorcestyleList.add(map);
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
