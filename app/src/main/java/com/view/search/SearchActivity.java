package com.view.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.app.tools.ClearWindows;
import com.test4s.gdb.History;
import com.test4s.gdb.HistoryDao;
import com.test4s.myapp.MyApplication;
import com.test4s.myapp.R;
import com.view.activity.BaseActivity;

public class SearchActivity extends BaseActivity implements View.OnClickListener {

    FragmentManager fragmentManager;

    private EditText editText;
    private ImageView clearInput;
    private ImageView search;
    private ImageView back;
    private HistoryDao historyDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setImmerseLayout(findViewById(R.id.titlebar_search));
        historyDao= MyApplication.daoSession.getHistoryDao();

        editText= (EditText) findViewById(R.id.edit_search);
        clearInput= (ImageView) findViewById(R.id.clearinput_search);
        search= (ImageView) findViewById(R.id.search_search);
        back= (ImageView) findViewById(R.id.back_search);
//        editText.clearFocus();


        fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.in_from_right,R.anim.out_to_left);
        fragmentManager.beginTransaction().replace(R.id.contianer_search,new SearchIndex()).commit();


//        getFocus(editText);

        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //延时启动输入法。否则瞬间弹出又消失
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
//                log.info("postDelayed");
                InputMethodManager inManager = (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        },500);
    }

    private void initListener() {
        back.setOnClickListener(this);
        search.setOnClickListener(this);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    clearInput.setVisibility(View.VISIBLE);
                }else {
                    clearInput.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        clearInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_search:
                ClearWindows.clearInput(this,editText);
                finish();
                break;
            case R.id.search_search:
                String message=editText.getText().toString();
                if (TextUtils.isEmpty(message)) {
                }else {
                    Intent intent=new Intent(this,SearchEndActivity.class);
                    intent.putExtra("keyword",message);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                    addHistory(message);
                }
                break;
        }
    }
    public void addHistory(String message){
        History history=new History();
        history.setKeyword(message);
        historyDao.insert(history);
    }



}
