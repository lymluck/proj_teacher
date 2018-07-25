package study.smart.transfer_management.entity;

import java.io.Serializable;

/**
 * @author yqy
 * @date on 2018/7/17
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyStudentInfo implements Serializable {
    private String id;
    private String userId;
    private String name;
    private String phone;
    private String serviceProductNames;
    private String createdAt;
    private String orderId;
    private String contractor;
    private String signedTime;
    private String targetDegree;
    private String preClosedStatus;
    private String targetApplicationYearSeason;
    private String status;
    private String targetDegreeName;
    private String statusName;
    private boolean isClose;
    private String time;
    private String centerName;
    private String softTeacherName;
    private String softTeacherStatus;
    private String hardTeacherName;
    private String hardTeacherStatus;
    private String signedTimeText;
    private String userName;
    private String publishTime;
    private String typeTEXT;
    private String completedCommunicationReportCount;

    public String getCompletedCommunicationReportCount() {
        return completedCommunicationReportCount;
    }

    public void setCompletedCommunicationReportCount(String completedCommunicationReportCount) {
        this.completedCommunicationReportCount = completedCommunicationReportCount;
    }

    public String getTypeTEXT() {
        return typeTEXT;
    }

    public void setTypeTEXT(String typeTEXT) {
        this.typeTEXT = typeTEXT;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getServiceProductNames() {
        return serviceProductNames;
    }

    public void setServiceProductNames(String serviceProductNames) {
        this.serviceProductNames = serviceProductNames;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }

    public String getSignedTime() {
        return signedTime;
    }

    public void setSignedTime(String signedTime) {
        this.signedTime = signedTime;
    }

    public String getTargetDegree() {
        return targetDegree;
    }

    public void setTargetDegree(String targetDegree) {
        this.targetDegree = targetDegree;
    }

    public String getPreClosedStatus() {
        return preClosedStatus;
    }

    public void setPreClosedStatus(String preClosedStatus) {
        this.preClosedStatus = preClosedStatus;
    }

    public String getTargetApplicationYearSeason() {
        return targetApplicationYearSeason;
    }

    public void setTargetApplicationYearSeason(String targetApplicationYearSeason) {
        this.targetApplicationYearSeason = targetApplicationYearSeason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTargetDegreeName() {
        return targetDegreeName;
    }

    public void setTargetDegreeName(String targetDegreeName) {
        this.targetDegreeName = targetDegreeName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean close) {
        isClose = close;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getSoftTeacherName() {
        return softTeacherName;
    }

    public void setSoftTeacherName(String softTeacherName) {
        this.softTeacherName = softTeacherName;
    }

    public String getSoftTeacherStatus() {
        return softTeacherStatus;
    }

    public void setSoftTeacherStatus(String softTeacherStatus) {
        this.softTeacherStatus = softTeacherStatus;
    }

    public String getHardTeacherName() {
        return hardTeacherName;
    }

    public void setHardTeacherName(String hardTeacherName) {
        this.hardTeacherName = hardTeacherName;
    }

    public String getHardTeacherStatus() {
        return hardTeacherStatus;
    }

    public void setHardTeacherStatus(String hardTeacherStatus) {
        this.hardTeacherStatus = hardTeacherStatus;
    }

    public String getSignedTimeText() {
        return signedTimeText;
    }

    public void setSignedTimeText(String signedTimeText) {
        this.signedTimeText = signedTimeText;
    }
}
