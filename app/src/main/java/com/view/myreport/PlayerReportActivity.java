package com.view.myreport;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.test4s.account.MyAccount;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.view.activity.BaseActivity;

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
import java.util.TreeMap;

/*
* 玩家评测报告
* */
public class PlayerReportActivity extends BaseActivity {
    ViewPager viewPager;
    List<Fragment> fragmentList;
    String gameid;

    String[] gameinfoMapkeys={"starts","sense","interactive","play","performance"};
    Map<String,List<Map<String,String>>> playerInfo;
    Map<String,GamePCInfo> gameInfoMap;
    List<List<QuesPCInfo>> questionList;
    List<String> questiontype;
    MyViewPagerAdapter adapter;

    ImageView back;
    TextView title;
    TextView save;

    ImageView button_right;
    int p=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_report);

        gameid=getIntent().getStringExtra("game_id");
        viewPager= (ViewPager) findViewById(R.id.viewpager_player_report);
        fragmentList=new ArrayList<>();
        PlayerInfoFragment playerInfoFragment=new PlayerInfoFragment();
        GamePCInfoFragment gamePCInfoFragment=new GamePCInfoFragment();
        GameSenseFragment gameSenseFragment=new GameSenseFragment();
        PlayerAdviseFragment playerAdviseFragment=new PlayerAdviseFragment();
        Bundle bundle=new Bundle();
        bundle.putString("game_id",gameid);
        playerInfoFragment.setArguments(bundle);
        gamePCInfoFragment.setArguments(bundle);
        gameSenseFragment.setArguments(bundle);
        playerAdviseFragment.setArguments(bundle);
        fragmentList.add(playerInfoFragment);
        fragmentList.add(gamePCInfoFragment);
        fragmentList.add(gameSenseFragment);
        fragmentList.add(playerAdviseFragment);
//        adapter.notifyDataSetChanged();
        adapter=new MyViewPagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(adapter);
        back= (ImageView) findViewById(R.id.back_savebar);
        title= (TextView) findViewById(R.id.textView_titlebar_save);
        save= (TextView) findViewById(R.id.save_savebar);

        title.setText("玩家测评报告");
        save.setVisibility(View.INVISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        button_right= (ImageView) findViewById(R.id.button_right_report);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                p=position;
                if (p==fragmentList.size()-1){
                    button_right.setVisibility(View.GONE);
                }else {
                    button_right.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        button_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p++;
                viewPager.setCurrentItem(p);

            }
        });


//        initData();



    }

    private void initData() {
        final BaseParams baseParams=new BaseParams("test/reportshow");
        baseParams.addParams("game_id",gameid);
        baseParams.addParams("token", MyAccount.getInstance().getToken());
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            String res;
            @Override
            public void onSuccess(String result) {
                MyLog.i("player report==="+result);
                res=result;
//                jsonPaser(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                MyLog.i("解析完成");

                GamePCInfoFragment gamePCInfoFragment=new GamePCInfoFragment();
                Bundle bundle=new Bundle();
                bundle.putString("data",res);
                gamePCInfoFragment.setArguments(bundle);
                fragmentList.add(gamePCInfoFragment);
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void jsonPaser(String result) {
        try {
            JSONObject json=new JSONObject(result);
            boolean su=json.getBoolean("success");
            int code=json.getInt("code");
            if (su&&code==200){
                JSONObject data=json.getJSONObject("data");
                JSONArray sexarray=data.getJSONArray("sex");
                playerInfo=new HashMap<>();
                List<Map<String,String>> sexlist=new ArrayList<>();
                for (int i=0;i<sexarray.length();i++){
                    JSONObject sexobject=sexarray.getJSONObject(i);
                    Map<String,String> map=new HashMap<>();
                    map.put("name",sexobject.getString("name"));
                    map.put("num",sexobject.getString("num"));
                    sexlist.add(map);
                }
                playerInfo.put("sex",sexlist);
                JSONArray agearray=data.getJSONArray("age");
                List<Map<String,String>> agelist=new ArrayList<>();
                for (int i=0;i<agearray.length();i++){
                    JSONObject object=agearray.getJSONObject(i);
                    Map<String,String> map=new HashMap<>();
                    map.put("name",object.getString("name"));
                    map.put("num",object.getString("num"));
                    agelist.add(map);
                }
                playerInfo.put("age",agelist);

                JSONArray jobarray=data.getJSONArray("job");
                List<Map<String,String>> joblist=new ArrayList<>();
                for (int i=0;i<jobarray.length();i++){
                    JSONObject object=jobarray.getJSONObject(i);
                    Map<String,String> map=new HashMap<>();
                    map.put("name",object.getString("name"));
                    map.put("num",object.getString("num"));
                    joblist.add(map);
                }
                playerInfo.put("job",joblist);

                JSONArray provincearray=data.getJSONArray("province");
                List<Map<String,String>> provincelist=new ArrayList<>();
                for (int i=0;i<provincearray.length();i++){
                    JSONObject object=provincearray.getJSONObject(i);
                    Map<String,String> map=new HashMap<>();
                    map.put("name",object.getString("name"));
                    map.put("num",object.getString("num"));
                    provincelist.add(map);
                }
                playerInfo.put("province",provincelist);

                JSONArray phonearray=data.getJSONArray("phone");
                List<Map<String,String>> phonelist=new ArrayList<>();
                for (int i=0;i<phonearray.length();i++){
                    JSONObject object=phonearray.getJSONObject(i);
                    Map<String,String> map=new HashMap<>();
                    map.put("name",object.getString("name"));
                    map.put("num",object.getString("num"));
                    phonelist.add(map);
                }
                playerInfo.put("phone",phonelist);

                gameInfoMap=new HashMap<>();
                for (int i=0;i<gameinfoMapkeys.length;i++){
                    gameInfoMap.put(gameinfoMapkeys[i],getGamePcInfo(data,gameinfoMapkeys[i]));
                }
                getQuesPcList(data);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getQuesPcList(JSONObject data) throws JSONException {
        questionList=new ArrayList<>();
        questiontype=new ArrayList<>();
        JSONArray main=data.getJSONArray("main");
        for (int i=0;i<main.length();i++){
            List<QuesPCInfo> infoList=new ArrayList<>();
            JSONObject object=main.getJSONObject(i);
            questiontype.add(object.getString("name"));
            JSONObject val=object.getJSONObject("val");
            Iterator<String> itera=val.keys();
            while (itera.hasNext()){
                QuesPCInfo quespcInfo=new QuesPCInfo();
                String key1=itera.next();
                JSONObject quesjson=val.getJSONObject(key1);

                quespcInfo.setName(quesjson.getString("name"));
                JSONObject numobject=quesjson.getJSONObject("num");
                Iterator<String> itera_num=numobject.keys();
                List<Map<String,String>> list=new ArrayList<>();
                while (itera_num.hasNext()){
                    String key_num=itera_num.next();
                    JSONObject object_num=numobject.getJSONObject(key_num);
                    Map<String,String> map=new HashMap<>();
                    map.put("name",object_num.getString("name"));
                    map.put("num",object_num.getString("num"));
                    list.add(map);
                }
                quespcInfo.setList(list);
                infoList.add(quespcInfo);
            }
            questionList.add(infoList);
        }


    }

    private GamePCInfo getGamePcInfo(JSONObject data, String name) {
        GamePCInfo info=new GamePCInfo();
        try {
            JSONObject object=data.getJSONObject(name);
            info.setName(object.getString("name"));
            info.setScore(object.getString("score"));
            JSONObject num=object.getJSONObject("num");
            List<Map<String,String>> list=new ArrayList<>();
            Iterator<String> itera=num.keys();
            while (itera.hasNext()){
                String key=itera.next();
                JSONObject numobject=num.getJSONObject(key);
                Map<String,String> map=new HashMap<>();
                map.put("name",numobject.getString("name"));
                map.put("num",numobject.getString("num"));
                list.add(map);
            }
            info.setNum(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return info;
    }

    class MyViewPagerAdapter extends FragmentPagerAdapter{
        private List<Fragment> list;
        public MyViewPagerAdapter(FragmentManager fm,List<Fragment> list) {
            super(fm);
            this.list=list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

}
