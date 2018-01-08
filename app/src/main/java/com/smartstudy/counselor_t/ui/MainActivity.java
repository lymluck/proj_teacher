package com.smartstudy.counselor_t.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.ui.activity.LoginActivity;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.util.ConstantUtils;
import com.smartstudy.counselor_t.util.SPCacheUtils;

import io.rong.imlib.RongIMClient;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        String ticket = (String) SPCacheUtils.get("ticket", ConstantUtils.CACHE_NULL);
        if (!TextUtils.isEmpty(ticket) && ConstantUtils.CACHE_NULL.equals(ticket)) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        setLeftImgVisible(View.GONE);
        setTitle(getString(R.string.msg_name));
        RongIMClient.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                setTitle(String.format(getString(R.string.msg_unread), integer + ""));
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
    }
}
