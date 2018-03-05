package com.smartstudy.counselor_t.app;


import android.app.ActivityManager;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.smartstudy.counselor_t.ui.provider.MyConversationListProvider;
import com.smartstudy.counselor_t.ui.provider.MyTextMessageItemProvider;
import com.smartstudy.router.Router;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.push.RongPushClient;


/**
 * Created by louis on 2017/2/22.
 */
public class BaseApplication extends MultiDexApplication {

    private static BaseApplication instance;
    public static Context appContext;

    // 程序是否后台运行标志，用于标识推送消息是否需要在通知栏显示，默认程序是在后台运行的
    private boolean isBackground = true;
    private boolean isDownload = false;
    private String downLoadUrl;

    public static BaseApplication getInstance() {
        if (null == instance) {
            synchronized (BaseApplication.class) {
                if (null == instance) {
                    instance = new BaseApplication();
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (getProcessName(this).equals(getPackageName())) {
            appContext = this;
            //注册容云组件
            initRong();
            //路由初始化
            Router.initialize(this);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public boolean isBackground() {
        return isBackground;
    }

    public void setBackground(boolean isBackground) {
        this.isBackground = isBackground;
    }

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean isDownload) {
        this.isDownload = isDownload;
    }

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    /**
     * 获取进程名。
     * 由于app是一个多进程应用，因此每个进程被os创建时，
     * onCreate()方法均会被执行一次，
     * 进行辨别初始化，针对特定进程进行相应初始化工作，
     * 此方法可以提高一半启动时间。
     *
     * @param context 上下文环境对象
     * @return 获取此进程的进程名
     */
    private String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return "";
        }

        for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {
            if (runningAppProcess.pid == android.os.Process.myPid()
                    && !TextUtils.isEmpty(runningAppProcess.processName)) {
                return runningAppProcess.processName;
            }
        }
        return "";
    }


    private void initRong() {
        RongPushClient.registerHWPush(this);
        RongPushClient.registerMiPush(this, "2882303761517712539", "5481771291539");
//        try {
//            RongPushClient.registerFCM(this);
//        } catch (RongException e) {
//            e.printStackTrace();
//        }
        RongIM.init(this, "25wehl3u29wqw");
        RongIM.getInstance().registerConversationTemplate(new MyConversationListProvider());
        RongIM.registerMessageTemplate(new MyTextMessageItemProvider());
        AppManager.init(this);
    }
}
