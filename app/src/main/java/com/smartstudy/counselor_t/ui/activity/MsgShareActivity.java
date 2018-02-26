package com.smartstudy.counselor_t.ui.activity;

import android.os.Bundle;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

public class MsgShareActivity extends BaseActivity<BasePresenter> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_share);
    }

    @Override
    public void initView() {
        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                for (Conversation conversation : conversations) {
                    conversation.getTargetId();
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }
}
