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
import com.view.s4server.CPDetailActivity;
import com.view.s4server.InvesmentDetialActivity;
import com.view.s4server.InvesmentSimpleInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.List;

/**
 * Created by Administrator on 2016/3/21.
 */
public class InvestAttentionAdapter extends BaseAdapter {
    List<Object> list;
    Activity context;
    ListView listView;

    public InvestAttentionAdapter(Activity context, List<Object> list, ListView listView){
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
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
        final InvesmentSimpleInfo invesmentSimpleInfo= (InvesmentSimpleInfo) list.get(position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, InvesmentDetialActivity.class);
                intent.putExtra("identity_cat","4");
                intent.putExtra("user_id",invesmentSimpleInfo.getUser_id());
                context.startActivity(intent);
                context.overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });
        Picasso.with(context)
                .load(Url.prePic+invesmentSimpleInfo.getLogo())
                .placeholder(R.drawable.default_icon)
                .into(viewHolder.icon);
        viewHolder.name.setText(invesmentSimpleInfo.getCompany_name());
        viewHolder.intro.setText("机构类型："+invesmentSimpleInfo.getInvest_cat()+"\n投资阶段："+invesmentSimpleInfo.getInvest_stage());
        viewHolder.uncare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
                AttentionChange.removeAttention2("4", invesmentSimpleInfo.getUser_id(), new Callback.CommonCallback<String>() {
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
        return convertView;
    }
    class ViewHolder{
        ImageView icon;
        TextView name;
        TextView intro;
        TextView uncare;
    }
}
