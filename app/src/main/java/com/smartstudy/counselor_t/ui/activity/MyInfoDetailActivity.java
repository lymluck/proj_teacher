package com.smartstudy.counselor_t.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.SaveItem;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.ui.fragment.MyFragment;

import org.greenrobot.eventbus.EventBus;

/**
 * @author yqy
 * @date on 2018/3/13
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyInfoDetailActivity extends BaseActivity<BasePresenter> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
    }

    @Override
    public void initEvent() {
        topdefaultLeftbutton.setOnClickListener(this);
        topdefaultRighttext.setOnClickListener(this);
        topdefaultRighttext.setClickable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            case R.id.topdefault_righttext:
                EventBus.getDefault().post(new SaveItem());
//                topdefaultRighttext.setClickable(false);
                break;
            default:
                break;
        }
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        setLeftImgVisible(View.VISIBLE);
        setTitleLineVisible(View.VISIBLE);
        setTopdefaultRighttextColor("#078CF1");
        setTitle("个人信息");
        setTopdefaultRighttextVisible(View.VISIBLE);
        setRightTxt("保存");
        topdefaultRighttext.setTextColor(Color.parseColor("#E4E5E6"));
        MyFragment myFragment = new MyFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_myinfo, myFragment);
        transaction.commit();
    }


    public void setPostClick() {
        topdefaultRighttext.setClickable(true);
        topdefaultRighttext.setTextColor(Color.parseColor("#078CF1"));
    }

    public void setPostUnClick() {
        topdefaultRighttext.setClickable(false);
        topdefaultRighttext.setTextColor(Color.parseColor("#E4E5E6"));
    }
}

