package study.smart.baselib.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author yqy
 * @date on 2018/7/13
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MessageDetailItemInfo implements Serializable {
    private String id;
    private String userId;
    private String userType;
    private String type;
    private String targetId;
    private String status;
    private String createdAt;
    private String detailedType;
    private String content;
    private String relatedId;
    private String relatedUserType;
    private String relatedStaff;
    private String statusText;
    private String createdAtText;
    private DetailData data;
    private boolean isRead;

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public DetailData getData() {
        return data;
    }

    public void setData(DetailData data) {
        this.data = data;
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
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

    public String getDetailedType() {
        return detailedType;
    }

    public void setDetailedType(String detailedType) {
        this.detailedType = detailedType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(String relatedId) {
        this.relatedId = relatedId;
    }

    public String getRelatedUserType() {
        return relatedUserType;
    }

    public void setRelatedUserType(String relatedUserType) {
        this.relatedUserType = relatedUserType;
    }

    public String getRelatedStaff() {
        return relatedStaff;
    }

    public void setRelatedStaff(String relatedStaff) {
        this.relatedStaff = relatedStaff;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getCreatedAtText() {
        return createdAtText;
    }

    public void setCreatedAtText(String createdAtText) {
        this.createdAtText = createdAtText;
    }


    public class DetailData implements Serializable {
        private String name;
        private String startTime;
        private String endTime;
        private String type;
        private String typeText;
        private String messageId;
        private String id;
        private String phone;
        private String orderId;
        private String serviceProductNames;
        private String contractor;
        private String targetApplicationYearSeason;
        private String signedTime;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypeText() {
            return typeText;
        }

        public void setTypeText(String typeText) {
            this.typeText = typeText;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getServiceProductNames() {
            return serviceProductNames;
        }

        public void setServiceProductNames(String serviceProductNames) {
            this.serviceProductNames = serviceProductNames;
        }

        public String getContractor() {
            return contractor;
        }

        public void setContractor(String contractor) {
            this.contractor = contractor;
        }

        public String getTargetApplicationYearSeason() {
            return targetApplicationYearSeason;
        }

        public void setTargetApplicationYearSeason(String targetApplicationYearSeason) {
            this.targetApplicationYearSeason = targetApplicationYearSeason;
        }

        public String getSignedTime() {
            return signedTime;
        }

        public void setSignedTime(String signedTime) {
            this.signedTime = signedTime;
        }
    }
}
