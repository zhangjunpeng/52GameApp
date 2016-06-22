package com.view.Evaluation;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test4s.myapp.R;
import com.view.myreport.GameReportInfo;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/3/28.
 */
public class QuestionFragment extends Fragment {
    View view;
    LinearLayout stars;
    TextView question;
    String ques;
    String id;
    PCActivity activity;
    List<GameReportInfo> gameReportInfos;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    activity.changeQuestion();
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        id=getArguments().getString("id");
        ques=getArguments().getString("ques");
        activity= (PCActivity) getActivity();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_question,null);
        stars= (LinearLayout) view.findViewById(R.id.stars_question);
        question= (TextView) view.findViewById(R.id.question_fragment);
        question.setText(id+"、 "+ques);

        clickStar(activity.scores.get(Integer.parseInt(id)-1)-1);
//        MyLog.i("score=="+score);
        initListener();
        return view;
    }

    private void initListener() {
        for (int i=0;i<stars.getChildCount();i++){
            final View star=stars.getChildAt(i);
            final  int j= i;
            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickStar(j);
                    activity.scores.set(Integer.parseInt(id)-1,j+1);

                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(200);
                                handler.sendEmptyMessage(0);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }
    }

    private void clickStar(int j) {
        for (int i=0;i<stars.getChildCount();i++){
            ImageView star= (ImageView) stars.getChildAt(i);
            if (i<=j){
                star.setImageResource(R.drawable.star_question_1);
            }else {
                star.setImageResource(R.drawable.star_question_0);
            }

        }
    }
}
