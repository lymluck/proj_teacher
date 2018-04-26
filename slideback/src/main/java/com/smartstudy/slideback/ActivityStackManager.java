package com.smartstudy.slideback;

import android.app.Activity;

import java.util.LinkedList;

/**
 * @author luoyongming
 * @date on 2018/4/12
 * @describe activity管理栈
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
final class ActivityStackManager {

    /**
     * 窗口栈。
     */
    private static LinkedList<Activity> mActivityStack = new LinkedList<>();

    /**
     * 得到指定activity前一个activity的实例
     *
     * @param curActivity 当前activity
     * @return 可能为null
     */
    public static Activity getPreviousActivity(Activity curActivity) {
        final LinkedList<Activity> activities = mActivityStack;
        Activity preActivity = null;
        for (int cur = activities.size() - 1; cur >= 0; cur--) {
            Activity activity = activities.get(cur);
            if (activity == curActivity) {
                int pre = cur - 1;
                if (pre >= 0) {
                    preActivity = activities.get(pre);
                }
            }
        }

        return preActivity;
    }

    /**
     * 从栈管理队列中移除该Activity。
     *
     * @param activity Activity。
     */
    public static synchronized void removeFromStack(Activity activity) {
        mActivityStack.remove(activity);
    }

    /**
     * 将Activity加入栈管理队列中。
     *
     * @param activity Activity。
     */
    public static synchronized void addToStack(Activity activity) {
        // 移到顶端。
        mActivityStack.remove(activity);
        mActivityStack.add(activity);
    }

    /**
     * 清除栈队列中的所有Activity。
     */
    public static synchronized void clearTask() {
        int size = mActivityStack.size();
        if (size > 0) {
            Activity[] activities = new Activity[size];
            mActivityStack.toArray(activities);

            for (Activity activity : activities) {
                activity.finish();
            }
        }
    }

    /**
     * 获得Activity栈
     */
    public static synchronized Activity[] getActivityStack() {
        Activity[] activities = new Activity[mActivityStack.size()];
        return mActivityStack.toArray(activities);
    }
}
