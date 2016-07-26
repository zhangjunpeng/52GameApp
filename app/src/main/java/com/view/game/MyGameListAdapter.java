package com.view.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tools.MyDisplayImageOptions;
import com.app.tools.MyLog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.test4s.account.AccountActivity;
import com.test4s.account.MyAccount;
import com.test4s.gdb.GameInfo;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.net.Url;
import com.view.myattention.AttentionChange;

import org.xutils.common.Callback;
import org.xutils.x;

import java.util.List;

public class MyGameListAdapter extends BaseAdapter {
        private Activity mcontext;
        private List<GameInfo> gameInfos;
        public  MyGameListAdapter(Activity context, List<GameInfo> list){
            mcontext=context;
            this.gameInfos=list;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView==null){
                viewHolder=new ViewHolder();
                convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_gamelist_fragment,null);
                viewHolder.icon= (ImageView) convertView.findViewById(R.id.imageView_list);
                viewHolder.name= (TextView) convertView.findViewById(R.id.name_item_list);
                viewHolder.down= (ImageView) convertView.findViewById(R.id.download_item_list);
                viewHolder.info= (TextView) convertView.findViewById(R.id.introuduction_item_list);
                viewHolder.gamerating= (ImageView) convertView.findViewById(R.id.gamerating);
                viewHolder.norms= (TextView) convertView.findViewById(R.id.norms_item_gamelist);
                viewHolder.care= (ImageView) convertView.findViewById(R.id.care_item_list);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            final GameInfo gameInfo=gameInfos.get(position);

            ImageLoader.getInstance().displayImage(Url.prePic+gameInfo.getGame_img(),viewHolder.icon, MyDisplayImageOptions.getroundImageOptions());
            if (TextUtils.isEmpty(gameInfo.getGame_grade())){
                viewHolder.gamerating.setVisibility(View.INVISIBLE);
            }else {
                viewHolder.gamerating.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(Url.prePic+gameInfo.getGame_grade(),viewHolder.gamerating, MyDisplayImageOptions.getdefaultImageOptions());
            }

//            Picasso.with(mcontext)
//                    .load(Url.prePic+gameInfo.getGame_img())
//                    .placeholder(R.drawable.default_icon)
//                    .into(viewHolder.icon);
//            Picasso.with(mcontext)
//                    .load(Url.prePic+gameInfo.getGame_grade())
//                    .into(viewHolder.gamerating);
            viewHolder.name.setText(gameInfo.getGame_name());
            if (TextUtils.isEmpty(gameInfo.getGame_download_url())){
                viewHolder.down.setClickable(false);
                viewHolder.down.setVisibility(View.GONE);
            }else {
                viewHolder.down.setVisibility(View.VISIBLE);
                viewHolder.down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downLoadGame(Url.packageurl+gameInfo.getGame_download_url());
                        if (MyAccount.isLogin){
                            addMyEvaluation(gameInfo.getGame_id());
                        }
                    }
                });
            }
            if ("1".equals(gameInfo.getNorms())){
                viewHolder.norms.setVisibility(View.VISIBLE);
            }else if("0".equals(gameInfo.getNorms())){
                viewHolder.norms.setVisibility(View.INVISIBLE);
            }
//            String down_nums=gameInfo.getGame_download_nums();
//            if (!TextUtils.isEmpty(down_nums)){
//                Long nums=Long.parseLong(down_nums);
//                if (nums>100000){
//                    down_nums=(nums/10000)+"万";
//                }
//            }

//            if ("1".equals(gameInfo.getPack())&&"1".equals(gameInfo.getChecked())){
//                viewHolder.down.setVisibility(View.VISIBLE);
//                viewHolder.down.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        downLoadGame(Url.packageurl+gameInfo.getGame_download_url());
//                        if (MyAccount.isLogin){
//                            addMyEvaluation(gameInfo.getGame_id());
//                        }
//                    }
//                });
//            }else {
//                viewHolder.down.setClickable(false);
//                viewHolder.down.setVisibility(View.INVISIBLE);
//
//            }
            String requirse="";
            if (TextUtils.isEmpty(gameInfo.getRequire())){
                requirse="暂无需求";
            }else {
                requirse=gameInfo.getRequire();
            }
            String mess=gameInfo.getGame_type()+"/"+gameInfo.getGame_stage()+"\n"+requirse;
            viewHolder.info.setText(mess);
            if (MyAccount.isLogin){
                if (gameInfo.iscare()){
                    viewHolder.care.setImageResource(R.drawable.cared);
                }else {
                    viewHolder.care.setImageResource(R.drawable.care_gray);
                }
                viewHolder.care.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (gameInfo.iscare()){
                            gameInfo.setIscare(false);
                            AttentionChange.removeAttention("1",gameInfo.getGame_id(), mcontext);
                        }else {
                            gameInfo.setIscare(true);
                            AttentionChange.addAttention("1",gameInfo.getGame_id(), mcontext);
                        } if (gameInfo.iscare()){
                            viewHolder.care.setImageResource(R.drawable.cared);
                        }else {
                            viewHolder.care.setImageResource(R.drawable.care_gray);
                        }


                    }
                });
            }else {
                viewHolder.care.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(mcontext, AccountActivity.class);
                        mcontext.startActivity(intent);
                        mcontext.overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);

                    }
                });

            }
            return convertView;
        }
        class ViewHolder{
            ImageView icon;
            TextView name;
            ImageView down;
            ImageView gamerating;
            TextView info;
            TextView norms;
            ImageView care;
        }

    private void downLoadGame(String url){
        //调用外部浏览器下载文件
        MyLog.i("Url==="+url);
        Uri uri = Uri.parse(url);
        Intent downloadIntent = new Intent(Intent.ACTION_VIEW, uri);
        mcontext.startActivity(downloadIntent);
    }
    private void addMyEvaluation(String game_id){
        BaseParams baseParams=new BaseParams("test/downloadgame");
        baseParams.addParams("game_id",game_id);
        if (MyAccount.isLogin) {
            baseParams.addParams("token", MyAccount.getInstance().getToken());
        }
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("add game to eva back=="+result);
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
    }