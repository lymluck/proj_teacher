package com.smartstudy.counselor_t.mvp.model;

import com.smartstudy.counselor_t.listener.FileDownLoadObserver;
import com.smartstudy.counselor_t.mvp.base.BaseModel;
import com.smartstudy.counselor_t.server.api.ApiManager;

import java.io.File;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * @author louis
 * @date on 2018/3/15
 * @describe 下载文件
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class DownloadFileModel extends BaseModel {

    public void downloadFile(String url, final File file, final String fileName, final FileDownLoadObserver listener) {
        fileObservalbe(ApiManager.getApiService().downLoadFile(url))
                .observeOn(Schedulers.computation())
                .map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(@NonNull ResponseBody responseBody) throws Exception {
                        return listener.saveFile(responseBody, file, fileName);
                    }
                }).subscribe(new Consumer<File>() {
                    @Override
                    public void accept(@NonNull File file) throws Exception {
                        listener.onNext(file);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        listener.onError(throwable.getMessage());
                    }
                });
    }
}
