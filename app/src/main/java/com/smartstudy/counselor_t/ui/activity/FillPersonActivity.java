package com.smartstudy.counselor_t.ui.activity;

import android.app.Activity;
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
import android.widget.Toast;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.MyUserInfo;
import com.smartstudy.counselor_t.entity.StudentPageInfo;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.mvp.contract.FillPersonContract;
import com.smartstudy.counselor_t.mvp.contract.StudentActivityContract;
import com.smartstudy.counselor_t.mvp.presenter.FillPersonPresenter;
import com.smartstudy.counselor_t.ui.MainActivity;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.ui.widget.ClipImageLayout;
import com.smartstudy.counselor_t.util.CheckUtil;
import com.smartstudy.counselor_t.util.ConstantUtils;
import com.smartstudy.counselor_t.util.DisplayImageUtils;
import com.smartstudy.counselor_t.util.ParameterUtils;
import com.smartstudy.counselor_t.util.SDCardUtils;
import com.smartstudy.counselor_t.util.SPCacheUtils;
import com.smartstudy.counselor_t.util.ToastUtils;
import com.smartstudy.counselor_t.util.Utils;

import java.io.File;

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

    private String ticket;
    private File photoFile;
    private File photoSaveFile;// 保存文件夹
    private String photoSaveName = null;// 图片名
    private String selected_path = null;

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
        ivAvatar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_post_info:
                if (!CheckUtil.checkEmail(getEmail())) {
                    ToastUtils.shortToast(this, "邮箱不合法");
                    return;
                }

                presenter.postPersonInfo(getNickName(), photoFile, getWorkTitle(), getGraduatedSchool(), getWorkExperience(), getEmail(), getRealName());
                break;
            case R.id.iv_avatar:
                Intent intent = new Intent(FillPersonActivity.this, SelectMyPhotoActivity.class);
                intent.putExtra("singlePic", true);
                startActivityForResult(intent, ParameterUtils.REQUEST_CODE_CHANGEPHOTO);
                break;
            default:
                break;
        }
    }


    @Override
    public void initView() {
        ticket = (String) SPCacheUtils.get("ticket", ConstantUtils.CACHE_NULL);
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
        DisplayImageUtils.displayCircleImage(this, "", ivPhoto);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ParameterUtils.REQUEST_CODE_CHANGEPHOTO:
                if ("from_capture".equals(data.getStringExtra("flag_from"))) {
                    photoSaveName = System.currentTimeMillis() + ".png";
                    photoSaveFile = SDCardUtils.getFileDirPath("Xxd" + File.separator + "pictures");// 存放照片的文件夹
                    Utils.startActionCapture(FillPersonActivity.this, new File(photoSaveFile.getAbsolutePath(), photoSaveName), ParameterUtils.REQUEST_CODE_CAMERA);
                }
                if ("from_album".equals(data.getStringExtra("flag_from"))) {
                    selected_path = data.getStringExtra("path");
                    Intent toClipImage = new Intent(FillPersonActivity.this, ClipPictureActivity.class);
                    toClipImage.putExtra("path", selected_path);
                    toClipImage.putExtra("clipType", ClipImageLayout.SQUARE);
                    this.startActivityForResult(toClipImage, ParameterUtils.REQUEST_CODE_CLIP_OVER);
                }
                break;
            case ParameterUtils.REQUEST_CODE_CAMERA:
                String path_capture = photoSaveFile.getAbsolutePath() + "/" + photoSaveName;
                Intent toClipImage = new Intent(getApplicationContext(), ClipPictureActivity.class);
                toClipImage.putExtra("path", path_capture);
                toClipImage.putExtra("clipType", ClipImageLayout.SQUARE);
                startActivityForResult(toClipImage, ParameterUtils.REQUEST_CODE_CLIP_OVER);
                break;
            case ParameterUtils.REQUEST_CODE_CLIP_OVER:
                final String temppath = data.getStringExtra("path");
                DisplayImageUtils.downloadImageFile(getApplicationContext(), temppath, new SimpleTarget<File>() {

                    @Override
                    public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                        photoFile = resource;
                        DisplayImageUtils.displayPersonRes(FillPersonActivity.this, resource, ivAvatar);
                    }
                });
                break;

            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(ticket) && !ConstantUtils.CACHE_NULL.equals(ticket)) {
            presenter.getAuditResult();
        }
    }

    @Override
    public void getStudentInfoDetailSuccess() {
        setBtEnBleClick();
    }

    @Override
    public void getAuditResult(int staus) {
        if (staus == 2) {
            this.startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if (staus == 1) {
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
        btPostInfo.setText("正在审核中...");
        btPostInfo.setBackgroundResource(R.drawable.bg_submit_review_grey);
    }

}
