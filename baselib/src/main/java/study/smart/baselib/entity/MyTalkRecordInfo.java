package study.smart.baselib.entity;

import java.io.Serializable;
import java.util.List;


/**
 * @author yqy
 * @date on 2018/7/24
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyTalkRecordInfo implements Serializable {
    private String year;
    private List<ShowMonth> months;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<ShowMonth> getMonths() {
        return months;
    }

    public void setMonths(List<ShowMonth> months) {
        this.months = months;
    }

    public class ShowMonth implements Serializable {
        private String key;
        private String year;
        private List<DataListInfo> list;

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<DataListInfo> getList() {
            return list;
        }

        public void setList(List<DataListInfo> list) {
            this.list = list;
        }

        public class DataListInfo implements Serializable {
            private String id;
            private String userId;
            private String creatorId;
            private String editorId;
            private String status;
            private String createdAt;
            private String updatedAt;
            private String centerName;
            private String name;
            private String type;
            private String userType;
            private String method;
            private String time;
            private String reason;
            private boolean scheduled;
            private String userName;
            private String userPhone;
            private String advisor;
            private String typeText;
            private String methodText;
            private String userTypeText;
            private String timeText;
            private String createdAtText;
            private String updatedAtText;
            private String statusText;
            private boolean isPublish;
            private List<CommunicationList> communicationList;

            public String getCenterName() {
                return centerName;
            }

            public void setCenterName(String centerName) {
                this.centerName = centerName;
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

            public String getCreatorId() {
                return creatorId;
            }

            public void setCreatorId(String creatorId) {
                this.creatorId = creatorId;
            }

            public String getEditorId() {
                return editorId;
            }

            public void setEditorId(String editorId) {
                this.editorId = editorId;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getUserType() {
                return userType;
            }

            public void setUserType(String userType) {
                this.userType = userType;
            }

            public String getMethod() {
                return method;
            }

            public void setMethod(String method) {
                this.method = method;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getReason() {
                return reason;
            }

            public void setReason(String reason) {
                this.reason = reason;
            }


            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getUserPhone() {
                return userPhone;
            }

            public void setUserPhone(String userPhone) {
                this.userPhone = userPhone;
            }

            public String getAdvisor() {
                return advisor;
            }

            public void setAdvisor(String advisor) {
                this.advisor = advisor;
            }

            public String getTypeText() {
                return typeText;
            }

            public void setTypeText(String typeText) {
                this.typeText = typeText;
            }

            public String getMethodText() {
                return methodText;
            }

            public void setMethodText(String methodText) {
                this.methodText = methodText;
            }

            public String getUserTypeText() {
                return userTypeText;
            }

            public void setUserTypeText(String userTypeText) {
                this.userTypeText = userTypeText;
            }

            public String getTimeText() {
                return timeText;
            }

            public void setTimeText(String timeText) {
                this.timeText = timeText;
            }

            public String getCreatedAtText() {
                return createdAtText;
            }

            public void setCreatedAtText(String createdAtText) {
                this.createdAtText = createdAtText;
            }

            public String getUpdatedAtText() {
                return updatedAtText;
            }

            public void setUpdatedAtText(String updatedAtText) {
                this.updatedAtText = updatedAtText;
            }

            public String getStatusText() {
                return statusText;
            }

            public void setStatusText(String statusText) {
                this.statusText = statusText;
            }

            public boolean isScheduled() {
                return scheduled;
            }

            public void setScheduled(boolean scheduled) {
                this.scheduled = scheduled;
            }

            public boolean isPublish() {
                return isPublish;
            }

            public void setPublish(boolean publish) {
                isPublish = publish;
            }

            public List<CommunicationList> getCommunicationList() {
                return communicationList;
            }

            public void setCommunicationList(List<CommunicationList> communicationList) {
                this.communicationList = communicationList;
            }
        }
    }
}
