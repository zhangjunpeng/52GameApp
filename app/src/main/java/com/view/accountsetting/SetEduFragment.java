package com.view.accountsetting;

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
import com.test4s.myapp.BaseFragment;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/22.
 */
public class SetEduFragment extends BaseFragment implements View.OnClickListener{
    List<EduInfo> eduList;

    View view;
    ListView listView;
    UserInfo useInfo;


    String edu_id;
    String edu_name;
    String edu_id_selected;

    private TextView save;
    private ImageView back;
    private TextView title;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        useInfo= MyAccount.getInstance().getUserInfo();
        edu_id_selected=useInfo.getEdu_id();
        initListData();
        super.onCreate(savedInstanceState);
    }

    private void initListData() {
        BaseParams baseParams=new BaseParams("user/edulist");
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("edulist==="+result);
                try {
                    eduList=new ArrayList<EduInfo>();

                    JSONObject js=new JSONObject(result);
                    boolean issu=js.getBoolean("success");
                    int code=js.getInt("code");
                    if (issu&&code==200) {
                        JSONObject js1 = js.getJSONObject("data");
                        JSONArray jsonArray = js1.getJSONArray("eduList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject js2 = jsonArray.getJSONObject(i);
                            EduInfo eduInfo = new EduInfo();
                            eduInfo.setId(js2.getString("id"));
                            eduInfo.setEdu_name(js2.getString("edu_name"));

                            eduList.add(eduInfo);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initListview();
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

    private void initListview() {
        listView.setAdapter(new MyAdapter(getActivity(),eduList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EduInfo eduInfo=eduList.get(position);
                edu_id=eduInfo.getId();
                edu_name=eduInfo.getEdu_name();
                edu_id_selected=eduInfo.getId();
                listView.setAdapter(new MyAdapter(getActivity(),eduList));
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_selectcity,null);

        setImmerseLayout(view.findViewById(R.id.title_setjob));

        listView= (ListView) view.findViewById(R.id.citylist_setaddress);
        save= (TextView) view.findViewById(R.id.save_savebar);
        title= (TextView) view.findViewById(R.id.textView_titlebar_save);
        back= (ImageView) view.findViewById(R.id.back_savebar);

        title.setText("学 历");

        save.setOnClickListener(this);
        back.setOnClickListener(this);
        return view;
    }

    private void changeEdu() {
        BaseParams baseParams=new BaseParams("user/chgedu");
        baseParams.addParams("token",MyAccount.getInstance().getToken());
        baseParams.addParams("edu_id",edu_id);
        baseParams.addSign();

        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("changeEdu==="+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean su=jsonObject.getBoolean("success");
                    int code=jsonObject.getInt("code");
                    if (su&&code==200){
                        useInfo.setEdu_id(edu_id);
                        useInfo.setEdu_name(edu_name);
                        MyAcountSettingFragment myAcountSettingFragment=new MyAcountSettingFragment();
                        FragmentTransaction transaction= getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.in_form_left,R.anim.out_to_right);
                        transaction.replace(R.id.contianner_mysetting,myAcountSettingFragment).commit();

                        CusToast.showToast(getActivity(),"修改成功",Toast.LENGTH_SHORT);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_savebar:
                if (TextUtils.isEmpty(edu_id)||TextUtils.isEmpty(edu_name)){

                    CusToast.showToast(getActivity(),"请选择学历",Toast.LENGTH_SHORT);
                    return;
                }
                changeEdu();
                break;
            case R.id.back_savebar:
                MyAcountSettingFragment myAcountSettingFragment=new MyAcountSettingFragment();
                FragmentTransaction transaction= getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.in_form_left,R.anim.out_to_right);
                transaction.replace(R.id.contianner_mysetting,myAcountSettingFragment).commit();
                break;
        }

    }

    class MyAdapter extends BaseAdapter {
        List<EduInfo> list;
        Context context;
        MyAdapter(Context context,List<EduInfo> list){
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
            EduInfo eduInfo=list.get(position);
            viewHolder.textView.setText(eduInfo.getEdu_name());

            if (!TextUtils.isEmpty(edu_id_selected)){
                if (edu_id_selected.equals(eduInfo.getId())){
                    viewHolder.imageView.setVisibility(View.VISIBLE);
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
