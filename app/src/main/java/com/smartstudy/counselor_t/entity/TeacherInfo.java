package com.smartstudy.counselor_t.entity;

import android.support.v4.os.ParcelableCompat;

import java.io.Serializable;

/**
 * @author yqy
 * @date on 2018/1/22
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TeacherInfo implements Serializable{

    /**
     * createTime : 2018-01-11T08:32:32.240Z
     * enabled : true
     * id : 8
     * imToken : MWU7nl7WMGqasc8ORJvFu2RTBQWS7neWQ5fFfhChK1DN8eLYrfkJkVqCuS8499ZOAbCL+bywGMqwocn5MbRHmjrInVSjLZx5Ehnj+9rMVuU=
     * imUserId : xxdcnsl#8@tdc
     * inited : true
     * orgId : 1
     * organization : {"id":1,"introduction":"智课网是智课教育旗下的在线学习平台，以翻转课堂为核心，致力于打造 \u201c学、练、改、管、测\u201d 一站式学习服务。智课网汇聚了出国考试、考研全科、四六级、语言学习等各科首席教学专家，以及遍布全球的外教专家，他们为学员提供名师课程、真题精讲、写作批改、口语诊断、学习督导等专业服务。智课网同时为学员提供模考、练习、抢考位、院校库、备考资料等一系列免费学习工具。","logo":"https://bkd-media.smartstudy.com/counsellor/org/logo/3a/fb/3afb58aa2ccebf447e550ca74f04b9acb5c.png","name":"智课教育科技有限公司","smallLogo":"https://bkd-media.smartstudy.com/counsellor/org/logo/33/d8/33d801bdd951abe77c8ee4c5b17848b03e2.png","subtitle":"一站式美国留学专家"}
     * phone : 18158142355
     * schoolCertified : false
     * visible : true
     */
    private String name;
    private String avatar;
    private String title;
    private String yearsOfWorking;
    private String school;
    private String email;
    private String realName;
    private int status;
    private String createTime;
    private boolean enabled;
    private int id;
    private String imToken;
    private String imUserId;
    private boolean inited;
    private int orgId;
    private OrganizationEntity organization;
    private String phone;
    private boolean schoolCertified;
    private boolean visible;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name == null ? "" : name;
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

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYearsOfWorking() {
        return yearsOfWorking == null ? "" : yearsOfWorking;
    }

    public void setYearsOfWorking(String yearsOfWorking) {
        this.yearsOfWorking = yearsOfWorking;
    }

    public String getSchool() {
        return school == null ? "" : school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getEmail() {
        return email == null ? "" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealName() {
        return realName == null ? "" : realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImToken(String imToken) {
        this.imToken = imToken;
    }

    public void setImUserId(String imUserId) {
        this.imUserId = imUserId;
    }

    public void setInited(boolean inited) {
        this.inited = inited;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public void setOrganization(OrganizationEntity organization) {
        this.organization = organization;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSchoolCertified(boolean schoolCertified) {
        this.schoolCertified = schoolCertified;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getCreateTime() {
        return createTime;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public int getId() {
        return id;
    }

    public String getImToken() {
        return imToken;
    }

    public String getImUserId() {
        return imUserId;
    }

    public boolean getInited() {
        return inited;
    }

    public int getOrgId() {
        return orgId;
    }

    public OrganizationEntity getOrganization() {
        return organization;
    }

    public String getPhone() {
        return phone;
    }

    public boolean getSchoolCertified() {
        return schoolCertified;
    }

    public boolean getVisible() {
        return visible;
    }

    public static class OrganizationEntity {
        /**
         * id : 1
         * introduction : 智课网是智课教育旗下的在线学习平台，以翻转课堂为核心，致力于打造 “学、练、改、管、测” 一站式学习服务。智课网汇聚了出国考试、考研全科、四六级、语言学习等各科首席教学专家，以及遍布全球的外教专家，他们为学员提供名师课程、真题精讲、写作批改、口语诊断、学习督导等专业服务。智课网同时为学员提供模考、练习、抢考位、院校库、备考资料等一系列免费学习工具。
         * logo : https://bkd-media.smartstudy.com/counsellor/org/logo/3a/fb/3afb58aa2ccebf447e550ca74f04b9acb5c.png
         * name : 智课教育科技有限公司
         * smallLogo : https://bkd-media.smartstudy.com/counsellor/org/logo/33/d8/33d801bdd951abe77c8ee4c5b17848b03e2.png
         * subtitle : 一站式美国留学专家
         */

        private int id;
        private String introduction;
        private String logo;
        private String name;
        private String smallLogo;
        private String subtitle;

        public void setId(int id) {
            this.id = id;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setSmallLogo(String smallLogo) {
            this.smallLogo = smallLogo;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public int getId() {
            return id;
        }

        public String getIntroduction() {
            return introduction;
        }

        public String getLogo() {
            return logo;
        }

        public String getName() {
            return name;
        }

        public String getSmallLogo() {
            return smallLogo;
        }

        public String getSubtitle() {
            return subtitle;
        }
    }
}
