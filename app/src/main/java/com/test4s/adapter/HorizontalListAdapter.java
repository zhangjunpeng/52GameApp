package com.test4s.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.test4s.gdb.CP;
import com.test4s.gdb.IP;
import com.test4s.gdb.Investment;
import com.test4s.gdb.OutSource;
import com.test4s.myapp.R;
import com.test4s.net.Url;

import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2015/12/21.
 */
public class HorizontalListAdapter extends BaseAdapter {



    public final static String TAG_CP="CP";
    public final static String TAG_IP="IP";
    public final static String TAG_INVESTMENT="INVESTMENT";
    public final static String TAG_OUTSOURCE="OUTSOUTCE";

    Context mcontext;
    List<Object> list;
    String tag;


    public HorizontalListAdapter(Context context, List<Object> list,String tag){
        this.list=list;
        this.tag=tag;
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
        String imageUrl="";
        String name="";
        switch (tag){
            case TAG_CP:
                CP cp= (CP) list.get(position);
                imageUrl= Url.prePic+cp.getLogo();
                name=cp.getCompany_name();
                break;
            case TAG_INVESTMENT:
                Investment investment= (Investment) list.get(position);
                imageUrl=Url.prePic+investment.getLogo();
                name=investment.getCompany_name();
                break;
            case TAG_IP:
                IP ip= (IP) list.get(position);
                imageUrl=Url.prePic+ip.getIp_logo();
                name=ip.getCompany_name();
                break;
            case TAG_OUTSOURCE:
                OutSource outSource= (OutSource) list.get(position);
                imageUrl=Url.prePic+outSource.getLogo();
                name=outSource.getCompany_name();
                break;
        }
        x.image().bind(viewHolder.imageView,imageUrl);
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
