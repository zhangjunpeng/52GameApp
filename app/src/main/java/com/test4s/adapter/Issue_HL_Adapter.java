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
import com.test4s.myapp.R;
import com.test4s.net.Url;
import com.view.s4server.IssueSimpleInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/3/11.
 */
public class Issue_HL_Adapter extends BaseAdapter {
    List<IssueSimpleInfo> list;
    Context mcontext;
    private ImageLoader imageLoader= ImageLoader.getInstance();

    public Issue_HL_Adapter(Context context, List<IssueSimpleInfo> list){
        mcontext=context;
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
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_horizaontal_index,null);
            viewHolder=new ViewHolder();
            viewHolder.imageView= (ImageView) convertView.findViewById(R.id.imageView_item_hor_index);
            viewHolder.textView= (TextView) convertView.findViewById(R.id.text_item_hor_index);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        IssueSimpleInfo issueSimpleInfo=list.get(position);
        String imageUrl= Url.prePic+issueSimpleInfo.getLogo();
        String name=issueSimpleInfo.getCompany_name();
//        Picasso.with(mcontext)
//                .load(imageUrl)
//                .into(viewHolder.imageView);
        imageLoader.displayImage(imageUrl,viewHolder.imageView, MyDisplayImageOptions.getdefaultImageOptions());

        viewHolder.textView.setText(name);
        return convertView;
    }
    class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
