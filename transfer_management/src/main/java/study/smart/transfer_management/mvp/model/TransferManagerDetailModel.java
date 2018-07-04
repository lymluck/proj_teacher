package study.smart.transfer_management.mvp.model;

import java.util.HashMap;
import java.util.Map;

import study.smart.baselib.listener.ObserverListener;
import study.smart.baselib.mvp.base.BaseModel;
import study.smart.baselib.server.api.ApiManager;
import study.smart.baselib.utils.HttpUrlUtils;

/**
 * @author yqy
 * @date on 2018/6/28
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TransferManagerDetailModel extends BaseModel {
    /**
     * 未分配中心结案
     *
     * @param id
     * @param listener
     */
    public void closeTransferCase(String id, ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().closeTransferCase(getHeadersMap(), String.format(HttpUrlUtils.URL_CLOSE_TRANSFER_CASE, id)), listener);
    }

    /**
     * 已分配中心结案
     *
     * @param id
     * @param listener
     */
    public void closeAllocatedTransferCase(String id, ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().closeAllocateTransferCase(getHeadersMap(), String.format(HttpUrlUtils.URL_CLOSE_ALLOCATED_CENTER, id)), listener);
    }

    /**
     * 未分配中心驳回
     */
    public void rejectTransferManagerCase(String id, String reason, ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("rejectReason", reason);
        apiSubscribe(ApiManager.getApiService().rejectTransferManagerCase(getHeadersMap(), String.format(HttpUrlUtils.URL_REJECT_TRANSFER_CASE, id), params), listener);
    }

    /**
     * 未分配中心转案重启
     *
     * @param id
     * @param listener
     */
    public void reopenTransferManagerCase(String id, ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().reopenTransferCase(getHeadersMap(), String.format(HttpUrlUtils.URL_REOPEN_TRANSFER_CASE, id)), listener);
    }


    /**
     * 获取未分配个人详情
     *
     * @param id
     * @param listener
     */
    public void getUnallocated(String id, ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getUnallocated(getHeadersMap(), String.format(HttpUrlUtils.URL_UNALLOCATED_TRANSFER_CASE, id)), listener);
    }

    /**
     * 获取已分配个人详情
     *
     * @param id
     * @param listener
     */
    public void getAllocated(String id, ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getAllocated(getHeadersMap(), String.format(HttpUrlUtils.URL_ALLOCATED_TRANSFER_CASE, id)), listener);
    }

    /**
     * 获取被驳回个人详情
     *
     * @param id
     * @param listener
     */
    public void getRejectedCenter(String id, ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getRejectedCenter(getHeadersMap(), String.format(HttpUrlUtils.URL_REJECTED_CENTER_DETAIL, id)), listener);
    }

    /**
     * 获取未分配导师个人详情
     *
     * @param id
     * @param listener
     */
    public void getUnallocatedCoachl(String id, ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getUnallocatedCoachl(getHeadersMap(), String.format(HttpUrlUtils.URL_UNALLOCATED_COACH_DETAIL, id)), listener);
    }


    /**
     * 获取已分配导师个人详情
     *
     * @param id
     * @param listener
     */
    public void getAllocatedCoachl(String id, ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().getAllocatedCoachl(getHeadersMap(), String.format(HttpUrlUtils.URL_ALLOCATED_COACH_DETAIL, id)), listener);
    }


    /**
     * 获取未分配个人详情
     *
     * @param id
     * @param listener
     */
    public void assignTransferCase(String id, String centerId, ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("centerId", centerId);
        apiSubscribe(ApiManager.getApiService().assignTransferCase(getHeadersMap(), String.format(HttpUrlUtils.URL_ASSIGN_TRANSFER_CASE, id), params), listener);
    }


    /**
     * 给案子分配导师
     *
     * @param id
     * @param listener
     */
    public void assignTransferCaseTeacher(String id, String hardTeacherId, String softTeacherId, ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("hardTeacherId", hardTeacherId);
        params.put("softTeacherId", softTeacherId);
        apiSubscribe(ApiManager.getApiService().assignTransferCaseTeacher(getHeadersMap(), String.format(HttpUrlUtils.URL_ASSIGN_UNALLOCATED_COACH, id), params), listener);
    }


    /**
     * 给案子分配导师
     *
     * @param id
     * @param listener
     */
    public void assignTransferCaseTeacherAgain(String id, String hardTeacherId, String softTeacherId, ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("hardTeacherId", hardTeacherId);
        params.put("softTeacherId", softTeacherId);
        apiSubscribe(ApiManager.getApiService().assignTransferCaseTeacherAgain(getHeadersMap(), String.format(HttpUrlUtils.URL_ASSIGN_ALLOCATED_COACH, id), params), listener);
    }

    /**
     * 分配中心 重启
     *
     * @param id
     * @param listener
     */
    public void reopenAllocatedCenter(String id, ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().reopenAllocatedCenter(getHeadersMap(), String.format(HttpUrlUtils.URL_REOPEN_ALLOCATED_CENTER, id)), listener);
    }


    /**
     * 被驳回
     *
     * @param id
     * @param listener
     */
    public void rejectRejectCenter(String id, String reason, ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("rejectReason", reason);
        apiSubscribe(ApiManager.getApiService().rejectRejectCenter(getHeadersMap(), String.format(HttpUrlUtils.URL_REJECT_REJECTED_CENTER, id), params), listener);
    }


    /**
     * 被驳回转案结案
     *
     * @param id
     * @param listener
     */
    public void closeRejectCenter(String id, ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().closeRejectCenter(getHeadersMap(), String.format(HttpUrlUtils.URL_CLOSE_REJECTED_CENTER, id)), listener);
    }

    /**
     * 被驳回转案结案
     *
     * @param id
     * @param listener
     */
    public void reopenRejectCenter(String id, ObserverListener listener) {

        apiSubscribe(ApiManager.getApiService().reopenRejectCenter(getHeadersMap(), String.format(HttpUrlUtils.URL_REOPEN_REJECTED_CENTER, id)), listener);
    }

    /**
     * 未分配导师驳回
     *
     * @param id
     * @param listener
     */
    public void rejectUnallocatedCoach(String id, String reason, ObserverListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("rejectReason", reason);
        apiSubscribe(ApiManager.getApiService().rejectUnallocatedCoach(getHeadersMap(), String.format(HttpUrlUtils.URL_REJECT_UNALLOCATED_COACH, id), params), listener);
    }


    /**
     * 未分配导师结案
     *
     * @param id
     * @param listener
     */
    public void closeUnallocatedCoach(String id, ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().closeUnallocatedCoach(getHeadersMap(), String.format(HttpUrlUtils.URL_CLOSE_UNALLOCATED_COACH, id)), listener);
    }

    /**
     * 未分配导师重启
     *
     * @param id
     * @param listener
     */
    public void reopenUnallocatedCoach(String id, ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().reopenUnallocatedCoach(getHeadersMap(), String.format(HttpUrlUtils.URL_REOPEN_UNALLOCATED_COACH, id)), listener);
    }

    /**
     * 已分配导师结案
     *
     * @param id
     * @param listener
     */
    public void closeAllocatedCoach(String id, ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().closeAllocatedCoach(getHeadersMap(), String.format(HttpUrlUtils.URL_CLOSE_ALLOCATED_COACH, id)), listener);
    }

    /**
     * 已分配导师重启
     *
     * @param id
     * @param listener
     */
    public void reopenAllocatedCoach(String id, ObserverListener listener) {
        apiSubscribe(ApiManager.getApiService().reopenAllocatedCoach(getHeadersMap(), String.format(HttpUrlUtils.URL_REOPEN_ALLOCATED_COACH, id)), listener);
    }
}
