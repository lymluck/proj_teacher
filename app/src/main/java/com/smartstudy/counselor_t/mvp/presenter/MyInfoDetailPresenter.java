package com.smartstudy.counselor_t.mvp.presenter;

import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import study.smart.baselib.entity.TeacherInfo;
import study.smart.baselib.entity.TokenBean;
import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BasePresenterImpl;
import study.smart.baselib.utils.DisplayImageUtils;
import study.smart.baselib.utils.SPCacheUtils;
import com.smartstudy.counselor_t.entity.IdNameInfo;
import com.smartstudy.counselor_t.mvp.contract.MyInfoDetailContract;
import com.smartstudy.counselor_t.mvp.model.MyInfoDetailModel;

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
        myInfoDetailModel.getAuditResult(new ObserverListener<TeacherInfo>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(TeacherInfo result) {
                if (result != null) {
                    view.getMyInfoSuccess(result);
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
        myInfoDetailModel.updatePersonInfo(avatar, new ObserverListener<TeacherInfo>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(TeacherInfo result) {
                if (result != null) {
                    //更新融云
                    String imUserId = (String) SPCacheUtils.get("imUserId", "");
                    if (!TextUtils.isEmpty(imUserId)) {
                        Uri avatarUri = null;
                        if (!TextUtils.isEmpty(result.getAvatar())) {
                            String avatarUrl = DisplayImageUtils.formatImgUrl(result.getAvatar(), ivAvatar.getWidth(), ivAvatar.getHeight());
                            avatarUri = Uri.parse(avatarUrl);
                        }
                        String name = null;
                        if (!TextUtils.isEmpty(result.getName())) {
                            name = result.getName();
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
                    SPCacheUtils.put("title", result.getTitle());
                    SPCacheUtils.put("year", result.getYearsOfWorking());
                    SPCacheUtils.put("company", result.getOrganization().getName());
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
        myInfoDetailModel.getOptions(new ObserverListener<JSONObject>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(JSONObject result) {
                if (result != null) {
                    List<IdNameInfo> workIdNameInfo = null;
                    List<IdNameInfo> adeptIdNameInfo = null;
                    if (result.containsKey("workingCity")) {
                        workIdNameInfo = JSONObject.parseArray(result.getString("workingCity"), IdNameInfo.class);
                    }

                    if (result.containsKey("adeptWorks")) {
                        adeptIdNameInfo = JSONObject.parseArray(result.getString("adeptWorks"), IdNameInfo.class);
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
            workingCityKey, adeptWorksKey, new ObserverListener<TeacherInfo>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    addDisposable(disposable);
                }

                @Override
                public void onNext(TeacherInfo result) {
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
        myInfoDetailModel.refreshTacken(new ObserverListener<TokenBean>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(TokenBean result) {
                view.refreshSuccess(result);
            }

            @Override
            public void onError(String msg) {
                view.updateFail(msg);
            }
        });
    }

    @Override
    public void updateVideoUrl(final String videoUrl) {
        myInfoDetailModel.updateVideoUrl(videoUrl, new ObserverListener<TeacherInfo>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(TeacherInfo result) {
                view.updateVideoUrlSuccess();
            }

            @Override
            public void onError(String msg) {
                view.updateFail(msg);
            }
        });
    }
}

