package study.smart.baselib.mvp.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

import io.reactivex.disposables.Disposable;
import study.smart.baselib.R;
import study.smart.baselib.entity.DataListInfo;
import study.smart.baselib.entity.MessageDetailItemInfo;
import study.smart.baselib.entity.MyStudentInfo;
import study.smart.baselib.entity.TransferManagerEntity;
import study.smart.baselib.entity.WorkingSearchInfo;
import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BasePresenterImpl;
import study.smart.baselib.mvp.contract.CommonSearchContract;
import study.smart.baselib.mvp.model.CommonSearchModel;
import study.smart.baselib.utils.Utils;

/**
 * Created by louis on 2017/3/4.
 */

public class CommonSearchPresenter extends BasePresenterImpl<CommonSearchContract.View> implements CommonSearchContract.Presenter {
    private CommonSearchModel commonSearchModel;

    public CommonSearchPresenter(CommonSearchContract.View view) {
        super(view);
        commonSearchModel = new CommonSearchModel();
    }


    @Override
    public void getTransferManagerList(String keyword, int page, final int request_state) {
        commonSearchModel.searTransferManagerList(keyword, page, new ObserverListener<DataListInfo>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(DataListInfo dataListInfo) {
                List<TransferManagerEntity> transferManagerEntities = JSONObject.parseArray(dataListInfo.getData(), TransferManagerEntity.class);
                if (transferManagerEntities != null) {
                    view.showTransferManagerList(transferManagerEntities, request_state);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void getMsgList(String keyword, int page, final int request_state) {
        commonSearchModel.searchMsgDetail(keyword, page, new ObserverListener<DataListInfo>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(DataListInfo dataListInfo) {
                List<MessageDetailItemInfo> messageDetailItemInfos = JSONObject.parseArray(dataListInfo.getData(), MessageDetailItemInfo.class);
                if (messageDetailItemInfos != null) {
                    view.showMsgList(messageDetailItemInfos, request_state);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void getAllStudentList(String keyword, int page, final int request_state) {
        commonSearchModel.getAllStudentList(keyword, page, new ObserverListener<DataListInfo>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(DataListInfo dataListInfo) {
                List<MyStudentInfo> myStudentInfos = JSONObject.parseArray(dataListInfo.getData(), MyStudentInfo.class);
                if (myStudentInfos != null) {
                    view.getAllStudentListSuccess(myStudentInfos, request_state);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void getMyStudentList(String keyword, int page, final int request_state) {
        commonSearchModel.getMyStudentList(keyword, page, new ObserverListener<DataListInfo>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(DataListInfo dataListInfo) {
                List<MyStudentInfo> myStudentInfos = JSONObject.parseArray(dataListInfo.getData(), MyStudentInfo.class);
                if (myStudentInfos != null) {
                    view.getAllStudentListSuccess(myStudentInfos, request_state);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void getTaskList(String keyword, int page, final int request_state) {
        commonSearchModel.getTaskList(keyword, page, new ObserverListener<DataListInfo>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(DataListInfo dataListInfo) {
                List<WorkingSearchInfo> workingSearchInfos = JSONObject.parseArray(dataListInfo.getData(), WorkingSearchInfo.class);
                if (workingSearchInfos != null) {
                    view.getTaskListSuccess(workingSearchInfos, request_state);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void getReportLit(String keyword, int page, final int request_state) {
        commonSearchModel.getReportList(keyword, page, new ObserverListener<DataListInfo>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(DataListInfo dataListInfo) {
                List<WorkingSearchInfo> workingSearchInfos = JSONObject.parseArray(dataListInfo.getData(), WorkingSearchInfo.class);
                if (workingSearchInfos != null) {
                    view.getReportListSuccess(workingSearchInfos, request_state);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void getTalkList(String keyword, int page, final int request_state) {
        commonSearchModel.getTalkList(keyword, page, new ObserverListener<DataListInfo>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(DataListInfo dataListInfo) {
                List<WorkingSearchInfo> workingSearchInfos = JSONObject.parseArray(dataListInfo.getData(), WorkingSearchInfo.class);
                if (workingSearchInfos != null) {
                    view.getTalkListSuccess(workingSearchInfos, request_state);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void getUnCompeleteStudent(String keyword, int page, final int request_state) {
        commonSearchModel.getUnCompeleteStudentList(keyword, page, new ObserverListener<DataListInfo>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(DataListInfo dataListInfo) {
                List<MyStudentInfo> myStudentInfos = JSONObject.parseArray(dataListInfo.getData(), MyStudentInfo.class);
                if (myStudentInfos != null) {
                    view.getAllStudentListSuccess(myStudentInfos, request_state);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void setEmptyView(LayoutInflater mInflater, final Context context, ViewGroup parent) {
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

    @Override
    public void detach() {
        super.detach();
        commonSearchModel = null;
    }
}
