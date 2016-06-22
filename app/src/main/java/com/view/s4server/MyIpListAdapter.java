package com.view.s4server;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.test4s.myapp.R;
import com.test4s.net.Url;

import java.util.List;

public class MyIpListAdapter extends BaseAdapter {
        List<IPSimpleInfo> list;
        Context context;

        public MyIpListAdapter(Context context, List<IPSimpleInfo> list){
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
                convertView= LayoutInflater.from(context).inflate(R.layout.item_iplistfragment,null);
                viewHolder=new ViewHolder();
                viewHolder.icon= (ImageView) convertView.findViewById(R.id.imageView_iplist);
                viewHolder.name= (TextView) convertView.findViewById(R.id.name_item_iplist);
                viewHolder.intro= (TextView) convertView.findViewById(R.id.introuduction_item_iplist);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }

            IPSimpleInfo ipSimpleInfo=list.get(position);

            Picasso.with(context).load(Url.prePic+ipSimpleInfo.getLogo())
                    .into(viewHolder.icon);
            viewHolder.name.setText(ipSimpleInfo.getIp_name());
            viewHolder.intro.setText("类        型 ："+ipSimpleInfo.getIp_cat()+"\n风        格 ："+ipSimpleInfo.getIp_style()+"\n授权范围 ："+ipSimpleInfo.getUthority());
            return convertView;
        }
        class ViewHolder{
            ImageView icon;
            TextView name;
            TextView intro;
        }
    }