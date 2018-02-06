package com.smartstudy.counselor_t.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.TeacherInfo;
import com.smartstudy.counselor_t.mvp.contract.MyInfoContract;
import com.smartstudy.counselor_t.mvp.presenter.MyInfoActivityPresenter;
import com.smartstudy.counselor_t.ui.activity.ClipPictureActivity;
import com.smartstudy.counselor_t.ui.activity.CommonEditNameActivity;
import com.smartstudy.counselor_t.ui.activity.SelectMyPhotoActivity;
import com.smartstudy.counselor_t.ui.base.UIFragment;
import com.smartstudy.counselor_t.ui.widget.ClipImageLayout;
import com.smartstudy.counselor_t.util.CheckUtil;
import com.smartstudy.counselor_t.util.DisplayImageUtils;
import com.smartstudy.counselor_t.util.ParameterUtils;
import com.smartstudy.counselor_t.util.SDCardUtils;
import com.smartstudy.counselor_t.util.ToastUtils;
import com.smartstudy.counselor_t.util.Utils;

import java.io.File;

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
    private File photoFile;

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
    public void updateMyInfoSuccesee() {
        ToastUtils.shortToast(mActivity, "修改成功");
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
                    public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                        photoFile = resource;
                        DisplayImageUtils.displayPersonRes(mActivity, resource, ivAvatar);
                    }
                });
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
