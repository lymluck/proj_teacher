package com.smartstudy.counselor_t.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.MyUserInfo;
import com.smartstudy.counselor_t.mvp.contract.MainActivityContract;
import com.smartstudy.counselor_t.entity.StudentInfo;
import com.smartstudy.counselor_t.mvp.presenter.MainActivityPresenter;
import com.smartstudy.counselor_t.ui.activity.LoginActivity;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.ui.fragment.MyConversationListFragment;
import com.smartstudy.counselor_t.util.ConstantUtils;
import com.smartstudy.counselor_t.util.SPCacheUtils;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

public class MainActivity extends BaseActivity<MainActivityContract.Presenter> implements MainActivityContract.View {
    private FragmentManager mfragmentManager;
    private MyConversationListFragment mConversationListFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mfragmentManager = getSupportFragmentManager();
        setTopLineVisibility(View.VISIBLE);
        showChatList(mfragmentManager.beginTransaction());
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

            @Override
            public UserInfo getUserInfo(String userId) {

                if (userId.equals(SPCacheUtils.get("imUserId", ""))) {
                    return new MyUserInfo(SPCacheUtils.get("imUserId", "").toString(),
                            SPCacheUtils.get("name", "").toString(),
                            Uri.parse(SPCacheUtils.get("avatar", "").toString()), "", "", "");
                } else {
                    presenter.getStudentInfo(userId);

                }
                return null;
            }

        }, true);
    }

    @Override
    public MainActivityContract.Presenter initPresenter() {
        return new MainActivityPresenter(this);
    }

    @Override
    public void initView() {
        String ticket = (String) SPCacheUtils.get("ticket", ConstantUtils.CACHE_NULL);
        if (!TextUtils.isEmpty(ticket) && ConstantUtils.CACHE_NULL.equals(ticket)) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        setLeftImgVisible(View.GONE);
        setTitle(getString(R.string.msg_name));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
            String imToken = SPCacheUtils.get("imToken", "").toString();
            if (!TextUtils.isEmpty(imToken)) {
                RongIM.connect(imToken, new RongIMClient.ConnectCallback() {

                    @Override
                    public void onSuccess(String s) {
                        unReadMessage();
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                    }

                    @Override
                    public void onTokenIncorrect() {
                    }
                });
            }
        } else {
            unReadMessage();
        }
    }


    private MyConversationListFragment initConversationList() {
        if (mConversationListFragment == null) {
            MyConversationListFragment listFragment = new MyConversationListFragment();
            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")
                    .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")
                    .build();
            listFragment.setUri(uri);
            mConversationListFragment = listFragment;
            return listFragment;
        } else {
            return mConversationListFragment;
        }
    }


    public void showChatList(FragmentTransaction ft) {
        /**
         * 如果Fragment为空，就新建一个实例
         * 如果不为空，就将它从栈中显示出来
         */
        if (mConversationListFragment == null) {
            mConversationListFragment = initConversationList();
            ft.add(R.id.conversation, mConversationListFragment);
        } else {
            ft.show(mConversationListFragment);
        }
        mConversationListFragment.setUserVisibleHint(true);
        ft.commitAllowingStateLoss();
        ft = null;
    }


    private void unReadMessage() {
        RongIM.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                setTitle(String.format(getString(R.string.msg_unread), integer + ""));
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
    }


    @Override
    public void getStudentInfoSuccess(String id, StudentInfo studentInfo) {
        if (studentInfo != null) {
            MyUserInfo myUserInfo = new MyUserInfo(id, studentInfo.getName(),
                    Uri.parse(TextUtils.isEmpty(studentInfo.getAvatar()) ? "" : studentInfo.getAvatar()), studentInfo.getAdmissionTime(),
                    studentInfo.getTargetCountry(), studentInfo.getTargetDegree());
            RongIM.getInstance().refreshUserInfoCache(myUserInfo);
            SPCacheUtils.put("Rong" + id, studentInfo.getAdmissionTime() + "" + ":" + studentInfo.getTargetCountry() + "" + ":" + studentInfo.getTargetDegree() + "");
        }
    }
}
