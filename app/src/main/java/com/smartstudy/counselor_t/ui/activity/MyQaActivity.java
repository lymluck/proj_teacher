package com.smartstudy.counselor_t.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.smartstudy.annotation.Route;

import study.smart.baselib.BaseApplication;
import study.smart.baselib.entity.TeacherInfo;
import study.smart.baselib.listener.OnProgressListener;
import study.smart.baselib.ui.activity.LoginActivity;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.utils.ConstantUtils;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.baselib.utils.SPCacheUtils;
import study.smart.baselib.utils.ScreenUtils;
import study.smart.baselib.utils.Utils;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.TotalSubQuestion;
import com.smartstudy.counselor_t.handler.WeakHandler;
import com.smartstudy.counselor_t.mvp.contract.MyQaActivityContract;
import com.smartstudy.counselor_t.mvp.presenter.MyQaActivityPresenter;
import com.smartstudy.counselor_t.service.VersionUpdateService;
import study.smart.baselib.ui.widget.dialog.AppBasicDialog;
import study.smart.baselib.ui.widget.dialog.DialogCreator;
import com.smartstudy.counselor_t.ui.fragment.MyFocusFragment;
import com.smartstudy.counselor_t.ui.fragment.MyQaFragment;
import com.smartstudy.counselor_t.ui.fragment.QaFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.rong.imkit.RongIM;

/**
 * @author yqy
 * @date on 2018/3/13
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */

@Route("MyQaActivity")
public class MyQaActivity extends BaseActivity<MyQaActivityContract.Presenter> implements MyQaActivityContract.View {
    private TextView allAnswer;
    private TextView myAnswer;
    private TextView myFocus;
    private Bundle state;
    private QaFragment qaFragment;
    private MyFocusFragment myFocusFragment;
    private FragmentManager mfragmentManager;
    private MyQaFragment myFragment;
    private TextView tvSubcount;
    private ImageView userIcon;
    private int update_type;
    private AppBasicDialog updateDialog;
    private String apk_path;
    private WeakHandler myHandler;
    private boolean isBinded;
    private VersionUpdateService.DownloadBinder binder;
    private Button updateBtn;
    private TextView updateTitle;
    private ProgressBar progressbar;
    private TextView tv_progress;
    private boolean isDestroy = true;
    private String mLastVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 首页禁用滑动返回
        setSlideable(false);
        super.onCreate(savedInstanceState);
        state = savedInstanceState;
        setContentView(R.layout.activity_qa_list);
        EventBus.getDefault().register(this);
    }

    @Override
    public MyQaActivityContract.Presenter initPresenter() {
        return new MyQaActivityPresenter(this);
    }

    @Override
    public void initView() {
        setHeadVisible(View.GONE);
        transTitleBar(findViewById(R.id.layout_qa_title));
        tvSubcount = findViewById(R.id.tv_subcount);
        allAnswer = findViewById(R.id.all_answer);
        myAnswer = findViewById(R.id.my_answer);
        myFocus = findViewById(R.id.tv_focus);
        userIcon = findViewById(R.id.user_icon);
        String ticket = (String) SPCacheUtils.get("ticket", ConstantUtils.CACHE_NULL);
        if (!TextUtils.isEmpty(ticket) && ConstantUtils.CACHE_NULL.equals(ticket)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            presenter.getAuditResult();
        }
        allAnswer.setSelected(true);
        mfragmentManager = getSupportFragmentManager();
        hideFragment(mfragmentManager);
        if (state == null) {
            presenter.showFragment(mfragmentManager, ParameterUtils.FRAGMENT_ONE);
        }
        if (!BaseApplication.getInstance().isDownload()) {
            presenter.checkVersion();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //继续下载apk
        if (isDestroy && BaseApplication.getInstance().isDownload()) {
            Intent it = new Intent(MyQaActivity.this, VersionUpdateService.class);
            it.putExtra("version", mLastVersion);
            startService(it);
            bindService(it, conn, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isDestroy = false;
    }

    @Override
    public void initEvent() {
        allAnswer.setOnClickListener(this);
        myAnswer.setOnClickListener(this);
        userIcon.setOnClickListener(this);
        myFocus.setOnClickListener(this);
        myHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case ParameterUtils.FLAG_UPDATE:
                        progressbar.setProgress(msg.arg1);
                        tv_progress.setText(msg.arg1 + "%");
                        break;
                    case ParameterUtils.MSG_WHAT_FINISH:
                        progressbar.setProgress(100);
                        tv_progress.setText("100%");
                        updateBtn.setVisibility(View.VISIBLE);
                        updateBtn.setText("安装");
                        updateTitle.setText("立即安装");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void getAuditResult(TeacherInfo teacherInfo) {
        if (teacherInfo != null) {
            if (teacherInfo.getStatus() != 2) {
                startActivity(new Intent(this, FillPersonActivity.class));
                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_icon:
                startActivity(new Intent(this, MyInfoDetailActivity.class));
                break;
            case R.id.all_answer:
                presenter.showFragment(mfragmentManager, ParameterUtils.FRAGMENT_ONE);
                break;
            case R.id.my_answer:
                presenter.showFragment(mfragmentManager, ParameterUtils.FRAGMENT_TWO);
                break;
            case R.id.tv_focus:
                presenter.showFragment(mfragmentManager, ParameterUtils.FRAGMENT_THREE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    /**
     * 保存fragment状态
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(ParameterUtils.FRAGMENT_TAG, presenter.currentIndex());
        super.onSaveInstanceState(outState);
    }

    /**
     * 复位fragment状态
     *
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            hideFragment(mfragmentManager);
            presenter.showFragment(mfragmentManager, savedInstanceState.getInt(ParameterUtils.FRAGMENT_TAG));
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void getLogOutSuccess() {
        RongIM.getInstance().logout();
        SPCacheUtils.put("phone", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("name", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("avatar", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("ticket", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("orgId", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("title", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("imToken", "");
        SPCacheUtils.put("imUserId", "");
        Utils.removeCookie(this);
        Intent to_login = new Intent(this, LoginActivity.class);
        to_login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
            Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(to_login);
    }


    @Override
    public void hideFragment(FragmentManager fragmentManager) {
        //如果不为空，就先隐藏起来
        if (fragmentManager != null && fragmentManager.getFragments().size() > 0) {
            for (Fragment fragment : fragmentManager.getFragments()) {
                fragment.setUserVisibleHint(false);
                if (fragment.isAdded()) {
                    fragmentManager.beginTransaction().hide(fragment)
                        .commitAllowingStateLoss();
                }
            }
        }
        allAnswer.setSelected(false);
        myAnswer.setSelected(false);
        myFocus.setSelected(false);
    }

    @Override
    public void showQaFragment(FragmentTransaction ft) {
        allAnswer.setSelected(true);
        /**
         * 如果Fragment为空，就新建一个实例
         * 如果不为空，就将它从栈中显示出来
         */
        if (qaFragment == null) {
            qaFragment = new QaFragment();
            ft.add(R.id.flyt_qa, qaFragment);
        } else {
            ft.show(qaFragment);
        }
        qaFragment.setUserVisibleHint(true);
        ft = null;
    }

    @Override
    public void showMyQaFragment(FragmentTransaction ft) {
        myAnswer.setSelected(true);
        /**
         * 如果Fragment为空，就新建一个实例
         * 如果不为空，就将它从栈中显示出来
         */
        if (myFragment == null) {
            myFragment = new MyQaFragment();
            ft.add(R.id.flyt_qa, myFragment);
        } else {
            ft.show(myFragment);
        }
        myFragment.setUserVisibleHint(true);
        ft = null;
    }

    @Override
    public void showMyFocus(FragmentTransaction ft) {
        myFocus.setSelected(true);
        if (myFocusFragment == null) {
            myFocusFragment = new MyFocusFragment();
            ft.add(R.id.flyt_qa, myFocusFragment);
        } else {
            ft.show(myFocusFragment);
        }
        myFocusFragment.setUserVisibleHint(true);
        ft = null;
    }

    @Override
    public void updateable(final String downUrl, String version, String des) {
        this.mLastVersion = version;
        update_type = ParameterUtils.FLAG_UPDATE;
        //提示当前有版本更新
        String isToUpdate = (String) SPCacheUtils.get("isToUpdate", "");
        if ("".equals(isToUpdate) || "yes".equals(isToUpdate)) {
            updateDialog = DialogCreator.createAppBasicDialog(this, getString(R.string.version_update), des,
                getString(R.string.update_vs_now), getString(R.string.not_update), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.positive_btn:
                                BaseApplication.getInstance().setDownload(true);
                                BaseApplication.getInstance().setDownLoadUrl(downUrl);
                                //开始下载
                                Intent it = new Intent(MyQaActivity.this, VersionUpdateService.class);
                                it.putExtra("version", mLastVersion);
                                startService(it);
                                bindService(it, conn, Context.BIND_AUTO_CREATE);
                                updateDialog.dismiss();
                                break;
                            case R.id.negative_btn:
                                SPCacheUtils.put("isToUpdate", "no");
                                updateDialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });
            ((TextView) updateDialog.findViewById(R.id.dialog_info)).setGravity(Gravity.CENTER_VERTICAL);
            WindowManager.LayoutParams p = updateDialog.getWindow().getAttributes();
            p.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
            updateDialog.getWindow().setAttributes(p);
            updateDialog.show();
        }
    }

    @Override
    public void forceUpdate(final String downUrl, String version, String des) {
        this.mLastVersion = version;
        update_type = ParameterUtils.FLAG_UPDATE_NOW;
        String title = getString(R.string.update_vs_now);
        updateDialog = DialogCreator.createBaseCustomDialog(MyQaActivity.this, title, des, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (apk_path != null) {
                    Utils.installApk(getApplicationContext(), apk_path);
                } else {
                    BaseApplication.getInstance().setDownload(true);
                    BaseApplication.getInstance().setDownLoadUrl(downUrl);
                    //开始下载
                    Intent it = new Intent(MyQaActivity.this, VersionUpdateService.class);
                    it.putExtra("version", mLastVersion);
                    startService(it);
                    bindService(it, conn, Context.BIND_AUTO_CREATE);
                }
            }
        });
        progressbar = updateDialog.findViewById(R.id.progressbar);
        tv_progress = updateDialog.findViewById(R.id.tv_progress);
        updateDialog.findViewById(R.id.dialog_base_confirm_btn).setBackgroundResource(R.drawable.bg_btn_wm_lrb_radius);
        ((TextView) updateDialog.findViewById(R.id.dialog_base_text_tv)).setGravity(Gravity.CENTER_VERTICAL);
        WindowManager.LayoutParams p = updateDialog.getWindow().getAttributes();
        p.width = (int) (ScreenUtils.getScreenWidth() * 0.85);
        updateDialog.getWindow().setAttributes(p);
        updateDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(TotalSubQuestion totalSubQuestion) {
        if (totalSubQuestion != null) {
            if (totalSubQuestion.getTotalSubQuestionCount() == 0) {
                tvSubcount.setVisibility(View.GONE);
            } else {
                tvSubcount.setVisibility(View.VISIBLE);
                if (totalSubQuestion.getTotalSubQuestionCount() < 100) {
                    if (totalSubQuestion.getTotalSubQuestionCount() < 10) {
                        tvSubcount.setBackgroundResource(R.drawable.bg_circle_red);
                    } else {
                        tvSubcount.setBackgroundResource(R.drawable.bg_count_answer);
                    }
                    tvSubcount.setText(totalSubQuestion.getTotalSubQuestionCount() + "");
                } else {
                    tvSubcount.setBackgroundResource(R.drawable.bg_count_answer);
                    tvSubcount.setText("99+");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter = null;
        }
        if (state != null) {
            state = null;
        }

        if (updateDialog != null) {
            updateDialog.dismiss();
            updateDialog = null;
        }
        if (myHandler != null) {
            myHandler = null;
        }
        if (isBinded) {
            unbindService(conn);
        }
        if (binder != null && binder.isCanceled()) {
            Intent it = new Intent(this, VersionUpdateService.class);
            it.putExtra("version", mLastVersion);
            stopService(it);
            binder = null;
        }

        EventBus.getDefault().unregister(this);
    }

    public TextView getSubCountTextView() {
        return tvSubcount;
    }

    /**
     * 连接版本下载service
     */
    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBinded = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (VersionUpdateService.DownloadBinder) service;
            // 开始下载
            isBinded = true;
            binder.start(update_type);
            if (update_type == ParameterUtils.FLAG_UPDATE_NOW) {
                updateBtn = updateDialog.findViewById(R.id.dialog_base_confirm_btn);
                updateTitle = updateDialog.findViewById(R.id.dialog_base_title_tv);
                updateBtn.setVisibility(View.GONE);
                updateDialog.findViewById(R.id.dialog_base_text_tv).setVisibility(View.GONE);
                updateDialog.findViewById(R.id.llyt_progress).setVisibility(View.VISIBLE);
                updateTitle.setText("开始更新");
                binder.setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(int progress) {
                        if (progress < 100) {
                            Message msg = Message.obtain();
                            msg.what = ParameterUtils.FLAG_UPDATE;
                            msg.arg1 = progress;
                            myHandler.sendMessage(msg);
                        }
                    }

                    @Override
                    public void onFinish(final String path) {
                        apk_path = path;
                        Message msg = Message.obtain();
                        msg.what = ParameterUtils.MSG_WHAT_FINISH;
                        myHandler.sendMessage(msg);
                    }
                });
            }
        }
    };

}
