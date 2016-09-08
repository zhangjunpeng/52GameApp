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
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.view.game.RmGameListActivity;
import com.view.myattention.AttentionChange;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/11.
 */
public class RecommendGameFragment extends Fragment {

    private List<GameType> titles;
    private Map<String,List> map;

    private LinearLayout linearLayout;

    private GestureDetector gestureDetector;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titles=GameFragment.titles;
        map=GameFragment.map;
        gestureDetector = new GestureDetector(getActivity(),onGestureListener);
    }

    private GestureDetector.OnGestureListener onGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    return true;
                }
            };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        NestedScrollView view= (NestedScrollView) inflater.inflate(R.layout.fragment_recommendgame,container,false);

//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return gestureDetector.onTouchEvent(event);
//            }
//        });

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
            LinearLayout contianer = (LinearLayout) contentView.findViewById(R.id.contianer_recommendgame);

            tj.setText(titles.get(i).getTitle());

//            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//            //设置Item增加、移除动画
//            recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//            recyclerView.setAdapter(new HomeAdapter(gameInfos));
            for (int m=0;m<gameInfos.size();m++){
                View view=LayoutInflater.from(
                        getActivity()).inflate(R.layout.item_recommend_game, contianer, false);
                ImageView icon = (ImageView) view.findViewById(R.id.icon);
                TextView name = (TextView) view.findViewById(R.id.name);
                TextView info = (TextView) view.findViewById(R.id.info);
                final TextView care = (TextView) view.findViewById(R.id.care);
                TextView norms = (TextView) view.findViewById(R.id.norms);
                ImageView grade = (ImageView) view.findViewById(R.id.grade);
                ImageView line = (ImageView) view.findViewById(R.id.line);

                final GameInfo gameInfo=gameInfos.get(m);

                name.setText(gameInfo.getGame_name());

                ImageLoader.getInstance().displayImage(Url.prePic+gameInfo.getGame_img(),icon,MyDisplayImageOptions.getroundImageOptions());
                if ("1".equals(gameInfo.getNorms())){
                    norms.setVisibility(View.VISIBLE);
                }else if("0".equals(gameInfo.getNorms())){
                    norms.setVisibility(View.INVISIBLE);
                }
                if (TextUtils.isEmpty(gameInfo.getGame_grade())){
                    grade.setVisibility(View.INVISIBLE);
                }else {
                    ImageLoader.getInstance().displayImage(Url.prePic+gameInfo.getGame_grade(),grade, MyDisplayImageOptions.getIconOptions());
                }

                String mess=gameInfo.getGame_type()+"/"+gameInfo.getGame_stage()+"\n"+gameInfo.getRequire();
                info.setText(mess);

                if (MyAccount.isLogin){
                    if (gameInfo.iscare()){
                        care.setText("已关注");
                        care.setSelected(true);
                    }else {
                        care.setText("关注");
                        care.setSelected(false);
                    }
                    care.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (gameInfo.iscare()){
                                gameInfo.setIscare(false);
                                AttentionChange.removeAttention("1",gameInfo.getGame_id(), getActivity());
                            }else {
                                gameInfo.setIscare(true);
                                AttentionChange.addAttention("1",gameInfo.getGame_id(), getActivity());
                            } if (gameInfo.iscare()){
                                care.setText("已关注");
                                care.setSelected(true);
                            }else {
                                care.setText("关注");
                                care.setSelected(false);
                            }
                        }
                    });

                }else {
                    care.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(getActivity(), AccountActivity.class);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                        }
                    });
                }

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goDetail(gameInfo.getGame_id(),gameInfo.getGame_dev());
                    }
                });
                if (m==gameInfos.size()-1){
                    line.setVisibility(View.INVISIBLE);
                }

                contianer.addView(view);

            }

            final int j=i;
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getActivity(), RmGameListActivity.class);
                    intent.putExtra("position",j);
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

    private void goDetail(String gameid,String ident_cat){
        Intent intent= new Intent(getActivity(),GameDetailActivity.class);
        intent.putExtra("game_id",gameid);
        intent.putExtra("ident_cat",ident_cat);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);

    }
}
