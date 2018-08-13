package study.smart.baselib.entity;

/**
 * @author yqy
 * @date on 2018/6/27
 * @describe TODOT
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TransferManagerEntity {
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
    private String targetApplicationYearSeason;
    private String targetDegreeName;
    private String statusName;
    private String isClose;
    private String preClosedStatus;

    public String getPreClosedStatus() {
        return preClosedStatus;
    }

    public void setPreClosedStatus(String preClosedStatus) {
        this.preClosedStatus = preClosedStatus;
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

    public String getTargetApplicationYearSeason() {
        return targetApplicationYearSeason;
    }

    public void setTargetApplicationYearSeason(String targetApplicationYearSeason) {
        this.targetApplicationYearSeason = targetApplicationYearSeason;
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

    public String getIsClose() {
        return isClose;
    }

    public void setIsClose(String isClose) {
        this.isClose = isClose;
    }
}
