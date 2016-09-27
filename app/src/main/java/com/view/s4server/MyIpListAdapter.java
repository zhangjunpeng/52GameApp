package com.view.s4server;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tools.MyDisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.test4s.account.AccountActivity;
import com.test4s.account.MyAccount;
import com.test4s.myapp.R;
import com.test4s.net.Url;
import com.view.myattention.AttentionChange;

import java.util.List;

public class MyIpListAdapter extends BaseAdapter {
        ImageLoader imageLoader=ImageLoader.getInstance();
        List<IPSimpleInfo> list;
        Activity context;

        public MyIpListAdapter(Activity context, List<IPSimpleInfo> list){
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
            final ViewHolder viewHolder;
            if (convertView==null){
                convertView= LayoutInflater.from(context).inflate(R.layout.item_iplistfragment,null);
                viewHolder=new ViewHolder();
                viewHolder.icon= (ImageView) convertView.findViewById(R.id.imageView_iplist);
                viewHolder.name= (TextView) convertView.findViewById(R.id.name_item_iplist);
                viewHolder.intro= (TextView) convertView.findViewById(R.id.introuduction_item_iplist);
                viewHolder.care= (TextView) convertView.findViewById(R.id.care_item_list);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }

            final IPSimpleInfo ipSimpleInfo=list.get(position);
//
//            Picasso.with(context).load(Url.prePic+ipSimpleInfo.getLogo())
//                    .into(viewHolder.icon);

            imageLoader.displayImage(Url.prePic+ipSimpleInfo.getLogo(),viewHolder.icon, MyDisplayImageOptions.getroundImageOptions());

            viewHolder.name.setText(ipSimpleInfo.getIp_name());
            viewHolder.intro.setText("类        型 ："+ipSimpleInfo.getIp_cat()+"\n风        格 ："+ipSimpleInfo.getIp_style()+"\n授权范围 ："+ipSimpleInfo.getUthority());

            viewHolder.care.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MyAccount.isLogin) {
                        if (ipSimpleInfo.iscare()) {
                            ipSimpleInfo.setIscare(false);
                            AttentionChange.removeAttention("5", ipSimpleInfo.getId(), context);
                        } else {
                            ipSimpleInfo.setIscare(true);
                            AttentionChange.addAttention("5", ipSimpleInfo.getId(), context);
                        }
                        if (ipSimpleInfo.iscare()) {
                            viewHolder.care.setText("已关注");
                            viewHolder.care.setSelected(true);
                        } else {
                            viewHolder.care.setText("关注");
                            viewHolder.care.setSelected(false);
                        }
                    }else {
                        Intent intent=new Intent(context, AccountActivity.class);
                        context.startActivity(intent);
                        context.overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                    }

                }
            });
            if (MyAccount.isLogin){
                if (ipSimpleInfo.iscare()){
                    viewHolder.care.setText("已关注");
                    viewHolder.care.setSelected(true);
                }else {
                    viewHolder.care.setText("关注");
                    viewHolder.care.setSelected(false);
                }

            }
            return convertView;
        }
        class ViewHolder{
            ImageView icon;
            TextView name;
            TextView intro;
            TextView care;
        }
    }