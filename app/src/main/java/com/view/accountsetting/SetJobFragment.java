package com.view.accountsetting;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
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
public class SetJobFragment extends BaseFragment {
    List<JobInfo> jobList;

    View view;
    ListView listView;
    UserInfo useInfo;

    String job_id;
    String job_name;
    String job_id_selected;

    private TextView save;
    private ImageView back;
    private TextView title;

    private MyAdapter adapter;

    private String tag="set";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        useInfo= MyAccount.getInstance().getUserInfo();
        if (useInfo!=null) {
            job_id_selected = useInfo.getJob_id();
        }
        Bundle bundle=getArguments();
        if (bundle!=null){
            tag=bundle.getString("tag","set");
        }

        initListData();
        super.onCreate(savedInstanceState);
    }

    private void initListData() {
        BaseParams baseParams=new BaseParams("user/joblist");
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("joblist==="+result);
                try {
                    jobList=new ArrayList<JobInfo>();

                    JSONObject js=new JSONObject(result);
                    boolean issu=js.getBoolean("success");
                    int code=js.getInt("code");
                    if (issu&&code==200) {
                        JSONObject js1 = js.getJSONObject("data");
                        JSONArray jsonArray = js1.getJSONArray("jobList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject js2 = jsonArray.getJSONObject(i);
                            JobInfo jobInfo = new JobInfo();
                            jobInfo.setId(js2.getString("id"));
                            jobInfo.setJob_name(js2.getString("job_name"));

                            jobList.add(jobInfo);
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
        adapter=new MyAdapter(getActivity(),jobList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JobInfo jobInfo=jobList.get(position);
                job_id=jobInfo.getId();
                job_name=jobInfo.getJob_name();
                job_id_selected=jobInfo.getId();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_selectcity,null);

        setImmerseLayout(view.findViewById(R.id.title_setjob));

        listView= (ListView) view.findViewById(R.id.citylist_setaddress);
        save= (TextView) view.findViewById(R.id.save_savebar);
        back= (ImageView) view.findViewById(R.id.back_savebar);
        title= (TextView) view.findViewById(R.id.textView_titlebar_save);

        title.setText("职 业");

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
                if (TextUtils.isEmpty(job_id)||TextUtils.isEmpty(job_name)){
                    CusToast.showToast(getActivity(),"请选择职业",Toast.LENGTH_SHORT);

                    return;
                }
                changeEdu();
            }
        });
        return view;
    }

    private void changeEdu() {
        BaseParams baseParams=new BaseParams("user/chgjob");
        baseParams.addParams("token",MyAccount.getInstance().getToken());
        baseParams.addParams("job_id",job_id);
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
                        useInfo.setJob_id(job_id);
                        useInfo.setJob_name(job_name);
                        FragmentTransaction transaction= getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.in_form_left,R.anim.out_to_right);
                        FragmentManager manager=getFragmentManager();
                        switch (tag){
                            case "set":

                                MyAcountSettingFragment myAcountSettingFragment=new MyAcountSettingFragment();

                                transaction.replace(R.id.contianner_mysetting,myAcountSettingFragment).commit();
                                break;
                            case "pc":
                                getActivity().setResult(Activity.RESULT_OK);
                                getActivity().finish();
                                break;
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

            }
        });
    }

    class MyAdapter extends BaseAdapter {
        List<JobInfo> list;
        Context context;
        MyAdapter(Context context,List<JobInfo> list){
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
            JobInfo jobInfo=list.get(position);
            viewHolder.textView.setText(jobInfo.getJob_name());
            if (!TextUtils.isEmpty(job_id_selected)){
                if (job_id_selected.equals(jobInfo.getId())) {
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
