package com.smartstudy.counselor_t.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.TeacherInfo;
import com.smartstudy.counselor_t.mvp.contract.MyInfoContract;
import com.smartstudy.counselor_t.mvp.presenter.MyInfoActivityPresenter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.ui.widget.ClipImageLayout;
import com.smartstudy.counselor_t.util.DisplayImageUtils;
import com.smartstudy.counselor_t.util.ParameterUtils;
import com.smartstudy.counselor_t.util.SDCardUtils;
import com.smartstudy.counselor_t.util.SPCacheUtils;
import com.smartstudy.counselor_t.util.ToastUtils;
import com.smartstudy.counselor_t.util.Utils;

import java.io.File;

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
    private File photoFile;

    private TextView tvLoginOut;

    private File photoSaveFile;// 保存文件夹
    private String photoSaveName = null;// 图片名
    private String selected_path = null;

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
        btSave.setOnClickListener(this);
        tv_nick_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                setBtSave();
                return false;
            }
        });
        tv_work_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                setBtSave();
                return false;
            }
        });
        tv_work_experience.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                setBtSave();
                return false;
            }
        });
        tv_graduated_school.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                setBtSave();
                return false;
            }
        });

        tv_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                setBtSave();
                return false;
            }
        });
        tv_email.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                setBtSave();
                return false;
            }
        });

        ivAvatar.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;

            case R.id.iv_avatar:
                Intent intent = new Intent(MyInfoActivity.this, SelectMyPhotoActivity.class);
                intent.putExtra("singlePic", true);
                startActivityForResult(intent, ParameterUtils.REQUEST_CODE_CHANGEPHOTO);
                break;

            case R.id.bt_post_info:
                presenter.updateMyInfo(getNickName(), photoFile, getWorkTitle(), getGraduatedSchool(), getWorkExperience(), getEmail(), getRealName());
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
        RongIM.getInstance().logout();
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
//        to_login.putExtra("toMain", true);
        to_login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(to_login);
    }

    @Override
    public void updateMyInfoSuccesee() {
        ToastUtils.shortToast(this, "修改成功");
        finish();
    }


    private void setBtSave() {
        btSave.setClickable(true);
        btSave.setTextColor(Color.parseColor("#FFFFFF"));
        btSave.setText("保存");
        btSave.setBackgroundResource(R.drawable.bg_submit_review_blue);
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
                    Utils.startActionCapture(MyInfoActivity.this, new File(photoSaveFile.getAbsolutePath(), photoSaveName), ParameterUtils.REQUEST_CODE_CAMERA);
                }
                if ("from_album".equals(data.getStringExtra("flag_from"))) {
                    selected_path = data.getStringExtra("path");
                    Intent toClipImage = new Intent(MyInfoActivity.this, ClipPictureActivity.class);
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
                        setBtSave();
                        DisplayImageUtils.displayPersonRes(MyInfoActivity.this, resource, ivAvatar);
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
