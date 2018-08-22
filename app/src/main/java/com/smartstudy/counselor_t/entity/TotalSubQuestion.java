package com.smartstudy.counselor_t.entity;

/**
 * @author yqy
 * @date on 2018/3/21
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TotalSubQuestion {
    private int totalSubQuestionCount;
    private int unreceivedSharedQuestionsCount;

    public int getUnreceivedSharedQuestionsCount() {
        return unreceivedSharedQuestionsCount;
    }

    public void setUnreceivedSharedQuestionsCount(int unreceivedSharedQuestionsCount) {
        this.unreceivedSharedQuestionsCount = unreceivedSharedQuestionsCount;
    }

    public TotalSubQuestion(int totalSubQuestionCount,int unreceivedSharedQuestionsCount) {
        this.totalSubQuestionCount = totalSubQuestionCount;
        this.unreceivedSharedQuestionsCount=unreceivedSharedQuestionsCount;
    }

    public int getTotalSubQuestionCount() {
        return totalSubQuestionCount;
    }

    public void setTotalSubQuestionCount(int totalSubQuestionCount) {
        this.totalSubQuestionCount = totalSubQuestionCount;
    }
}
