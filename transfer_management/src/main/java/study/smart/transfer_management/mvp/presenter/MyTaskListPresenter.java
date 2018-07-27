package study.smart.transfer_management.mvp.presenter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

import io.reactivex.disposables.Disposable;
import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BasePresenterImpl;
import study.smart.baselib.utils.DisplayImageUtils;
import study.smart.baselib.utils.Utils;
import study.smart.transfer_management.R;
import study.smart.baselib.entity.MyTaskInfo;
import study.smart.transfer_management.mvp.contract.MyTaskListContract;
import study.smart.transfer_management.mvp.model.MyTaskListModel;

/**
 * @author yqy
 * @date on 2018/7/25
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyTaskListPresenter extends BasePresenterImpl<MyTaskListContract.View> implements MyTaskListContract.Presenter {

    private MyTaskListModel myTaskListModel;

    public MyTaskListPresenter(MyTaskListContract.View view) {
        super(view);
        myTaskListModel = new MyTaskListModel();
    }

    @Override
    public void detach() {
        super.detach();
        myTaskListModel = null;
    }


    @Override
    public void showLoading(Context context, View emptyView) {
        ImageView iv_err = (ImageView) emptyView.findViewById(R.id.iv_err);
        TextView tv_err_tip = (TextView) emptyView.findViewById(R.id.tv_err_tip);
        if (!Utils.isNetworkConnected()) {
            emptyView.findViewById(R.id.llyt_err).setVisibility(View.VISIBLE);
            emptyView.findViewById(R.id.iv_loading).setVisibility(View.GONE);
            iv_err.setImageResource(R.drawable.ic_net_err);
            tv_err_tip.setText(context.getString(R.string.no_net_tip));
        } else {
            emptyView.findViewById(R.id.llyt_err).setVisibility(View.GONE);
            ImageView iv_loading = (ImageView) emptyView.findViewById(R.id.iv_loading);
            iv_loading.setVisibility(View.VISIBLE);
            DisplayImageUtils.displayGif(context, R.drawable.gif_data_loading, iv_loading);
        }
        view.showEmptyView(emptyView);
        context = null;
        emptyView = null;
    }

    @Override
    public void setEmptyView(View emptyView) {
        emptyView.findViewById(R.id.llyt_err).setVisibility(View.VISIBLE);
        emptyView.findViewById(R.id.iv_loading).setVisibility(View.GONE);
        view.showEmptyView(emptyView);
        emptyView = null;
    }

    @Override
    public void getMyTaskList(String from, String userId) {
        myTaskListModel.getMyTalkRecordList(from, userId, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                List<MyTaskInfo> myTaskInfoList = JSONObject.parseArray(result, MyTaskInfo.class);
                view.getMyTaskListSuccess(myTaskInfoList);
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

}

