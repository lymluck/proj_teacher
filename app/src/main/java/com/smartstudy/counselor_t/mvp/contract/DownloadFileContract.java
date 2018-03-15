package com.smartstudy.counselor_t.mvp.contract;

import com.smartstudy.counselor_t.listener.OnDownloadFileListener;
import com.smartstudy.counselor_t.mvp.base.BasePresenter;
import com.smartstudy.counselor_t.mvp.base.BaseView;

import java.io.File;

/**
 * Created by louis on 2017/3/4.
 */

public interface DownloadFileContract {

    interface View extends BaseView {
    }

    interface Presenter extends BasePresenter {

        void downloadFile(String url, File downloadFile, final OnDownloadFileListener<File> listener);
    }
}
