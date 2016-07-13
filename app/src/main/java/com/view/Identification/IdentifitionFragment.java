package com.view.Identification;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.test4s.myapp.R;

import java.util.Map;

/**
 * Created by Administrator on 2016/6/28.
 */
public class IdentifitionFragment extends Fragment implements View.OnClickListener {

    private String type="2";

    private TextView ident_person;
    private TextView ident_company;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type=getArguments().getString("type","2");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_identification,null);

        ident_person= (TextView) view.findViewById(R.id.ident_toc);
        ident_company= (TextView) view.findViewById(R.id.ident_tob);
        ident_person.setOnClickListener(this);
        ident_company.setOnClickListener(this);

        if (type.equals("6")){
            ident_person.setVisibility(View.GONE);
        }

        return view;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ident_toc:
                Intent intent=new Intent(getActivity(),IdentificaSubActivity.class);
                intent.putExtra("cattype",type);
                intent.putExtra("type","person");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                break;
            case R.id.ident_tob:
                Intent intent1=new Intent(getActivity(),IdentificaSubActivity.class);
                intent1.putExtra("cattype",type);
                intent1.putExtra("type","company");
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);

                break;
        }
    }
}
