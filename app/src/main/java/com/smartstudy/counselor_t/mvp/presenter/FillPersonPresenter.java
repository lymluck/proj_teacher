package com.smartstudy.counselor_t.mvp.presenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.counselor_t.entity.IdNameInfo;
import com.smartstudy.counselor_t.entity.TeacherInfo;
import com.smartstudy.counselor_t.listener.ObserverListener;
import com.smartstudy.counselor_t.mvp.base.BasePresenterImpl;
import com.smartstudy.counselor_t.mvp.contract.FillPersonContract;
import com.smartstudy.counselor_t.mvp.model.FillPersonModel;
import com.smartstudy.counselor_t.util.SPCacheUtils;

import java.io.File;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * @author yqy
 * @date on 2018/1/18
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class FillPersonPresenter extends BasePresenterImpl<FillPersonContract.View> implements FillPersonContract.Presenter {

    private FillPersonModel fillPersonModel;

    public FillPersonPresenter(FillPersonContract.View view) {
        super(view);
        fillPersonModel = new FillPersonModel();
    }


    @Override
    public void detach() {
        super.detach();
        fillPersonModel = null;
    }

    @Override
    public void postPersonInfo(String name, File avatar, String title, String school, String yearsOfWorking,
                               String email, String realName, String workingCityKey, String adeptWorksKey, String introdction) {
        fillPersonModel.postPersonInfo(name, avatar, title, school, yearsOfWorking, email, realName
            , workingCityKey, adeptWorksKey, introdction, new ObserverListener<String>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                    addDisposable(disposable);
                }

                @Override
                public void onNext(String s) {
                    view.getStudentInfoDetailSuccess();
                }

                @Override
                public void onError(String msg) {
                    view.showTip(msg);
                }
            });
    }

    @Override
    public void getAuditResult() {
        fillPersonModel.getAuditResult(new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String s) {
                TeacherInfo teacherInfo = JSON.parseObject(s, TeacherInfo.class);
                if (teacherInfo != null) {
                    SPCacheUtils.put("title", teacherInfo.getTitle());
                    SPCacheUtils.put("year", teacherInfo.getYearsOfWorking());
                    if (teacherInfo.getOrganization() != null) {
                        SPCacheUtils.put("company", teacherInfo.getOrganization().getName());
                    }
                    view.showAuditResult(teacherInfo);
                }
            }

            @Override
            public void onError(String msg) {
//                view.showTip(msg);
            }
        });
    }

    @Override
    public void getOptions() {
        fillPersonModel.getOptions(new ObserverListener<JSONObject>() {
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
}

