package com.smartstudy.counselor_t.ui.base;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.base.tools.SystemBarTintManager;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.mvp.base.BaseView;
import com.smartstudy.counselor_t.util.ParameterUtils;
import com.smartstudy.counselor_t.util.ScreenUtils;
import com.smartstudy.counselor_t.util.StringUtis;
import com.smartstudy.counselor_t.util.ToastUtils;
import com.smartstudy.permissions.AfterPermissionGranted;
import com.smartstudy.permissions.AppSettingsDialog;
import com.smartstudy.permissions.Permission;
import com.smartstudy.permissions.PermissionUtil;
import com.smartstudy.slideback.SlideBackActivity;

import java.util.Arrays;
import java.util.List;

/**
 * Created by louis on 2017/12/14.
 */

public abstract class BaseActivity<P extends BasePresenter> extends SlideBackActivity implements BaseView, View.OnClickListener, PermissionUtil.PermissionCallbacks {

    private FrameLayout contentView;
    private RelativeLayout rlytTop;
    protected ImageView topdefaultLeftbutton;
    protected ImageView topdefaultRightbutton;
    protected TextView topdefaultLefttext;
    protected TextView topdefaultCentertitle;
    protected TextView topdefaultRighttext;
    private AppSettingsDialog permissionDialog;
    protected LayoutInflater mInflater;
    private SystemBarTintManager tintManager;
    private boolean darkMode = true;
    private View mView;
    private View topLine;
    protected boolean hasBasePer = false;
    protected P presenter;
    private static String[] mDenyPerms = StringUtis.concatAll(
        Permission.STORAGE, Permission.PHONE, Permission.MICROPHONE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        presenter = initPresenter();
        mInflater = getLayoutInflater();
    }

    @Override
    protected void onResume() {
        initSystemBar();
        requestPermissions();
        super.onResume();
    }

    @AfterPermissionGranted(ParameterUtils.REQUEST_CODE_PERMISSIONS)
    public void requestPermissions() {
        if (!PermissionUtil.hasPermissions(this, mDenyPerms)) {
            hasBasePer = false;
            //申请基本的权限
            PermissionUtil.requestPermissions(this, Permission.getPermissionContent(Arrays.asList(mDenyPerms)),
                ParameterUtils.REQUEST_CODE_PERMISSIONS, mDenyPerms);
        } else {
            hasBasePer = true;
            hasRequestPermission();
        }
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.detach();//在presenter中解绑释放view
            presenter = null;
        }
        if (mInflater != null) {
            mInflater = null;
        }

        if (tintManager != null) {
            tintManager = null;
        }
        if (permissionDialog != null) {
            permissionDialog.dialogDismiss();
            permissionDialog = null;
        }
        super.onDestroy();
    }

    @Override
    public void setContentView(View view) {
        initTopBar();
        initSystemBar();
        mView = view;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        contentView.addView(mView, lp);
        initView();
        // 初始化view后，如果不是自定义titlebar
        if (rlytTop.getVisibility() == View.VISIBLE) {
            transTitleBar(rlytTop);
        }
        initEvent();
    }

    protected <T extends View> T _id(@IdRes int id) {
        return mView.findViewById(id);
    }

    @Override
    public void setContentView(int layoutResID) {
        mView = LayoutInflater.from(this).inflate(layoutResID, null);
        setContentView(mView);
    }

    public void initTopBar() {
        contentView = findViewById(R.id.content_view);
        rlytTop = findViewById(R.id.rlyt_top);
        topdefaultLeftbutton = findViewById(R.id.topdefault_leftbutton);
        topdefaultRightbutton = findViewById(R.id.topdefault_rightbutton);
        topdefaultLefttext = findViewById(R.id.topdefault_lefttext);
        topdefaultCentertitle = findViewById(R.id.topdefault_centertitle);
        topdefaultRighttext = findViewById(R.id.topdefault_righttext);
        topLine = findViewById(R.id.top_line);
    }

    public void initEvent() {
        topdefaultLeftbutton.setOnClickListener(this);
        topdefaultRightbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            default:
                break;
        }
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    @Override
    public void showTip(String message) {
        if (!TextUtils.isEmpty(message)) {
            ToastUtils.shortToast(this, message);
        }
    }

    /**
     * 在子类中初始化对应的presenter
     *
     * @return 相应的presenter
     */
    public abstract P initPresenter();

    /**
     * 初始化view
     */
    public abstract void initView();

    //获取了100%的基本权限
    public void hasRequestPermission() {
    }

    public void setTitleLineVisible(int visible) {
        findViewById(R.id.title_line).setVisibility(visible);
    }

    public void setHeadVisible(int visible) {
        rlytTop.setVisibility(visible);
    }

    public void setLeftImgVisible(int visible) {
        topdefaultLeftbutton.setVisibility(visible);
    }

    public void setRightImgVisible(int visible) {
        topdefaultRightbutton.setVisibility(visible);
    }

    public void setLeftImg(int resId) {
        topdefaultLeftbutton.setImageResource(resId);
    }

    public void setRightImg(int resId) {
        topdefaultRightbutton.setImageResource(resId);
    }

    public void setLeftTxt(String leftTxt) {
        topdefaultLefttext.setText(leftTxt);
    }

    public void setRightTxt(String rightTxt) {
        topdefaultRighttext.setText(rightTxt);
    }

    public void setTitle(String title) {
        topdefaultCentertitle.setText(title);
    }

    public void setTopLineVisibility(int visible) {
        topLine.setVisibility(visible);
    }

    public View getHeadView() {
        return rlytTop;
    }

    public void setTopdefaultLefttextVisible(int visible) {
        topdefaultLefttext.setVisibility(visible);
    }


    public void setTopdefaultRighttextVisible(int visible) {
        topdefaultRighttext.setVisibility(visible);
    }

    public void setTopdefaultLefttextColor(String color) {
        topdefaultLefttext.setTextColor(Color.parseColor(color));
    }

    public void setTopdefaultRighttextColor(String color) {
        topdefaultRighttext.setTextColor(Color.parseColor(color));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> denyPerms) {
        String[] perms = new String[denyPerms.size()];
        mDenyPerms = denyPerms.toArray(perms);
        if (PermissionUtil.shouldShowRationale(this, mDenyPerms)) {
            //继续申请被拒绝了的基本权限
            PermissionUtil.requestPermissions(this, Permission.getPermissionContent(denyPerms),
                requestCode, mDenyPerms);
        } else {
            verifyPermission(denyPerms);
        }
    }

    public void verifyPermission(List<String> denyPerms) {
        if (denyPerms != null && denyPerms.size() > 0) {
            if (permissionDialog != null && permissionDialog.isShowing()) {
                permissionDialog.dialogDismiss();
            }
            permissionDialog = new AppSettingsDialog.Builder(this).build(denyPerms);
            permissionDialog.show();
        }
    }


    /**
     * 沉浸式状态栏
     */
    public void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            if (tintManager == null) {
                tintManager = new SystemBarTintManager(this);
            }
            tintManager.setStatusBarLightMode(this, true);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.app_top_color);
        }
    }

    /**
     * 透明状态栏
     *
     * @param on
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 初始化标题栏,LinearLayout.LayoutParams代表标题栏的父view为LinearLayout
     */
    public void transTitleBar(View titleBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            titleBar.setPadding(0, config.getPixelInsetTop(true), 0, config.getPixelInsetBottom());
            int topHeight = getResources().getDimensionPixelSize(R.dimen.app_top_height);
            int statusBarHeight = ScreenUtils.getStatusHeight(getApplicationContext());
            titleBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, statusBarHeight + topHeight));
            titleBar.setPadding(titleBar.getPaddingLeft(), statusBarHeight, titleBar.getPaddingRight(), 0);
        }
    }

}