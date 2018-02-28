package com.smartstudy.counselor_t.entity;

import java.util.List;

/**
 * Created by yqy on 2017/12/4.
 */

public class QaDetailInfo {
    private String id;

    private String question;

    private String askTime;

    private String answer;


    private Asker asker;

    private int likedCount;

    private Answerer answerer;

    private boolean liked;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(int likedCount) {
        this.likedCount = likedCount;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    private List<TagsData> tagsData;

    private List<QuestionsAfter> questionsAfter;


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAskTime() {
        return askTime;
    }

    public void setAskTime(String askTime) {
        this.askTime = askTime;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Asker getAsker() {
        return asker;
    }

    public void setAsker(Asker asker) {
        this.asker = asker;
    }

    public Answerer getAnswerer() {
        return answerer;
    }

    public void setAnswerer(Answerer answerer) {
        this.answerer = answerer;
    }

    public List<TagsData> getTagsData() {
        return tagsData;
    }

    public void setTagsData(List<TagsData> tagsData) {
        this.tagsData = tagsData;
    }

    public List<QuestionsAfter> getQuestionsAfter() {
        return questionsAfter;
    }

    public void setQuestionsAfter(List<QuestionsAfter> questionsAfter) {
        this.questionsAfter = questionsAfter;
    }



    public class TagsData {
        private String id;

        private String name;

        private String type;


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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }


    public class QuestionsAfter {
        private String id;

        private String score;

        private String question;

        private String answer;


        private String askTime;

        private boolean answererIsUser;

        private boolean answered;

        private Asker asker;

        private int likedCount;

        private boolean liked;

        private Answerer answerer;

        public int getLikedCount() {
            return likedCount;
        }

        public void setLikedCount(int likedCount) {
            this.likedCount = likedCount;
        }

        public boolean isLiked() {
            return liked;
        }

        public void setLiked(boolean liked) {
            this.liked = liked;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getAskTime() {
            return askTime;
        }

        public void setAskTime(String askTime) {
            this.askTime = askTime;
        }

        public boolean isAnswererIsUser() {
            return answererIsUser;
        }

        public void setAnswererIsUser(boolean answererIsUser) {
            this.answererIsUser = answererIsUser;
        }

        public boolean isAnswered() {
            return answered;
        }

        public void setAnswered(boolean answered) {
            this.answered = answered;
        }

        public Asker getAsker() {
            return asker;
        }

        public void setAsker(Asker asker) {
            this.asker = asker;
        }

        public Answerer getAnswerer() {
            return answerer;
        }

        public void setAnswerer(Answerer answerer) {
            this.answerer = answerer;
        }
    }


}
