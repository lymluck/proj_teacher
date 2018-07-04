package com.smartstudy.counselor_t.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.TotalSubQuestion;
import com.smartstudy.counselor_t.mvp.contract.MyQaActivityContract;
import com.smartstudy.counselor_t.mvp.presenter.MyQaActivityPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.ui.activity.LoginActivity;
import study.smart.baselib.ui.base.UIFragment;
import study.smart.baselib.ui.widget.NoScrollViewPager;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.baselib.utils.SPCacheUtils;
import study.smart.baselib.utils.ScreenUtils;
import study.smart.baselib.utils.ToastUtils;
import study.smart.baselib.utils.Utils;

/**
 * @author yqy
 * @date on 2018/6/26
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyAllQaFragment extends UIFragment implements ViewPager.OnPageChangeListener {
    private TextView allAnswer;
    private TextView myAnswer;
    private TextView myFocus;
    private TextView tvSubcount;
    private List<Fragment> mFragment = new ArrayList<>();
    public static NoScrollViewPager mViewPager;

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
    }

    @Override
    protected View getLayoutView() {
        EventBus.getDefault().register(this);
        return mActivity.getLayoutInflater().inflate(
            R.layout.activity_qa_list, null);
    }

    @Override
    protected void initView(View rootView) {
        RelativeLayout layoutQaTitle = rootView.findViewById(R.id.layout_qa_title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layoutQaTitle.getLayoutParams();
            params.height = params.height + ScreenUtils.getStatusHeight(mActivity);
            layoutQaTitle.setLayoutParams(params);
            layoutQaTitle.setPadding(0, ScreenUtils.getStatusHeight(mActivity), 0, 0);
        }
        tvSubcount = rootView.findViewById(R.id.tv_subcount);
        allAnswer = rootView.findViewById(R.id.all_answer);
        myAnswer = rootView.findViewById(R.id.my_answer);
        myFocus = rootView.findViewById(R.id.tv_focus);
        mFragment.add(new QaFragment());
        mFragment.add(new MyFocusFragment());
        mFragment.add(new MyQaFragment());
        mViewPager = rootView.findViewById(R.id.main_viewpager);
        mViewPager.setNoScroll(true);
        changeTextViewColor();
        changeSelectedTabState(0);
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }
        };
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(mFragment.size());
        mViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void initEvent() {
        allAnswer.setOnClickListener(this);
        myAnswer.setOnClickListener(this);
        myFocus.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_answer:
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.my_answer:
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.tv_focus:
                mViewPager.setCurrentItem(1, false);
                break;
            default:
                break;
        }
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

    private void changeSelectedTabState(int position) {
        switch (position) {
            case 0:
                allAnswer.setTextColor(Color.parseColor("#ffffff"));
                allAnswer.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_qa_border));
                break;
            case 1:
                myFocus.setTextColor(Color.parseColor("#ffffff"));
                myFocus.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_qa_border));
                break;
            case 2:
                myAnswer.setTextColor(Color.parseColor("#ffffff"));
                myAnswer.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_qa_border));
                break;
            default:
                break;
        }
    }

    public void changeTextViewColor() {
        allAnswer.setBackgroundDrawable(null);
        myFocus.setBackgroundDrawable(null);
        myAnswer.setBackgroundDrawable(null);
        allAnswer.setTextColor(Color.parseColor("#58646E"));
        myFocus.setTextColor(Color.parseColor("#58646E"));
        myAnswer.setTextColor(Color.parseColor("#58646E"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter = null;
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    public TextView getSubCountTextView() {
        return tvSubcount;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changeTextViewColor();
        changeSelectedTabState(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

