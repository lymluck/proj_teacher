package study.smart.baselib.entity;

/**
 * @author yqy
 * @date on 2018/7/18
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TeacherRankInfo {
    private String id;
    private String answeredQuestionsCount;
    private int ratingsCount;
    private String averageScore;
    private String name;
    private String imUserId;
    private String avatar;
    private String title;
    private String schoolCertified;
    private String yearsOfWorking;
    private String status;
    private String realName;
    private boolean isMyself;
    private int rank;

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnsweredQuestionsCount() {
        return answeredQuestionsCount;
    }

    public void setAnsweredQuestionsCount(String answeredQuestionsCount) {
        this.answeredQuestionsCount = answeredQuestionsCount;
    }

    public int getRatingsCount() {
        return ratingsCount;
    }

    public void setRatingsCount(int ratingsCount) {
        this.ratingsCount = ratingsCount;
    }

    public String getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(String averageScore) {
        this.averageScore = averageScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImUserId() {
        return imUserId;
    }

    public void setImUserId(String imUserId) {
        this.imUserId = imUserId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSchoolCertified() {
        return schoolCertified;
    }

    public void setSchoolCertified(String schoolCertified) {
        this.schoolCertified = schoolCertified;
    }

    public String getYearsOfWorking() {
        return yearsOfWorking;
    }

    public void setYearsOfWorking(String yearsOfWorking) {
        this.yearsOfWorking = yearsOfWorking;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public boolean isMyself() {
        return isMyself;
    }

    public void setMyself(boolean myself) {
        isMyself = myself;
    }
}
