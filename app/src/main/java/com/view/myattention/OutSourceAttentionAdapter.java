package com.view.myattention;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.squareup.picasso.Picasso;
import com.test4s.myapp.R;
import com.test4s.net.Url;
import com.view.s4server.IssueDetailActivity;
import com.view.s4server.OutSourceActivity;
import com.view.s4server.OutSourceSimpleInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.List;

/**
 * Created by Administrator on 2016/3/21.
 */
public class OutSourceAttentionAdapter extends BaseAdapter {
    List<Object> list;
    Activity context;
    ListView listView;

    public OutSourceAttentionAdapter(Activity context, List<Object> list, ListView listView){
        this.context=context;
        this.list=list;
        this.listView=listView;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_cp_attentionlist,null);
            viewHolder=new ViewHolder();
            viewHolder.icon= (ImageView) convertView.findViewById(R.id.imageView_item_cpattention);
            viewHolder.name= (TextView) convertView.findViewById(R.id.name_item_cpattention);
            viewHolder.intro= (TextView) convertView.findViewById(R.id.introuduction_item_cpattention);
            viewHolder.uncare= (TextView) convertView.findViewById(R.id.cancel_care_cp);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        final OutSourceSimpleInfo osSimpleInfo= (OutSourceSimpleInfo) list.get(position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, OutSourceActivity.class);
                intent.putExtra("identity_cat","3");
                intent.putExtra("user_id",osSimpleInfo.getUser_id());
                context.startActivity(intent);
                context.overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });
        Picasso.with(context).load(Url.prePic+osSimpleInfo.getLogo())
                .into(viewHolder.icon);
        viewHolder.name.setText(osSimpleInfo.getCompany_name());
        viewHolder.uncare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
                AttentionChange.removeAttention2("3", osSimpleInfo.getUser_id(), new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        MyLog.i("adapter==="+result);
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            boolean su=jsonObject.getBoolean("success");
                            int code=jsonObject.getInt("code");
                            if (su&&code==200){
                                if (list.size()==0){
                                    listView.setVisibility(View.GONE);
                                    MyLog.i("parent===INVISIBLE=="+View.INVISIBLE);
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
        });
        viewHolder.intro.setText("公司规模 ："+osSimpleInfo.getCompany_scale()+"\n类    型 ："+osSimpleInfo.getOutsource_cat());
        return convertView;
    }
    class ViewHolder{
        ImageView icon;
        TextView name;
        TextView intro;
        TextView uncare;
    }
}
