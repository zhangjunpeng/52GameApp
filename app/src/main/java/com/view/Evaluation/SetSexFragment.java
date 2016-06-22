package com.view.Evaluation;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.test4s.account.MyAccount;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.myapp.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/28.
 */
public class SetSexFragment extends BaseFragment{
    String sex;
    String gameid;
    View view;
    ListView listview;
    private TextView save;
    private ImageView back;
    private TextView title;
    List<String> stringlist;
    private String sexselect;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        sex=getArguments().getString("sex","0");
        gameid=getArguments().getString("game_id","");
        stringlist=new ArrayList<>();
        stringlist.add("男");
        stringlist.add("女");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_selectcity,container,false);
        listview= (ListView) view.findViewById(R.id.citylist_setaddress);
        save= (TextView) view.findViewById(R.id.save_savebar);
        back= (ImageView) view.findViewById(R.id.back_savebar);
        title= (TextView) view.findViewById(R.id.textView_titlebar_save);

        title.setText("设置性别");

        initView();
        initListener();
        return view;
    }

    private void initListener() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i=0;i<listview.getChildCount();i++){
                    View view1=listview.getChildAt(i);
                    MyAdapter.ViewHolder viewHolder= (MyAdapter.ViewHolder) view1.getTag();
                    viewHolder.imageView.setVisibility(View.INVISIBLE);
                }
                MyAdapter.ViewHolder viewHolder= (MyAdapter.ViewHolder) view.getTag();
                viewHolder.imageView.setVisibility(View.VISIBLE);
                sexselect=(id+1)+"";
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
            }
        });
    }

    private void saveInfo() {
        BaseParams params=new BaseParams("test/saveinfo");
        params.addParams("token", MyAccount.getInstance().getToken());
        params.addParams("game_id",gameid);
        params.addParams("key","sex");
        params.addParams("val",sexselect);
        x.http().post(params.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("saveInfo=="+result);
                try {
                    JSONObject res=new JSONObject(result);
                    boolean su=res.getBoolean("success");
                    int code=res.getInt("code");
                    if (su&&code==200){
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
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

            }
        });
    }

    private void initView() {
        listview.setAdapter(new MyAdapter());
    }
    class MyAdapter extends BaseAdapter {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public Object getItem(int position) {
                return stringlist.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder;
                if (convertView==null){
                    convertView=LayoutInflater.from(getActivity()).inflate(R.layout.item_listview_setaddress,null);
                    viewHolder=new ViewHolder();
                    viewHolder.textView= (TextView) convertView.findViewById(R.id.cityname_item_listview_setaddress);
                    viewHolder.imageView= (ImageView) convertView.findViewById(R.id.selected_item_list_setadress);
                    convertView.setTag(viewHolder);
                }else {
                    viewHolder= (ViewHolder) convertView.getTag();
                }
                viewHolder.textView.setText(stringlist.get(position));
                if (!"0".equals(sex)){
                    if (Integer.parseInt(sex)-1==position){
                        viewHolder.imageView.setVisibility(View.VISIBLE);
                    }else {
                        viewHolder.imageView.setVisibility(View.INVISIBLE);
                    }
                }
                return convertView;
            }
            class ViewHolder{
                TextView textView;
                ImageView imageView;
            }
    }
}
