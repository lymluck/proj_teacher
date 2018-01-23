package com.smartstudy.counselor_t.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.StudentInfo;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.util.SPCacheUtils;

//CallKit start 1
//CallKit end 1

/**
 * 会话页面
 * 1，设置 ActionBar title
 * 2，加载会话页面
 * 3，push 和 通知 判断
 */
public class ConversationActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private String targeId;
    private TextView tvTitleTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        setHeadVisible(View.GONE);
    }


    @Override
    public void initEvent() {
        this.findViewById(R.id.topdefault_rightbutton2).setOnClickListener(this);
        this.findViewById(R.id.topdefault_leftbutton2).setOnClickListener(this);
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        tvTitle = (TextView) findViewById(R.id.topdefault_centertitle2);
        tvTitleTag = findViewById(R.id.tv_title_tag);
        tvTitle.setText(getIntent().getData().getQueryParameter("title"));
        targeId = getIntent().getData().getQueryParameter("targetId");
        String myUserInfo = (String) SPCacheUtils.get("Rong" + targeId, "");
        String[] detail = myUserInfo.split(":");
        if (detail.length == 3) {
            String detailInfo = "";
            if (!TextUtils.isEmpty(detail[0])) {
                detailInfo += detail[0];
            }

            if (!TextUtils.isEmpty(detail[1])) {
                detailInfo += " | " + detail[1];
            }

            if (!TextUtils.isEmpty(detail[2])) {
                detailInfo += " | " + detail[2];
            }

            tvTitleTag.setText(detailInfo);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_rightbutton2:
                Intent intent = new Intent();
                intent.putExtra("ids", targeId);
                intent.setClass(this, StudentInfoActivity.class);
                startActivity(intent);
                break;

            case R.id.topdefault_leftbutton2:
                finish();
                break;
            default:
                break;

        }

    }
}