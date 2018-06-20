package com.smartstudy.counselor_t.mvp.presenter;

import android.util.Log;

import com.smartstudy.counselor_t.listener.FileDownLoadObserver;
import com.smartstudy.counselor_t.listener.OnDownloadFileListener;
import com.smartstudy.counselor_t.mvp.base.BasePresenterImpl;
import com.smartstudy.counselor_t.mvp.contract.DownloadFileContract;
import com.smartstudy.counselor_t.mvp.model.DownloadFileModel;

import java.io.File;

import io.reactivex.disposables.Disposable;

/**
 * @author louis
 * @date on 2018/3/15
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class DownloadFilePresenter extends BasePresenterImpl<DownloadFileContract.View> implements DownloadFileContract.Presenter {

    private DownloadFileModel model;

    public DownloadFilePresenter(DownloadFileContract.View view) {
        super(view);
        model = new DownloadFileModel();
    }

    @Override
    public void detach() {
        super.detach();
        model = null;
    }

    @Override
    public void downloadFile(String url, File fileDir, String fileName, final OnDownloadFileListener<File> listener) {
        model.downloadFile(url, fileDir, fileName, new FileDownLoadObserver<File>() {
            @Override
            public void onProgress(int progress, long total) {
                listener.onProgress(progress);
            }

            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(File result) {
                listener.onFinish(result);
            }

            @Override
            public void onError(String msg) {
                listener.onErr(msg);
            }
        });
    }
}
