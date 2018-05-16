package com.smartstudy.counselor_t.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.base.config.CustomShapeTransformation;
import com.smartstudy.counselor_t.base.config.GlideApp;
import com.smartstudy.counselor_t.base.config.glideprogress.ProgressTarget;
import com.smartstudy.counselor_t.ui.widget.GlideBlurformation;

import java.io.File;


/**
 * 展示图片封装类
 * Created by louis on 2017/3/4.
 */
public class DisplayImageUtils {
    public static void displayGif(Context context, int resId, ImageView view) {
        GlideApp.with(context).asGif().load(resId).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(view);
    }

    public static void displayImage(Context context, String url, ImageView view) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(R.drawable.ic_img_default)
            .error(R.drawable.ic_img_default).centerCrop().dontAnimate().into(view);
    }

    public static void displayBlurImage(Context context, Object url, ImageView view) {
        GlideApp.with(context).load(url).transform(new GlideBlurformation(context)).diskCacheStrategy(DiskCacheStrategy.DATA)
            .placeholder(R.drawable.ic_img_default).error(R.drawable.ic_img_default).dontAnimate().into(view);
    }

    public static void displayBubbleImage(Context context, String url, ImageView view) {
        GlideApp.with(context).load(url).transform(new CustomShapeTransformation(context, R.drawable.bg_msg_img_left))
            .placeholder(R.drawable.bg_msg_img_left).error(R.drawable.bg_msg_img_left)
            .into(view);
    }

    public static void displayImageNoHolder(Context context, String url, ImageView view) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.DATA)
            .centerCrop().dontAnimate().into(view);
    }

    public static void displayImage(Context context, String url, ProgressTarget<String, Drawable> progressTarget) {
        GlideApp.with(context)
            .load(url)
            .dontAnimate()
            .into(progressTarget);
    }

    public static void displayLocationImage(Context context, String url, ImageView view) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.DATA)
            .error(R.drawable.location_default).centerCrop().dontAnimate().into(view);
    }

    public static void displayImage(Context context, String url, SimpleTarget<Bitmap> simpleTarget) {
        GlideApp.with(context).asBitmap().load(url).diskCacheStrategy(DiskCacheStrategy.DATA)
            .error(R.drawable.ic_img_default).centerCrop().dontAnimate().into(simpleTarget);
    }

    public static void displayImage(Context context, String url, ImageView view, RequestListener<Drawable> listener) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).listener(listener).into(view);
    }

    public static void displayImageFile(Context context, String url, SimpleTarget<File> simpleTarget) {
        GlideApp.with(context).downloadOnly().load(url).diskCacheStrategy(DiskCacheStrategy.DATA).into(simpleTarget);
    }

    public static void displayImageFile(Context context, Bitmap btp, SimpleTarget<File> simpleTarget) {
        GlideApp.with(context).downloadOnly().load(BitmapUtils.compressBitmap2Byte(btp)).into(simpleTarget);
    }

    public static void displayImageRes(Context context, int id, ImageView view) {
        GlideApp.with(context).load(id).error(R.drawable.ic_img_default).centerCrop().into(view);
    }

    public static void displayImageRes(Context context, Drawable drawable, ImageView view) {
        GlideApp.with(context).load(drawable).error(R.drawable.ic_img_default).centerCrop().into(view);
    }

    public static void displayImageRes(Context context, Bitmap bitmap, ImageView view) {
        GlideApp.with(context).load(BitmapUtils.compressBitmap2Byte(bitmap)).centerCrop().error(R.drawable.ic_img_default).into(view);
    }

    public static void displayCircleImage(Context context, String url, ImageView view) {
        GlideApp.with(context).load(url).circleCrop().diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(R.drawable.ic_circleimg_default)
            .error(R.drawable.ic_circleimg_default)
            .dontAnimate().into(view);
    }


    public static void displayRoundImage(Context context, String url, ImageView view, int radius_dp) {
        GlideApp.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.DATA)
            .centerCrop().transform(new RoundedCorners(radius_dp))
            .dontAnimate().into(view);
    }

    public static void displayRoundImageNoHolder(Context context, String url, ImageView view, int radius_dp) {
        GlideApp.with(context).load(url).centerCrop().diskCacheStrategy(DiskCacheStrategy.DATA)
            .transform(new RoundedCorners(radius_dp))
            .dontAnimate().into(view);
    }

    public static void displayCircleImageWithborder(Context context, String url, ImageView view,
                                                    int border_width, int border_color) {
        GlideApp.with(context).load(url).circleCrop().diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(R.drawable.ic_circleimg_default)
            .error(R.drawable.ic_circleimg_default)
            .dontAnimate().into(view);
    }

    public static void displayPersonImageWithborder(Context context, String url, ImageView view,
                                                    int border_width, int border_color) {
        GlideApp.with(context).load(url).circleCrop().diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(R.drawable.rc_default_portrait)
            .error(R.drawable.rc_default_portrait)
            .dontAnimate().into(view);
    }

    public static void displayPersonImage(Context context, String url, ImageView view) {
        GlideApp.with(context).load(url).circleCrop().diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(R.drawable.rc_default_portrait)
            .error(R.drawable.rc_default_portrait)
            .dontAnimate().into(view);
    }

    public static void displayPersonImage(Context context, String url, ImageView view, RequestListener<Drawable> listener) {
        GlideApp.with(context).load(url).circleCrop().diskCacheStrategy(DiskCacheStrategy.DATA).listener(listener).placeholder(R.drawable.rc_default_portrait)
            .error(R.drawable.rc_default_portrait)
            .dontAnimate().into(view);
    }

    public static void displayPersonRes(Context context, int id, ImageView view) {
        GlideApp.with(context).load(id).circleCrop().error(R.drawable.rc_default_portrait).dontAnimate().into(view);
    }

    public static void displayPersonRes(Context context, File file, ImageView view) {
        GlideApp.with(context).load(file).circleCrop().error(R.drawable.rc_default_portrait).dontAnimate().into(view);
    }

    public static void displayPersonRes(Context context, Bitmap bmp, ImageView view) {
        GlideApp.with(context).load(BitmapUtils.compressBitmap2Byte(bmp)).circleCrop().error(R.drawable.rc_default_portrait)
            .dontAnimate().into(view);
    }

    public static String formatPersonImgUrl(final Context context, final String url, final ImageView iv) {
        String new_url = url;
        iv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            boolean hasMeasured = false;

            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    int width = iv.getMeasuredWidth();
                    int height = iv.getMeasuredHeight();
                    String params = "?x-oss-process=image/resize,m_pad,w_" + width + ",h_" + height;
                    Log.d("url", url + params);
                    DisplayImageUtils.displayPersonImage(context, url + params, iv);
                    iv.getViewTreeObserver().removeOnPreDrawListener(this);
                    hasMeasured = true;
                }
                return true;
            }
        });
        return new_url;
    }

    public static String formatCircleImgUrl(final Context context, final String url, final ImageView iv) {
        String new_url = url;
        iv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            boolean hasMeasured = false;

            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    int width = iv.getMeasuredWidth();
                    int height = iv.getMeasuredHeight();
                    String params = "?x-oss-process=image/resize,m_pad,w_" + width + ",h_" + height;
                    Log.d("url", url + params);
                    DisplayImageUtils.displayCircleImage(context, url + params, iv);
                    iv.getViewTreeObserver().removeOnPreDrawListener(this);
                    hasMeasured = true;
                }
                return true;
            }
        });
        return new_url;
    }

    public static String formatImgUrl(final Context context, final String url, final ImageView iv) {
        String new_url = url;
        iv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            boolean hasMeasured = false;

            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    int width = iv.getMeasuredWidth();
                    int height = iv.getMeasuredHeight();
                    String params = "?x-oss-process=image/resize,m_lfit,w_" + width + ",h_" + height;
                    Log.d("url", url + params);
                    DisplayImageUtils.displayImage(context, url + params, iv);
                    iv.getViewTreeObserver().removeOnPreDrawListener(this);
                    hasMeasured = true;
                }
                return true;
            }
        });
        return new_url;
    }

    public static String formatImgUrl(final Context context, final String url, final ImageView iv, final RequestListener<Drawable> listener) {
        String new_url = url;
        iv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            boolean hasMeasured = false;

            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    int width = iv.getMeasuredWidth();
                    int height = iv.getMeasuredHeight();
                    String params = "?x-oss-process=image/resize,m_lfit,w_" + width + ",h_" + height;
                    Log.d("url", url + params);
                    DisplayImageUtils.displayImage(context, url + params, iv, listener);
                    iv.getViewTreeObserver().removeOnPreDrawListener(this);
                    hasMeasured = true;
                }
                return true;
            }
        });
        return new_url;
    }

    public static String formatImgUrlNoHolder(final Context context, final String url, final ImageView iv) {
        String new_url = url;
        iv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            boolean hasMeasured = false;

            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    int width = iv.getMeasuredWidth();
                    int height = iv.getMeasuredHeight();
                    String params = "?x-oss-process=image/resize,m_lfit,w_" + width + ",h_" + height;
                    Log.d("url", url + params);
                    DisplayImageUtils.displayImageNoHolder(context, url + params, iv);
                    iv.getViewTreeObserver().removeOnPreDrawListener(this);
                    hasMeasured = true;
                }
                return true;
            }
        });
        return new_url;
    }

    public static String formatRoundImgUrl(final Context context, final String url, final ImageView iv, final int radius) {
        String new_url = url;
        iv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            boolean hasMeasured = false;

            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    int width = iv.getMeasuredWidth();
                    int height = iv.getMeasuredHeight();
                    String params = "?x-oss-process=image/resize,m_lfit,w_" + width + ",h_" + height;
                    Log.d("url", url + params);
                    DisplayImageUtils.displayRoundImage(context, url + params, iv, radius);
                    iv.getViewTreeObserver().removeOnPreDrawListener(this);
                    hasMeasured = true;
                }
                return true;
            }
        });
        return new_url;
    }

    public static String formatRoundImgUrlNoHolder(final Context context, final String url, final ImageView iv, final int radius) {
        String new_url = url;
        iv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            boolean hasMeasured = false;

            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    int width = iv.getMeasuredWidth();
                    int height = iv.getMeasuredHeight();
                    String params = "?x-oss-process=image/resize,m_lfit,w_" + width + ",h_" + height;
                    Log.d("url", url + params);
                    DisplayImageUtils.displayRoundImageNoHolder(context, url + params, iv, radius);
                    iv.getViewTreeObserver().removeOnPreDrawListener(this);
                    hasMeasured = true;
                }
                return true;
            }
        });
        return new_url;
    }

    public static String formatImgUrl(String url, int width, int height) {
        String new_url = url;
        String params = "?x-oss-process=image/resize,m_lfit,w_" + width + ",h_" + height;
        if (!TextUtils.isEmpty(new_url)) {
            if (new_url.contains("!")) {
                new_url = new_url.split("!")[0] + params;
            } else {
                new_url = new_url + params;
            }
        }
        return new_url;
    }
}
