package com.smartstudy.counselor_t.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.TeacherInfo;
import com.smartstudy.counselor_t.mvp.contract.MyInfoContract;
import com.smartstudy.counselor_t.mvp.presenter.MyInfoActivityPresenter;
import com.smartstudy.counselor_t.ui.activity.ClipPictureActivity;
import com.smartstudy.counselor_t.ui.activity.CommonEditNameActivity;
import com.smartstudy.counselor_t.ui.activity.SelectMyPhotoActivity;
import com.smartstudy.counselor_t.ui.base.UIFragment;
import com.smartstudy.counselor_t.ui.widget.ClipImageLayout;
import com.smartstudy.counselor_t.util.DisplayImageUtils;
import com.smartstudy.counselor_t.util.ParameterUtils;
import com.smartstudy.counselor_t.util.SDCardUtils;
import com.smartstudy.counselor_t.util.SPCacheUtils;
import com.smartstudy.counselor_t.util.ToastUtils;
import com.smartstudy.counselor_t.util.Utils;

import java.io.File;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * @author yqy
 * @date on 2018/2/6
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyFragment extends UIFragment<MyInfoContract.Presenter> implements MyInfoContract.View {
    private ImageView ivAvatar;
    private TextView tv_nick_name;
    private TextView tv_work_name;
    private TextView tv_work_experience;
    private TextView tv_graduated_school;
    private TextView tv_email;
    private TextView tv_name;

    private File photoSaveFile;// 保存文件夹
    private String photoSaveName = null;// 图片名
    private String selected_path = null;
    private TeacherInfo teacherInfo;

    private LinearLayout ll_nick_name;
    private LinearLayout ll_work_name;
    private LinearLayout ll_work_experience;
    private LinearLayout ll_graduated_school;
    private LinearLayout ll_name;
    private LinearLayout ll_email;

    @Override
    protected View getLayoutView() {
        return mActivity.getLayoutInflater().inflate(R.layout.fragment_person_info, null);
    }

    @Override
    protected void initView(View rootView) {
        ivAvatar = rootView.findViewById(R.id.iv_avatar);
        tv_nick_name = rootView.findViewById(R.id.tv_nick_name);
        tv_work_name = rootView.findViewById(R.id.tv_work_name);
        tv_work_experience = rootView.findViewById(R.id.tv_work_experience);
        tv_graduated_school = rootView.findViewById(R.id.tv_graduated_school);
        tv_name = rootView.findViewById(R.id.tv_name);
        tv_email = rootView.findViewById(R.id.tv_email);
        ll_nick_name = rootView.findViewById(R.id.ll_nick_name);
        ll_work_name = rootView.findViewById(R.id.ll_work_name);
        ll_work_experience = rootView.findViewById(R.id.ll_work_experience);
        ll_graduated_school = rootView.findViewById(R.id.ll_graduated_school);
        ll_name = rootView.findViewById(R.id.ll_name);
        ll_email = rootView.findViewById(R.id.ll_email);
        if (presenter != null) {
            presenter.getMyInfo();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_nick_name:
                Intent toNickName = new Intent(mActivity, CommonEditNameActivity.class);
                toNickName.putExtra("value", getNickName());
                toNickName.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_NAME);
                toNickName.putExtra("title", "修改昵称");
                startActivityForResult(toNickName, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                break;

            case R.id.iv_avatar:
                Intent intent = new Intent(mActivity, SelectMyPhotoActivity.class);
                intent.putExtra("singlePic", true);
                startActivityForResult(intent, ParameterUtils.REQUEST_CODE_CHANGEPHOTO);
                break;

            case R.id.ll_work_name:
                Intent toWorkName = new Intent(mActivity, CommonEditNameActivity.class);
                toWorkName.putExtra("value", getWorkTitle());
                toWorkName.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_WORK_NAME);
                toWorkName.putExtra("title", "修改工作职称");
                startActivityForResult(toWorkName, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                break;

            case R.id.ll_work_experience:
                Intent toWorkExperience = new Intent(mActivity, CommonEditNameActivity.class);
                toWorkExperience.putExtra("value", getWorkExperience());
                toWorkExperience.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_WORK_EXPERIENCE);
                toWorkExperience.putExtra("title", "修改工作经验");
                startActivityForResult(toWorkExperience, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                break;

            case R.id.ll_graduated_school:
                Intent toGraduatedSchool = new Intent(mActivity, CommonEditNameActivity.class);
                toGraduatedSchool.putExtra("value", getGraduatedSchool());
                toGraduatedSchool.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_GRADUATED_SCHOOL);
                toGraduatedSchool.putExtra("title", "修改毕业学校");
                startActivityForResult(toGraduatedSchool, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                break;
            case R.id.ll_name:
                Intent toRealName = new Intent(mActivity, CommonEditNameActivity.class);
                toRealName.putExtra("value", getRealName());
                toRealName.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_REAL_NAME);
                toRealName.putExtra("title", "修改真实姓名");
                startActivityForResult(toRealName, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                break;

            case R.id.ll_email:
                Intent toEmail = new Intent(mActivity, CommonEditNameActivity.class);
                toEmail.putExtra("value", getEmail());
                toEmail.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_EMAIL);
                toEmail.putExtra("title", "修改邮箱");
                startActivityForResult(toEmail, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                break;

        }
    }

    @Override
    protected void initEvent() {
        ll_nick_name.setOnClickListener(this);
        ll_work_name.setOnClickListener(this);
        ll_work_experience.setOnClickListener(this);
        ll_graduated_school.setOnClickListener(this);
        ll_name.setOnClickListener(this);
        ll_email.setOnClickListener(this);
        ivAvatar.setOnClickListener(this);
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
            DisplayImageUtils.formatPersonImgUrl(mActivity, teacherInfo.getAvatar(), ivAvatar);
            tv_nick_name.setText(teacherInfo.getName());
            tv_work_name.setText(teacherInfo.getTitle());
            tv_work_experience.setText(teacherInfo.getYearsOfWorking());
            tv_graduated_school.setText(teacherInfo.getSchool());
            tv_email.setText(teacherInfo.getEmail());
            tv_name.setText(teacherInfo.getRealName());
        }
    }


    @Override
    public void updateMyAvatarSuccesee() {
        ToastUtils.shortToast(mActivity, "头像修改成功");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ParameterUtils.REQUEST_CODE_CHANGEPHOTO:
                if ("from_capture".equals(data.getStringExtra("flag_from"))) {
                    photoSaveName = System.currentTimeMillis() + ".png";
                    // 存放照片的文件夹
                    photoSaveFile = SDCardUtils.getFileDirPath("Xxd" + File.separator + "pictures");
                    Utils.startActionCapture(mActivity, new File(photoSaveFile.getAbsolutePath(), photoSaveName), ParameterUtils.REQUEST_CODE_CAMERA);
                }
                if ("from_album".equals(data.getStringExtra("flag_from"))) {
                    selected_path = data.getStringExtra("path");
                    Intent toClipImage = new Intent(mActivity, ClipPictureActivity.class);
                    toClipImage.putExtra("path", selected_path);
                    toClipImage.putExtra("clipType", ClipImageLayout.SQUARE);
                    this.startActivityForResult(toClipImage, ParameterUtils.REQUEST_CODE_CLIP_OVER);
                }
                break;
            case ParameterUtils.REQUEST_CODE_CAMERA:
                String path_capture = photoSaveFile.getAbsolutePath() + "/" + photoSaveName;
                Intent toClipImage = new Intent(mActivity.getApplicationContext(), ClipPictureActivity.class);
                toClipImage.putExtra("path", path_capture);
                toClipImage.putExtra("clipType", ClipImageLayout.SQUARE);
                startActivityForResult(toClipImage, ParameterUtils.REQUEST_CODE_CLIP_OVER);
                break;
            case ParameterUtils.REQUEST_CODE_CLIP_OVER:
                final String temppath = data.getStringExtra("path");
                DisplayImageUtils.downloadImageFile(mActivity.getApplicationContext(), temppath, new SimpleTarget<File>(100, 100) {
                    @Override
                    public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                        DisplayImageUtils.displayPersonRes(mActivity, resource, ivAvatar);
                        presenter.updateMyAvatarInfo(resource, ivAvatar);
                    }
                });
                break;

            case ParameterUtils.REQUEST_CODE_EDIT_MYINFO:
                String flag = data.getStringExtra(ParameterUtils.TRANSITION_FLAG);
                String value = data.getStringExtra("new_value");
                TeacherInfo teacherInfo = (TeacherInfo) data.getSerializableExtra("");
                if (ParameterUtils.EDIT_NAME.equals(flag)) {
                    tv_nick_name.setText(value);
                } else if (ParameterUtils.EDIT_WORK_NAME.equals(flag)) {
                    tv_work_name.setText(value);
                } else if (ParameterUtils.EDIT_WORK_EXPERIENCE.equals(flag)) {
                    tv_work_experience.setText(value);
                } else if (ParameterUtils.EDIT_GRADUATED_SCHOOL.equals(flag)) {
                    tv_graduated_school.setText(value);
                } else if (ParameterUtils.EDIT_REAL_NAME.equals(flag)) {
                    tv_name.setText(value);
                } else if (ParameterUtils.EDIT_EMAIL.equals(flag)) {
                    tv_email.setText(value);
                }

                //更新缓存
                if (teacherInfo != null) {
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
                                if (avatarUri != null && name == null) {
                                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(imUserId, (String) SPCacheUtils.get("name", ""), avatarUri));
                                }
                                if (name != null && avatarUri == null) {
                                    String avatar = (String) SPCacheUtils.get("avatar", "");
                                    UserInfo userInfo = new UserInfo(imUserId, name, TextUtils.isEmpty(avatar) ? null : Uri.parse(avatar));
                                    RongIM.getInstance().refreshUserInfoCache(userInfo);
                                    RongIM.getInstance().setCurrentUserInfo(userInfo);
                                }
                            }
                        }
                    }
                    SPCacheUtils.put("title", teacherInfo.getTitle());
                    SPCacheUtils.put("year", teacherInfo.getYearsOfWorking());
                    SPCacheUtils.put("company", teacherInfo.getOrganization().getName());
                }
                break;

            default:
                break;
        }
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


}
