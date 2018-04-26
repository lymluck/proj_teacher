package com.smartstudy.counselor_t.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.ItemOnClick;
import com.smartstudy.counselor_t.entity.SaveItem;
import com.smartstudy.counselor_t.entity.TeacherInfo;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.mvp.contract.MainActivityContract;
import com.smartstudy.counselor_t.mvp.contract.MyInfoDetailContract;
import com.smartstudy.counselor_t.mvp.presenter.MainActivityPresenter;
import com.smartstudy.counselor_t.mvp.presenter.MyInfoDetailPresenter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.ui.fragment.MyFragment;
import com.smartstudy.counselor_t.ui.fragment.QaFragment;
import com.smartstudy.counselor_t.util.ParameterUtils;
import com.smartstudy.counselor_t.util.SPCacheUtils;
import com.smartstudy.counselor_t.util.Utils;

import org.greenrobot.eventbus.EventBus;

import io.rong.imkit.RongIM;

/**
 * @author yqy
 * @date on 2018/3/13
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyInfoDetailActivity extends BaseActivity<MyInfoDetailContract.Presenter> implements MyInfoDetailContract.View {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa_list);
        findViewById(R.id.layout_qa_title).setVisibility(View.GONE);
    }

    @Override
    public MyInfoDetailContract.Presenter initPresenter() {
        return new MyInfoDetailPresenter(this);
    }

    @Override
    public void initEvent() {
        topdefaultLeftbutton.setOnClickListener(this);
        topdefaultRighttext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            case R.id.topdefault_righttext:
                EventBus.getDefault().post(new SaveItem());
                break;
            default:
                break;
        }
    }

    @Override
    public void initView() {
        setTopdefaultLefttextVisible(View.VISIBLE);
        setLeftImgVisible(View.VISIBLE);
        setTitleLineVisible(View.VISIBLE);
        setTopdefaultRighttextColor("#078CF1");
        setTitle("个人信息");
        setTopdefaultRighttextVisible(View.VISIBLE);
        setRightTxt("保存");
        MyFragment myFragment = new MyFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flyt_qa, myFragment);
        transaction.commit();
    }

    @Override
    public void getAuditResult(TeacherInfo teacherInfo) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

