package com.smartstudy.counselor_t.receiver;

import android.content.Context;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * @author louis
 * @date on 2018/1/30
 * @describe Rong消息通知接收器
 * @org com.smartstudy.counselor_t
 * @email luoyongming@innobuddy.com
 */

public class RongNotificationReceiver extends PushMessageReceiver {
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage message) {
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage message) {
        return false;
    }
}
