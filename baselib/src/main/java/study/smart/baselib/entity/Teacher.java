package study.smart.baselib.entity;

/**
 * @author yqy
 * @date on 2018/8/20
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class Teacher {
    private String id;
    private String realName;
    private String avatar;
    private String workingCityKey;
    private String group;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

}
