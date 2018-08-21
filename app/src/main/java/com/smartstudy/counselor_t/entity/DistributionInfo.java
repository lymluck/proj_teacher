package com.smartstudy.counselor_t.entity;

import java.io.Serializable;

/**
 * @author yqy
 * @date on 2018/8/16
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class DistributionInfo implements Serializable {
    private String id;
    private String questionId;
    private String senderId;
    private String receiverId;
    private String note;
    private String read;
    private boolean received;
    private String createTime;
    private String createTimeText;

    private Receiver receiver;

    private Sender sender;

    private QuestionInfo question;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeText() {
        return createTimeText;
    }

    public void setCreateTimeText(String createTimeText) {
        this.createTimeText = createTimeText;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }


    public QuestionInfo getQuestion() {
        return question;
    }

    public void setQuestion(QuestionInfo question) {
        this.question = question;
    }

    public class Sender implements Serializable {
        private String id;
        private String name;
        private String realName;
        private String avatar;
        private String workingCityKey;
        private String orgId;
        private String group;

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getWorkingCityKey() {
            return workingCityKey;
        }

        public void setWorkingCityKey(String workingCityKey) {
            this.workingCityKey = workingCityKey;
        }

        public String getOrgId() {
            return orgId;
        }

        public void setOrgId(String orgId) {
            this.orgId = orgId;
        }
    }

    public class Receiver implements Serializable {
        private String id;
        private String name;
        private String realName;
        private String avatar;
        private String workingCityKey;
        private String orgId;
        private String group;

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getWorkingCityKey() {
            return workingCityKey;
        }

        public void setWorkingCityKey(String workingCityKey) {
            this.workingCityKey = workingCityKey;
        }

        public String getOrgId() {
            return orgId;
        }

        public void setOrgId(String orgId) {
            this.orgId = orgId;
        }
    }


}
