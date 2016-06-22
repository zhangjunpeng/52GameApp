package com.view.Introduce;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test4s.myapp.MainActivity;
import com.test4s.myapp.R;

/**
 * Created by Administrator on 2016/4/21.
 */
public class IntroduceFragment extends Fragment{

    private ImageView image;
    private TextView go;
    private TextView text;

    private int index;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        index=getArguments().getInt("index",0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_introduce,null);
        image= (ImageView) view.findViewById(R.id.image_intro);
        text= (TextView) view.findViewById(R.id.text_intro);
        go= (TextView) view.findViewById(R.id.go_intro);
        switch (index){
            case 0:
                image.setImageResource(R.drawable.intro1);
                break;
            case 1:
                image.setImageResource(R.drawable.intro2);
                text.setText("海量游戏免费评测\n评测报告一键查看");
                break;
            case 2:
                image.setImageResource(R.drawable.intro3);
                text.setText("最新游戏资讯及时掌握");
                break;
        }
        initView();
        return view;
    }

    private void initView() {
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}
