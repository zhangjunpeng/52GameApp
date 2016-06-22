package com.view.Evaluation;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.test4s.account.MyAccount;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.test4s.myapp.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

/**
 * Created by Administrator on 2016/4/8.
 */
public class SetAgeFragment extends BaseFragment {
    String age;
    String gameid;

    View view;
    TextView name_text;
    EditText age_edit;

    ImageView back;
    TextView title;
    TextView save;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        age=getArguments().getString("age");
        gameid=getArguments().getString("game_id");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_setnick,container,false);
        save= (TextView) view.findViewById(R.id.save_savebar);
        back= (ImageView) view.findViewById(R.id.back_savebar);
        title= (TextView) view.findViewById(R.id.textView_titlebar_save);

        name_text= (TextView) view.findViewById(R.id.name_text_setnick);
        age_edit= (EditText) view.findViewById(R.id.nick_setnick);
        age_edit.setText(age);
        age_edit.setInputType(InputType.TYPE_CLASS_NUMBER);
        age_edit.setHint("请输入您的年龄");
        getFocus(age_edit);
        name_text.setText("年龄");

        title.setText("设置年龄");

        initListener();

        return view;
    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
            }
        });

    }

    private void saveInfo() {
        BaseParams params=new BaseParams("test/saveinfo");
        params.addParams("token", MyAccount.getInstance().getToken());
        params.addParams("game_id",gameid);
        params.addParams("key","age");
        params.addParams("val",age_edit.getText().toString());
        x.http().post(params.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("saveInfo=="+result);
                try {
                    JSONObject res=new JSONObject(result);
                    boolean su=res.getBoolean("success");
                    int code=res.getInt("code");
                    if (su&&code==200){
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


}
