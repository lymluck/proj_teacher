package study.smart.baselib.manager;

import android.net.Uri;
import android.text.TextUtils;

import study.smart.baselib.entity.StudentPageInfo;
import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;
import study.smart.baselib.utils.DisplayImageUtils;

import io.reactivex.disposables.Disposable;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * @author louis
 * @date on 2018/1/30
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class StudentInfoManager extends BaseModel {

    private static StudentInfoManager instance;

    public static StudentInfoManager getInstance() {
        if (null == instance) {
            synchronized (StudentInfoManager.class) {
                if (null == instance) {
                    instance = new StudentInfoManager();
                }
            }
        }
        return instance;
    }

    public void getStudentInfo(String userId) {
        apiSubscribe(ApiManager.getApiService().getStudentInfo(getHeadersMap(), userId), new ObserverListener<StudentPageInfo>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }

            @Override
            public void onNext(StudentPageInfo result) {
                if (result != null) {
                    String cacheUrl = DisplayImageUtils.formatImgUrl(result.getAvatar(), 90, 90);
                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(result.getImUserId(), result.getName(), TextUtils.isEmpty(cacheUrl) ? null : Uri.parse(cacheUrl)));
                }
            }

            @Override
            public void onError(String msg) {
            }
        });
    }

}
