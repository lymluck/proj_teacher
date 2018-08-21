package study.smart.baselib.mvp.contract;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import study.smart.baselib.entity.CityTeacherInfo;
import study.smart.baselib.entity.MessageDetailItemInfo;
import study.smart.baselib.entity.MyStudentInfo;
import study.smart.baselib.entity.Teacher;
import study.smart.baselib.entity.TeacherInfo;
import study.smart.baselib.entity.TransferManagerEntity;
import study.smart.baselib.entity.WorkingSearchInfo;
import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;

import java.util.List;

/**
 * Created by louis on 2017/3/4.
 */

public interface CommonSearchContract {

    interface View extends BaseView {

        void showTransferManagerList(List<TransferManagerEntity> transferManagerEntities, int request_state);

        void showMsgList(List<MessageDetailItemInfo> messageDetailItemInfos, int request_state);

        void getAllStudentListSuccess(List<MyStudentInfo> myStudentInfos, int request_state);

        void getTaskListSuccess(List<WorkingSearchInfo> workingSearchInfos, int request_state);

        void getReportListSuccess(List<WorkingSearchInfo> workingSearchInfos, int request_state);

        void getTalkListSuccess(List<WorkingSearchInfo> workingSearchInfos, int request_state);

        void getMessageDetailSuccess(TransferManagerEntity transferManagerEntity);

        void getTeacherListSuccess(List<Teacher> teacherInfos, int request_state);

        void showEmptyView(android.view.View view);

        void shareQuestionSuccess();

        void shareQuestionFail();

    }

    interface Presenter extends BasePresenter {

        void shareQuestion(String questionId, String receiverId, String note);

        void getTransferManagerList(String keyword, int page, int request_state);

        void getMsgList(String keyword, int page, int request_state);

        void getAllStudentList(String keyword, int page, int request_state);

        void getMyStudentList(String keyword, int page, int request_state);

        void getTaskList(String keyword, int page, int request_state);

        void getReportLit(String keyword, int page, int request_state);

        void getTalkList(String keyword, int page, int request_state);

        void getTeacheList(String keyword, int page, int request_state);

        void getMessageDetail(MessageDetailItemInfo messageDetailItemInfo);

        void getUnCompeleteStudent(String keyword, int page, int request_state);

        void setEmptyView(LayoutInflater mInflater, Context context, ViewGroup parent);

    }
}
