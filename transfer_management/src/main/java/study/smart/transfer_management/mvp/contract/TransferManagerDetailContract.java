package study.smart.transfer_management.mvp.contract;

import android.content.Context;

import java.util.List;

import study.smart.baselib.entity.TransferManagerEntity;
import study.smart.baselib.mvp.base.BasePresenter;
import study.smart.baselib.mvp.base.BaseView;
import study.smart.transfer_management.entity.TransferDetailentity;

/**
 * @author yqy
 * @date on 2018/6/28
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface TransferManagerDetailContract {
    interface View extends BaseView {
        void closeTranseferCaseSuccess();

        void rejectTransferCaseSuccess();

        void reopenTransferCaseSuccess();

        void showAllocatedTransferDetail(TransferDetailentity transferDetailentity);

        void assignTransferCaseSuccess();

    }

    interface Presenter extends BasePresenter {
        /**
         * 未分配中心结案
         *
         * @param id
         */
        void closeTransferCase(String id);

        /**
         * 给已分配中心结案
         *
         * @param id
         */
        void closeTransferAllocatedCenter(String id);

        /**
         * 未分配中心驳回
         *
         * @param id
         * @param reason
         */
        void rejectTransferCase(String id, String reason);

        /**
         * 未分配中心案例重启
         *
         * @param id
         */
        void reopenTransferCase(String id);

        /**
         * 已分配中心重启¬
         *
         * @param id
         */
        void reopenAllocatedCenter(String id);

        /**
         * 获取未分配个人详情
         *
         * @param id
         */
        void getUnallocated(String id);

        /**
         * 获取已分配个人详情
         *
         * @param id
         */
        void getAllocated(String id);

        /**
         * 获取被驳回个人详情
         *
         * @param id
         */
        void getRejectedCenter(String id);

        /**
         * 获取未分配导师个人详情
         *
         * @param id
         */
        void getUnallocatedCoachl(String id);

        /**
         * 获取已分配导师个人详情
         *
         * @param id
         */
        void getAllocatedCoachl(String id);


        /**
         * 给案子分配中心
         *
         * @param id
         * @param centerId
         */
        void assignTransferCase(String id, String centerId);

        /**
         * 分配导师
         *
         * @param id
         * @param hardTeacherId
         * @param softTeacherId
         */
        void assigunTransferCaseTeacher(String id, String hardTeacherId, String softTeacherId);

        /**
         * 重新分配导师
         *
         * @param id
         * @param hardTeacherId
         * @param softTeacherId
         */
        void assigunTransferCaseTeacherAgain(String id, String hardTeacherId, String softTeacherId);

        /**
         * 从被驳回中心驳回到crm
         *
         * @param id
         * @param reason
         */
        void rejectRejectCenter(String id, String reason);

        /**
         * 被驳回转案结案
         *
         * @param id
         */
        void closeRejectCenter(String id);

        /**
         * 被驳回转案重启
         *
         * @param id
         */
        void reopenRejectCenter(String id);

        /**
         * 未分配老师驳回
         *
         * @param id
         */
        void rejectUnallocatedCoach(String id, String word);

        /**
         * 未分配导师结案
         *
         * @param id
         */
        void closeUnallocatedCoac(String id);

        /**
         * 未分配导师重启
         *
         * @param id
         */
        void reopenUnallocatedCoac(String id);


        /**
         * 已分配导师结案
         *
         * @param id
         */
        void closeAllocatedCoac(String id);

        /**
         * 已分配导师重启
         *
         * @param id
         */
        void reopenAllocatedCoac(String id);

    }
}
