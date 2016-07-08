package com.view.Identification;

import android.content.Intent;
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
    private ImageView stageImage;
    private TextView stageText;
    private TextView reson;
    private TextView servers;
    private TextView check;

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

        stageImage= (ImageView) view.findViewById(R.id.stageimage_idtf);
        stageText= (TextView) view.findViewById(R.id.stage_idtf);
        reson= (TextView) view.findViewById(R.id.reson_idtf);
        servers= (TextView) view.findViewById(R.id.servers_idtf);
        check= (TextView) view.findViewById(R.id.check_idtf);


        check.setOnClickListener(this);
        setView();

        return view;
    }

    private void setView() {
        int stage=Integer.parseInt(mess.getChecked());
        switch (stage){
            case 0:
                stageImage.setImageResource(R.drawable.rz_ing);
                stageText.setText("等待审核");
                break;
            case 2:
                stageImage.setImageResource(R.drawable.rz_fail);
                stageText.setText("认证失败");
                reson.setVisibility(View.VISIBLE);
                reson.setText(mess.getNote());
                break;

            case 4:
                stageImage.setImageResource(R.drawable.rz_fail);
                stageText.setText("未认证");

                break;
            case 1:
            case 3:
                stageImage.setImageResource(R.drawable.rz_success);
                stageText.setText("认证成功");
                check.setText(mess.getTip());
                break;
        }
        servers.setText(mess.getService());
        check.setText(mess.getTip());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_idtf:
                Intent intent=new Intent(getActivity(),IdentificaSubActivity.class);
                intent.putExtra("type",mess.getId());
                startActivity(intent);
                break;
        }
    }

}
