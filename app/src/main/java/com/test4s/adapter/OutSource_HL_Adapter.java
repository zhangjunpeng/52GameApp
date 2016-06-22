package com.test4s.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tools.MyDisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.test4s.gdb.IndexItemInfo;
import com.test4s.myapp.R;
import com.test4s.net.Url;

import java.util.List;

/**
 * Created by Administrator on 2016/1/14.
 */
public class OutSource_HL_Adapter extends BaseAdapter{
    List<IndexItemInfo> list;
    Context mcontext;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader= ImageLoader.getInstance();

    public OutSource_HL_Adapter(Context context,List<IndexItemInfo> outSourceList){
        list=outSourceList;
        mcontext=context;
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
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_horizaontal_index,null);
            viewHolder=new ViewHolder();
            viewHolder.imageView= (ImageView) convertView.findViewById(R.id.imageView_item_hor_index);
            viewHolder.textView= (TextView) convertView.findViewById(R.id.text_item_hor_index);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        IndexItemInfo outSource=list.get(position);
        String imageUrl= Url.prePic+outSource.getLogo();
        String name=outSource.getCompany_name();

        imageLoader.displayImage(imageUrl,viewHolder.imageView, MyDisplayImageOptions.getdefaultImageOptions());

        viewHolder.textView.setText(name);
        return convertView;
    }
    class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
