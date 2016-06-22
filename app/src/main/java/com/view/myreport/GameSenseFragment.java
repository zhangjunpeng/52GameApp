package com.view.myreport;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/30.
 */
public class GameSenseFragment extends Fragment {

    View view;

    String gameid;

    private int[] textid={R.id.text_gamese1,R.id.text_gamese2,R.id.text_gamese3,R.id.text_gamese4,R.id.text_gamese5,R.id.text_gamese6};

    List<TextView> textViewList;
    List<String> tips;
    List<QuesPCInfo> dataList;
    Map<String,List<QuesPCInfo>> gameSenseInfo;

    LinearLayout contianer_gamesense;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameid=getArguments().getString("game_id","");


    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_game_sense,container,false);
        textViewList=new ArrayList<>();
        contianer_gamesense= (LinearLayout) view.findViewById(R.id.contianer_fragment_sense);
        for (int i=0;i<textid.length;i++){
            TextView textView= (TextView) view.findViewById(textid[i]);
            textViewList.add(textView);
        }
        initData();

        return view;
    }
    private void initData() {
        BaseParams baseparams=new BaseParams("test/sensetest");
        baseparams.addParams("game_id",gameid);
        baseparams.addSign();
        x.http().post(baseparams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("game sense back=="+result);
                try {
                    jsonparser(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                initTextView();
            }
        });
    }
    private void initTextView() {
        for (int i=0;i<tips.size();i++){
            TextView text=textViewList.get(i);
            text.setText(tips.get(i));
            final int j=i;
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeTextView(j);
                }
            });
        }
        changeTextView(0);
    }
    private void changeTextView(int position) {
        for (int i=0;i<tips.size();i++){
            TextView textView=textViewList.get(i);
            if (i==position){
                textView.setSelected(true);
                textView.setTextColor(Color.WHITE);
            }else {
                textView.setSelected(false);
                textView.setTextColor(Color.rgb(76,76,76));
            }
        }
        changePic(position);
    }

    private void changePic(int position) {
        contianer_gamesense.removeAllViews();
        dataList=gameSenseInfo.get(tips.get(position));

        for (int i=0;i<dataList.size();i++){
            View continer=LayoutInflater.from(getActivity()).inflate(R.layout.item_gamesense_pc,contianer_gamesense,false);
            QuesPCInfo ques=dataList.get(i);
            TextView name= (TextView) continer.findViewById(R.id.quses_name_gamesense);
            name.setText(ques.getName());
            LinearLayout scoreline= (LinearLayout) continer.findViewById(R.id.linear_star);
            for (int j=0;j<scoreline.getChildCount();j++){
                View iamgeview=scoreline.getChildAt(i);
                LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) iamgeview.getLayoutParams();
                float num=Float.parseFloat(ques.getList().get(j).get("num"));
                params.weight=num;
            }
            contianer_gamesense.addView(continer);
        }
    }

    private void jsonparser(String result) throws JSONException {
        JSONObject res=new JSONObject(result);
        boolean su=res.getBoolean("success");
        int code=res.getInt("code");
        if (su&&code==200){
            JSONArray data=res.getJSONArray("data");
            tips=new ArrayList<>();
            gameSenseInfo=new HashMap<>();
            for (int i=0;i<data.length();i++){
                JSONObject object=data.getJSONObject(i);
                String na=object.getString("name");
                tips.add(na);
                JSONObject val=object.getJSONObject("val");
                Iterator<String> itera=val.keys();
                List<QuesPCInfo> quesList=new ArrayList<>();
                while (itera.hasNext()){
                    String key=itera.next();
                    JSONObject qu=val.getJSONObject(key);
                    QuesPCInfo quesInfo=new QuesPCInfo();
                    quesInfo.setName(qu.getString("name"));
                    JSONObject num=qu.getJSONObject("num");
                    Iterator<String> it=num.keys();
                    List<Map<String,String>> ques=new ArrayList<>();
                    while (it.hasNext()){
                        String keynum=it.next();
                        JSONObject numobj=num.getJSONObject(keynum);
                        Map<String,String> map=new HashMap<>();
                        map.put("name",numobj.getString("name"));
                        map.put("num",numobj.getString("num"));
                        ques.add(map);
                    }
                    quesInfo.setList(ques);
                    quesList.add(quesInfo);
                }
                gameSenseInfo.put(na,quesList);
            }
        }
    }
}
