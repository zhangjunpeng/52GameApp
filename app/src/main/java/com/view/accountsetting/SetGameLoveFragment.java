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
public class SetGameLoveFragment extends BaseFragment {
    List<GameTypeInfo> gameTypeList;

    View view;
    ListView listView;
    UserInfo useInfo;


    String gametype_id;
    String gametype;
    String gametype_selected;

    private TextView save;
    private ImageView back;
    private TextView title;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        useInfo= MyAccount.getInstance().getUserInfo();
        gametype_selected=useInfo.getGame_like();
        initListData();
        super.onCreate(savedInstanceState);
    }

    private void initListData() {
        BaseParams baseParams=new BaseParams("user/gamelovelist");
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("joblist==="+result);
                try {
                    gameTypeList=new ArrayList<GameTypeInfo>();

                    JSONObject js=new JSONObject(result);
                    boolean issu=js.getBoolean("success");
                    int code=js.getInt("code");
                    if (issu&&code==200) {
                        JSONObject js1 = js.getJSONObject("data");
                        JSONArray jsonArray = js1.getJSONArray("gameloveList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject js2 = jsonArray.getJSONObject(i);
                            GameTypeInfo gameTypeInfo = new GameTypeInfo();
                            gameTypeInfo.setId(js2.getString("id"));
                            gameTypeInfo.setVal(js2.getString("val"));

                            gameTypeList.add(gameTypeInfo);
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
        listView.setAdapter(new MyAdapter(getActivity(),gameTypeList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GameTypeInfo gameTypeInfo=gameTypeList.get(position);
                gametype_id=gameTypeInfo.getId();
                gametype=gameTypeInfo.getVal();
                gametype_selected=gameTypeInfo.getVal();
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
        title= (TextView) view.findViewById(R.id.textView_titlebar_save);
        title.setText("游戏偏好");

        back= (ImageView) view.findViewById(R.id.back_savebar);
        save= (TextView) view.findViewById(R.id.save_savebar);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(gametype_id)||TextUtils.isEmpty(gametype)){
                    CusToast.showToast(getActivity(),"请选择偏好游戏类型",Toast.LENGTH_SHORT);

                    return;
                }
                changeGameLove();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAcountSettingFragment myAcountSettingFragment=new MyAcountSettingFragment();
                FragmentTransaction transaction= getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.in_form_left,R.anim.out_to_right);
                transaction.replace(R.id.contianner_mysetting,myAcountSettingFragment).commit();
            }
        });
        return view;
    }

    private void changeGameLove() {
        BaseParams baseParams=new BaseParams("user/chggamelove");
        baseParams.addParams("token",MyAccount.getInstance().getToken());
        baseParams.addParams("gamelove_id",gametype_id);
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
                        useInfo.setGame_like(gametype);

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

    class MyAdapter extends BaseAdapter {
        List<GameTypeInfo> list;
        Context context;
        MyAdapter(Context context,List<GameTypeInfo> list){
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
            GameTypeInfo gameTypeInfo=list.get(position);
            viewHolder.textView.setText(gameTypeInfo.getVal());
            if (!TextUtils.isEmpty(gametype_selected)){
                if (gametype_selected.equals(gameTypeInfo.getVal())){
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
