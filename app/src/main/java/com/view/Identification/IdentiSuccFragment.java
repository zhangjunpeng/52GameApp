package com.view.Identification;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test4s.myapp.R;

/**
 * Created by Administrator on 2016/6/28.
 */
public class IdentiSuccFragment extends Fragment implements View.OnClickListener {

    private ImageView more1;
    private ImageView more2;
    private TextView server_iden;
    private TextView server_realname;
    private RelativeLayout checkInfo;
    private RelativeLayout identi_real;

    private TextView identi_real_text;

    private ImageView stageImage;
    private TextView stageText;
    private ImageView gxtg;
    private TextView server_title;
    private TextView server_title_realname;

    private IdentiMess mess;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mess= (IdentiMess) getArguments().getSerializable("data");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_identisucc,null);
        more1= (ImageView) view.findViewById(R.id.more_idtensucc);
        more2= (ImageView) view.findViewById(R.id.more_server_realname);
        server_iden= (TextView) view.findViewById(R.id.servers_idensucc);
        server_realname= (TextView) view.findViewById(R.id.servers_realname_idensucc);
        checkInfo= (RelativeLayout) view.findViewById(R.id.checkinfo_idensu);
        identi_real= (RelativeLayout) view.findViewById(R.id.certification_idensucc);

        stageImage= (ImageView) view.findViewById(R.id.stageImge_idensucc);
        stageText= (TextView) view.findViewById(R.id.stage_idensucc);
        gxtg= (ImageView) view.findViewById(R.id.gxtg_idtensucc);
        server_title= (TextView) view.findViewById(R.id.servertitle_idensucc);
        server_title_realname= (TextView) view.findViewById(R.id.servertitle_realname_idensucc);

        identi_real_text= (TextView) view.findViewById(R.id.certification_idensucc_text);


        checkInfo.setOnClickListener(this);
        identi_real.setOnClickListener(this);
        more1.setOnClickListener(this);
        more2.setOnClickListener(this);

        setView();

        return view;
    }

    private void setView() {
        server_title.setText(mess.getVal()+"身份认证成功!");
        server_iden.setText(mess.getService());
        server_realname.setText(mess.getCertificationservice());
        if (TextUtils.isEmpty(mess.getCertification())){
            stageImage.setImageResource(R.drawable.rz_fail);
            stageText.setText("您还未进行实名认证！");
        }else {
            switch (mess.getCertification()) {
                case "0":
                    stageImage.setImageResource(R.drawable.rz_success);
                    stageText.setText("实名认证正在审核中！");
                    stageText.setTextColor(Color.rgb(255,156,0));
                    identi_real_text.setText("正在审核中");
                    identi_real_text.setTextColor(Color.rgb(76,76,76));
                    identi_real.setClickable(false);
                    break;
                case "1":
                    stageImage.setImageResource(R.drawable.rz_success);
                    stageText.setText("实名认证成功！");
                    stageText.setTextColor(Color.rgb(255,156,0));
                    identi_real_text.setText("查看认证资料");
                    identi_real_text.setTextColor(Color.rgb(76,76,76));
                    gxtg.setVisibility(View.VISIBLE);
                    break;
                case "2":
                    stageImage.setImageResource(R.drawable.rz_fail_orange);
                    stageText.setText("实名认证失败！");
                    stageText.setTextColor(Color.rgb(255,156,0));
                    identi_real_text.setText("查看详情");
                    identi_real_text.setTextColor(Color.rgb(76,76,76));
                    break;
                case "3":
                    stageImage.setImageResource(R.drawable.rz_success);
                    stageText.setText("管理员已认证！");
                    stageText.setTextColor(Color.rgb(255,156,0));
                    identi_real_text.setText("查看详情");
                    identi_real_text.setTextColor(Color.rgb(76,76,76));
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.more_idtensucc:
                if (server_iden.getMaxLines()==3){
                    more1.setImageResource(R.drawable.up_arrow);
                    server_iden.setMaxLines(100);
                }else {
                    more1.setImageResource(R.drawable.down_arrow);
                    server_iden.setMaxLines(3);
                }
                break;
            case R.id.more_server_realname:
                if (server_realname.getMaxLines()==3){
                    more2.setImageResource(R.drawable.up_arrow);
                    server_realname.setMaxLines(100);
                }else {
                    more2.setImageResource(R.drawable.down_arrow);
                    server_realname.setMaxLines(3);
                }
                break;
        }
    }
}
