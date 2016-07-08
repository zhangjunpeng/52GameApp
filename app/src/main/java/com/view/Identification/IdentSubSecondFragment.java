package com.view.Identification;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test4s.myapp.R;

/**
 * Created by Administrator on 2016/7/5.
 */
public class IdentSubSecondFragment extends Fragment implements View.OnClickListener {

    private String type="person";
    View view;

    private LinearLayout idnum_lauout;
    private LinearLayout companyphoto_layout;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type=getArguments().getString("type","company");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ident_company, null);
        return view;
    }

    private EditText name_edit;
    private EditText phone_edit;
    private EditText code_edit;
    private TextView getcode_text;

    private TextView selectAddress;
    private EditText address_edit;
    private EditText idnum_edit;

    private ImageView comphoto;
    private LinearLayout up_need;

    private TextView submit;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        idnum_lauout= (LinearLayout) view.findViewById(R.id.layout_idnum);
        companyphoto_layout= (LinearLayout) view.findViewById(R.id.layout_companyphoto);
        name_edit= (EditText) view.findViewById(R.id.edit_username);
        phone_edit= (EditText) view.findViewById(R.id.edit_phone);
        code_edit= (EditText) view.findViewById(R.id.edit_code);
        getcode_text= (TextView) view.findViewById(R.id.getcode_iden_sub);
        selectAddress= (TextView) view.findViewById(R.id.address_text);
        address_edit= (EditText) view.findViewById(R.id.address_edit);
        idnum_edit= (EditText) view.findViewById(R.id.edit_idnum);
        comphoto= (ImageView) view.findViewById(R.id.companyphoto_ident);
        up_need= (LinearLayout) view.findViewById(R.id.layout_up_need);

        submit= (TextView) view.findViewById(R.id.submit_iden_sub);

        submit.setOnClickListener(this);
        selectAddress.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_iden_sub:
                break;
            case R.id.address_text:

                break;
        }
    }
}
