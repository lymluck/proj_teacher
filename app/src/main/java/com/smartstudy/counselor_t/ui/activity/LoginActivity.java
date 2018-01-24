package com.smartstudy.counselor_t.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.handler.WeakHandler;
import com.smartstudy.counselor_t.listener.SoftKeyBoardListener;
import com.smartstudy.counselor_t.mvp.contract.LoginActivityContract;
import com.smartstudy.counselor_t.mvp.presenter.LoginAcitivityPresenter;
import com.smartstudy.counselor_t.ui.MainActivity;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.ui.widget.EditTextWithClear;
import com.smartstudy.counselor_t.util.AppUtils;
import com.smartstudy.counselor_t.util.ConstantUtils;
import com.smartstudy.counselor_t.util.DensityUtils;
import com.smartstudy.counselor_t.util.HttpUrlUtils;
import com.smartstudy.counselor_t.util.KeyBoardUtils;
import com.smartstudy.counselor_t.util.SPCacheUtils;
import com.smartstudy.counselor_t.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends BaseActivity<LoginActivityContract.Presenter> implements LoginActivityContract.View {
    private EditTextWithClear etcmobile;
    private EditText etc_yzm;
    private TextView tv_yzm;
    private Button btn_login;
    private TextView tv_xxd_contract;

    private CountDownTimer countDownTimer = null;
    private ImageView img_logo;
    private FrameLayout flt_logo;
    private List<View> list = null;
    private boolean isSelected = false;
    private boolean isDebug = AppUtils.isApkInDebug(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public LoginActivityContract.Presenter initPresenter() {
        return new LoginAcitivityPresenter(this);
    }


    @Override
    public void initView() {
        setLeftImgVisible(View.GONE);
        setRightImg(R.drawable.ic_cha);
        setRightImgVisible(View.VISIBLE);
        img_logo = _id(R.id.img_logo);
        flt_logo = _id(R.id.flt_logo);
        tv_xxd_contract = _id(R.id.tv_xxd_contract);
        tv_xxd_contract.setText(Html.fromHtml("登录即表示同意" + "<font color=#078CF1>"
                + "选校帝用户协议" + "</font>"));
        tv_yzm = _id(R.id.tv_yzm);
        btn_login = _id(R.id.btn_login);
        etcmobile = _id(R.id.etc_mobile);
        etc_yzm = _id(R.id.etc_yzm);
        etc_yzm.setInputType(InputType.TYPE_CLASS_NUMBER);
        etc_yzm.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etc_yzm.setHint("请输入验证码");
        etcmobile.getMyEditText().setHint("请输入您的手机号");
        etcmobile.getMyEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        etcmobile.setInputType(InputType.TYPE_CLASS_NUMBER);
        String account = (String) SPCacheUtils.get("user_account", ConstantUtils.CACHE_NULL);
        if (!ConstantUtils.CACHE_NULL.equals(account)) {
            etcmobile.setText(account);
        }
    }

    @Override
    public void initEvent() {
        super.initEvent();
        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                KeyBoardUtils.openKeybord(etcmobile.getMyEditText(), LoginActivity.this);
            }
        }, 300);
        etc_yzm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (TextUtils.isEmpty(etcmobile.getText()) || etcmobile.getText().length() != 11) {
                        ToastUtils.shortToast(LoginActivity.this, getString(R.string.enter_phone));
                    } else if (TextUtils.isEmpty(etc_yzm.getText())) {
                        ToastUtils.shortToast(LoginActivity.this, String.format(getString(R.string.not_null), "验证码"));
                    } else {
                        presenter.phoneCodeLogin(etcmobile.getText(), etc_yzm.getText().toString());
                    }
                }
                return true;
            }
        });
        btn_login.setOnClickListener(this);
        tv_xxd_contract.setOnClickListener(this);
        tv_yzm.setOnClickListener(this);
        animEvent();
        forTest();
    }

    private void animEvent() {
        final AnimatorSet set = new AnimatorSet();
        //当键盘弹起的时候用屏幕的高度减去布局的高度，同时获取到键盘的高度，用键盘的高度和剩余的高度做对比
        SoftKeyBoardListener.setListener(LoginActivity.this, new

                SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {

                    @Override
                    public void keyBoardShow(int height) {
                        set.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) flt_logo.getLayoutParams();
                                layoutParams.height = 0;
                                flt_logo.setLayoutParams(layoutParams);
                            }

                        });
                        set.playTogether(
                                ObjectAnimator.ofFloat(flt_logo, "scaleX", 1, 0f),
                                ObjectAnimator.ofFloat(flt_logo, "scaleY", 1, 0f),
                                ObjectAnimator.ofFloat(flt_logo, "alpha", 1, 0.25f, 1)
                        );
                        set.setDuration(300).start();
                    }

                    @Override
                    public void keyBoardHide(int height) {
                        AnimatorSet set = new AnimatorSet();
                        set.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) flt_logo.getLayoutParams();
                                layoutParams.height = getResources().getDimensionPixelOffset(R.dimen.login_logo_wh);
                                flt_logo.setLayoutParams(layoutParams);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationStart(animation);
                            }
                        });
                        set.playTogether(
                                ObjectAnimator.ofFloat(flt_logo, "scaleX", 0, 1f),
                                ObjectAnimator.ofFloat(flt_logo, "scaleY", 0, 1f),
                                ObjectAnimator.ofFloat(flt_logo, "alpha", 1, 0.25f, 1)
                        );
                        set.setDuration(300).start();
                    }
                });
    }

    private void forTest() {
        //测试用
//        if (isDebug) {
        img_logo.setOnClickListener(this);
        _id(R.id.btn_master).setOnClickListener(this);
        _id(R.id.btn_test).setOnClickListener(this);
        _id(R.id.btn_dev).setOnClickListener(this);
        list = new ArrayList<>();
        list.add(_id(R.id.btn_master));
        list.add(_id(R.id.btn_test));
        list.add(_id(R.id.btn_dev));
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        img_logo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ToastUtils.shortToast(LoginActivity.this, "手机屏幕分辨率:" + displayMetrics.widthPixels + "X" + displayMetrics.heightPixels + "\n" + "密度:"
                        + displayMetrics.density + "\n" + "使用每英寸的像素点来显示密度:" + displayMetrics.densityDpi + "\n" + "xdpi"
                        + displayMetrics.xdpi + "\n" + "ydpi" + displayMetrics.ydpi + "\n" + "scaledDensity"
                        + displayMetrics.scaledDensity);
                return false;
            }
        });
//        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_login:
                if (TextUtils.isEmpty(etcmobile.getText()) || etcmobile.getText().length() != 11) {
                    ToastUtils.shortToast(LoginActivity.this, getString(R.string.enter_phone));
                    return;
                } else if (TextUtils.isEmpty(etc_yzm.getText())) {
                    ToastUtils.shortToast(LoginActivity.this, String.format(getString(R.string.not_null), "验证码"));
                    return;
                }
                presenter.phoneCodeLogin(etcmobile.getText(), etc_yzm.getText().toString());
                break;
            case R.id.tv_yzm:
                tv_yzm.setEnabled(false);
                //获取验证码,手机号加密
                if (TextUtils.isEmpty(etcmobile.getText()) || etcmobile.getText().length() != 11) {
                    ToastUtils.shortToast(LoginActivity.this, getString(R.string.enter_phone));
                    tv_yzm.setEnabled(true);
                    return;
                }
                presenter.getPhoneCode(etcmobile.getText());
                break;
            case R.id.topdefault_rightbutton:
                KeyBoardUtils.closeKeybord(etcmobile.getMyEditText(), this);
                finish();
                break;
            case R.id.tv_xxd_contract:
                Bundle data = new Bundle();
                data.putString("web_url", HttpUrlUtils.getWebUrl(HttpUrlUtils.URL_USER_CONTRACT));
                data.putString("title", "选校帝用户协议");
                data.putString("url_action", "get");
//                startActivity(new Intent(this,));
                break;
            case R.id.img_logo:
                //测试用
//                if (isDebug) {
                if (!isSelected) {
                    startAnimator();
                } else {
                    endAnimator();
                }
//                }
                break;
            case R.id.btn_master:
                //测试用
//                if (isDebug) {
                if (!isSelected) {
                    startAnimator();
                } else {
                    endAnimator();
                }
                SPCacheUtils.put(ConstantUtils.API_SERVER, "master");
//                }
                break;
            case R.id.btn_test:
                //测试用
//                if (isDebug) {
                if (!isSelected) {
                    startAnimator();
                } else {
                    endAnimator();
                }
                SPCacheUtils.put(ConstantUtils.API_SERVER, "test");
//                }
                break;
            case R.id.btn_dev:
                //测试用
//                if (isDebug) {
                if (!isSelected) {
                    startAnimator();
                } else {
                    endAnimator();
                }
                SPCacheUtils.put(ConstantUtils.API_SERVER, "dev");
//                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        KeyBoardUtils.closeKeybord(etcmobile.getMyEditText(), this);
        super.onBackPressed();

    }

    @Override
    public void getPhoneCodeSuccess() {
        startTimer();
    }

    @Override
    public void phoneCodeLoginSuccess(int status) {
        KeyBoardUtils.closeKeybord(etc_yzm, this);
    }

    protected void startTimer() {
        countDownTimer = new CountDownTimer(ConstantUtils.SMS_TIMEOUT, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                tv_yzm.setTextColor(getResources().getColor(R.color.app_text_color));
                tv_yzm.setText(String.format(getString(R.string.get_code_again), millisUntilFinished / 1000 + "s"));
                tv_yzm.setEnabled(false);//防止重复点击
            }

            @Override
            public void onFinish() {
                //可以在这做一些操作,如果没有获取到验证码再去请求服务器
                tv_yzm.setEnabled(true);//防止重复点击
                tv_yzm.setTextColor(getResources().getColor(R.color.app_main_color));
                tv_yzm.setText(getString(R.string.send_again));
            }
        };
        countDownTimer.start();
    }

    private void endAnimator() {
        isSelected = false;
        ObjectAnimator animator = ObjectAnimator.ofFloat(img_logo, "rotation", 0F, 360F).setDuration(300);
        animator.setInterpolator(new BounceInterpolator());//设置插值器
        animator.start();//开始动画
        int n = DensityUtils.dip2px(45);
        ObjectAnimator objectAnimator;
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                objectAnimator = ObjectAnimator.ofFloat(list.get(i), "translationX", DensityUtils.dip2px(52), 0F).setDuration(1000);
            } else {
                objectAnimator = ObjectAnimator.ofFloat(list.get(i), "translationX", n * (i + 1), 0F).setDuration(1000);
            }
            objectAnimator.start();
        }
    }

    private void startAnimator() {
        isSelected = true;
        int n = DensityUtils.dip2px(45);
        for (int i = 0; i < 3; i++) {
            ObjectAnimator animator;
            if (i == 0) {
                animator = ObjectAnimator.ofFloat(list.get(i), "translationX", 0F, DensityUtils.dip2px(52)).setDuration(1000);
            } else {
                animator = ObjectAnimator.ofFloat(list.get(i), "translationX", 0F, n * (i + 1)).setDuration(1000);
            }
            //设置插值器
            animator.setInterpolator(new BounceInterpolator());
            animator.start();
        }
        ObjectAnimator.ofFloat(img_logo, "rotation", 0F, 360F).setDuration(300).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
    }

    @Override
    public void showTip(String message) {
        super.showTip(message);
        tv_yzm.setEnabled(true);
    }
}
