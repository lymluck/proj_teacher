package com.smartstudy.slideback;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * @author luoyongming
 * @date on 2018/4/12
 * @describe 这个类用来管理 activity 的栈
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
class ActivityInterfaceImpl extends AppCompatActivity implements ActivityInterface {

    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityStackManager.addToStack(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ActivityStackManager.removeFromStack(this);

        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityDestroyed(this);
        }
    }

    @Override
    public void setActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callbacks) {
        mActivityLifecycleCallbacks = callbacks;
    }
}
