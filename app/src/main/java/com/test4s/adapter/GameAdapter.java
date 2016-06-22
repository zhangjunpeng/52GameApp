package com.test4s.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.test4s.gdb.GameInfo;
import com.test4s.myapp.R;
import com.test4s.net.Url;

import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2015/12/30.
 */
public class GameAdapter extends BaseAdapter {
    Context mcontext;
    List<GameInfo> gameList;

    public GameAdapter(Context context,List<GameInfo> list){
        mcontext=context;
        gameList=list;
    }

    @Override
    public int getCount() {
        MyLog.i("listcount::"+gameList.size());
        return gameList.size();
    }

    @Override
    public Object getItem(int position) {
        return gameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyLog.i("Gamelist::getView"+position);
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView=LayoutInflater.from(mcontext).inflate(R.layout.item_gamelist_listactivity,null);
            viewHolder.imageView= (ImageView) convertView.findViewById(R.id.imageView_gamelist);
            viewHolder.name= (TextView) convertView.findViewById(R.id.name_item_gamelist);

            viewHolder.introduction= (TextView) convertView.findViewById(R.id.introuduction_item_gamelist);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        GameInfo gameInfo=gameList.get(position);
        x.image().bind(viewHolder.imageView, Url.prePic+gameInfo.getGame_img());
        viewHolder.name.setText(gameInfo.getGame_name());
        MyLog.i("name::"+gameInfo.getGame_name());
        viewHolder.introduction.setText(gameInfo.getGame_download_nums()+"下载/"+gameInfo.getGame_size()+"M\n"+gameInfo.getRequire());
        return convertView;
    }

    class ViewHolder{
        ImageView imageView;
        TextView name;
        TextView introduction;
    }
}
