package study.smart.baselib.listener;

/**
 * 进度条监听接口
 * Created by louis on 2017/3/6.
 */
public interface OnDownloadFileListener<T> {
    void onErr(String msg);

    void onProgress(int progress);

    void onFinish(T result);
}