package com.smartstudy.counselor_t.mvp.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstudy.counselor_t.R;
import com.smartstudy.counselor_t.mvp.base.BasePresenterImpl;
import com.smartstudy.counselor_t.mvp.contract.CommonSearchContract;
import com.smartstudy.counselor_t.util.Utils;

/**
 * Created by louis on 2017/3/4.
 */

public class CommonSearchPresenter extends BasePresenterImpl<CommonSearchContract.View> implements CommonSearchContract.Presenter {


    public CommonSearchPresenter(CommonSearchContract.View view) {
        super(view);
    }

    @Override
    public void getSchools(int cacheType, String countryId, final String keyword, final int page,
                           final int request_state, final String flag) {
    }

    @Override
    public void setEmptyView(LayoutInflater mInflater, final Context context, ViewGroup parent, String from) {
        View emptyView = mInflater.inflate(R.layout.layout_empty, parent, false);
        emptyView.findViewById(R.id.iv_loading).setVisibility(View.GONE);
        emptyView.findViewById(R.id.llyt_err).setVisibility(View.VISIBLE);
        ImageView iv_err = (ImageView) emptyView.findViewById(R.id.iv_err);
        Button tv_qa_btn = (Button) emptyView.findViewById(R.id.tv_qa_btn);
        TextView tv_err_tip = (TextView) emptyView.findViewById(R.id.tv_err_tip);
        tv_err_tip.setText(context.getString(R.string.no_search_tip));
        if (!Utils.isNetworkConnected()) {
            emptyView.findViewById(R.id.llyt_err).setVisibility(View.VISIBLE);
            tv_qa_btn.setVisibility(View.GONE);
            iv_err.setImageResource(R.drawable.ic_net_err);
            tv_err_tip.setText(context.getString(R.string.no_net_tip));
        }
        view.showEmptyView(emptyView);
        mInflater = null;
        parent = null;
    }
}
