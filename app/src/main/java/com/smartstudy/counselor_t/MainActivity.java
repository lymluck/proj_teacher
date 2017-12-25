package com.smartstudy.counselor_t;

import android.os.Bundle;
import android.view.View;

import com.smartstudy.paysdk.pay.ZkPayPlatform;

import io.rong.imlib.RongIMClient;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initEvent(Bundle savedInstanceState) {
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
