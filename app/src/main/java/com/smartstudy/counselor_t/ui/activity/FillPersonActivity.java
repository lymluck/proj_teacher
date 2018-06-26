package com.smartstudy.counselor_t.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.smartstudy.annotation.Route;
import study.smart.baselib.entity.TeacherInfo;
import study.smart.baselib.ui.activity.LoginActivity;
import study.smart.baselib.ui.activity.SelectMyPhotoActivity;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.utils.ConstantUtils;
import study.smart.baselib.utils.DisplayImageUtils;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.baselib.utils.SPCacheUtils;
import study.smart.baselib.utils.ToastUtils;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.IdNameInfo;
import com.smartstudy.counselor_t.mvp.contract.FillPersonContract;
import com.smartstudy.counselor_t.mvp.presenter.FillPersonPresenter;
import study.smart.baselib.ui.widget.TagsLayout;
import com.smartstudy.counselor_t.util.CheckUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * @author yqy
 * @date on 2018/1/16
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
@Route("FillPersonActivity")
public class FillPersonActivity extends BaseActivity<FillPersonContract.Presenter> implements FillPersonContract.View {
    private ImageView ivAvatar;
    private TextView btPostInfo;
    private EditText tvNickName;
    private EditText tvWorkName;
    private EditText tvWorkExperience;
    private EditText tvGraduatedSchool;
    private EditText tvEmail;
    private EditText tvName;
    private File photoFile;
    private boolean isImage = false;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout llCity;
    private TextView tvCity;
    private LinearLayout llGoodBusiness;
    private TagsLayout tlyTags;
    private String cityValue;
    private String bussinessValue;
    List<IdNameInfo> workIdNameInfos = new ArrayList<>();
    List<IdNameInfo> adeptIdNameInfos = new ArrayList<>();
    private EditText tvPersonalProfile;
    private TeacherInfo teacherInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_person_info);
        setTopLineVisibility(View.VISIBLE);
        setLeftImgVisible(View.INVISIBLE);
        setTitle("完善个人信息");
        swipeRefreshLayout = findViewById(R.id.srl_person);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getAuditResult();
            }
        });
        presenter.getOptions();
    }

    @Override
    public FillPersonContract.Presenter initPresenter() {
        return new FillPersonPresenter(this);
    }

    @Override
    public void initEvent() {
        btPostInfo.setOnClickListener(this);
        ivAvatar.setOnClickListener(this);
        llCity.setOnClickListener(this);
        llGoodBusiness.setOnClickListener(this);
        topdefaultLeftbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_post_info:
                if (!CheckUtil.checkEmail(getEmail())) {
                    ToastUtils.shortToast("邮箱不合法");
                    return;
                }
                if (photoFile == null && !isImage) {
                    ToastUtils.shortToast("头像不能为空");
                    return;
                }
                if (!checkInput()) {
                    ToastUtils.shortToast("信息未填写完整");
                    return;
                }
                presenter.postPersonInfo(getNickName(), photoFile, getWorkTitle(), getGraduatedSchool()
                    , getWorkExperience(), getEmail(), getRealName(), cityValue, bussinessValue, getPersonalProfile());
                break;
            case R.id.iv_avatar:
                Intent intent = new Intent(FillPersonActivity.this, SelectMyPhotoActivity.class);
                intent.putExtra("singlePic", true);
                startActivityForResult(intent, ParameterUtils.REQUEST_CODE_CHANGEPHOTO);
                break;

            case R.id.ll_city:
                Intent toCity = new Intent(this, ChooseListActivity.class);
                toCity.putExtra("value", tvCity.getText());
                toCity.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) workIdNameInfos);
                toCity.putExtra("ischange", false);
                toCity.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.WORK_CITY);
                toCity.putExtra("title", "工作城市");
                startActivityForResult(toCity, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                break;
            case R.id.ll_good_business:
                Intent toBuss = new Intent(this, ChooseListActivity.class);
                if (tlyTags.getTabValue() != null) {
                    toBuss.putExtra("value", tlyTags.getTabValue());
                }
                toBuss.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) adeptIdNameInfos);
                toBuss.putExtra("ischange", true);
                toBuss.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.WORK_BUSSIENSS);
                toBuss.putExtra("title", "擅长业务");
                startActivityForResult(toBuss, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                break;

            case R.id.topdefault_leftbutton:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void initView() {
        ivAvatar = findViewById(R.id.iv_avatar);
        btPostInfo = findViewById(R.id.bt_post_info);
        tvNickName = findViewById(R.id.tv_nick_name);
        tvWorkName = findViewById(R.id.tv_work_name);
        tvWorkExperience = findViewById(R.id.tv_work_experience);
        tvGraduatedSchool = findViewById(R.id.tv_graduated_school);
        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
        llCity = findViewById(R.id.ll_city);
        tvCity = findViewById(R.id.tv_work_city);
        llGoodBusiness = findViewById(R.id.ll_good_business);
        tvPersonalProfile = findViewById(R.id.tv_personal_profile);
        tlyTags = findViewById(R.id.tly_tags);
        presenter.getAuditResult();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ParameterUtils.REQUEST_CODE_CHANGEPHOTO:
                final String temppath = data.getStringExtra("path");
                DisplayImageUtils.displayImageFile(getApplicationContext(), temppath, new SimpleTarget<File>(100, 100) {
                    @Override
                    public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                        photoFile = resource;
                        DisplayImageUtils.displayPersonRes(FillPersonActivity.this, resource, ivAvatar);
                    }
                });
                break;

            case ParameterUtils.REQUEST_CODE_EDIT_MYINFO:
                String flag = data.getStringExtra(ParameterUtils.TRANSITION_FLAG);
                if (flag.equals(ParameterUtils.WORK_CITY)) {
                    cityValue = data.getStringExtra("new_value");
                    if (!TextUtils.isEmpty(cityValue)) {
                        tvCity.setText(getCityValue(cityValue));
                    }
                } else {
                    bussinessValue = data.getStringExtra("new_value");
                    List<String> list = Arrays.asList(getBussinessValue(bussinessValue).split(","));
                    tlyTags.setBackGroup(R.drawable.bg_good_bussienss_border);
                    tlyTags.createTab(FillPersonActivity.this, list);
                }
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String ticket = (String) SPCacheUtils.get("ticket", ConstantUtils.CACHE_NULL);
        if (!TextUtils.isEmpty(ticket) && !ConstantUtils.CACHE_NULL.equals(ticket)) {
            presenter.getAuditResult();
        }
    }

    @Override
    public void getStudentInfoDetailSuccess() {
        setBtEnBleClick();
    }

    @Override
    public void showAuditResult(TeacherInfo teacherInfo) {
        swipeRefreshLayout.setRefreshing(false);
        this.teacherInfo = teacherInfo;
        if (teacherInfo.getStatus() == 2) {
            //更新融云
            String imUserId = (String) SPCacheUtils.get("imUserId", "");
            if (!TextUtils.isEmpty(imUserId)) {
                Uri avatarUri = null;
                if (!TextUtils.isEmpty(teacherInfo.getAvatar())) {
                    String avatarUrl = DisplayImageUtils.formatImgUrl(teacherInfo.getAvatar(), ivAvatar.getWidth(), ivAvatar.getHeight());
                    avatarUri = Uri.parse(avatarUrl);
                }
                String name = null;
                if (!TextUtils.isEmpty(teacherInfo.getName())) {
                    name = teacherInfo.getName();
                }
                if (RongIM.getInstance() != null) {
                    if (avatarUri != null && name != null) {
                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(imUserId, name, avatarUri));
                    } else {
                        if (avatarUri != null) {
                            RongIM.getInstance().refreshUserInfoCache(new UserInfo(imUserId, (String) SPCacheUtils.get("name", ""), avatarUri));
                        }
                        if (name != null) {
                            String avatar = (String) SPCacheUtils.get("avatar", "");
                            UserInfo userInfo = new UserInfo(imUserId, name, TextUtils.isEmpty(avatar) ? null : Uri.parse(avatar));
                            RongIM.getInstance().refreshUserInfoCache(userInfo);
                        }
                    }
                }
            }
            this.startActivity(new Intent(this, MyQaActivity.class));
            finish();
        } else if (teacherInfo.getStatus() == 1) {
            btPostInfo.setClickable(false);
            btPostInfo.setTextColor(Color.parseColor("#949BA1"));
            btPostInfo.setText("正在审核中...");
            btPostInfo.setBackgroundResource(R.drawable.bg_submit_review_grey);
        } else {
            btPostInfo.setClickable(true);
            btPostInfo.setTextColor(Color.parseColor("#FFFFFF"));
            btPostInfo.setText("提交审核");
            btPostInfo.setBackgroundResource(R.drawable.bg_submit_review_blue);
        }

        if (!TextUtils.isEmpty(teacherInfo.getAvatar()) && photoFile == null) {
            isImage = true;
            DisplayImageUtils.formatPersonImgUrl(this, teacherInfo.getAvatar(), ivAvatar);
        }
        if (!TextUtils.isEmpty(teacherInfo.getName()) && TextUtils.isEmpty(getNickName())) {
            tvNickName.setText(teacherInfo.getName());
        }
        if (!TextUtils.isEmpty(teacherInfo.getTitle()) && TextUtils.isEmpty(getWorkTitle())) {
            tvWorkName.setText(teacherInfo.getTitle());
        }
        if (!TextUtils.isEmpty(teacherInfo.getYearsOfWorking()) && TextUtils.isEmpty(getWorkExperience())) {
            tvWorkExperience.setText(teacherInfo.getYearsOfWorking());
        }
        if (!TextUtils.isEmpty(teacherInfo.getSchool()) && TextUtils.isEmpty(getGraduatedSchool())) {
            tvGraduatedSchool.setText(teacherInfo.getSchool());
        }
        if (!TextUtils.isEmpty(teacherInfo.getEmail()) && TextUtils.isEmpty(getEmail())) {
            tvEmail.setText(teacherInfo.getEmail());
        }
        if (!TextUtils.isEmpty(teacherInfo.getRealName()) && TextUtils.isEmpty(getRealName())) {
            tvName.setText(teacherInfo.getRealName());
        }

        if (!TextUtils.isEmpty(teacherInfo.getWorkingCityKey()) && TextUtils.isEmpty(getCity())) {
            tvCity.setText(getCityValue(teacherInfo.getWorkingCityKey()));
            cityValue = teacherInfo.getWorkingCityKey();
        }
        if (!TextUtils.isEmpty(teacherInfo.getAdeptWorksKey()) && TextUtils.isEmpty(getBussiness())) {
            List<String> list = Arrays.asList(getBussinessValue(teacherInfo.getAdeptWorksKey()).split(","));
            tlyTags.setBackGroup(R.drawable.bg_good_bussienss_border);
            tlyTags.createTab(FillPersonActivity.this, list);
            bussinessValue = teacherInfo.getAdeptWorksKey();
        }

        if (!TextUtils.isEmpty(teacherInfo.getIntroduction()) && TextUtils.isEmpty(getPersonalProfile())) {
            tvPersonalProfile.setText(teacherInfo.getIntroduction());
        }
    }

    @Override
    public void getOptionsSuccess(List<IdNameInfo> workIdNameInfos, List<IdNameInfo> adeptIdNameInfos) {
        if (this.workIdNameInfos != null) {
            this.workIdNameInfos.clear();
            this.workIdNameInfos.addAll(workIdNameInfos);
        }
        if (this.adeptIdNameInfos != null) {
            this.adeptIdNameInfos.clear();
            this.adeptIdNameInfos.addAll(adeptIdNameInfos);
        }

        if (teacherInfo != null) {
            if (!TextUtils.isEmpty(teacherInfo.getWorkingCityKey()) && TextUtils.isEmpty(getCity())) {
                tvCity.setText(getCityValue(teacherInfo.getWorkingCityKey()));
            }
            if (!TextUtils.isEmpty(teacherInfo.getAdeptWorksKey()) && TextUtils.isEmpty(getBussiness())) {
                List<String> list = Arrays.asList(getBussinessValue(teacherInfo.getWorkingCityKey()).split(","));
                tlyTags.setBackGroup(R.drawable.bg_good_bussienss_border);
                tlyTags.createTab(FillPersonActivity.this, list);
            }
        }

    }

    private String getNickName() {
        return tvNickName.getText().toString().trim();
    }

    private String getWorkTitle() {
        return tvWorkName.getText().toString().trim();
    }

    private String getWorkExperience() {
        return tvWorkExperience.getText().toString().trim();
    }

    private String getGraduatedSchool() {
        return tvGraduatedSchool.getText().toString().trim();
    }

    private String getRealName() {
        return tvName.getText().toString().trim();
    }

    private String getEmail() {
        return tvEmail.getText().toString().trim();
    }

    private String getCity() {
        return tvCity.getText().toString().trim();
    }


    private String getBussiness() {
        return tlyTags.getTabValue();
    }


    private String getPersonalProfile() {
        return tvPersonalProfile.getText().toString().trim();
    }


    private void setBtEnBleClick() {
        btPostInfo.setClickable(false);
        btPostInfo.setTextColor(Color.parseColor("#949BA1"));
        btPostInfo.setText("正在审核中...");
        btPostInfo.setBackgroundResource(R.drawable.bg_submit_review_grey);
    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(getNickName()) || TextUtils.isEmpty(getWorkTitle()) ||
            TextUtils.isEmpty(getWorkExperience()) || TextUtils.isEmpty(getWorkExperience()) ||
            TextUtils.isEmpty(getGraduatedSchool()) || TextUtils.isEmpty(getRealName()) ||
            TextUtils.isEmpty(getEmail()) || TextUtils.isEmpty(getCity()) || TextUtils.isEmpty(getBussiness())
            || TextUtils.isEmpty(getPersonalProfile())) {
            return false;
        }
        return true;
    }

    private String getCityValue(String key) {
        if (workIdNameInfos != null) {
            for (IdNameInfo idNameInfo : workIdNameInfos) {
                if (idNameInfo.getId().equals(key)) {
                    return idNameInfo.getValue();
                }
            }
        }
        return "";
    }

    private String getBussinessValue(String key) {
        String[] ids = key.split(",");
        String value = "";
        if (adeptIdNameInfos != null) {
            for (IdNameInfo idNameInfo : adeptIdNameInfos) {
                for (String id : ids) {
                    if (id.equals(idNameInfo.getId())) {
                        value += idNameInfo.getValue() + ",";
                    }
                }
            }
        }
        return value;
    }
}
