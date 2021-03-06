package com.smartstudy.counselor_t.ui.adapter.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.text.util.Linkify;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.smartstudy.counselor_t.util.DisplayImageUtils;


/**
 * Created by louis on 2017/3/2.
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mConvertView;
    private Context mContext;

    public ViewHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
        mConvertView = itemView;
        mViews = new SparseArray<View>();
    }


    public static ViewHolder createViewHolder(Context context, View itemView) {
        ViewHolder holder = new ViewHolder(context, itemView);
        return holder;
    }

    public static ViewHolder createViewHolder(Context context,
                                              ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        ViewHolder holder = new ViewHolder(context, itemView);
        return holder;
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }


    /****以下为辅助方法*****/

    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public ViewHolder setText(int viewId, Spanned text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public ViewHolder setImageUrl(int viewId, String url) {
        ImageView view = getView(viewId);
        DisplayImageUtils.displayImage(mContext, url, view);
        return this;
    }

    public ViewHolder setImageUrl(ImageView view, String url, boolean isScale) {
        if (isScale) {
            DisplayImageUtils.formatImgUrl(mContext, url, view);
        } else {
            DisplayImageUtils.displayImage(mContext, url, view);
        }
        return this;
    }

    public ViewHolder setImageUrl(ImageView view, String url) {
        DisplayImageUtils.displayImage(mContext, url, view);
        return this;
    }

    public ViewHolder setCircleImageUrl(int viewId, final String url, boolean isScale) {
        final ImageView view = getView(viewId);
        if (isScale) {
            DisplayImageUtils.formatCircleImgUrl(mContext, url, view);
        } else {
            DisplayImageUtils.displayCircleImage(mContext, url, view);
        }
        return this;
    }

    public ViewHolder setPersonImageUrl(int viewId, final String url, boolean isScale) {
        final ImageView view = getView(viewId);
        if (isScale) {
            DisplayImageUtils.formatPersonImgUrl(mContext, url, view);
        } else {
            DisplayImageUtils.displayPersonImage(mContext, url, view);
        }
        return this;
    }

    public ViewHolder setRoundImageUrl(int viewId, final String url, int radius, boolean isScale) {
        final ImageView view = getView(viewId);
        if (isScale) {
            DisplayImageUtils.formatRoundImgUrl(mContext, url, view, radius);
        } else {
            DisplayImageUtils.displayRoundImage(mContext, url, view, radius);
        }
        return this;
    }

    public ViewHolder setCircleImageUrlWithBorder(int viewId, final String url, boolean isScale,
                                                  final int borderWidth, final int borderColor) {
        final ImageView view = getView(viewId);
        if (isScale) {
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                boolean hasMeasured = false;

                @Override
                public boolean onPreDraw() {
                    if (!hasMeasured) {
                        int width = view.getMeasuredWidth() - borderWidth;
                        int height = view.getMeasuredHeight() - borderWidth;
                        String params = "?x-oss-process=image/resize,m_pad,w_" + width + ",h_" + height;
                        Log.d("url", url + params);
                        DisplayImageUtils.displayCircleImageWithborder(mContext, url + params, view, borderWidth, borderColor);
                        view.getViewTreeObserver().removeOnPreDrawListener(this);
                        hasMeasured = true;
                    }
                    return true;
                }
            });
        } else {
            DisplayImageUtils.displayCircleImage(mContext, url, view);
        }
        return this;
    }

    public ViewHolder setPersonImageUrlWithBorder(int viewId, final String url, boolean isScale,
                                                  final int borderWidth, final int borderColor) {
        final ImageView view = getView(viewId);
        if (isScale) {
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                boolean hasMeasured = false;

                @Override
                public boolean onPreDraw() {
                    if (!hasMeasured) {
                        int width = view.getMeasuredWidth() - borderWidth;
                        int height = view.getMeasuredHeight() - borderWidth;
                        String params = "?x-oss-process=image/resize,m_pad,w_" + width + ",h_" + height;
                        Log.d("url", url + params);
                        DisplayImageUtils.displayPersonImageWithborder(mContext, url + params, view, borderWidth, borderColor);
                        view.getViewTreeObserver().removeOnPreDrawListener(this);
                        hasMeasured = true;
                    }
                    return true;
                }
            });
        } else {
            DisplayImageUtils.displayPersonImage(mContext, url, view);
        }
        return this;
    }

    public ViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        DisplayImageUtils.displayImageRes(mContext, resId, view);
        return this;
    }

    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        DisplayImageUtils.displayImageRes(mContext, bitmap, view);
        return this;
    }

    public ViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        DisplayImageUtils.displayImageRes(mContext, drawable, view);
        return this;
    }

    public ViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public ViewHolder setBackgroundRes(int viewId, int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public ViewHolder setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public ViewHolder setTextColorRes(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        view.setTextColor(mContext.getResources().getColor(textColorRes));
        return this;
    }

    @SuppressLint("NewApi")
    public ViewHolder setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    public ViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public ViewHolder linkify(int viewId) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public ViewHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    public ViewHolder setProgress(int viewId, int progress) {
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    public ViewHolder setProgress(int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public ViewHolder setMax(int viewId, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }

    public ViewHolder setRating(int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    public ViewHolder setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    public ViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public ViewHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public ViewHolder setChecked(int viewId, boolean checked) {
        Checkable view = (Checkable) getView(viewId);
        view.setChecked(checked);
        return this;
    }

    /**
     * 关于事件的
     */
    public ViewHolder setOnClickListener(int viewId,
                                         View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public ViewHolder setOnTouchListener(int viewId,
                                         View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public ViewHolder setOnLongClickListener(int viewId,
                                             View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }


}
