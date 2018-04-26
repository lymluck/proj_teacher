package com.smartstudy.slideback;

import android.app.Application;

/**
 * @author luoyongming
 * @date on 2018/4/12
 * @describe activity生命周期接口
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public interface ActivityInterface {
    /**
     * Set the callback for activity lifecycle
     *
     * @param callbacks callbacks
     */
    void setActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callbacks);
}
