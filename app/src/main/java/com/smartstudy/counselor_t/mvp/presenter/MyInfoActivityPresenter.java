package com.smartstudy.counselor_t.mvp.presenter;

import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.smartstudy.counselor_t.entity.TeacherInfo;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BasePresenterImpl;
import com.smartstudy.counselor_t.mvp.contract.MyInfoContract;
import com.smartstudy.counselor_t.mvp.model.MyInfoModel;
import com.smartstudy.counselor_t.util.DisplayImageUtils;
import com.smartstudy.counselor_t.util.SPCacheUtils;

import java.io.File;

import io.reactivex.disposables.Disposable;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * @author yqy
 * @date on 2018/1/22
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyInfoActivityPresenter extends BasePresenterImpl<MyInfoContract.View> implements MyInfoContract.Presenter {

    private MyInfoModel myInfoModel;

    public MyInfoActivityPresenter(MyInfoContract.View view) {
        super(view);
        myInfoModel = new MyInfoModel();
    }


    @Override
    public void detach() {
        super.detach();
        myInfoModel = null;
    }


    @Override
    public void getMyInfo() {
        myInfoModel.getAuditResult(new ObserverListener<String>() {
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
    public void getLogOut() {
        myInfoModel.getLogOut(new ObserverListener<String>() {
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
    public void updateMyInfo(String name, File avatar, String title, String school, String yearsOfWorking,
                             String email, String realName, final ImageView ivAvatar) {
        myInfoModel.updatePersonInfo(name, avatar, title, school, yearsOfWorking, email, realName, new ObserverListener<String>() {
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
                    view.updateMyInfoSuccesee();
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }
}

