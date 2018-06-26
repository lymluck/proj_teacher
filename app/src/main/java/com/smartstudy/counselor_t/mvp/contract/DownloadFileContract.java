package com.smartstudy.counselor_t.mvp.contract;


import study.smart.baselib.listener.OnDownloadFileListener;
import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;

import java.io.File;

/**
 * Created by louis on 2017/3/4.
 */

public interface DownloadFileContract {

    interface View extends BaseView {
    }

    interface Presenter extends BasePresenter {

        void downloadFile(String url, File fileDir, String fileName, final OnDownloadFileListener<File> listener);
    }
}
