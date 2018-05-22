package com.smartstudy.counselor_t.ossServer;

import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.smartstudy.counselor_t.entity.ProgressItem;
import com.smartstudy.medialib.ijkplayer.util.NetUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author yqy
 * @date on 2018/5/21
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class OssService {
    private OSS oss;
    private String bucket;
    private picResultCallback callback;//回调接口
    private String path = "";

    public OssService(OSS oss, String bucket, picResultCallback callback) {
        this.oss = oss;
        this.bucket = bucket;
        this.callback = callback;
    }

    public void asyncPutVideo(String object, final String localFile) {
        if (object.equals("")) {
            return;
        }
        File file = new File(localFile);
        if (!file.exists()) {
            return;
        }
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(bucket, object, localFile);
        put.setCallbackParam(new HashMap<String, String>() {
            {
                put("callbackUrl", path);
                put("callbackBody", "filename=${object}&size=${size}&id=${x:id}&action=${x:action}");
            }
        });

        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {

            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                int progress = (int) (100 * currentSize / totalSize);
                EventBus.getDefault().post(new ProgressItem(progress, localFile));
            }
        });
        OSSAsyncTask ossAsyncTask = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, final PutObjectResult result) {
                callback.getPicData(request.getObjectKey());
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                callback.uploadError();
                String info = "";
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());

                }
            }
        });
    }

    //成功的回调接口
    public interface picResultCallback {
        void getPicData(String videoUrl);

        void uploadError();
    }
}

