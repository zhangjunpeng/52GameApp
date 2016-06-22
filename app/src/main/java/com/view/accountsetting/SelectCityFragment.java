package com.view.accountsetting;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
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
 * Created by Administrator on 2016/2/24.
 */
public class SelectCityFragment extends BaseFragment {
    List<CityInfo> cityList;
    String upid;

    View view;
    ListView listview_city;

    String province;
    String province_id;
    String city;
    String city_id;
    String county;
    String county_id;

    UserInfo userInfo;

    private ImageView back;
    private TextView title;
    private TextView save;

    //根据tag判断fragment的调用activity
    private String tag="set";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle=getArguments();
        if (bundle!=null){
            tag=bundle.getString("tag","pc");
        }

        userInfo= MyAccount.getInstance().getUserInfo();
        upid="0";
        initData(upid);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_selectcity,null);

        setImmerseLayout(view.findViewById(R.id.title_setjob));

        listview_city= (ListView) view.findViewById(R.id.citylist_setaddress);
        title= (TextView) view.findViewById(R.id.textView_titlebar_save);
        back= (ImageView) view.findViewById(R.id.back_savebar);
        save= (TextView) view.findViewById(R.id.save_savebar);

        save.setVisibility(View.INVISIBLE);

        title.setText("省 份");


        listview_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CityInfo cityInfo=cityList.get(position);
                switch (cityInfo.getLevel()){
                    case "1":
                        province=cityInfo.getName();
                        province_id=cityInfo.getId();
                        initData(cityInfo.getId());
                        title.setText("城 市");
                        break;
                    case "2":
                        city=cityInfo.getName();
                        city_id=cityInfo.getId();
                        initData(cityInfo.getId());
                        title.setText("地 区");
                        break;
                    case "3":
                        county=cityInfo.getName();
                        county_id=cityInfo.getId();
                        initData(cityInfo.getId());

                        Bundle bundle=new Bundle();
                        bundle.putString("province_id",province_id);
                        bundle.putString("city_id",city_id);
                        bundle.putString("county_id",county_id);
                        bundle.putString("province",province);
                        bundle.putString("city",city);
                        bundle.putString("county",county);
                        SetAddressFragment setAddressFragment=new SetAddressFragment();
                        setAddressFragment.setArguments(bundle);
                        FragmentTransaction transaction= getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.in_form_left,R.anim.out_to_right);
                        switch (tag){
                            case "set":
                                transaction.replace(R.id.contianner_mysetting,setAddressFragment).commit();
                                break;
                            case "pc":
                                bundle.putString("tag","pc");
                                bundle.putBoolean("setback",true);
                                setAddressFragment.setArguments(bundle);
                                transaction.replace(R.id.contianer_pcgame,setAddressFragment).commit();
                                break;
                        }


                        break;
                    case "4":
                        break;
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAddressFragment setAddressFragment=new SetAddressFragment();
                FragmentTransaction transaction= getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.in_form_left,R.anim.out_to_right);
                transaction.replace(R.id.contianner_mysetting,setAddressFragment).commit();
            }
        });

        return view;
    }




    private void initData(String id) {
        BaseParams baseParams=new BaseParams("api/district");
        baseParams.addParams("upid",id);
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("城市联动=="+result);
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean issu=jsonObject.getBoolean("success");
                    int code=jsonObject.getInt("code");
                    if (issu&&code==200){
                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                        JSONArray jsonArray=jsonObject1.getJSONArray("districtList");
                        cityList=new ArrayList<CityInfo>();
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject info=jsonArray.getJSONObject(i);
                            CityInfo cityInfo=new CityInfo();
                            cityInfo.setId(info.getString("id"));
                            cityInfo.setName(info.getString("name"));
                            cityInfo.setUpid(info.getString("upid"));
                            cityInfo.setLevel(info.getString("level"));
                            cityList.add(cityInfo);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                initListView();
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

    private void initListView() {
        MyAdapter myadapter=new MyAdapter(getActivity(),cityList);
        listview_city.setAdapter(myadapter);
    }
    class MyAdapter extends BaseAdapter {
        List<CityInfo> list;
        Context context;
        MyAdapter(Context context,List<CityInfo> list){
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
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            CityInfo cityinfo=list.get(position);
            viewHolder.textView.setText(cityinfo.getName());
            return convertView;
        }
        class ViewHolder{
            TextView textView;
        }
    }



}
