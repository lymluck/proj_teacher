package study.smart.baselib.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author yqy
 * @date on 2018/7/30
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class WorkingSearchInfo implements Serializable {
    private String name;
    private String id;
    private String userId;
    private String targetApplicationYearSeason;
    private String targetDegreeName;
    private String typeText;
    private String centerName;
    private String userName;
    private String endTime;
    private String status;
    private String publishTime;
    private String typeTEXT;
    private String time;
    private String userTypeText;
    private String methodText;
    private String reason;
    private List<CommunicationList> communicationList;

    public List<CommunicationList> getCommunicationList() {
        return communicationList;
    }

    public void setCommunicationList(List<CommunicationList> communicationList) {
        this.communicationList = communicationList;
    }

    public String getMethodText() {
        return methodText;
    }

    public void setMethodText(String methodText) {
        this.methodText = methodText;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUserTypeText() {
        return userTypeText;
    }

    public void setUserTypeText(String userTypeText) {
        this.userTypeText = userTypeText;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTypeText() {
        return typeText;
    }

    public void setTypeText(String typeText) {
        this.typeText = typeText;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
