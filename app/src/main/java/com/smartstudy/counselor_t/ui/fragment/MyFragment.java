package com.smartstudy.counselor_t.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.IdNameInfo;
import com.smartstudy.counselor_t.entity.SaveItem;
import com.smartstudy.counselor_t.entity.TeacherInfo;
import com.smartstudy.counselor_t.mvp.contract.MyInfoContract;
import com.smartstudy.counselor_t.mvp.presenter.MyInfoActivityPresenter;
import com.smartstudy.counselor_t.ui.activity.ChooseListActivity;
import com.smartstudy.counselor_t.ui.activity.LoginActivity;
import com.smartstudy.counselor_t.ui.activity.SelectMyPhotoActivity;
import com.smartstudy.counselor_t.ui.base.UIFragment;
import com.smartstudy.counselor_t.ui.widget.TagsLayout;
import com.smartstudy.counselor_t.util.CheckUtil;
import com.smartstudy.counselor_t.util.DisplayImageUtils;
import com.smartstudy.counselor_t.util.ParameterUtils;
import com.smartstudy.counselor_t.util.SPCacheUtils;
import com.smartstudy.counselor_t.util.ToastUtils;
import com.smartstudy.counselor_t.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.rong.imkit.RongIM;

/**
 * @author yqy
 * @date on 2018/2/6
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyFragment extends UIFragment<MyInfoContract.Presenter> implements MyInfoContract.View {
    private ImageView ivAvatar;
    private ImageView ivTeacherBg;
    private EditText tvNickName;
    private EditText tvWorkName;
    private EditText tvWorkExperience;
    private EditText tvGraduatedSchool;
    private EditText tvEmail;
    private EditText tvName;
    private File photoFile;
    private LinearLayout llCity;
    private TextView tvCity;
    private LinearLayout llGoodBusiness;
    private TagsLayout tlyTags;
    private String cityValue;
    private String bussinessValue;
    private TeacherInfo teacherInfo;
    private TextView tvLoginOut;
    List<IdNameInfo> workIdNameInfos = new ArrayList<>();
    List<IdNameInfo> adeptIdNameInfos = new ArrayList<>();
    private EditText tvPersonalProfile;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    protected View getLayoutView() {
        return mActivity.getLayoutInflater().inflate(R.layout.fragment_person_info, null);
    }

    @Override
    protected void initView(View rootView) {
        ivAvatar = rootView.findViewById(R.id.iv_avatar);
        ivTeacherBg = rootView.findViewById(R.id.iv_teacher_bg);
        tvNickName = rootView.findViewById(R.id.tv_nick_name);
        tvWorkName = rootView.findViewById(R.id.tv_work_name);
        tvWorkExperience = rootView.findViewById(R.id.tv_work_experience);
        tvGraduatedSchool = rootView.findViewById(R.id.tv_graduated_school);
        tvName = rootView.findViewById(R.id.tv_name);
        tvEmail = rootView.findViewById(R.id.tv_email);
        llCity = rootView.findViewById(R.id.ll_city);
        tvCity = rootView.findViewById(R.id.tv_work_city);
        llGoodBusiness = rootView.findViewById(R.id.ll_good_business);
        tvLoginOut = rootView.findViewById(R.id.tv_login_out);
        tlyTags = rootView.findViewById(R.id.tly_tags);
        tvPersonalProfile = rootView.findViewById(R.id.tv_personal_profile);
        presenter.getOptions();
        if (presenter != null) {
            presenter.getMyInfo();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_out:
                showNormalDialog();
                break;
            case R.id.ll_city:
                Intent toCity = new Intent(mActivity, ChooseListActivity.class);
                toCity.putExtra("value", tvCity.getText());
                toCity.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) workIdNameInfos);
                toCity.putExtra("ischange", false);
                toCity.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.WORK_CITY);
                toCity.putExtra("title", "工作城市");
                startActivityForResult(toCity, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                break;
            case R.id.ll_good_business:
                Intent toBuss = new Intent(mActivity, ChooseListActivity.class);
                if (tlyTags.getTabValue() != null) {
                    toBuss.putExtra("value", tlyTags.getTabValue());
                }
                toBuss.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) adeptIdNameInfos);
                toBuss.putExtra("ischange", true);
                toBuss.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.WORK_BUSSIENSS);
                toBuss.putExtra("title", "擅长业务");
                startActivityForResult(toBuss, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                break;
            case R.id.iv_avatar:
                Intent intent = new Intent(mActivity, SelectMyPhotoActivity.class);
                intent.putExtra("singlePic", true);
                startActivityForResult(intent, ParameterUtils.REQUEST_CODE_CHANGEPHOTO);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initEvent() {
        llCity.setOnClickListener(this);
        llGoodBusiness.setOnClickListener(this);
        ivAvatar.setOnClickListener(this);
        tvLoginOut.setOnClickListener(this);

    }

    @Override
    public MyInfoContract.Presenter initPresenter() {
        return new MyInfoActivityPresenter(this);
    }

    @Override
    public void showTip(String message) {
        ToastUtils.shortToast(mActivity, message);
    }

    @Override
    public void getMyInfoSuccess(TeacherInfo teacherInfo) {
        if (teacherInfo != null) {
            this.teacherInfo = teacherInfo;
            if (!TextUtils.isEmpty(teacherInfo.getAvatar()) && photoFile == null) {
                DisplayImageUtils.displayBlurImage(mActivity, teacherInfo.getAvatar(), ivTeacherBg);
                DisplayImageUtils.formatPersonImgUrl(mActivity, teacherInfo.getAvatar(), ivAvatar);
            }
            tvNickName.setText(teacherInfo.getName());
            tvWorkName.setText(teacherInfo.getTitle());
            tvWorkExperience.setText(teacherInfo.getYearsOfWorking());
            tvGraduatedSchool.setText(teacherInfo.getSchool());
            tvEmail.setText(teacherInfo.getEmail());
            tvName.setText(teacherInfo.getRealName());
            tvCity.setText(getCityValue(teacherInfo.getWorkingCityKey()));
            if (!TextUtils.isEmpty(teacherInfo.getAdeptWorksKey())) {
                List<String> list = Arrays.asList(getBussinessValue(teacherInfo.getAdeptWorksKey()).split(","));
                tlyTags.setBackGroup(R.drawable.bg_good_bussienss_border);
                tlyTags.createTab(mActivity, list);
            }
            tvPersonalProfile.setText(teacherInfo.getIntroduction());
        }
    }

    @Override
    public void updateMyAvatarSuccesee() {
        ToastUtils.shortToast(mActivity, "头像修改成功");
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
        Utils.removeCookie(mActivity);
        Intent to_login = new Intent(mActivity, LoginActivity.class);
        to_login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
            Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(to_login);
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

        if (this.teacherInfo != null) {
            tvCity.setText(getCityValue(teacherInfo.getWorkingCityKey()));
            if (!TextUtils.isEmpty(teacherInfo.getAdeptWorksKey())) {
                List<String> list = Arrays.asList(getBussinessValue(teacherInfo.getAdeptWorksKey()).split(","));
                tlyTags.setBackGroup(R.drawable.bg_good_bussienss_border);
                tlyTags.createTab(mActivity, list);
            }
        }
    }

    @Override
    public void updateMyInfoSuccess() {
        ToastUtils.shortToast(mActivity, "更新成功");
        mActivity.finish();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ParameterUtils.REQUEST_CODE_CHANGEPHOTO:
                final String temppath = data.getStringExtra("path");
                DisplayImageUtils.displayImageFile(mActivity.getApplicationContext(), temppath, new SimpleTarget<File>(100, 100) {
                    @Override
                    public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                        photoFile = resource;
                        DisplayImageUtils.displayPersonRes(mActivity, resource, ivAvatar);
                        DisplayImageUtils.displayBlurImage(mActivity, resource, ivTeacherBg);
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
                    tlyTags.createTab(mActivity, list);
                }
                break;

            default:
                break;
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

    private String getIntroduction() {
        return tvPersonalProfile.getText().toString().trim();
    }


    private void showNormalDialog() {
        final AlertDialog.Builder normalDialog =
            new AlertDialog.Builder(mActivity);
        normalDialog.setMessage("确定要退出登录吗?");
        normalDialog.setPositiveButton("确定",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    presenter.getLogOut();
                }
            });
        normalDialog.setNegativeButton("取消",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        // 显示
        normalDialog.show();
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


    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(SaveItem saveItem) {
        if (!CheckUtil.checkEmail(getEmail())) {
            ToastUtils.shortToast(mActivity, "邮箱不合法");
            return;
        }
        if (checkInput()) {
            presenter.updateMyInfo(getNickName(), photoFile, getWorkTitle(), getGraduatedSchool(), getWorkExperience(),
                getEmail(), getRealName(), getIntroduction(), cityValue, bussinessValue);
        } else {
            ToastUtils.shortToast(mActivity, "信息不能为空");
        }
    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(getNickName()) || TextUtils.isEmpty(getWorkTitle()) ||
            TextUtils.isEmpty(getWorkExperience()) || TextUtils.isEmpty(getWorkExperience()) ||
            TextUtils.isEmpty(getGraduatedSchool()) || TextUtils.isEmpty(getRealName()) ||
            TextUtils.isEmpty(getEmail()) || TextUtils.isEmpty(getIntroduction())) {
            return false;
        }
        return true;
    }
}
