package study.smart.transfer_management.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author yqy
 * @date on 2018/6/28
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TransferDetailentity implements Serializable {

    /**
     * centerId 分配中心ID,
     * centerName 分配中心名称,
     * contractBase 合同信息 { contractId 合同编号, receiptId 收据编号 },
     * user 用户基本信息 { name 姓名, sex 性别, phone 手机, email 邮箱, parentPhone 家长手机, city 所在城市, school 在读学校, grade 在读年级, graduationTime 毕业时间, worksYears 工作年限 },
     * contractData 签约信息 { signedTime 签约时间, serviceProductNames 服务项目, serviceFee 服务金额, paymentMethod 缴费方式, targetCountry 申请国家, targetDegree 申请方向, targetMajor 意向专业, contractor 签约人员, contractorTeacher 助签老师, counselor 后期顾问, temperamentTrait 家长和学生的性格特点, userSpecialDemand 家长对服务的需求特点, supplementContent 补充协议内容, otherContent 其他说明 },
     * results 成绩信息 [{ examType 考试类型, score 总成绩, examTime 考试时间, notes 备注 }],
     * socialPractice 社会实践 [{ type 活动类型(公益类、学术研究、领导组织类、个性天赋爱好类，其它), name 活动名称, level 活动级别(校级，地区级，国家级，国际级), time 活动时间，frequency 活动频率, role 担任角色, desc 活动及学员在其中的描述 }],
     * eduBackground 教育背景 [{ stages 学习阶段(高中/本科/研究生)，subject 学科: (文科/理科)，schoolName 院校名称, time 就读起止时间，major 就读专业，degree 获得学位，gpa 学习成绩 }],
     * workExp 实习/工作经历 [{ name 公司名称，startTime 开始时间, endTime 结束时间，role 担当职位, content 负责内容, contribution 功劳和成绩 }],
     * activities 课外活动 [{ name 活动名称, startTime 开始时间, endTime 结束时间，role 担任职位, content 负责内容, contribution 功劳和成绩，}],
     * academicExp 学术经历 [{ name 项目名称，startTime 开始时间, endTime 结束时间，article 发表的文章，content 主要表现， others 其它 }],
     * awarderExp 获奖经历 [{ name 奖项名称，time 获奖时间，organization 颁奖机构, leve 颁奖级别, contribution 主要成就及内容，others 其它 }],
     * personalSkills 个人技能 [{ name 技能名称， startTime 开始时间, endTime 结束时间， contribution 取得成就，content 主要表现和内容描述，others 其它 }],
     * meta 需要用到的下拉列表 { centerList 中心列表 [{ id 中心ID, value 中心名称 }], coachList Coach列表 [{ id coachID, value coach名称 }]},
     */

    private String userId;
    private String centerId;
    private String hardTeacherId;
    private String softTeacherId;
    private String centerName;
    private String hardTeacher;
    private String softTeacher;
    private String rejector;
    private String rejectedTime;
    private String rejectReason;
    private ContractBase contractBase;
    private User user;
    private ContractData contractData;
    private List<Results> results;
    private List<SocialPractice> socialPractice;
    private List<EduBackground> eduBackground;
    private List<WorkExp> workExp;
    private List<Activities> activities;
    private List<AcademicExp> academicExp;
    private List<AwarderExp> awarderExp;
    private List<PersonalSkills> personalSkills;
    private Meta meta;

    public String getRejector() {
        return rejector;
    }

    public void setRejector(String rejector) {
        this.rejector = rejector;
    }

    public String getRejectedTime() {
        return rejectedTime;
    }

    public void setRejectedTime(String rejectedTime) {
        this.rejectedTime = rejectedTime;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getHardTeacher() {
        return hardTeacher;
    }

    public void setHardTeacher(String hardTeacher) {
        this.hardTeacher = hardTeacher;
    }

    public String getSoftTeacher() {
        return softTeacher;
    }

    public void setSoftTeacher(String softTeacher) {
        this.softTeacher = softTeacher;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCenterId() {
        return centerId;
    }

    public void setCenterId(String centerId) {
        this.centerId = centerId;
    }

    public String getHardTeacherId() {
        return hardTeacherId;
    }

    public void setHardTeacherId(String hardTeacherId) {
        this.hardTeacherId = hardTeacherId;
    }

    public String getSoftTeacherId() {
        return softTeacherId;
    }

    public void setSoftTeacherId(String softTeacherId) {
        this.softTeacherId = softTeacherId;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public ContractBase getContractBase() {
        return contractBase;
    }

    public void setContractBase(ContractBase contractBase) {
        this.contractBase = contractBase;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ContractData getContractData() {
        return contractData;
    }

    public void setContractData(ContractData contractData) {
        this.contractData = contractData;
    }

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    public List<SocialPractice> getSocialPractice() {
        return socialPractice;
    }

    public void setSocialPractice(List<SocialPractice> socialPractice) {
        this.socialPractice = socialPractice;
    }

    public List<EduBackground> getEduBackground() {
        return eduBackground;
    }

    public void setEduBackground(List<EduBackground> eduBackground) {
        this.eduBackground = eduBackground;
    }

    public List<WorkExp> getWorkExp() {
        return workExp;
    }

    public void setWorkExp(List<WorkExp> workExp) {
        this.workExp = workExp;
    }

    public List<Activities> getActivities() {
        return activities;
    }

    public void setActivities(List<Activities> activities) {
        this.activities = activities;
    }

    public List<AcademicExp> getAcademicExp() {
        return academicExp;
    }

    public void setAcademicExp(List<AcademicExp> academicExp) {
        this.academicExp = academicExp;
    }

    public List<AwarderExp> getAwarderExp() {
        return awarderExp;
    }

    public void setAwarderExp(List<AwarderExp> awarderExp) {
        this.awarderExp = awarderExp;
    }

    public List<PersonalSkills> getPersonalSkills() {
        return personalSkills;
    }

    public void setPersonalSkills(List<PersonalSkills> personalSkills) {
        this.personalSkills = personalSkills;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public class ContractBase implements Serializable {
        private String contractId;
        private String receiptId;

        public String getContractId() {
            return contractId;
        }

        public void setContractId(String contractId) {
            this.contractId = contractId;
        }

        public String getReceiptId() {
            return receiptId;
        }

        public void setReceiptId(String receiptId) {
            this.receiptId = receiptId;
        }
    }

    public class User implements Serializable {
        private String name;
        private String sex;
        private String phone;
        private String email;
        private String parentPhone;
        private String city;
        private String school;
        private String grade;
        private String graduationTime;
        private String workYears;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getParentPhone() {
            return parentPhone;
        }

        public void setParentPhone(String parentPhone) {
            this.parentPhone = parentPhone;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getGraduationTime() {
            return graduationTime;
        }

        public void setGraduationTime(String graduationTime) {
            this.graduationTime = graduationTime;
        }

        public String getWorkYears() {
            return workYears;
        }

        public void setWorkYears(String workYears) {
            this.workYears = workYears;
        }
    }

    public class ContractData implements Serializable {
        private String signedTime;
        private String serviceProductNames;
        private String serviceFee;
        private String paymentMethod;
        private String targetCountry;
        private String targetDegree;
        private String targetMajor;
        private String contractor;
        private String contractorTeacher;
        private String counselor;
        private String temperamentTrait;
        private String userSpecialDemand;
        private String supplementContent;
        private String otherContent;

        public String getContractor() {
            return contractor;
        }

        public void setContractor(String contractor) {
            this.contractor = contractor;
        }

        public String getContractorTeacher() {
            return contractorTeacher;
        }

        public void setContractorTeacher(String contractorTeacher) {
            this.contractorTeacher = contractorTeacher;
        }

        public String getCounselor() {
            return counselor;
        }

        public void setCounselor(String counselor) {
            this.counselor = counselor;
        }

        public String getTemperamentTrait() {
            return temperamentTrait;
        }

        public void setTemperamentTrait(String temperamentTrait) {
            this.temperamentTrait = temperamentTrait;
        }

        public String getUserSpecialDemand() {
            return userSpecialDemand;
        }

        public void setUserSpecialDemand(String userSpecialDemand) {
            this.userSpecialDemand = userSpecialDemand;
        }

        public String getSupplementContent() {
            return supplementContent;
        }

        public void setSupplementContent(String supplementContent) {
            this.supplementContent = supplementContent;
        }

        public String getOtherContent() {
            return otherContent;
        }

        public void setOtherContent(String otherContent) {
            this.otherContent = otherContent;
        }

        public String getSignedTime() {
            return signedTime;
        }

        public void setSignedTime(String signedTime) {
            this.signedTime = signedTime;
        }

        public String getServiceProductNames() {
            return serviceProductNames;
        }

        public void setServiceProductNames(String serviceProductNames) {
            this.serviceProductNames = serviceProductNames;
        }

        public String getServiceFee() {
            return serviceFee;
        }

        public void setServiceFee(String serviceFee) {
            this.serviceFee = serviceFee;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getTargetCountry() {
            return targetCountry;
        }

        public void setTargetCountry(String targetCountry) {
            this.targetCountry = targetCountry;
        }

        public String getTargetDegree() {
            return targetDegree;
        }

        public void setTargetDegree(String targetDegree) {
            this.targetDegree = targetDegree;
        }

        public String getTargetMajor() {
            return targetMajor;
        }

        public void setTargetMajor(String targetMajor) {
            this.targetMajor = targetMajor;
        }
    }

    public class Results implements Serializable {
        private String examType;
        private String score;
        private String examTime;
        private String notes;

        public String getExamType() {
            return examType;
        }

        public void setExamType(String examType) {
            this.examType = examType;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getExamTime() {
            return examTime;
        }

        public void setExamTime(String examTime) {
            this.examTime = examTime;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }

    public class SocialPractice implements Serializable {
        private String type;
        private String name;
        private String level;
        private String startTime;
        private String endTime;
        private String frequency;
        private String role;
        private String content;
        private String contribution;
        private String desc;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
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

        public String getFrequency() {
            return frequency;
        }

        public void setFrequency(String frequency) {
            this.frequency = frequency;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContribution() {
            return contribution;
        }

        public void setContribution(String contribution) {
            this.contribution = contribution;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public class EduBackground implements Serializable {
        private String stages;
        private String schoolName;
        private String subject;
        private String startTime;
        private String endTime;
        private String major;
        private String degree;
        private String gpa;

        public String getStages() {
            return stages;
        }

        public void setStages(String stages) {
            this.stages = stages;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
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

        public String getMajor() {
            return major;
        }

        public void setMajor(String major) {
            this.major = major;
        }

        public String getDegree() {
            return degree;
        }

        public void setDegree(String degree) {
            this.degree = degree;
        }

        public String getGpa() {
            return gpa;
        }

        public void setGpa(String gpa) {
            this.gpa = gpa;
        }
    }

    public class WorkExp implements Serializable {
        private String name;
        private String startTime;
        private String endTime;
        private String role;
        private String content;
        private String contribution;

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

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContribution() {
            return contribution;
        }

        public void setContribution(String contribution) {
            this.contribution = contribution;
        }
    }

    public class Activities implements Serializable {
        private String type;
        private String name;
        private String level;
        private String startTime;
        private String endTime;
        private String frequency;
        private String role;
        private String content;
        private String contribution;
        private String desc;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
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

        public String getFrequency() {
            return frequency;
        }

        public void setFrequency(String frequency) {
            this.frequency = frequency;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContribution() {
            return contribution;
        }

        public void setContribution(String contribution) {
            this.contribution = contribution;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public class AcademicExp implements Serializable {
        private String name;
        private String startTime;
        private String endTime;
        private String article;
        private String content;
        private String others;

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

        public String getArticle() {
            return article;
        }

        public void setArticle(String article) {
            this.article = article;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getOthers() {
            return others;
        }

        public void setOthers(String others) {
            this.others = others;
        }
    }

    public class AwarderExp implements Serializable {
        private String name;
        private String time;
        private String organization;
        private String level;
        private String contribution;
        private String others;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getOrganization() {
            return organization;
        }

        public void setOrganization(String organization) {
            this.organization = organization;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getContribution() {
            return contribution;
        }

        public void setContribution(String contribution) {
            this.contribution = contribution;
        }

        public String getOthers() {
            return others;
        }

        public void setOthers(String others) {
            this.others = others;
        }
    }

    public class PersonalSkills implements Serializable {
        private String name;
        private String startTime;
        private String endTime;
        private String contribution;
        private String content;
        private String others;

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

        public String getContribution() {
            return contribution;
        }

        public void setContribution(String contribution) {
            this.contribution = contribution;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getOthers() {
            return others;
        }

        public void setOthers(String others) {
            this.others = others;
        }
    }

    public class Meta implements Serializable {
        public List<CenterList> centerList;

        public List<SoftTeachers> softTeachers;

        public List<HardTeachers> hardTeachers;

        public List<CenterList> getCenterList() {
            return centerList;
        }

        public List<SoftTeachers> getSoftTeachers() {
            return softTeachers;
        }

        public void setSoftTeachers(List<SoftTeachers> softTeachers) {
            this.softTeachers = softTeachers;
        }

        public List<HardTeachers> getHardTeachers() {
            return hardTeachers;
        }

        public void setHardTeachers(List<HardTeachers> hardTeachers) {
            this.hardTeachers = hardTeachers;
        }

        public void setCenterList(List<CenterList> centerList) {
            this.centerList = centerList;
        }

        public class CenterList implements Serializable {
            private String id;
            private String value;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public class HardTeachers implements Serializable {
            private String id;
            private String value;
            private boolean crossCenter;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public boolean isCrossCenter() {
                return crossCenter;
            }

            public void setCrossCenter(boolean crossCenter) {
                this.crossCenter = crossCenter;
            }
        }

        public class SoftTeachers implements Serializable {
            private String id;
            private String value;
            private boolean crossCenter;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public boolean isCrossCenter() {
                return crossCenter;
            }

            public void setCrossCenter(boolean crossCenter) {
                this.crossCenter = crossCenter;
            }
        }
    }
}
