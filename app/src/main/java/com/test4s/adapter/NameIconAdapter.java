package com.test4s.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.test4s.myapp.R;
import com.test4s.net.Url;

import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2015/12/24.
 */
public class NameIconAdapter extends BaseAdapter {
    Context mcontext;
    List<NameIcon> nameIconList;

    public NameIconAdapter(Context context, List<NameIcon> list){
        mcontext=context;
        nameIconList=list;
    }

    @Override
    public int getCount() {
        return nameIconList.size();
    }

    @Override
    public Object getItem(int position) {
        return nameIconList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_cplistfragment,null);
            viewHolder=new ViewHolder();
            viewHolder.imageView= (ImageView) convertView.findViewById(R.id.imageView_cplist_listac);
            viewHolder.name= (TextView) convertView.findViewById(R.id.name_item_cp_listac);
            viewHolder.introduction= (TextView) convertView.findViewById(R.id.introuduction_item_cp_listac);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        NameIcon nameIcon=nameIconList.get(position);
        x.image().bind(viewHolder.imageView, Url.prePic+nameIcon.getImgUrl());
        viewHolder.name.setText(nameIcon.getName());
        viewHolder.introduction.setText(nameIcon.getIntroduction());
        return convertView;
    }
    class ViewHolder{
        ImageView imageView;
        TextView name;
        TextView introduction;
    }
}
