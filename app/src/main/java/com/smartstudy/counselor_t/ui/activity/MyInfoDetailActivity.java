package com.smartstudy.counselor_t.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.IdNameInfo;
import com.smartstudy.counselor_t.entity.TeacherInfo;
import com.smartstudy.counselor_t.handler.WeakHandler;
import com.smartstudy.counselor_t.mvp.contract.MyInfoDetailContract;
import com.smartstudy.counselor_t.mvp.presenter.MyInfoDetailPresenter;
import com.smartstudy.counselor_t.ui.base.BaseActivity;
import com.smartstudy.counselor_t.ui.dialog.DialogCreator;
import com.smartstudy.counselor_t.ui.widget.TagsLayout;
import com.smartstudy.counselor_t.util.CheckUtil;
import com.smartstudy.counselor_t.util.DensityUtils;
import com.smartstudy.counselor_t.util.DisplayImageUtils;
import com.smartstudy.counselor_t.util.FastBlur;
import com.smartstudy.counselor_t.util.MediaUtils;
import com.smartstudy.counselor_t.util.ParameterUtils;
import com.smartstudy.counselor_t.util.SPCacheUtils;
import com.smartstudy.counselor_t.util.ToastUtils;
import com.smartstudy.counselor_t.util.Utils;
import com.smartstudy.medialib.ijkplayer.listener.OnPlayComplete;
import com.smartstudy.medialib.ijkplayer.listener.OnToggleFullScreenListener;
import com.smartstudy.medialib.ijkplayer.widget.PlayStateParams;
import com.smartstudy.medialib.ijkplayer.widget.PlayerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.rong.imkit.RongIM;

/**
 * @author yqy
 * @date on 2018/3/13
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyInfoDetailActivity extends BaseActivity<MyInfoDetailContract.Presenter> implements MyInfoDetailContract.View {
    private ImageView ivAvatar;
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
    private List<IdNameInfo> workIdNameInfos = new ArrayList<>();
    private List<IdNameInfo> adeptIdNameInfos = new ArrayList<>();
    private EditText tvPersonalProfile;
    private TextView tvAddGood;
    private ImageView ivVideoInfo;
    private ImageView ivUpLoad;
    public PlayerView player;
    private ImageView iv_player;
    private RelativeLayout.LayoutParams params;
    private NestedScrollView rslInfo;
    private FrameLayout flAvatar;
    private ImageView ivThumb;
    private LinearLayout llUpload;
    private LinearLayout llProgress;
    private ProgressBar pb;
    private WeakHandler mHandler;
    private String videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
    }

    @Override
    public void initView() {
        setLeftImgVisible(View.VISIBLE);
        setTopdefaultRighttextColor("#078CF1");
        setTitle("个人信息");
        setTopdefaultRighttextVisible(View.VISIBLE);
        setRightTxt("保存");
        topdefaultRighttext.setTextColor(Color.parseColor("#E4E5E6"));
        flAvatar = findViewById(R.id.fl_avatar);
        ivAvatar = findViewById(R.id.iv_avatar);
        tvNickName = findViewById(R.id.tv_nick_name);
        tvWorkName = findViewById(R.id.tv_work_name);
        tvWorkExperience = findViewById(R.id.tv_work_experience);
        tvGraduatedSchool = findViewById(R.id.tv_graduated_school);
        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
        llCity = findViewById(R.id.ll_city);
        tvAddGood = findViewById(R.id.tv_add_good);
        tvCity = findViewById(R.id.tv_work_city);
        llGoodBusiness = findViewById(R.id.ll_good_business);
        tvLoginOut = findViewById(R.id.tv_login_out);
        tlyTags = findViewById(R.id.tly_tags);
        ivVideoInfo = findViewById(R.id.iv_video_info);
        ivUpLoad = findViewById(R.id.iv_upLoad);
        rslInfo = findViewById(R.id.sv_info);
        llUpload = findViewById(R.id.ll_upload);
        llProgress = findViewById(R.id.ll_progress);
        pb = findViewById(R.id.pb_qa);
        tvPersonalProfile = findViewById(R.id.tv_personal_profile);
        presenter.getOptions();
        if (presenter != null) {
            presenter.getMyInfo();
        }
        initHandler();
        initPlayer();
    }

    @Override
    public void initEvent() {
        topdefaultLeftbutton.setOnClickListener(this);
        topdefaultRighttext.setOnClickListener(this);
        topdefaultRighttext.setClickable(false);

        llCity.setOnClickListener(this);
        llGoodBusiness.setOnClickListener(this);
        ivAvatar.setOnClickListener(this);
        tvLoginOut.setOnClickListener(this);
        tvAddGood.setOnClickListener(this);
        ivVideoInfo.setOnClickListener(this);
        ivUpLoad.setOnClickListener(this);
        setEditTextTextWatch(tvNickName);
        setEditTextTextWatch(tvWorkName);
        setEditTextTextWatch(tvWorkExperience);
        setEditTextTextWatch(tvGraduatedSchool);
        setEditTextTextWatch(tvEmail);
        setEditTextTextWatch(tvName);
        setEditTextTextWatch(tvPersonalProfile);
        handleProfile();
        handleAvatar();
    }

    private void handleProfile() {
        rslInfo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (rslInfo.getChildAt(0).getHeight() - rslInfo.getHeight() == rslInfo.getScrollY()) {
                    if (tvPersonalProfile.isFocused()) {
                        tvPersonalProfile.setSelection(tvPersonalProfile.getText().toString().length());
                    }
                }
                return false;
            }
        });
    }

    private void handleAvatar() {
        // 头像的半径
        final int size = DensityUtils.dip2px(90f);
        rslInfo.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY <= size) {
                    int padding = scrollY / 2;
                    flAvatar.setPadding(padding, padding, padding, padding);
                    flAvatar.setVisibility(View.VISIBLE);
                } else {
                    flAvatar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initHandler() {
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case ParameterUtils.EMPTY_WHAT:
                        int progress = msg.arg1;
                        if (progress < 100) {
                            llProgress.setVisibility(View.VISIBLE);
                            pb.setProgress(progress);
                        } else {
                            llProgress.setVisibility(View.GONE);
                            llUpload.setVisibility(View.VISIBLE);
                            tvAddGood.setVisibility(View.VISIBLE);
                            if (player != null) {
                                videoUrl = (String) msg.obj;
                                player.autoPlay(videoUrl);
                            }
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void initPlayer() {
        player = new PlayerView(this, rootView);
        if (player != null) {
            player.setOnPlayComplete(new OnPlayComplete() {
                @Override
                public void onComplete() {
                    // 播放完成
                    player.startPlay();
                }
            });
            player.setOnToggleFullScreenListener(new OnToggleFullScreenListener() {
                @Override
                public void onLandScape() {
                    setHeadVisible(View.GONE);
                    flAvatar.setVisibility(View.GONE);
                    llUpload.setVisibility(View.GONE);
                    tvAddGood.setVisibility(View.GONE);
                    player.forbidScroll(false).hideBottomBar(false).hideCenterPlayer(false)
                        .setHideAllUI(false);
                    params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    iv_player.setLayoutParams(params);
                    rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                }

                @Override
                public void onPortrait() {
                    setHeadVisible(View.VISIBLE);
                    flAvatar.setVisibility(View.VISIBLE);
                    llUpload.setVisibility(View.VISIBLE);
                    tvAddGood.setVisibility(View.VISIBLE);
                    player.forbidScroll(true).hideBottomBar(true).hideCenterPlayer(true)
                        .setHideAllUI(true);
                    params.width = DensityUtils.dip2px(45);
                    params.height = DensityUtils.dip2px(45);
                    iv_player.setLayoutParams(params);
                    rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                }
            });
            ivThumb = player.getIv_trumb();
            player.setScaleType(PlayStateParams.fillparent)
                .hideControlPanl(true)
                .hideCenterPlayer(true)
                .setHideAllUI(true)
                .hideTopBar(true)
                .hideBottomBar(true)
                .hideCenterPlayer(true)
                .hideRotation(true);
            iv_player = player.getPlayerView();
            params = (RelativeLayout.LayoutParams) iv_player.getLayoutParams();
            params.topMargin = DensityUtils.dip2px(8);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            //初始小屏模式
            player.forbidScroll(true);
            params.width = DensityUtils.dip2px(45);
            params.height = DensityUtils.dip2px(45);
            iv_player.setLayoutParams(params);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                if (player != null && player.onBackPressed()) {
                    return;
                }
                finish();
                break;
            case R.id.topdefault_righttext:
                if (!CheckUtil.checkEmail(getEmail())) {
                    ToastUtils.shortToast(this, "邮箱不合法");
                    return;
                }
                if (checkInput()) {
                    presenter.updateMyInfo(getNickName(), photoFile, getWorkTitle(), getGraduatedSchool(), getWorkExperience(),
                        getEmail(), getRealName(), getIntroduction(), cityValue, bussinessValue);
                } else {
                    ToastUtils.shortToast(this, "信息不能为空");
                }
                break;

            case R.id.tv_login_out:
                showNormalDialog();
                break;
            case R.id.tv_add_good:
                startActivity(new Intent(this, AddGoodDetailActivity.class));
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
            case R.id.iv_avatar:
                Intent intent = new Intent(this, SelectMyPhotoActivity.class);
                intent.putExtra("singlePic", true);
                startActivityForResult(intent, ParameterUtils.REQUEST_CODE_CHANGEPHOTO);
                break;
            case R.id.iv_video_info:
                DialogCreator.createVedioDialog(this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DialogCreator.createVedioClaimDialog(MyInfoDetailActivity.this);
                    }
                });
                break;
            case R.id.iv_upLoad:
                Intent toVideo = new Intent(this, SelectMyVideoActivity.class);
                toVideo.putExtra("singlePic", true);
                startActivityForResult(toVideo, ParameterUtils.REQUEST_VIDEO);
                break;
            default:
                break;
        }
    }

    @Override
    public MyInfoDetailContract.Presenter initPresenter() {
        return new MyInfoDetailPresenter(this);
    }


    public void setPostClick() {
        topdefaultRighttext.setClickable(true);
        topdefaultRighttext.setTextColor(Color.parseColor("#078CF1"));
    }

    public void setPostUnClick() {
        topdefaultRighttext.setClickable(false);
        topdefaultRighttext.setTextColor(Color.parseColor("#E4E5E6"));
    }

    @Override
    public void getMyInfoSuccess(final TeacherInfo teacherInfo) {
        if (teacherInfo != null) {
            this.teacherInfo = teacherInfo;
            videoUrl = teacherInfo.getVideo();
            if (!TextUtils.isEmpty(teacherInfo.getAvatar()) && photoFile == null) {
                DisplayImageUtils.displayImage(this, teacherInfo.getAvatar(), new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull final Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        if (TextUtils.isEmpty(videoUrl)) {
                            // 如果没有视频，展示封面
                            ivThumb.setImageBitmap(resource);
                            ViewTreeObserver vto = ivThumb.getViewTreeObserver();
                            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                @Override
                                public void onGlobalLayout() {
                                    ViewTreeObserver obs = ivThumb.getViewTreeObserver();
                                    ivThumb.buildDrawingCache();
                                    Bitmap bmp = ivThumb.getDrawingCache();
                                    FastBlur.blur(bmp, ivThumb);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        obs.removeOnGlobalLayoutListener(this);
                                    } else {
                                        obs.removeGlobalOnLayoutListener(this);
                                    }
                                }

                            });
                        }
                        ivAvatar.setImageBitmap(resource);
                    }
                });
            }
            if (!TextUtils.isEmpty(videoUrl)) {
                // 播放视频
                if (player != null) {
                    player.hideControlPanl(false)
                        .hideSteam(true)
                        .hideTopBar(true)
                        .autoPlay(videoUrl)
                        .hideCenterPlayer(false)
                        .hidePlayUI();
                    this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            }

            tvNickName.setText(teacherInfo.getName());
            tvWorkName.setText(teacherInfo.getTitle());
            tvWorkExperience.setText(teacherInfo.getYearsOfWorking());
            tvGraduatedSchool.setText(teacherInfo.getSchool());
            tvEmail.setText(teacherInfo.getEmail());
            tvName.setText(teacherInfo.getRealName());
            tvAddGood.setText(teacherInfo.getLikesCount());
            tvCity.setText(getCityValue(teacherInfo.getWorkingCityKey()));
            if (!TextUtils.isEmpty(teacherInfo.getAdeptWorksKey())) {
                List<String> list = Arrays.asList(getBussinessValue(teacherInfo.getAdeptWorksKey()).split(","));
                tlyTags.setBackGroup(R.drawable.bg_good_bussienss_border);
                tlyTags.createTab(this, list);
            }
            tvPersonalProfile.setText(teacherInfo.getIntroduction());
        }
    }

    @Override
    public void updateMyAvatarSuccesee() {
        ToastUtils.shortToast(this, "头像修改成功");
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
        Utils.removeCookie(this);
        Intent to_login = new Intent(this, LoginActivity.class);
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
                tlyTags.createTab(this, list);
            }
        }
    }

    @Override
    public void updateMyInfoSuccess() {
        ToastUtils.shortToast(this, "更新成功");
        finish();
    }

    @Override
    public void onLoading(int progress, String url) {
        if (mHandler != null) {
            Message msg = Message.obtain();
            msg.what = ParameterUtils.EMPTY_WHAT;
            msg.arg1 = progress;
            msg.obj = url;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
            player.startPlay();
            player.hideCenterPlayer(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
        /**恢复系统其它媒体的状态*/
        MediaUtils.muteAudioFocus(this, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
            player = null;
        }
        if (mHandler != null) {
            mHandler = null;
        }
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ParameterUtils.REQUEST_CODE_CHANGEPHOTO:
                final String temppath = data.getStringExtra("path");
                DisplayImageUtils.displayImageFile(this.getApplicationContext(), temppath, new SimpleTarget<File>(100, 100) {
                    @Override
                    public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                        photoFile = resource;
                        // 如果没有视频，则要同步改变封面
                        DisplayImageUtils.displayPersonRes(MyInfoDetailActivity.this, resource, ivAvatar);
                        if (TextUtils.isEmpty(videoUrl)) {
                            ivThumb.setImageBitmap(BitmapFactory.decodeFile(resource.getAbsolutePath()));
                            ViewTreeObserver vto = ivThumb.getViewTreeObserver();
                            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                @Override
                                public void onGlobalLayout() {
                                    ViewTreeObserver obs = ivThumb.getViewTreeObserver();
                                    ivThumb.buildDrawingCache();
                                    Bitmap bmp = ivThumb.getDrawingCache();
                                    FastBlur.blur(bmp, ivThumb);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        obs.removeOnGlobalLayoutListener(this);
                                    } else {
                                        obs.removeGlobalOnLayoutListener(this);
                                    }
                                }

                            });
                        }
                        presenter.updateMyAvatarInfo(photoFile, ivAvatar);
                    }
                });
                break;
            case ParameterUtils.REQUEST_CODE_EDIT_MYINFO:
                String flag = data.getStringExtra(ParameterUtils.TRANSITION_FLAG);
                if (flag.equals(ParameterUtils.WORK_CITY)) {
                    cityValue = data.getStringExtra("new_value");
                    if (!TextUtils.isEmpty(cityValue)) {
                        tvCity.setText(getCityValue(cityValue));
                        //监听是否发生改变
                        if (teacherInfo != null) {
                            if (!getCityValue(cityValue).equals(getCityValue(teacherInfo.getWorkingCityKey()))) {
                                setPostClick();
                            } else {
                                setPostUnClick();
                            }
                        }
                    }
                } else {
                    bussinessValue = data.getStringExtra("new_value");
                    if (!TextUtils.isEmpty(bussinessValue)) {
                        if (teacherInfo != null) {
                            if (!bussinessValue.equals(teacherInfo.getAdeptWorksKey())) {
                                setPostClick();
                            } else {
                                setPostUnClick();
                            }
                        }
                    }
                    List<String> list = Arrays.asList(getBussinessValue(bussinessValue).split(","));
                    tlyTags.setBackGroup(R.drawable.bg_good_bussienss_border);
                    tlyTags.createTab(this, list);
                }
                break;
            case ParameterUtils.REQUEST_VIDEO:
                String videoPath = data.getStringExtra("path");
                llUpload.setVisibility(View.GONE);
                tvAddGood.setVisibility(View.GONE);
                presenter.uploadVideo(new File(videoPath));
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
            new AlertDialog.Builder(this);
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

    private boolean checkInput() {
        if (TextUtils.isEmpty(getNickName()) || TextUtils.isEmpty(getWorkTitle()) ||
            TextUtils.isEmpty(getWorkExperience()) || TextUtils.isEmpty(getWorkExperience()) ||
            TextUtils.isEmpty(getGraduatedSchool()) || TextUtils.isEmpty(getRealName()) ||
            TextUtils.isEmpty(getEmail()) || TextUtils.isEmpty(getIntroduction())) {
            return false;
        }
        return true;
    }

    private void setEditTextTextWatch(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable.toString())) {
                    if (teacherInfo != null) {
                        String beforeValue = "";
                        if (editText.getId() == tvNickName.getId()) {
                            beforeValue = teacherInfo.getName();
                        } else if (editText.getId() == tvWorkName.getId()) {
                            beforeValue = teacherInfo.getTitle();
                        } else if (editText.getId() == tvWorkExperience.getId()) {
                            beforeValue = teacherInfo.getYearsOfWorking();
                        } else if (editText.getId() == tvGraduatedSchool.getId()) {
                            beforeValue = teacherInfo.getSchool();
                        } else if (editText.getId() == tvEmail.getId()) {
                            beforeValue = teacherInfo.getEmail();
                        } else if (editText.getId() == tvName.getId()) {
                            beforeValue = teacherInfo.getRealName();
                        } else if (editText.getId() == tvPersonalProfile.getId()) {
                            beforeValue = teacherInfo.getIntroduction();
                        } else {
                            return;
                        }
                        if (!editable.toString().equals(beforeValue)) {
                            setPostClick();
                        } else {
                            setPostUnClick();
                        }
                    }
                }
            }
        });
    }
}

