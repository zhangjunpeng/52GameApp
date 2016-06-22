package com.view.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.test4s.myapp.R;
import com.view.game.GameDetailActivity;
import com.view.s4server.CPDetailActivity;
import com.view.s4server.IPDetailActivity;
import com.view.s4server.InvesmentDetialActivity;
import com.view.s4server.IssueDetailActivity;
import com.view.s4server.OutSourceActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/23.
 */
public class SearchEndFragment extends Fragment {

    private ListView listView;

    private String keyword;
    private String identity_cat;
    private List<Object> datalist;
    private SearchEndActivity activity;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        keyword=getArguments().getString("keyword","");
        MyLog.i("keyword");
        identity_cat=getArguments().getString("identity_cat","1");
        activity= (SearchEndActivity) getActivity();
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_search_end,container,false);
        listView= (ListView) view.findViewById(R.id.listview_searchend);
        initData();
        return view;
    }
    private void initData() {
        Search.search(activity.keyword, identity_cat, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("search back"+result);
                josnParser(result);
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

    private void josnParser(String result) {
        try {
            JSONObject json=new JSONObject(result);
            JSONObject data=json.getJSONObject("data");

            JSONArray searchData=data.getJSONArray("searchData");
            datalist=new ArrayList<>();
            if (identity_cat.equals("1")||identity_cat.equals("5")){
                for (int i=0;i<searchData.length();i++){
                    SearchIPInfo ipinfo=new SearchIPInfo();
                    JSONObject info=searchData.getJSONObject(i);
                    ipinfo.setId(info.getString("id"));
                    ipinfo.setName(info.getString("name"));
                    datalist.add(ipinfo);
                }
            }else {
                for (int i=0;i<searchData.length();i++){
                    SearchDataInfo datainfo=new SearchDataInfo();
                    JSONObject info=searchData.getJSONObject(i);
                    datainfo.setUser_id(info.getString("user_id"));
                    datainfo.setIdentity_cat(info.getString("identity_cat"));
                    datainfo.setName(info.getString("name"));
                    datalist.add(datainfo);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        initView();
    }

    private void initView() {
        if (datalist.size()==0){
            listView.setVisibility(View.GONE);
        }else {
            listView.setAdapter(new MyListAdapter(getActivity(), datalist));
        }
    }

    class MyListAdapter extends BaseAdapter{
        Context context;
        List<Object> list;


        public MyListAdapter(Context context,List<Object> dataList){
            list=dataList;
            this.context=context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView==null){
                convertView=LayoutInflater.from(context).inflate(R.layout.item_listview_search_end,parent,false);
                viewHolder=new ViewHolder();
                viewHolder.name= (TextView) convertView.findViewById(R.id.name_listview_searchend);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            String id_s="";
            String iden_cat="";
            if (identity_cat.equals("1")||identity_cat.equals("5")){
                SearchIPInfo ipInfo= (SearchIPInfo) list.get(position);
                viewHolder.name.setText(ipInfo.getName());
                id_s=ipInfo.getId();
            }else {
                SearchDataInfo dataInfo= (SearchDataInfo) list.get(position);
                viewHolder.name.setText(dataInfo.getName());
                id_s=dataInfo.getUser_id();
                iden_cat=dataInfo.getIdentity_cat();
            }
            final String finalId_s = id_s;
            final String finalIden_cat = iden_cat;
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=null;
                    switch (identity_cat){
                        case "1":
                            intent=new Intent(getActivity(), GameDetailActivity.class);
                            intent.putExtra("game_id", finalId_s);
                            break;
                        case "2":
                            intent=new Intent(getActivity(), CPDetailActivity.class);
                            intent.putExtra("user_id", finalId_s);
                            intent.putExtra("identity_cat", finalIden_cat);
                            break;
                        case "3":
                            intent=new Intent(getActivity(), OutSourceActivity.class);
                            intent.putExtra("user_id", finalId_s);
                            intent.putExtra("identity_cat", finalIden_cat);
                            break;
                        case "4":
                            intent=new Intent(getActivity(), InvesmentDetialActivity.class);
                            intent.putExtra("user_id", finalId_s);
                            intent.putExtra("identity_cat", finalIden_cat);
                            break;
                        case "5":
                            intent=new Intent(getActivity(), IPDetailActivity.class);
                            intent.putExtra("id", finalId_s);
                            break;
                        case "6":
                            intent=new Intent(getActivity(), IssueDetailActivity.class);
                            intent.putExtra("user_id", finalId_s);
                            intent.putExtra("identity_cat", finalIden_cat);
                            break;
                    }
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                }
            });
            return convertView;
        }
        class ViewHolder{
            TextView name;
        }
    }

}
