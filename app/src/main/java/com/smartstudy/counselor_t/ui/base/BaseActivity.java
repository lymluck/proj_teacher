package com.smartstudy.counselor_t.ui.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.mvp.base.BaseView;
import com.smartstudy.counselor_t.util.ParameterUtils;
import com.smartstudy.counselor_t.util.StringUtis;
import com.smartstudy.counselor_t.util.ToastUtils;
import com.smartstudy.permissions.AfterPermissionGranted;
import com.smartstudy.permissions.AppSettingsDialog;
import com.smartstudy.permissions.Permission;
import com.smartstudy.permissions.PermissionUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by louis on 2017/12/14.
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView, View.OnClickListener, PermissionUtil.PermissionCallbacks {

    private FrameLayout contentView;
    private RelativeLayout rlytTop;
    protected ImageView topdefaultLeftbutton;
    protected ImageView topdefaultRightbutton;
    protected TextView topdefaultLefttext;
    protected TextView topdefaultCentertitle;
    protected TextView topdefaultRighttext;
    private AppSettingsDialog permissionDialog;
    protected LayoutInflater mInflater;

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
        if (permissionDialog != null) {
            permissionDialog.dialogDismiss();
            permissionDialog = null;
        }
        super.onDestroy();
    }

    @Override
    public void setContentView(View view) {
        init();
        mView = view;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        contentView.addView(mView, lp);
        initView();
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

    public void init() {
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
//        topdefaultLefttext.setOnClickListener(this);
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

    public View getHeadView(){return  rlytTop;}

    public void setTopdefaultLefttextVisible(int visible) {
        topdefaultLefttext.setVisibility(visible);
    }


    public void setTopdefaultRighttextVisible(int visible) {
        topdefaultRighttext.setVisibility(visible);
    }

    public void setTopdefaultLefttextColor(String color) {
        topdefaultLefttext.setTextColor(Color.parseColor(color));
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
}