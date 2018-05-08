package com.smartstudy.counselor_t.entity;

/**
 * Created by louis on 17/4/10.
 */

public class QuestionInfo {
    private int id;
    private int userId;
    private String content;
    private boolean isClassic;
    private int answerCount;
    private String visitCount;
    private String createTime;
    private boolean answered;
    private String likedCount;
    private String collectedCount;
    private int subQuestionCount;
    private Asker asker;
    private String createTimeText;
    private String platform;
    private String userLocation;
    private String schoolName;
    private boolean hasUnreadAnswersOfMe;


    public boolean isHasUnreadAnswersOfMe() {
        return hasUnreadAnswersOfMe;
    }

    public void setHasUnreadAnswersOfMe(boolean hasUnreadAnswersOfMe) {
        this.hasUnreadAnswersOfMe = hasUnreadAnswersOfMe;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public static class Asker {
        private String id;
        private String name;
        private String avatar;
        private boolean canContact;

        public boolean isCanContact() {
            return canContact;
        }

        public void setCanContact(boolean canContact) {
            this.canContact = canContact;
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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

    }

    public String getCreateTimeText() {
        return createTimeText;
    }

    public void setCreateTimeText(String createTimeText) {
        this.createTimeText = createTimeText;
    }

    public Asker getAsker() {
        return asker;
    }

    public void setAsker(Asker asker) {
        this.asker = asker;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isClassic() {
        return isClassic;
    }

    public void setClassic(boolean classic) {
        isClassic = classic;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public String getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(String visitCount) {
        this.visitCount = visitCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public String getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(String likedCount) {
        this.likedCount = likedCount;
    }

    public String getCollectedCount() {
        return collectedCount;
    }

    public void setCollectedCount(String collectedCount) {
        this.collectedCount = collectedCount;
    }

    public int getSubQuestionCount() {
        return subQuestionCount;
    }

    public void setSubQuestionCount(int subQuestionCount) {
        this.subQuestionCount = subQuestionCount;
    }
}
