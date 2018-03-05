package com.smartstudy.counselor_t.entity;

import android.net.Uri;

import java.util.List;

/**
 * Created by yqy on 2017/12/4.
 */

public class Answerer {
    private String id;

    private int commenterId;

    private String commenterRole;

    private String commentType;

    private String atCommentId;

    private String createTime;

    private String content;

    private Uri voiceUrl;

    private String questionId;

    private Commenter commenter;

    private int likedCount;

    private int collectedCount;

    private List<Comments> comments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCommenterId() {
        return commenterId;
    }

    public void setCommenterId(int commenterId) {
        this.commenterId = commenterId;
    }

    public String getCommenterRole() {
        return commenterRole;
    }

    public void setCommenterRole(String commenterRole) {
        this.commenterRole = commenterRole;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public String getAtCommentId() {
        return atCommentId;
    }

    public void setAtCommentId(String atCommentId) {
        this.atCommentId = atCommentId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Uri getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(Uri voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public Commenter getCommenter() {
        return commenter;
    }

    public void setCommenter(Commenter commenter) {
        this.commenter = commenter;
    }

    public int getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(int likedCount) {
        this.likedCount = likedCount;
    }

    public int getCollectedCount() {
        return collectedCount;
    }

    public void setCollectedCount(int collectedCount) {
        this.collectedCount = collectedCount;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public static class Comments {
        private String id;
        private String commenterId;
        private String commenterRole;
        private String commentType;
        private String atCommentId;
        private String createTime;
        private String content;
        private Uri voiceUrl;
        private String questionId;
        private int likedCount;
        private int collectedCount;
        private Commenter commenter;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCommenterId() {
            return commenterId;
        }

        public void setCommenterId(String commenterId) {
            this.commenterId = commenterId;
        }

        public String getCommenterRole() {
            return commenterRole;
        }

        public void setCommenterRole(String commenterRole) {
            this.commenterRole = commenterRole;
        }

        public String getCommentType() {
            return commentType;
        }

        public void setCommentType(String commentType) {
            this.commentType = commentType;
        }

        public String getAtCommentId() {
            return atCommentId;
        }

        public void setAtCommentId(String atCommentId) {
            this.atCommentId = atCommentId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Uri getVoiceUrl() {
            return voiceUrl;
        }

        public void setVoiceUrl(Uri voiceUrl) {
            this.voiceUrl = voiceUrl;
        }

        public String getQuestionId() {
            return questionId;
        }

        public void setQuestionId(String questionId) {
            this.questionId = questionId;
        }

        public int getLikedCount() {
            return likedCount;
        }

        public void setLikedCount(int likedCount) {
            this.likedCount = likedCount;
        }

        public int getCollectedCount() {
            return collectedCount;
        }

        public void setCollectedCount(int collectedCount) {
            this.collectedCount = collectedCount;
        }

        public Commenter getCommenter() {
            return commenter;
        }

        public void setCommenter(Commenter commenter) {
            this.commenter = commenter;
        }
    }

    public static class Commenter extends Asker {

    }

}
