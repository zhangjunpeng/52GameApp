package com.view.myattention;

import android.app.Activity;
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
import com.test4s.gdb.GameInfo;
import com.test4s.myapp.R;
import com.test4s.net.Url;
import com.view.game.GameDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.List;

/**
 * Created by Administrator on 2016/3/21.
 */
public class GameAttentionAdapter extends BaseAdapter {
    private Activity mcontext;
    private List<Object> gameInfos;
    ListView listView;

    public  GameAttentionAdapter(Activity context, List<Object> list, ListView listView){
        mcontext=context;
        this.gameInfos=list;
        this.listView=listView;
    }

    @Override
    public int getCount() {
        return gameInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return gameInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_game_attentionlist,null);
            viewHolder.icon= (ImageView) convertView.findViewById(R.id.imageView_gameattention);
            viewHolder.name= (TextView) convertView.findViewById(R.id.name_item_gameattention);
            viewHolder.uncare= (Button) convertView.findViewById(R.id.cancel_care_game);
            viewHolder.info= (TextView) convertView.findViewById(R.id.introuduction_item_gameattention);
            viewHolder.gamerating= (ImageView) convertView.findViewById(R.id.gamerating_gameattention);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        final GameInfo gameInfo= (GameInfo) gameInfos.get(position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mcontext, GameDetailActivity.class);
                intent.putExtra("game_id",gameInfo.getGame_id());
                mcontext.startActivity(intent);
                mcontext.overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });
        Picasso.with(mcontext)
                .load(Url.prePic+gameInfo.getGame_img())
                .placeholder(R.drawable.default_icon)
                .into(viewHolder.icon);
        viewHolder.name.setText(gameInfo.getGame_name());
        Picasso.with(mcontext)
                .load(Url.prePic+gameInfo.getGame_grade())
                .into(viewHolder.gamerating);
        viewHolder.info.setText("游戏类型"+gameInfo.getGame_type()+"\n游戏阶段"+gameInfo.getGame_stage());
        viewHolder.uncare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameInfos.remove(position);
                notifyDataSetChanged();
                AttentionChange.removeAttention2("1", gameInfo.getGame_id(), new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        MyLog.i("adapter==="+result);
                        try {
                            JSONObject jsonObject=new JSONObject(result);
                            boolean su=jsonObject.getBoolean("success");
                            int code=jsonObject.getInt("code");
                            if (su&&code==200){
                                if (gameInfos.size()==0){
                                    listView.setVisibility(View.GONE);
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
        Button uncare;
        TextView info;
        ImageView gamerating;
    }
}
