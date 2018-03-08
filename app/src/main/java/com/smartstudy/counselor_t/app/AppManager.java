package com.smartstudy.counselor_t.app;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.manager.StudentInfoManager;
import com.smartstudy.counselor_t.ui.activity.ReloginActivity;

import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.InformationNotificationMessage;

/**
 * @author louis
 * @date on 2018/2/24
 * @describe Application管理工具类
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class AppManager implements RongIMClient.ConnectionStatusListener,
        RongIMClient.OnReceiveMessageListener {

    private static AppManager mInstance;
    //application context
    private Context mContext;

    public AppManager(Context mContext) {
        this.mContext = mContext;
        initListener();
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String s) {
                if (!TextUtils.isEmpty(s)) {
                    UserInfo info = RongUserInfoManager.getInstance().getUserInfo(s);
                    if (info == null) {
                        StudentInfoManager.getInstance().getStudentInfo(s);
                    }
                }
                return null;

            }
        }, true);
    }

    public static void init(Context context) {
        if (mInstance == null) {
            synchronized (AppManager.class) {
                if (mInstance == null) {
                    mInstance = new AppManager(context);
                }
            }
        }
    }

    private void initListener() {
        RongIM.setConnectionStatusListener(this);
        RongIM.setOnReceiveMessageListener(this);
    }

    @Override
    public void onChanged(ConnectionStatus connectionStatus) {
        if (ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT.equals(connectionStatus)) {
            RongIM.getInstance().logout();
            mContext.startActivity(new Intent(mContext, ReloginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        if (ConnectionStatus.CONN_USER_BLOCKED.equals(connectionStatus)) {
            RongIM.getInstance().logout();
            mContext.startActivity(new Intent(mContext, ReloginActivity.class)
                    .putExtra("content", mContext.getString(R.string.blocked_msg))
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @Override
    public boolean onReceived(Message message, int i) {
        if (InformationNotificationMessage.class.isAssignableFrom(message.getContent().getClass())) {
            RongIM.getInstance().deleteMessages(new int[]{message.getMessageId()}, null);
        }
        return false;
    }
}
