package com.smartstudy.counselor_t.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.util.Log;
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

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import study.smart.baselib.entity.TeacherInfo;
import study.smart.baselib.entity.TokenBean;
import study.smart.baselib.ui.activity.LoginActivity;
import study.smart.baselib.ui.activity.SelectMyPhotoActivity;
import study.smart.baselib.ui.activity.SelectMyVideoActivity;
import study.smart.baselib.ui.base.BaseActivity;
import study.smart.baselib.ui.base.UIFragment;
import study.smart.baselib.utils.DensityUtils;
import study.smart.baselib.utils.DisplayImageUtils;
import study.smart.baselib.utils.ParameterUtils;
import study.smart.baselib.utils.SPCacheUtils;
import study.smart.baselib.utils.ToastUtils;
import study.smart.baselib.utils.Utils;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.IdNameInfo;
import com.smartstudy.counselor_t.entity.ProgressItem;
import com.smartstudy.counselor_t.handler.WeakHandler;
import com.smartstudy.counselor_t.mvp.contract.MyInfoDetailContract;
import com.smartstudy.counselor_t.mvp.presenter.MyInfoDetailPresenter;
import com.smartstudy.counselor_t.ossServer.OssService;
import com.smartstudy.counselor_t.ossServer.STSGetter;

import study.smart.baselib.ui.widget.dialog.DialogCreator;
import study.smart.baselib.ui.widget.TagsLayout;

import com.smartstudy.counselor_t.ui.MainActivity;
import com.smartstudy.counselor_t.ui.activity.AddGoodDetailActivity;
import com.smartstudy.counselor_t.ui.activity.ChooseListActivity;
import com.smartstudy.counselor_t.ui.activity.CommonEditNameActivity;
import com.smartstudy.counselor_t.util.CheckUtil;
import com.smartstudy.counselor_t.util.FastBlur;
import com.smartstudy.counselor_t.util.MediaUtils;
import com.smartstudy.medialib.ijkplayer.listener.OnPlayComplete;
import com.smartstudy.medialib.ijkplayer.listener.OnToggleFullScreenListener;
import com.smartstudy.medialib.ijkplayer.widget.PlayStateParams;
import com.smartstudy.medialib.ijkplayer.widget.PlayerView;

import net.sourceforge.zbar.Image;

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
 * @date on 2018/3/13
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyInfoDetailFragment extends UIFragment<MyInfoDetailContract.Presenter> implements MyInfoDetailContract.View, OssService.picResultCallback {
    private ImageView ivAvatar;
    private TextView tvNickName;
    private TextView tvWorkName;
    private TextView tvWorkExperience;
    private TextView tvGraduatedSchool;
    private TextView tvEmail;
    private TextView tvName;
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
    private TextView tvPersonalProfile;
    private TextView tvAddGood;
    private ImageView ivVideoInfo;
    private ImageView ivUpLoad;
    private PlayerView player;
    private ImageView ivPlayer;
    private RelativeLayout.LayoutParams params;
    private NestedScrollView rslInfo;
    private FrameLayout flAvatar;
    private ImageView ivThumb;
    private LinearLayout llUpload;
    private LinearLayout llProgress;
    private ProgressBar pb;
    private WeakHandler mHandler;
    private String videoUrl;
    private boolean isLoading;
    private String videoPath = "";
    private OssService ossService;
    private MainActivity mainActivity;
    private LinearLayout llName;
    private LinearLayout llNickName;
    private LinearLayout llWorkName;
    private LinearLayout llWorkExperience;
    private LinearLayout llGraduatedSchool;
    private LinearLayout llEmail;
    private LinearLayout llPersonalProfile;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        presenter.getOptions();
        if (presenter != null) {
            presenter.getMyInfo();
        }
    }

    @Override
    protected View getLayoutView() {
        EventBus.getDefault().register(this);
        return mActivity.getLayoutInflater().inflate(R.layout.activity_my_info, null);
    }

    @Override
    protected void initView(View rootView) {
        flAvatar = rootView.findViewById(R.id.fl_avatar);
        ivAvatar = rootView.findViewById(R.id.iv_avatar);
        tvNickName = rootView.findViewById(R.id.tv_nick_name);
        tvWorkName = rootView.findViewById(R.id.tv_work_name);
        tvWorkExperience = rootView.findViewById(R.id.tv_work_experience);
        tvGraduatedSchool = rootView.findViewById(R.id.tv_graduated_school);
        tvName = rootView.findViewById(R.id.tv_name);
        tvEmail = rootView.findViewById(R.id.tv_email);
        llCity = rootView.findViewById(R.id.ll_city);
        tvAddGood = rootView.findViewById(R.id.tv_add_good);
        tvCity = rootView.findViewById(R.id.tv_work_city);
        llGoodBusiness = rootView.findViewById(R.id.ll_good_business);
        tvLoginOut = rootView.findViewById(R.id.tv_login_out);
        tlyTags = rootView.findViewById(R.id.tly_tags);
        ivVideoInfo = rootView.findViewById(R.id.iv_video_info);
        ivUpLoad = rootView.findViewById(R.id.iv_upLoad);
        rslInfo = rootView.findViewById(R.id.sv_info);
        llUpload = rootView.findViewById(R.id.ll_upload);
        llName = rootView.findViewById(R.id.ll_name);
        llProgress = rootView.findViewById(R.id.ll_progress);
        pb = rootView.findViewById(R.id.pb_qa);
        llNickName = rootView.findViewById(R.id.ll_nick_name);
        tvPersonalProfile = rootView.findViewById(R.id.tv_personal_profile);
        llWorkName = rootView.findViewById(R.id.ll_work_name);
        llWorkExperience = rootView.findViewById(R.id.ll_work_experience);
        llGraduatedSchool = rootView.findViewById(R.id.ll_graduated_school);
        llEmail = rootView.findViewById(R.id.ll_email);
        llPersonalProfile = rootView.findViewById(R.id.ll_personal_profile);
        initHandler();
        initPlayer();
    }

    @Override
    public void initEvent() {
        String smartTicket = (String) SPCacheUtils.get("smart_ticket", "");
        if (TextUtils.isEmpty(smartTicket)) {
            llName.setOnClickListener(this);
            llEmail.setOnClickListener(this);
        }
        llCity.setOnClickListener(this);
        llGoodBusiness.setOnClickListener(this);
        ivAvatar.setOnClickListener(this);
        tvLoginOut.setOnClickListener(this);
        tvAddGood.setOnClickListener(this);
        ivVideoInfo.setOnClickListener(this);
        ivUpLoad.setOnClickListener(this);
        llWorkName.setOnClickListener(this);
        llNickName.setOnClickListener(this);
        llWorkExperience.setOnClickListener(this);
        llGraduatedSchool.setOnClickListener(this);
        llPersonalProfile.setOnClickListener(this);
        handleAvatar();
    }

    private void handleAvatar() {
        // 头像的半径
        final int size = DensityUtils.dip2px(90f);
        rslInfo.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (mActivity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    if (!isLoading) {
                        if (scrollY <= size) {
                            int padding = scrollY / 2;
                            flAvatar.setPadding(padding, padding, padding, padding);
                            flAvatar.setVisibility(View.VISIBLE);
                        } else {
                            flAvatar.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
    }

    private void initHandler() {
        isLoading = false;
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case ParameterUtils.EMPTY_WHAT:
                        int progress = msg.arg1;
                        if (progress < 100) {
                            isLoading = true;
                            llProgress.setVisibility(View.VISIBLE);
                            pb.setProgress(progress);
                        } else {
                            isLoading = false;
                            llProgress.setVisibility(View.GONE);
                            llUpload.setVisibility(View.VISIBLE);
                            tvAddGood.setVisibility(View.VISIBLE);
                            if (player != null) {
                                videoUrl = (String) msg.obj;
                                player.autoPlay(videoUrl);
                                player.hideCenterPlayer(true);
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
        player = new PlayerView(mActivity, rootView);
        rootView.findViewById(R.id.app_video_menu).setVisibility(View.GONE);
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
                    mainActivity.getHeadView().setVisibility(View.GONE);
                    mainActivity.setBottomVisibility(View.GONE);
                    flAvatar.setVisibility(View.GONE);
                    llUpload.setVisibility(View.GONE);
                    tvAddGood.setVisibility(View.GONE);
                    mainActivity.setTopLineVisibility(View.GONE);
                    player.forbidScroll(false).hideBottomBar(false).hideCenterPlayer(false)
                        .hideTopBar(false).setHideAllUI(false);
                    params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    ivPlayer.setLayoutParams(params);
                    rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
                }

                @Override
                public void onPortrait() {
                    mainActivity.getHeadView().setVisibility(View.VISIBLE);
                    mainActivity.setBottomVisibility(View.VISIBLE);
                    flAvatar.setVisibility(View.VISIBLE);
                    llUpload.setVisibility(View.VISIBLE);
                    tvAddGood.setVisibility(View.VISIBLE);
                    mainActivity.setTopLineVisibility(View.VISIBLE);
                    player.forbidScroll(true).hideBottomBar(true).hideCenterPlayer(true)
                        .hideTopBar(true).setHideAllUI(true);
                    params.width = DensityUtils.dip2px(45);
                    params.height = DensityUtils.dip2px(45);
                    ivPlayer.setLayoutParams(params);
                    rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                }
            });
            ivThumb = player.getIv_trumb();
            player.setScaleType(PlayStateParams.fillparent)
                .hideControlPanl(true)
                .hideCenterPlayer(true)
                .hideRotation(true)
                .hideSteam(true)
                .setForbidDoulbeUp(true)
                .setHideAllUI(true);
            ivPlayer = player.getPlayerView();
            params = (RelativeLayout.LayoutParams) ivPlayer.getLayoutParams();
            params.topMargin = DensityUtils.dip2px(8);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            //初始小屏模式
            player.forbidScroll(true);
            params.width = DensityUtils.dip2px(45);
            params.height = DensityUtils.dip2px(45);
            ivPlayer.setLayoutParams(params);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_righttext:
                if (!CheckUtil.checkEmail(getEmail())) {
                    ToastUtils.shortToast("邮箱不合法");
                    return;
                }
                if (checkInput()) {
                    presenter.updateMyInfo(getNickName(), photoFile, getWorkTitle(), getGraduatedSchool(), getWorkExperience(),
                        getEmail(), getRealName(), getIntroduction(), cityValue, bussinessValue);
                } else {
                    ToastUtils.shortToast("信息不能为空");
                }
                break;
            case R.id.ll_name:
                Intent toRealName = new Intent(mActivity, CommonEditNameActivity.class);
                toRealName.putExtra("value", getRealName());
                toRealName.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_REAL_NAME);
                toRealName.putExtra("title", "修改真实姓名");
                startActivityForResult(toRealName, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                break;
            case R.id.ll_personal_profile:
                Intent toRemark = new Intent(mActivity, CommonEditNameActivity.class);
                toRemark.putExtra("value", getIntroduction());
                toRemark.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_REMARK);
                toRemark.putExtra("title", "修改备注");
                startActivityForResult(toRemark, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                break;
            case R.id.ll_work_name:
                Intent toWorkName = new Intent(mActivity, CommonEditNameActivity.class);
                toWorkName.putExtra("value", getWorkTitle());
                toWorkName.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_WORK_NAME);
                toWorkName.putExtra("title", "修改工作职称");
                startActivityForResult(toWorkName, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                break;
            case R.id.ll_email:
                Intent toEmail = new Intent(mActivity, CommonEditNameActivity.class);
                toEmail.putExtra("value", getEmail());
                toEmail.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_EMAIL);
                toEmail.putExtra("title", "修改邮箱");
                startActivityForResult(toEmail, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                break;
            case R.id.ll_graduated_school:
                Intent toGraduatedSchool = new Intent(mActivity, CommonEditNameActivity.class);
                toGraduatedSchool.putExtra("value", getGraduatedSchool());
                toGraduatedSchool.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_GRADUATED_SCHOOL);
                toGraduatedSchool.putExtra("title", "修改毕业学校");
                startActivityForResult(toGraduatedSchool, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                break;
            case R.id.ll_work_experience:
                Intent toWorkExperience = new Intent(mActivity, CommonEditNameActivity.class);
                toWorkExperience.putExtra("value", getWorkExperience());
                toWorkExperience.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_WORK_EXPERIENCE);
                toWorkExperience.putExtra("title", "修改工作经验");
                startActivityForResult(toWorkExperience, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                break;

            case R.id.ll_nick_name:
                Intent toNickName = new Intent(mActivity, CommonEditNameActivity.class);
                toNickName.putExtra("value", getNickName());
                toNickName.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_NAME);
                toNickName.putExtra("title", "修改昵称");
                startActivityForResult(toNickName, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                break;
            case R.id.tv_login_out:
                showNormalDialog();
                break;
            case R.id.tv_add_good:
                startActivity(new Intent(mActivity, AddGoodDetailActivity.class));
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
            case R.id.iv_video_info:
                DialogCreator.createVedioDialog(mActivity, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DialogCreator.createVedioClaimDialog(mActivity, "video_info");
                    }
                });
                break;
            case R.id.iv_upLoad:
                String key = SPCacheUtils.get("imUserId", "").toString() + "ISFIRST";
                if ((boolean) SPCacheUtils.get(key, false)) {
                    Intent toVideo = new Intent(mActivity, SelectMyVideoActivity.class);
                    toVideo.putExtra("singlePic", true);
                    startActivityForResult(toVideo, ParameterUtils.REQUEST_VIDEO);
                } else {
                    SPCacheUtils.put(key, true);
                    DialogCreator.createVedioDialog(mActivity, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DialogCreator.createVedioClaimDialog(mActivity, "upLoad");
                        }
                    });
                }
                break;
            default:
                break;
        }
    }

    @Override
    public MyInfoDetailContract.Presenter initPresenter() {
        return new MyInfoDetailPresenter(this);
    }

    @Override
    public void getMyInfoSuccess(final TeacherInfo teacherInfo) {
        if (teacherInfo != null) {
            this.teacherInfo = teacherInfo;
            videoUrl = teacherInfo.getVideo();
            if (!TextUtils.isEmpty(teacherInfo.getAvatar()) && photoFile == null) {
                DisplayImageUtils.displayImage(mActivity, teacherInfo.getAvatar(), new SimpleTarget<Bitmap>() {
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
                    player.hideControlPanl(true)
                        .setForbidDoulbeUp(false)
                        .autoPlay(videoUrl)
                        .hideCenterPlayer(true);
                    mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
                tlyTags.createTab(mActivity, list);
            }
            tvPersonalProfile.setText(teacherInfo.getIntroduction());
        }
    }

    @Override
    public void updateMyAvatarSuccesee() {
        ToastUtils.shortToast("头像修改成功");
    }

    @Override
    public void getLogOutSuccess() {
        RongIM.getInstance().logout();
        SPCacheUtils.put("phone", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("name", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("avatar", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("ticket", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("smart_ticket", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("privileges", ParameterUtils.CACHE_NULL);
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
        ToastUtils.shortToast("更新成功");
    }

    @Override
    public void refreshSuccess(TokenBean tokenBean) {
        ossService = initOSS(tokenBean.getEndpoint(), tokenBean.getBucket(), tokenBean);
        if (ossService != null) {
            if (tokenBean != null) {
                ossService.asyncPutVideo(tokenBean.getKey(), videoPath);
            }
        }
    }

    @Override
    public void updateVideoUrlSuccess() {
        ToastUtils.shortToast("视频上传成功");
    }

    @Override
    public void updateFail(String msg) {
        ToastUtils.shortToast(msg);
        llUpload.setVisibility(View.VISIBLE);
        tvAddGood.setVisibility(View.VISIBLE);
        flAvatar.setVisibility(View.VISIBLE);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isHidden()) {
            if (player != null) {
                player.onResume();
                if (!TextUtils.isEmpty(videoUrl)) {
                    player.setForbidDoulbeUp(false).startPlay();
                }
                player.hideCenterPlayer(true);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
        /**恢复系统其它媒体的状态*/
        MediaUtils.muteAudioFocus(mActivity, true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
                DisplayImageUtils.displayImageFile(mActivity.getApplicationContext(), temppath, new SimpleTarget<File>(100, 100) {
                    @Override
                    public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                        photoFile = resource;
                        // 如果没有视频，则要同步改变封面
                        DisplayImageUtils.displayPersonRes(mActivity, resource, ivAvatar);
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
                                presenter.updateMyInfo("", null, "", "", "", "", "", "", cityValue, "");
                            }
                        }
                    }
                } else if (ParameterUtils.EDIT_REAL_NAME.equals(flag)) {
                    tvName.setText(data.getStringExtra("new_value"));
                } else if (ParameterUtils.EDIT_NAME.equals(flag)) {
                    tvNickName.setText(data.getStringExtra("new_value"));
                } else if (ParameterUtils.EDIT_WORK_NAME.equals(flag)) {
                    tvWorkName.setText(data.getStringExtra("new_value"));
                } else if (ParameterUtils.EDIT_WORK_EXPERIENCE.equals(flag)) {
                    tvWorkExperience.setText(data.getStringExtra("new_value"));
                } else if (ParameterUtils.EDIT_GRADUATED_SCHOOL.equals(flag)) {
                    tvGraduatedSchool.setText(data.getStringExtra("new_value"));
                } else if (ParameterUtils.EDIT_EMAIL.equals(flag)) {
                    tvEmail.setText(data.getStringExtra("new_value"));
                } else if (ParameterUtils.EDIT_REMARK.equals(flag)) {
                    tvPersonalProfile.setText(data.getStringExtra("new_value"));
                } else {
                    bussinessValue = data.getStringExtra("new_value");
                    if (!TextUtils.isEmpty(bussinessValue)) {
                        if (teacherInfo != null) {
                            if (!bussinessValue.equals(teacherInfo.getAdeptWorksKey())) {
                                presenter.updateMyInfo("", null, "", "", "", "", "", "", "", bussinessValue);
                            }
                        }
                    }
                    List<String> list = Arrays.asList(getBussinessValue(bussinessValue).split(","));
                    tlyTags.setBackGroup(R.drawable.bg_good_bussienss_border);
                    tlyTags.createTab(mActivity, list);
                }
                break;
            case ParameterUtils.REQUEST_VIDEO:
                videoPath = data.getStringExtra("path");
                llUpload.setVisibility(View.GONE);
                tvAddGood.setVisibility(View.GONE);
                flAvatar.setVisibility(View.GONE);
                isLoading = true;
                presenter.refreshTacken();
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

    private boolean checkInput() {
        if (TextUtils.isEmpty(getNickName()) || TextUtils.isEmpty(getWorkTitle()) ||
            TextUtils.isEmpty(getWorkExperience()) || TextUtils.isEmpty(getWorkExperience()) ||
            TextUtils.isEmpty(getGraduatedSchool()) || TextUtils.isEmpty(getRealName()) ||
            TextUtils.isEmpty(getEmail()) || TextUtils.isEmpty(getIntroduction())) {
            return false;
        }
        return true;
    }

    public OssService initOSS(String endpoint, String bucket, TokenBean tokenBean) {
        OSSCredentialProvider credentialProvider;
        credentialProvider = new STSGetter(tokenBean);
        //设置网络参数
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSS oss = new OSSClient(mActivity.getApplicationContext(), endpoint, credentialProvider, conf);
        return new OssService(oss, bucket, this);

    }

    @Override
    public void getPicData(String videoUrl) {
        presenter.updateVideoUrl(videoUrl);
    }

    @Override
    public void uploadError() {
        llUpload.setVisibility(View.VISIBLE);
        tvAddGood.setVisibility(View.VISIBLE);
        flAvatar.setVisibility(View.VISIBLE);
    }


    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(ProgressItem progressItem) {
        if (progressItem != null) {
            if (mHandler != null) {
                llUpload.setVisibility(View.GONE);
                tvAddGood.setVisibility(View.GONE);
                flAvatar.setVisibility(View.GONE);
                Message msg = Message.obtain();
                msg.what = ParameterUtils.EMPTY_WHAT;
                msg.arg1 = progressItem.getProgress();
                msg.obj = progressItem.getVoideoUrl();
                mHandler.sendMessage(msg);
            }
        }
    }

    public void stopPlay() {
        if (player != null) {
            player.pausePlay();
        }
    }

    public void startPlay() {
        if (player != null) {
            player.startPlay();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void showTip(String message) {
        ToastUtils.shortToast(message);
    }
}

