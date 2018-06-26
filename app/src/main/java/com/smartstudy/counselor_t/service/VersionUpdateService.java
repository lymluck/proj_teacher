package com.smartstudy.counselor_t.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import study.smart.baselib.BaseApplication;
import study.smart.baselib.listener.OnDownloadFileListener;
import study.smart.baselib.listener.OnProgressListener;
import study.smart.baselib.utils.AppUtils;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.baselib.utils.SDCardUtils;
import study.smart.baselib.utils.ToastUtils;
import study.smart.baselib.utils.Utils;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.handler.WeakHandler;
import com.smartstudy.counselor_t.mvp.contract.DownloadFileContract;
import com.smartstudy.counselor_t.mvp.presenter.DownloadFilePresenter;
import com.smartstudy.counselor_t.ui.activity.MyQaActivity;

import java.io.File;

public class VersionUpdateService extends Service implements DownloadFileContract.View {

    private static final int NOTIFY_ID = 0;
    private int mProgress;
    private NotificationManager mNotificationManager;
    private boolean canceled;
    private DownloadBinder binder;
    private BaseApplication app;
    private boolean serviceIsDestroy = false;
    private String apk_path;
    private RemoteViews mContentView;
    private WeakHandler mHandler;
    private String lastVersion;

    /**
     * 更新进度的回调接口
     */
    private static OnProgressListener mProgressListener;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        lastVersion = intent.getStringExtra("version");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 假如被销毁了，无论如何都默认取消了。
        app.setDownload(false);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new DownloadBinder();
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case ParameterUtils.MSG_WHAT_PROGRESS:
                        // 更新进度
                        if (mProgress < 100) {
                            //进度发生变化通知调用方
                            mContentView.setTextViewText(R.id.tv_progress, mProgress + "%");
                            mContentView.setProgressBar(R.id.progressbar, 100, mProgress, false);
                        } else {
                            // 下载完毕后变换通知形式
                            mNotification.build().flags = Notification.FLAG_AUTO_CANCEL;
                            mNotification.setContent(null);
                            Intent intent = new Intent(VersionUpdateService.this, MyQaActivity.class);
                            // 更新参数,注意flags要使用FLAG_UPDATE_CURRENT
                            PendingIntent contentIntent = PendingIntent.getActivity(VersionUpdateService.this, 0, intent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                            mNotification.setContentTitle("下载完成");
                            mNotification.setContentText("文件已下载完毕");
                            mNotification.setContentIntent(contentIntent);
                            serviceIsDestroy = true;
                            app.setDownload(false);
                            stopSelf();// 停掉服务自身
                        }
                        mNotificationManager.notify(NOTIFY_ID, mNotification.build());
                        break;
                    case ParameterUtils.MSG_WHAT_ERR:
                        mNotificationManager.cancel(NOTIFY_ID);
                        ToastUtils.shortToast((String) msg.obj);
                        break;
                    case ParameterUtils.MSG_WHAT_FINISH:
                        app.setDownload(false);
                        // 下载完毕
                        // 取消通知
                        mNotificationManager.cancel(NOTIFY_ID);
                        Utils.installApk(VersionUpdateService.this, apk_path);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        setForeground(true);// 这个不确定是否有作用
        app = BaseApplication.getInstance();
    }

    @Override
    public void showTip(String message) {

    }

    public class DownloadBinder extends Binder {
        public void start(int flag) {
            mProgress = 0;
            if (flag == ParameterUtils.FLAG_UPDATE) {
                setUpNotification();
            }
            startDownload(flag);
        }

        public int getProgress() {
            return mProgress;
        }

        public boolean isCanceled() {
            return canceled;
        }

        public boolean serviceIsDestroy() {
            return serviceIsDestroy;
        }

        public void setOnProgressListener(OnProgressListener onProgressListener) {
            mProgressListener = onProgressListener;
        }

    }

    private void startDownload(int flag) {
        canceled = false;
        downloadApk(flag);
    }

    //
    NotificationCompat.Builder mNotification;

    // 通知栏

    /**
     * 创建通知
     */
    private void setUpNotification() {
        int icon = R.mipmap.ic_launcher;
        CharSequence tickerText = "开始下载";
        long when = System.currentTimeMillis();
        mNotification = new NotificationCompat.Builder(VersionUpdateService.this);
        mNotification.setSmallIcon(icon);
        mNotification.setTicker(tickerText);
        mNotification.setWhen(when);
        // 放置在"正在运行"栏目中
        mNotification.build().flags = Notification.FLAG_ONGOING_EVENT;
        mContentView = new RemoteViews(getPackageName(), R.layout.download_notification_layout);
        mContentView.setTextViewText(R.id.name, "下载中，请稍候...");
        // 指定个性化视图
        mNotification.setContent(mContentView);

        Intent intent = new Intent(this, MyQaActivity.class);
        // 下面两句是 在按home后，点击通知栏，返回之前activity 状态;
        // 有下面两句的话，假如service还在后台下载， 在点击程序图片重新进入程序时，直接到下载界面，相当于把程序MAIN 入口改了 - -
        // 是这么理解么。。。
        // intent.setAction(Intent.ACTION_MAIN);
        // intent.addCategory(Intent.CATEGORY_LAUNCHER);

        // 指定内容意图
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFY_ID, mNotification.build());
    }

    public void downloadApk(final int flag) {
        String fileName = AppUtils.getAppName(this) + "_" + lastVersion + ".apk";
        File fileDir = SDCardUtils.getFileDirPath("Xxd_im/file");
        DownloadFilePresenter presenter = new DownloadFilePresenter(this);
        presenter.downloadFile(app.getDownLoadUrl(), fileDir, fileName, new OnDownloadFileListener<File>() {
            @Override
            public void onErr(String msg) {
                Message message = Message.obtain();
                message.obj = msg;
                message.what = ParameterUtils.MSG_WHAT_ERR;
                mHandler.sendMessage(message);
            }

            @Override
            public void onProgress(int progress) {
                app.setDownload(true);
                if (flag == ParameterUtils.FLAG_UPDATE) {
                    if (mProgress < 100 && mProgress < progress) {
                        mProgress = progress;
                        mHandler.sendEmptyMessage(ParameterUtils.MSG_WHAT_PROGRESS);
                    }
                } else if (flag == ParameterUtils.FLAG_UPDATE_NOW) {
                    if (mProgressListener != null) {
                        mProgressListener.onProgress(progress);
                    }
                }
            }

            @Override
            public void onFinish(File result) {
                apk_path = result.getAbsolutePath();
                if (flag == ParameterUtils.FLAG_UPDATE_NOW) {
                    if (mProgressListener != null) {
                        mProgressListener.onFinish(apk_path);
                    }
                } else if (flag == ParameterUtils.FLAG_UPDATE) {
                    mHandler.sendEmptyMessage(ParameterUtils.MSG_WHAT_FINISH);
                }
                // 下载完了，cancelled也要设置
                canceled = true;
            }
        });
    }
}
