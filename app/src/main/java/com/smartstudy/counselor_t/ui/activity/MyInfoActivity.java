package com.smartstudy.counselor_t.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.base.config.BaseRequestConfig;
import com.smartstudy.counselor_t.entity.TeacherInfo;
import com.smartstudy.counselor_t.mvp.contract.MyInfoContract;
import com.smartstudy.counselor_t.mvp.presenter.MyInfoActivityPresenter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.util.DisplayImageUtils;
import com.smartstudy.counselor_t.util.HttpUrlUtils;
import com.smartstudy.counselor_t.util.ParameterUtils;
import com.smartstudy.counselor_t.util.SPCacheUtils;
import com.smartstudy.counselor_t.util.Utils;

import java.util.Map;

import io.rong.imkit.RongIM;

/**
 * @author yqy
 * @date on 2018/1/22
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyInfoActivity extends BaseActivity<MyInfoContract.Presenter> implements MyInfoContract.View {
    private TextView tv_login_out;
    private Button btSave;
    private ImageView ivAvatar;
    private ImageView ivPhoto;
    private EditText tv_nick_name;
    private EditText tv_work_name;
    private EditText tv_work_experience;
    private EditText tv_graduated_school;
    private EditText tv_email;
    private EditText tv_name;

    private TextView tvLoginOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_person_info);
    }


    @Override
    public void initView() {
        tv_login_out = findViewById(R.id.tv_login_out);
        tv_login_out.setVisibility(View.VISIBLE);
        btSave = findViewById(R.id.bt_post_info);
        btSave.setClickable(false);
        btSave.setTextColor(Color.parseColor("#949BA1"));
        btSave.setText("保存");
        btSave.setBackgroundResource(R.drawable.bg_submit_review_grey);
        ivAvatar = findViewById(R.id.iv_avatar);
        ivPhoto = findViewById(R.id.iv_photo);
        tv_nick_name = findViewById(R.id.tv_nick_name);
        tv_work_name = findViewById(R.id.tv_work_name);
        tv_work_experience = findViewById(R.id.tv_work_experience);
        tv_graduated_school = findViewById(R.id.tv_graduated_school);
        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tvLoginOut = findViewById(R.id.tv_login_out);
        if (presenter != null) {
            presenter.getMyInfo();
        }
    }

    @Override
    public void initEvent() {
        topdefaultLeftbutton.setOnClickListener(this);
        tvLoginOut.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            case R.id.tv_login_out:
                presenter.getLogOut();
                break;
            default:
                break;
        }
    }

    @Override
    public MyInfoContract.Presenter initPresenter() {
        return new MyInfoActivityPresenter(this);
    }

    @Override
    public void getMyInfoSuccess(TeacherInfo teacherInfo) {
        if (teacherInfo != null) {
            DisplayImageUtils.displayCircleImage(this, "", ivPhoto);
            if (!TextUtils.isEmpty(teacherInfo.getAvatar())) {
                DisplayImageUtils.displayCircleImage(this, teacherInfo.getAvatar(), ivAvatar);
            }
            tv_nick_name.setText(teacherInfo.getName());
            tv_work_name.setText(teacherInfo.getTitle());
            tv_work_experience.setText(teacherInfo.getYearsOfWorking());
            tv_graduated_school.setText(teacherInfo.getSchool());
            tv_email.setText(teacherInfo.getEmail());
            tv_name.setText(teacherInfo.getRealName());
        }
    }

    @Override
    public void getLogOutSuccess() {
        //友盟账号统计
        SPCacheUtils.put("user_name", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("user_pic", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("ticket", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("user", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("ss_user", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("user_id", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("imUserId", "");
        SPCacheUtils.put("imToken", "");

        Utils.removeCookie(MyInfoActivity.this);
        Intent to_login = new Intent(MyInfoActivity.this, LoginActivity.class);
        to_login.putExtra("toMain", true);
        to_login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(to_login);
    }

}
