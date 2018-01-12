package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.ChatDetailContract;

//CallKit start 1
//CallKit end 1

/**
 * 会话页面
 * 1，设置 ActionBar title
 * 2，加载会话页面
 * 3，push 和 通知 判断
 */
public class ConversationActivity extends UIActivity {
    private TextView tvTitle;
    private String targeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
    }

    @Override
    protected void initViewAndData() {
        tvTitle = (TextView) findViewById(R.id.topdefault_centertitle);
        tvTitle.setText(getIntent().getData().getQueryParameter("title"));
        targeId = getIntent().getData().getQueryParameter("targetId");
    }

    @Override
    public void initEvent() {
        this.findViewById(R.id.topdefault_rightbutton).setOnClickListener(this);
        this.findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_rightbutton:
                Intent intent=new Intent();
                intent.putExtra("ids",targeId);
                intent.setClass(this,ChatDetilActivity.class);
                startActivity(intent);
                break;

            case R.id.topdefault_leftbutton:
                finish();
                break;
            default:
                break;

        }
    }

}