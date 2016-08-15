package com.view.index.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tools.MyDisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.test4s.account.AccountActivity;
import com.test4s.account.MyAccount;
import com.test4s.gdb.GameInfo;
import com.test4s.myapp.R;
import com.test4s.net.Url;
import com.view.myattention.AttentionChange;

import java.util.List;

/**
 * Created by Administrator on 2016/8/12.
 */
public class RecyclerGameFragment extends Fragment {
    RecyclerView recyclerView;

    private List<GameInfo> gameInfoList;
    private String type;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type=getArguments().getString("type","new");
        if (type.equals("new")){
            gameInfoList=GameFragment.newGames;
        }else {
            gameInfoList=GameFragment.gradeGames;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView= (RecyclerView) inflater.inflate(R.layout.fragment_recylergame,container,false);
        return recyclerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(new HomeAdapter(gameInfoList));

    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        private List<GameInfo> datalist;

        ImageLoader imageLoader=ImageLoader.getInstance();


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    getActivity()).inflate(R.layout.item_gamelist_fragment, parent, false));
            return holder;
        }

        public HomeAdapter(List<GameInfo> datalist) {
            this.datalist=datalist;
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final GameInfo gameInfo=datalist.get(position);

            holder.name.setText(gameInfo.getGame_name());

            imageLoader.displayImage(Url.prePic+gameInfo.getGame_img(),holder.icon, MyDisplayImageOptions.getroundImageOptions());
            if (TextUtils.isEmpty(gameInfo.getGame_grade())){
                holder.grade.setVisibility(View.INVISIBLE);
            }else {
                imageLoader.displayImage(Url.prePic+gameInfo.getGame_grade(),holder.grade, MyDisplayImageOptions.getIconOptions());
            }

            if ("1".equals(gameInfo.getNorms())){
                holder.norms.setVisibility(View.VISIBLE);
            }else if("0".equals(gameInfo.getNorms())){
                holder.norms.setVisibility(View.INVISIBLE);
            }
            if (MyAccount.isLogin){
                if (gameInfo.iscare()){
                    holder.care.setText("已关注");
                    holder.care.setSelected(true);
                }else {
                    holder.care.setText("关注");
                    holder.care.setSelected(false);
                }
                holder.care.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (gameInfo.iscare()){
                            gameInfo.setIscare(false);
                            AttentionChange.removeAttention("1",gameInfo.getGame_id(), getActivity());
                        }else {
                            gameInfo.setIscare(true);
                            AttentionChange.addAttention("1",gameInfo.getGame_id(), getActivity());
                        } if (gameInfo.iscare()){
                            holder.care.setText("已关注");
                            holder.care.setSelected(true);
                        }else {
                            holder.care.setText("关注");
                            holder.care.setSelected(false);
                        }


                    }
                });
            }else {
                holder.care.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(), AccountActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);

                    }
                });

            }

            String mess=gameInfo.getGame_type()+"/"+gameInfo.getGame_stage()+"\n"+gameInfo.getRequire();
            holder.info.setText(mess);

            holder.care.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MyAccount.isLogin){

                    }else {
                        Intent intent=new Intent(getActivity(), AccountActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return datalist.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView icon;
            TextView name;
            TextView info;
            TextView care;
            View item;
            TextView norms;
            ImageView grade;

            public MyViewHolder(View view) {
                super(view);
                icon = (ImageView) view.findViewById(R.id.icon);
                name = (TextView) view.findViewById(R.id.name);
                info = (TextView) view.findViewById(R.id.info);
                care = (TextView) view.findViewById(R.id.care);
                norms= (TextView) view.findViewById(R.id.norms);
                grade= (ImageView) view.findViewById(R.id.grade);
                item = view;
            }
        }
    }
}
