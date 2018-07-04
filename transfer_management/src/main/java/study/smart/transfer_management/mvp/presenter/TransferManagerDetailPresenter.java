package study.smart.transfer_management.mvp.presenter;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

import io.reactivex.disposables.Disposable;
import study.smart.baselib.entity.DataListInfo;
import study.smart.baselib.entity.TransferManagerEntity;
import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BasePresenterImpl;
import study.smart.transfer_management.entity.TransferDetailentity;
import study.smart.transfer_management.mvp.contract.TransferManagerDetailContract;
import study.smart.transfer_management.mvp.contract.TransferManagerListContract;
import study.smart.transfer_management.mvp.model.TransferManagerDetailModel;
import study.smart.transfer_management.mvp.model.TransferManagerListModel;

/**
 * @author yqy
 * @date on 2018/6/28
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TransferManagerDetailPresenter extends BasePresenterImpl<TransferManagerDetailContract.View> implements TransferManagerDetailContract.Presenter {

    private TransferManagerDetailModel transferManagerDetailModel;

    public TransferManagerDetailPresenter(TransferManagerDetailContract.View view) {
        super(view);
        transferManagerDetailModel = new TransferManagerDetailModel();
    }


    @Override
    public void detach() {
        super.detach();
        transferManagerDetailModel = null;
    }

    @Override
    public void closeTransferCase(String id) {
        transferManagerDetailModel.closeTransferCase(id, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                view.closeTranseferCaseSuccess();
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });

    }

    @Override
    public void closeTransferAllocatedCenter(String id) {
        transferManagerDetailModel.closeAllocatedTransferCase(id, new ObserverListener() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(Object result) {
                view.closeTranseferCaseSuccess();
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void rejectTransferCase(String id, String reason) {
        transferManagerDetailModel.rejectTransferManagerCase(id, reason, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                view.rejectTransferCaseSuccess();
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void reopenTransferCase(String id) {
        transferManagerDetailModel.reopenTransferManagerCase(id, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                view.reopenTransferCaseSuccess();
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void reopenAllocatedCenter(String id) {
        transferManagerDetailModel.reopenAllocatedCenter(id, new ObserverListener() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(Object result) {
                view.reopenTransferCaseSuccess();
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void getUnallocated(String id) {
        transferManagerDetailModel.getUnallocated(id, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                TransferDetailentity transferDetailentity = JSONObject.parseObject(result, TransferDetailentity.class);
                if (transferDetailentity != null) {
                    view.showAllocatedTransferDetail(transferDetailentity);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void getAllocated(String id) {
        transferManagerDetailModel.getAllocated(id, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                TransferDetailentity transferDetailentity = JSONObject.parseObject(result, TransferDetailentity.class);
                if (transferDetailentity != null) {
                    view.showAllocatedTransferDetail(transferDetailentity);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void getRejectedCenter(String id) {
        transferManagerDetailModel.getRejectedCenter(id, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                TransferDetailentity transferDetailentity = JSONObject.parseObject(result, TransferDetailentity.class);
                if (transferDetailentity != null) {
                    view.showAllocatedTransferDetail(transferDetailentity);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void getUnallocatedCoachl(String id) {
        transferManagerDetailModel.getUnallocatedCoachl(id, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                TransferDetailentity transferDetailentity = JSONObject.parseObject(result, TransferDetailentity.class);
                if (transferDetailentity != null) {
                    view.showAllocatedTransferDetail(transferDetailentity);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void getAllocatedCoachl(String id) {
        transferManagerDetailModel.getAllocatedCoachl(id, new ObserverListener<String>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(String result) {
                TransferDetailentity transferDetailentity = JSONObject.parseObject(result, TransferDetailentity.class);
                if (transferDetailentity != null) {
                    view.showAllocatedTransferDetail(transferDetailentity);
                }
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void assignTransferCase(String id, String centerId) {
        transferManagerDetailModel.assignTransferCase(id, centerId, new ObserverListener() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(Object result) {
                view.assignTransferCaseSuccess();
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void assigunTransferCaseTeacher(String id, String hardTeacherId, String softTeacherId) {
        transferManagerDetailModel.assignTransferCaseTeacher(id, hardTeacherId, softTeacherId, new ObserverListener() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(Object result) {
                view.assignTransferCaseSuccess();
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void assigunTransferCaseTeacherAgain(String id, String hardTeacherId, String softTeacherId) {
        transferManagerDetailModel.assignTransferCaseTeacherAgain(id, hardTeacherId, softTeacherId, new ObserverListener() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(Object result) {
                view.assignTransferCaseSuccess();
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void rejectRejectCenter(String id, String reason) {
        transferManagerDetailModel.rejectRejectCenter(id, reason, new ObserverListener() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(Object result) {
                view.rejectTransferCaseSuccess();
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void closeRejectCenter(String id) {
        transferManagerDetailModel.closeRejectCenter(id, new ObserverListener() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(Object result) {
                view.closeTranseferCaseSuccess();
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void reopenRejectCenter(String id) {
        transferManagerDetailModel.reopenRejectCenter(id, new ObserverListener() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(Object result) {
                view.reopenTransferCaseSuccess();
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void rejectUnallocatedCoach(String id, String reason) {
        transferManagerDetailModel.rejectUnallocatedCoach(id, reason, new ObserverListener() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(Object result) {
                view.rejectTransferCaseSuccess();
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void closeUnallocatedCoac(String id) {
        transferManagerDetailModel.closeUnallocatedCoach(id, new ObserverListener() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(Object result) {
                view.closeTranseferCaseSuccess();
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void reopenUnallocatedCoac(String id) {
        transferManagerDetailModel.reopenUnallocatedCoach(id, new ObserverListener() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(Object result) {
                view.reopenTransferCaseSuccess();
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void closeAllocatedCoac(String id) {
        transferManagerDetailModel.closeAllocatedCoach(id, new ObserverListener() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(Object result) {
                view.closeTranseferCaseSuccess();
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }

    @Override
    public void reopenAllocatedCoac(String id) {
        transferManagerDetailModel.reopenAllocatedCoach(id, new ObserverListener() {
            @Override
            public void onSubscribe(Disposable disposable) {
                addDisposable(disposable);
            }

            @Override
            public void onNext(Object result) {
                view.reopenTransferCaseSuccess();
            }

            @Override
            public void onError(String msg) {
                view.showTip(msg);
            }
        });
    }
}