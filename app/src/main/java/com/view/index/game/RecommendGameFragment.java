package com.view.index.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tools.MyDisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.test4s.account.AccountActivity;
import com.test4s.account.MyAccount;
import com.test4s.gdb.GameInfo;
import com.test4s.gdb.GameType;
import com.test4s.myapp.R;
import com.test4s.net.Url;
import com.view.activity.ListActivity;
import com.view.game.GameDetailActivity;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/11.
 */
public class RecommendGameFragment extends Fragment{

    private List<GameType> titles;
    private Map<String,List> map;

    private LinearLayout linearLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titles=GameFragment.titles;
        map=GameFragment.map;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        NestedScrollView view= (NestedScrollView) inflater.inflate(R.layout.fragment_recommendgame,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        linearLayout= (LinearLayout) view.findViewById(R.id.linear);
        initView();

        super.onViewCreated(view, savedInstanceState);

    }

    private void initView() {
        for (int i=0;i<map.size();i++) {
            List<GameInfo> gameInfos=map.get(titles.get(i).getTitle());
            View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.item_recycler_recommendgame, null);
            TextView tj = (TextView) contentView.findViewById(R.id.text);
            TextView more = (TextView) contentView.findViewById(R.id.more);
            RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_index_item);

            tj.setText(titles.get(i).getTitle());

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            //设置Item增加、移除动画
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            recyclerView.setAdapter(new HomeAdapter(gameInfos));


            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), ListActivity.class);
                    intent.putExtra("tag",ListActivity.IP_TAG);
                    intent.putExtra("remment",true);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                }
            });

            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayout.addView(contentView,layoutParams);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        private List<GameInfo> datalist;

        ImageLoader imageLoader=ImageLoader.getInstance();


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    getActivity()).inflate(R.layout.item_recommend_game, parent, false));
            return holder;
        }

        public HomeAdapter(List<GameInfo> datalist) {
            this.datalist=datalist;
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            GameInfo gameInfo=datalist.get(position);

            holder.name.setText(gameInfo.getGame_name());

            imageLoader.displayImage(Url.prePic+gameInfo.getGame_img(),holder.icon,MyDisplayImageOptions.getroundImageOptions());
            if ("1".equals(gameInfo.getNorms())){
                holder.norms.setVisibility(View.VISIBLE);
            }else if("0".equals(gameInfo.getNorms())){
                holder.norms.setVisibility(View.INVISIBLE);
            }
            if (TextUtils.isEmpty(gameInfo.getGame_grade())){
                holder.grade.setVisibility(View.INVISIBLE);
            }else {
                imageLoader.displayImage(Url.prePic+gameInfo.getGame_grade(),holder.grade, MyDisplayImageOptions.getIconOptions());
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
            return 3;
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

    private void goDetail(String gameid,String ident_cat){
        Intent intent= new Intent(getActivity(),GameDetailActivity.class);
        intent.putExtra("game_id",gameid);
        intent.putExtra("ident_cat",ident_cat);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);

    }
}
