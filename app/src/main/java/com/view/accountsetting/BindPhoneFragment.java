package com.view.accountsetting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.ClearWindows;
import com.app.tools.CusToast;
import com.test4s.account.MyAccount;
import com.test4s.account.UserInfo;
import com.test4s.myapp.BaseFragment;
import com.test4s.myapp.R;

/**
 * Created by Administrator on 2016/2/22.
 */
public class BindPhoneFragment extends BaseFragment implements View.OnClickListener{

    UserInfo userInfo;

    View view;

    TextView phone;
    EditText phone_input;
    Button check;

    private TextView title;
    private ImageView back;
    private TextView save;

    private TextView warning;
    private RelativeLayout layout;

    String phoneNum;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        userInfo= MyAccount.getInstance().getUserInfo();
        phoneNum=userInfo.getPhone();
        String mes=phoneNum.substring(3,7);
        phoneNum=phoneNum.replace(mes,"****");

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_bindphone,null);

        setImmerseLayout(view.findViewById(R.id.title_bindphone));

        phone= (TextView) view.findViewById(R.id.phonenum_bindphone);
        phone_input= (EditText) view.findViewById(R.id.edit_phone_bindphone);
        check= (Button) view.findViewById(R.id.check_bindphone);
        warning= (TextView) view.findViewById(R.id.warning_bindphone);
        layout= (RelativeLayout) view.findViewById(R.id.re_tips_bindphone);

        //titlebar
        title= (TextView) view.findViewById(R.id.textView_titlebar_save);
        back= (ImageView) view.findViewById(R.id.back_savebar);
        save= (TextView) view.findViewById(R.id.save_savebar);

        save.setVisibility(View.INVISIBLE);
        title.setText("安全验证");

        phone.setText("您已绑定手机号："+phoneNum);

        back.setOnClickListener(this);
        check.setOnClickListener(this);

        getFocus(phone_input);

        check.setClickable(false);

        phone_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==11){
                    check.setClickable(true);
                    check.setBackgroundResource(R.drawable.border_button_orange);
                }else {
                    check.setClickable(false);
                    check.setBackgroundResource(R.drawable.border_button_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void checkPhone() {
        String phonenum=phone_input.getText().toString();
        if (phonenum.equals(userInfo.getPhone())){
            CusToast.showToast(getActivity(),"手机号验证成功",Toast.LENGTH_SHORT);
            BindNewPhoneFragment bindnewphone=new BindNewPhoneFragment();
            FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right,R.anim.out_to_left);
            transaction.replace(R.id.contianner_mysetting,bindnewphone).commit();

            ClearWindows.clearInput(getActivity(),phone_input);
//            layout.setVisibility(View.INVISIBLE);
        }else {
            layout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.check_bindphone:
                checkPhone();
                break;
            case R.id.back_savebar:
                ClearWindows.clearInput(getActivity(),phone_input);
                MyAcountSettingFragment myAcountSettingFragment=new MyAcountSettingFragment();
                FragmentTransaction transaction= getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.in_form_left,R.anim.out_to_right);
                transaction.replace(R.id.contianner_mysetting,myAcountSettingFragment).commit();
                break;
        }
    }
}
