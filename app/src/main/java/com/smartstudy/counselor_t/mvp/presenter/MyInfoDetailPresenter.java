package com.smartstudy.counselor_t.mvp.presenter;

import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.counselor_t.entity.IdNameInfo;
import com.smartstudy.counselor_t.entity.TeacherInfo;
import com.smartstudy.counselor_t.entity.TokenBean;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BasePresenterImpl;
import com.smartstudy.counselor_t.mvp.contract.MyInfoDetailContract;
import com.smartstudy.counselor_t.mvp.model.MyInfoDetailModel;
import com.smartstudy.counselor_t.util.DisplayImageUtils;
import com.smartstudy.counselor_t.util.SPCacheUtils;

import java.io.File;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * @author yqy
 * @date on 2018/3/27
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyInfoDetailPresenter extends BasePresenterImpl<MyInfoDetailContract.View> implements MyInfoDetailContract.Presenter {

    private MyInfoDetailModel myInfoDetailModel;

    public MyInfoDetailPresenter(MyInfoDetailContract.View view) {
        super(view);
        myInfoDetailModel = new MyInfoDetailModel();
    }

    @Override
    public void detach() {
        super.detach();
        myInfoDetailModel = null;
    }

    @Override
    public void getMyInfo() {
        myInfoDetailModel.getAuditResult(new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String s) {
                TeacherInfo teacherInfo = JSON.parseObject(s, TeacherInfo.class);
                if (teacherInfo != null) {
                    view.getMyInfoSuccess(teacherInfo);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void updateMyAvatarInfo(File avatar, final ImageView ivAvatar) {
        myInfoDetailModel.updatePersonInfo(avatar, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String s) {
                TeacherInfo teacherInfo = JSON.parseObject(s, TeacherInfo.class);
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
                    view.updateMyAvatarSuccesee();
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void getLogOut() {
        myInfoDetailModel.getLogOut(new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String s) {
                view.getLogOutSuccess();
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void getOptions() {
        myInfoDetailModel.getOptions(new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String s) {
                JSONObject jsonObject = JSONObject.parseObject(s);
                if (jsonObject != null) {
                    List<IdNameInfo> workIdNameInfo = null;
                    List<IdNameInfo> adeptIdNameInfo = null;
                    if (jsonObject.containsKey("workingCity")) {
                        workIdNameInfo = JSONObject.parseArray(jsonObject.getString("workingCity"), IdNameInfo.class);
                    }

                    if (jsonObject.containsKey("adeptWorks")) {
                        adeptIdNameInfo = JSONObject.parseArray(jsonObject.getString("adeptWorks"), IdNameInfo.class);
                    }
                    view.getOptionsSuccess(workIdNameInfo, adeptIdNameInfo);
                }

            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void updateMyInfo(String name, File avatar, String title, String school, String yearsOfWorking,
                             String email, String realName, String introduction, String workingCityKey,
                             String adeptWorksKey) {
        myInfoDetailModel.updateMyInfo(name, avatar, title, school, yearsOfWorking, email, realName, introduction,
            workingCityKey, adeptWorksKey, new ObserverListener<String>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    addDisposable(disposable);
                }

                @Override
                public void onNext(String s) {
                    view.updateMyInfoSuccess();
                }

                @Override
                public void onError(String msg) {
                    view.showTip(msg);
                }
            });
    }

    @Override
    public void refreshTacken() {
        myInfoDetailModel.refreshTacken(new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                TokenBean tokenBean = JSONObject.parseObject(result, TokenBean.class);
                view.refreshSuccess(tokenBean);
            }

            @Override
            public void onError(String msg) {
                view.updateFail(msg);
            }
        });
    }

    @Override
    public void updateVideoUrl(final String videoUrl) {
        myInfoDetailModel.updateVideoUrl(videoUrl, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                view.updateVideoUrlSuccess();

            }

            @Override
            public void onError(String msg) {
                view.updateFail(msg);
            }
        });
    }
}

