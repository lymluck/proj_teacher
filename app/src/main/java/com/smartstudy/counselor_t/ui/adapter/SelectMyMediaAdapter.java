package com.smartstudy.counselor_t.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.entity.MediaInfo;
import com.smartstudy.counselor_t.ui.adapter.base.ViewHolder;
import com.smartstudy.counselor_t.util.Utils;

import java.util.ArrayList;
import java.util.List;


public class SelectMyMediaAdapter extends CommonAdapter<MediaInfo> {

    private TextView mOKBtn;
    private boolean mChecked;
    /**
     * 用来存储图片的选中情况
     */
    private SparseBooleanArray mSelectMap = new SparseBooleanArray();

    public SelectMyMediaAdapter(Context context, List<MediaInfo> mDatas, int itemLayoutId) {
        super(context, itemLayoutId, mDatas);
        mOKBtn = (TextView) ((Activity) context).findViewById(R.id.topdefault_righttext);
    }

    @Override
    public void convert(final ViewHolder helper, final MediaInfo info, final int position) {
        ImageView imageView = helper.getView(R.id.id_item_image);
        LinearLayout mCheckBoxLl = helper.getView(R.id.llyt_check_pic);
        final TextView mCapture = helper.getView(R.id.photo_item_one);
        TextView tvSize = helper.getView(R.id.tv_size);
        // 设置图片
        if (info != null) {
            helper.setImageUrl(imageView, info.getPath());
            imageView.setVisibility(View.VISIBLE);
            mCapture.setVisibility(View.GONE);
            if ("video".equals(info.getType())) {
                tvSize.setVisibility(View.VISIBLE);
                tvSize.setText(info.getSize());
            } else {
                tvSize.setVisibility(View.GONE);
            }
        } else {
            imageView.setVisibility(View.GONE);
            mCapture.setVisibility(View.VISIBLE);
            mCheckBoxLl.setVisibility(View.GONE);
            tvSize.setVisibility(View.GONE);
        }

        final CheckBox mCheckBox = helper.getView(R.id.child_checkbox);
        mCheckBox.setChecked(mSelectMap.get(position));
        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCheckBox.isChecked()) {
                    if (mSelectMap.size() < 9) {
                        mChecked = true;
                        mSelectMap.put(position, true);
                        Utils.addAnimation(mCheckBox);
                    } else {
                        mChecked = false;
                        Toast.makeText(mContext, mContext.getString(R.string.picture_max), Toast.LENGTH_SHORT).show();
                        mCheckBox.setChecked(mSelectMap.get(position));
                    }
                } else if (mSelectMap.size() <= 9) {
                    mChecked = false;
                    mSelectMap.delete(position);
                }

                if (mSelectMap.size() > 0) {
                    mOKBtn.setClickable(true);
                    mOKBtn.setText(mContext.getString(R.string.sure) + "(" + mSelectMap.size() + "/" + "9)");
                } else {
                    mOKBtn.setText(mContext.getString(R.string.sure));
                    mOKBtn.setClickable(false);
                }
            }
        });
        mCheckBoxLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mChecked) {
                    if (mSelectMap.size() < 9) {
                        mSelectMap.put(position, true);
                        mCheckBox.setChecked(true);
                        Utils.addAnimation(mCheckBox);
                        mChecked = true;
                    } else {
                        mChecked = false;
                        Toast.makeText(mContext, mContext.getString(R.string.picture_max), Toast.LENGTH_SHORT).show();
                        mCheckBox.setChecked(mSelectMap.get(position));
                    }
                } else if (mSelectMap.size() <= 9) {
                    mSelectMap.delete(position);
                    mChecked = false;
                    mCheckBox.setChecked(false);
                }

                if (mSelectMap.size() > 0) {
                    mOKBtn.setClickable(true);
                    mOKBtn.setText(mContext.getString(R.string.sure) + "(" + mSelectMap.size() + "/" + "9)");
                } else {
                    mOKBtn.setText(mContext.getString(R.string.sure));
                    mOKBtn.setClickable(false);
                }
            }
        });
        if (mOKBtn.getVisibility() == View.VISIBLE) {
            mCheckBoxLl.setVisibility(View.VISIBLE);
        } else if (mOKBtn.getVisibility() == View.GONE) {
            mCheckBoxLl.setVisibility(View.GONE);
        }
    }

    /**
     * 获取选中的Item的position
     *
     * @return 选中的图片路径集合
     */
    public List<Integer> getSelectItems() {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < mSelectMap.size(); i++) {
            list.add(mSelectMap.keyAt(i));
        }
        return list;
    }

}
