package com.smartstudy.counselor_t;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.squareup.leakcanary.LeakCanary;

import study.smart.baselib.BaseApplication;

/**
 * @author yqy
 * @date on 2018/6/25
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class XxdImApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        // 检测内存泄漏
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}

