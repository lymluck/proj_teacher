package com.smartstudy.counselor_t.base.config;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.smartstudy.counselor_t.base.config.https.OkHttpUrlLoader;
import com.smartstudy.counselor_t.listener.GlideProgressListener;
import com.smartstudy.counselor_t.util.SDCardUtils;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;

/**
 * glide加载图片自定义配置
 * Created by louis on 2017/3/6.
 */
@GlideModule
public class GlideConfiguration extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        File cache_file = SDCardUtils.getFileDirPath("Xxd_im" + File.separator + "cache");
        if (cache_file != null) {
            //100M
            builder.setDiskCache(new DiskLruCacheFactory(cache_file.getAbsolutePath(), 100 * 1024 * 1024));
        }
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }


    public static void forget(String url) {
        DispatchingProgressListener.forget(url);
    }

    public static void expect(String url, GlideProgressListener listener) {
        DispatchingProgressListener.expect(url, listener);
    }

    private interface ResponseProgressListener {
        void update(HttpUrl url, long bytesRead, long contentLength);
    }

    private static class DispatchingProgressListener implements ResponseProgressListener {
        private static final Map<String, GlideProgressListener> LISTENERS = new HashMap<>();
        private static final Map<String, Long> PROGRESSES = new HashMap<>();

        private final Handler handler;

        DispatchingProgressListener() {
            this.handler = new Handler(Looper.getMainLooper());
        }

        static void forget(String url) {
            LISTENERS.remove(url);
            PROGRESSES.remove(url);
        }

        static void expect(String url, GlideProgressListener listener) {
            LISTENERS.put(url, listener);
        }

        @Override
        public void update(HttpUrl url, final long bytesRead, final long contentLength) {
            //System.out.printf("%s: %d/%d = %.2f%%%n", url, bytesRead, contentLength, (100f * bytesRead) / contentLength);
            String key = url.toString();
            final GlideProgressListener listener = LISTENERS.get(key);
            if (listener == null) {
                return;
            }
            if (contentLength <= bytesRead) {
                forget(key);
            }
            if (needsDispatch(key, bytesRead, contentLength, listener.getGranularityPercentage())) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onProgress(bytesRead, contentLength);
                    }
                });
            }
        }

        private boolean needsDispatch(String key, long current, long total, float granularity) {
            if (granularity == 0 || current == 0 || total == current) {
                return true;
            }
            float percent = 100f * current / total;
            long currentProgress = (long) (percent / granularity);
            Long lastProgress = PROGRESSES.get(key);
            if (lastProgress == null || currentProgress != lastProgress) {
                PROGRESSES.put(key, currentProgress);
                return true;
            } else {
                return false;
            }
        }
    }
}
