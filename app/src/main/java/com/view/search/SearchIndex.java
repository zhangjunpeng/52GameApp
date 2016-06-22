package com.view.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.test4s.gdb.History;
import com.test4s.gdb.HistoryDao;
import com.test4s.myapp.MyApplication;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.myapp.BaseFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by Administrator on 2016/3/22.
 */
public class SearchIndex extends BaseFragment{

    private int[] rmid={R.id.rm_search1,R.id.rm_search2,R.id.rm_search3,
                        R.id.rm_search4,R.id.rm_search5,R.id.rm_search6,
                        R.id.rm_search7,R.id.rm_search8,R.id.rm_search9,
                        R.id.rm_search10,R.id.rm_search11,R.id.rm_search12};
    private List<TextView> rms;
    private List<String> rcstring;
    View view;

    private HistoryDao historyDao;

    private List<History> histories;

    private LinearLayout searchHistory;
    private ImageView deleHistory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        rms=new ArrayList<>();
        historyDao= MyApplication.daoSession.getHistoryDao();
        histories=gethistory();
        initData();
        super.onCreate(savedInstanceState);

    }

    private void initData() {
        BaseParams baseparams=new BaseParams("search/index");
        baseparams.addSign();
        x.http().post(baseparams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("search index==="+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean su=jsonObject.getBoolean("success");
                    int code=jsonObject.getInt("code");
                    if (su&&code==200){
                        JSONObject data=jsonObject.getJSONObject("data");
                        JSONArray hotWords=data.getJSONArray("hotWords");
                        rcstring=new ArrayList<String>();
                        for (int i=0;i<hotWords.length();i++){
                            JSONObject jsonObject2=hotWords.getJSONObject(i);
                            String word=jsonObject2.getString("word");
                            rcstring.add(word);
                        }
                    }
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
                initView();
            }
        });
    }

    private void initView() {
        for (int i=0;i<rcstring.size();i++){
            rms.get(i).setText(rcstring.get(i));
        }
        for (int i=0;i<histories.size();i++){
            final History history= histories.get(i);
            MyLog.i("history=="+history.getKeyword());
            View view=LayoutInflater.from(getActivity()).inflate(R.layout.item_searchhistory_search,null);
            TextView textview= (TextView) view.findViewById(R.id.name_item_searchhistory);
            textview.setText(history.getKeyword());
            searchHistory.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search(history.getKeyword());
                }
            });
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_search_index,null);

        for (int i=0;i<rmid.length;i++){
            TextView textView= (TextView) view.findViewById(rmid[i]);
            rms.add(textView);
        }
        searchHistory= (LinearLayout) view.findViewById(R.id.contianer_searchhistory);
        deleHistory= (ImageView) view.findViewById(R.id.delehistory_search);
        initListener();
        return view;
    }

    private void initListener() {
        View.OnClickListener clickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mess= (String) ((TextView) v).getText();
                search(mess);
                addHistory(mess);
            }
        };
        for (int i=0;i<rms.size();i++){
            rms.get(i).setOnClickListener(clickListener);
        }
        deleHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllHistory();
                searchHistory.removeAllViews();
            }
        });
    }

    public List<History> gethistory(){
        Query query = historyDao.queryBuilder()
                .build();
        List histories=query.list();
        return histories;
    }
    public void deleteAllHistory(){
        historyDao.deleteAll();
    }

    public void search(String message){
        Intent intent=new Intent(getActivity(),SearchEndActivity.class);
        intent.putExtra("keyword",message);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
    }
    public void addHistory(String message){
        Query query=historyDao.queryBuilder()
                .where(HistoryDao.Properties.Keyword.eq(message))
                .build();
        if (query.list().size()==0){
            History history=new History();
            history.setKeyword(message);
            historyDao.insert(history);
        }
        QueryBuilder.LOG_SQL=true;
        QueryBuilder.LOG_VALUES=true;

    }
}
