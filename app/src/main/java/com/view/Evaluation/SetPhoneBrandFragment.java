package com.view.Evaluation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.CusToast;
import com.app.tools.MyLog;
import com.test4s.account.MyAccount;
import com.test4s.account.UserInfo;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.myapp.BaseFragment;
import com.view.accountsetting.MyAcountSettingFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/28.
 */
public class SetPhoneBrandFragment extends BaseFragment {

    String gameid;

    String phone_id;
    String phone_name;
    String phone_id_selected;
    List<PhoneBrandInfo> phoneBrandlist;


    View view;
    ListView listView;
    UserInfo useInfo;
    private TextView save;
    private ImageView back;
    private TextView title;

    private MyAdapter adapter;


    private String tag="set";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if (bundle!=null){
            tag=bundle.getString("tag","set");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_selectcity,null);
        gameid=getArguments().getString("game_id","");
        phone_id_selected=getArguments().getString("phonebrand_id","");
        setImmerseLayout(view.findViewById(R.id.title_setjob));

        listView= (ListView) view.findViewById(R.id.citylist_setaddress);
        save= (TextView) view.findViewById(R.id.save_savebar);
        back= (ImageView) view.findViewById(R.id.back_savebar);
        title= (TextView) view.findViewById(R.id.textView_titlebar_save);

        title.setText("选择手机品牌");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (tag){
                    case "set":
                        MyAcountSettingFragment myAcountSettingFragment=new MyAcountSettingFragment();
                        FragmentTransaction transaction= getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.in_form_left,R.anim.out_to_right);
                        transaction.replace(R.id.contianner_mysetting,myAcountSettingFragment).commit();
                        break;
                    case "pc":
                        getActivity().finish();
                        break;
                }

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(phone_id)||TextUtils.isEmpty(phone_name)){
                    CusToast.showToast(getActivity(),"请选择职业",Toast.LENGTH_SHORT);

                    return;
                }
                changePhoneBrand();
            }
        });
        initData();
        return view;
    }

    private void changePhoneBrand() {
        BaseParams baseParams=new BaseParams("test/saveinfo");
        baseParams.addParams("token",MyAccount.getInstance().getToken());
        baseParams.addParams("game_id",gameid);
        baseParams.addParams("key","phone_brand_id");
        baseParams.addParams("val",phone_id);
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("phonebrand==="+result);
                try {
                    JSONObject json=new JSONObject(result);
                    boolean su=json.getBoolean("success");
                    int code=json.getInt("code");
                    if (su&&code==200){
                        CusToast.showToast(getActivity(),"修改成功",Toast.LENGTH_SHORT);

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

    private void initData() {
        BaseParams baseParams=new BaseParams("test/phonelist");
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("phonebrand==="+result);
                try {
                    JSONObject json=new JSONObject(result);
                    boolean su=json.getBoolean("success");
                    int code=json.getInt("code");
                    if (su&&code==200){
                        JSONObject data=json.getJSONObject("data");
                        JSONArray phone=data.getJSONArray("phoneList");
                        phoneBrandlist=new ArrayList<PhoneBrandInfo>();
                        for (int i=0;i<phone.length();i++){
                            JSONObject info=phone.getJSONObject(i);
                            PhoneBrandInfo phoneBrand=new PhoneBrandInfo();
                            phoneBrand.setId(info.getString("id"));
                            phoneBrand.setBrand_name(info.getString("brand_name"));
                            phoneBrandlist.add(phoneBrand);
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
        adapter=new MyAdapter(getActivity(),phoneBrandlist);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhoneBrandInfo info=phoneBrandlist.get(position);
                phone_id=info.getId();
                phone_name=info.getBrand_name();
                phone_id_selected=info.getId();
                for (int i=0;i<listView.getChildCount();i++){
                    View view1=listView.getChildAt(i);
                    MyAdapter.ViewHolder viewHolder= (MyAdapter.ViewHolder) view1.getTag();
                    viewHolder.imageView.setVisibility(View.INVISIBLE);
                }
                MyAdapter.ViewHolder viewHolder= (MyAdapter.ViewHolder) view.getTag();
                viewHolder.imageView.setVisibility(View.VISIBLE);
            }
        });
    }

    class MyAdapter extends BaseAdapter {
        List<PhoneBrandInfo> list;
        Context context;
        MyAdapter(Context context,List<PhoneBrandInfo> list){
            this.context=context;
            this.list=list;
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
                convertView=LayoutInflater.from(context).inflate(R.layout.item_listview_setaddress,null);
                viewHolder=new ViewHolder();
                viewHolder.textView= (TextView) convertView.findViewById(R.id.cityname_item_listview_setaddress);
                viewHolder.imageView= (ImageView) convertView.findViewById(R.id.selected_item_list_setadress);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            PhoneBrandInfo info=list.get(position);
            viewHolder.textView.setText(info.getBrand_name());
            if (!TextUtils.isEmpty(phone_id_selected)){
                if (phone_id_selected.equals(info.getId())) {
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
