package com.smartstudy.counselor_t.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.MyUserInfo;
import com.smartstudy.counselor_t.entity.StudentPageInfo;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.mvp.contract.FillPersonContract;
import com.smartstudy.counselor_t.mvp.contract.StudentActivityContract;
import com.smartstudy.counselor_t.mvp.presenter.FillPersonPresenter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.util.ConstantUtils;
import com.smartstudy.counselor_t.util.DisplayImageUtils;
import com.smartstudy.counselor_t.util.SPCacheUtils;

/**
 * @author yqy
 * @date on 2018/1/16
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class FillPersonActivity extends BaseActivity<FillPersonContract.Presenter> implements FillPersonContract.View {

    private ImageView ivAvatar;
    private ImageView ivPhoto;
    private Button btPostInfo;
    private EditText tv_nick_name;
    private EditText tv_work_name;
    private EditText tv_work_experience;
    private EditText tv_graduated_school;
    private EditText tv_email;
    private EditText tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_person_info);
        setTopLineVisibility(View.VISIBLE);
        setLeftImgVisible(View.INVISIBLE);
        setTitle("完善个人信息");
    }

    @Override
    public FillPersonContract.Presenter initPresenter() {
        return new FillPersonPresenter(this);
    }


    @Override
    public void initEvent() {
        btPostInfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_post_info:
                presenter.postPersonInfo(getNickName(), "", getWorkTitle(), getGraduatedSchool(), getWorkExperience(), getEmail(), getRealName());
                break;
            default:
                break;
        }
    }

    @Override
    public void initView() {
        String ticket = (String) SPCacheUtils.get("ticket", ConstantUtils.CACHE_NULL);
//        Log.w("kim","ticket---->"+ticket);
        if (!TextUtils.isEmpty(ticket) && ConstantUtils.CACHE_NULL.equals(ticket)) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        ivAvatar = findViewById(R.id.iv_avatar);
        ivPhoto = findViewById(R.id.iv_photo);
        btPostInfo = findViewById(R.id.bt_post_info);
        tv_nick_name = findViewById(R.id.tv_nick_name);
        tv_work_name = findViewById(R.id.tv_work_name);
        tv_work_experience = findViewById(R.id.tv_work_experience);
        tv_graduated_school = findViewById(R.id.tv_graduated_school);
        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        DisplayImageUtils.displayCircleImage(this, "77/fa/77fa2d9eb368d911e2ecb809212ea2d451fc.jpg", ivPhoto);
//        DisplayImageUtils.displayCircleImage(this,"https://bkd-media.smartstudy.com/user/avatar/default/77/fa/77fa2d9eb368d911e2ecb809212ea2d451fc.jpg",ivAvatar);
    }

    @Override
    public void getStudentInfoDetailSuccess() {
        setBtEnBleClick();
    }


    private String getNickName() {
        return tv_nick_name.getText().toString().trim();
    }

    private String getWorkTitle() {
        return tv_work_name.getText().toString().trim();
    }

    private String getWorkExperience() {
        return tv_work_experience.getText().toString().trim();
    }

    private String getGraduatedSchool() {
        return tv_graduated_school.getText().toString().trim();
    }

    private String getRealName() {
        return tv_name.getText().toString().trim();
    }

    private String getEmail() {
        return tv_email.getText().toString().trim();
    }

    private void setBtEnBleClick() {
        btPostInfo.setClickable(false);
        btPostInfo.setTextColor(Color.parseColor("#949BA1"));
        btPostInfo.setBackgroundResource(R.drawable.bg_submit_review_grey);
    }
}
