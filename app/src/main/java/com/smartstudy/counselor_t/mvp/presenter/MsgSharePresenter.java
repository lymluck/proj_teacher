package com.smartstudy.counselor_t.mvp.presenter;

import android.text.TextUtils;

import study.smart.baselib.mvp.base.BasePresenterImpl;
import study.smart.baselib.entity.ChatUserInfo;
import com.smartstudy.counselor_t.mvp.contract.MsgShareContract;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * @author louis
 * @date on 2018/2/26
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class MsgSharePresenter extends BasePresenterImpl<MsgShareContract.View> implements MsgShareContract.Presenter {

    public MsgSharePresenter(MsgShareContract.View view) {
        super(view);
    }

    @Override
    public void getChatUsers() {
        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                List<ChatUserInfo> datas = new ArrayList<>();
                ChatUserInfo info = null;
                for (Conversation conversation : conversations) {
                    info = new ChatUserInfo();
                    info.setId(conversation.getTargetId());
                    info.setName(TextUtils.isEmpty(conversation.getConversationTitle()) ? conversation.getTargetId() : conversation.getConversationTitle());
                    info.setAvatar(conversation.getPortraitUrl().toString());
                    datas.add(info);
                    info = null;
                }
                view.showUsers(datas);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
    }
}
