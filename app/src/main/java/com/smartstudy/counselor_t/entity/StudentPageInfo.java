package com.smartstudy.counselor_t.entity;

/**
 * @author yqy
 * @date on 2018/1/12
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class StudentPageInfo {
    private String id;
    private String zhikeId;
    private String name;
    private String avatar;
    private String genderId;
    private String contact;
    private String location;
    private String email;
    private String currentGrade;
    private String imUserId;
    private String likedCount;
    private boolean liked;
    private boolean followed;
    private TargetSection targetSection;
    private BackgroundSection backgroundSection;
    private WatchSchools watchSchools;

    public class TargetSection {
        private AdmissionTime admissionTime;

        private TargetCountry targetCountry;

        private TargetDegree targetDegree;

        private TargetMajorDirection targetMajorDirection;

        private TargetSchoolRank targetSchoolRank;

        public class AdmissionTime extends CommonTarge {


        }

        public class TargetCountry extends CommonTarge {
        }


        public class TargetDegree extends CommonTarge {

        }

        public class TargetMajorDirection extends CommonTarge {

        }

        public class TargetSchoolRank extends CommonTarge {
        }

        public AdmissionTime getAdmissionTime() {
            return admissionTime;
        }

        public void setAdmissionTime(AdmissionTime admissionTime) {
            this.admissionTime = admissionTime;
        }

        public TargetCountry getTargetCountry() {
            return targetCountry;
        }

        public void setTargetCountry(TargetCountry targetCountry) {
            this.targetCountry = targetCountry;
        }

        public TargetSchoolRank getTargetSchoolRank() {
            return targetSchoolRank;
        }

        public void setTargetSchoolRank(TargetSchoolRank targetSchoolRank) {
            this.targetSchoolRank = targetSchoolRank;
        }

        public TargetDegree getTargetDegree() {
            return targetDegree;
        }

        public void setTargetDegree(TargetDegree targetDegree) {
            this.targetDegree = targetDegree;
        }

        public TargetMajorDirection getTargetMajorDirection() {
            return targetMajorDirection;
        }

        public void setTargetMajorDirection(TargetMajorDirection targetMajorDirection) {
            this.targetMajorDirection = targetMajorDirection;
        }
    }


    public class BackgroundSection {
        private CurrentSchool currentSchool;


        private Score score;

        private ScoreLanguage scoreLanguage;


        private ActivityInternship activityInternship;

        private ActivityResearch activityResearch;

        private ActivityCommunity activityCommunity;

        private ActivityExchange activityExchange;

        private ScoreStandardEntity scoreStandard;

        private ActivitySocialEntity activitySocial;

        public ActivityExchange getActivityExchange() {
            return activityExchange;
        }

        public void setActivityExchange(ActivityExchange activityExchange) {
            this.activityExchange = activityExchange;
        }

        public ActivityCommunity getActivityCommunity() {
            return activityCommunity;
        }

        public void setActivityCommunity(ActivityCommunity activityCommunity) {
            this.activityCommunity = activityCommunity;
        }

        public ActivityResearch getActivityResearch() {
            return activityResearch;
        }

        public void setActivityResearch(ActivityResearch activityResearch) {
            this.activityResearch = activityResearch;
        }


        public ActivityInternship getActivityInternship() {
            return activityInternship;
        }

        public void setActivityInternship(ActivityInternship activityInternship) {
            this.activityInternship = activityInternship;
        }

        public ScoreLanguage getScoreLanguage() {
            return scoreLanguage;
        }

        public void setScoreLanguage(ScoreLanguage scoreLanguage) {
            this.scoreLanguage = scoreLanguage;
        }


        public Score getScore() {
            return score;
        }

        public void setScore(Score score) {
            this.score = score;
        }

        public CurrentSchool getCurrentSchool() {
            return currentSchool;
        }

        public void setCurrentSchool(CurrentSchool currentSchool) {
            this.currentSchool = currentSchool;
        }

        public class CurrentSchool extends CommonStudentInfo {

            private String input;

            public String getInput() {
                return input;
            }

            public void setInput(String input) {
                this.input = input;
            }
        }


        public class Score extends CommonTarge {

        }


        public class ScoreLanguage extends CommonStudentInfo {

        }


        public class ScoreStandardEntity extends CommonTarge {
        }


        public class ActivitySocialEntity extends CommonTarge {
        }

        public ActivitySocialEntity getActivitySocial() {
            return activitySocial;
        }

        public void setActivitySocial(ActivitySocialEntity activitySocial) {
            this.activitySocial = activitySocial;
        }

        public ScoreStandardEntity getScoreStandard() {
            return scoreStandard;
        }

        public void setScoreStandard(ScoreStandardEntity scoreStandard) {
            this.scoreStandard = scoreStandard;
        }

        public class ActivityInternship extends CommonTarge {

        }

        public class ActivityResearch extends CommonTarge {

        }

        public class ActivityCommunity extends CommonTarge {

        }

        public class ActivityExchange extends CommonTarge {

        }

    }

    public class WatchSchools {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZhikeId() {
        return zhikeId;
    }

    public void setZhikeId(String zhikeId) {
        this.zhikeId = zhikeId;
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

    public String getGenderId() {
        return genderId;
    }

    public void setGenderId(String genderId) {
        this.genderId = genderId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrentGrade() {
        return currentGrade;
    }

    public void setCurrentGrade(String currentGrade) {
        this.currentGrade = currentGrade;
    }

    public String getImUserId() {
        return imUserId;
    }

    public void setImUserId(String imUserId) {
        this.imUserId = imUserId;
    }

    public String getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(String likedCount) {
        this.likedCount = likedCount;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public TargetSection getTargetSection() {
        return targetSection;
    }

    public void setTargetSection(TargetSection targetSection) {
        this.targetSection = targetSection;
    }

    public BackgroundSection getBackgroundSection() {
        return backgroundSection;
    }

    public void setBackgroundSection(BackgroundSection backgroundSection) {
        this.backgroundSection = backgroundSection;
    }

    public WatchSchools getWatchSchools() {
        return watchSchools;
    }

    public void setWatchSchools(WatchSchools watchSchools) {
        this.watchSchools = watchSchools;
    }
}
