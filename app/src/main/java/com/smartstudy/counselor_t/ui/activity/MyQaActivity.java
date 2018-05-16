package com.smartstudy.counselor_t.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.TeacherInfo;
import com.smartstudy.counselor_t.entity.TotalSubQuestion;
import com.smartstudy.counselor_t.listener.OnSendMsgDialogClickListener;
import com.smartstudy.counselor_t.mvp.contract.MyQaActivityContract;
import com.smartstudy.counselor_t.mvp.presenter.MyQaActivityPresenter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.ui.dialog.DialogCreator;
import com.smartstudy.counselor_t.ui.fragment.MyFocusFragment;
import com.smartstudy.counselor_t.ui.fragment.MyQaFragment;
import com.smartstudy.counselor_t.ui.fragment.QaFragment;
import com.smartstudy.counselor_t.util.ConstantUtils;
import com.smartstudy.counselor_t.util.ParameterUtils;
import com.smartstudy.counselor_t.util.SPCacheUtils;
import com.smartstudy.counselor_t.util.Utils;

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
    }

    @Override
    public void initEvent() {
        allAnswer.setOnClickListener(this);
        myAnswer.setOnClickListener(this);
        userIcon.setOnClickListener(this);
        myFocus.setOnClickListener(this);
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
//                DialogCreator.createVedioDialog(this, new OnSendMsgDialogClickListener() {
//                    @Override
//                    public void onPositive(String word) {
//                        DialogCreator.createVedioClaimDialog(MyQaActivity.this, new OnSendMsgDialogClickListener() {
//                            @Override
//                            public void onPositive(String word) {
//
//                            }
//
//                            @Override
//                            public void onNegative() {
//
//                            }
//                        });
//                    }
//                    @Override
//                    public void onNegative() {
//
//                    }
//                });
//                startActivity(new Intent(this, AddGoodDetailActivity.class));
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
        EventBus.getDefault().unregister(this);
    }

    public TextView getSubCountTextView() {
        return tvSubcount;
    }

}
