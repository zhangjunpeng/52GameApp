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
public class Invesment_HL_Adapter extends BaseAdapter {
    List<IndexItemInfo> list;
    Context mcontext;
    ImageLoader imageLoader=ImageLoader.getInstance();
    public Invesment_HL_Adapter(Context context, List<IndexItemInfo> ipList){
        list=ipList;
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
        IndexItemInfo investment=list.get(position);
        String imageUrl= Url.prePic+investment.getLogo();
        String name=investment.getCompany_name();

//        Picasso.with(mcontext)
//                .load(imageUrl)
//                .into(viewHolder.imageView);
        imageLoader.displayImage(imageUrl,viewHolder.imageView, MyDisplayImageOptions.getdefaultImageOptions());
//        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(mcontext, CPDetailActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                mcontext.startActivity(intent);
//                Activity activity= (Activity) mcontext;
//                activity.overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
//            }
//        });
        viewHolder.textView.setText(name);
        return convertView;
    }
    class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
