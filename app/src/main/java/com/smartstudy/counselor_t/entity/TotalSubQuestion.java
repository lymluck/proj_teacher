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

    public TotalSubQuestion(int totalSubQuestionCount) {
        this.totalSubQuestionCount = totalSubQuestionCount;
    }

    public int getTotalSubQuestionCount() {
        return totalSubQuestionCount;
    }

    public void setTotalSubQuestionCount(int totalSubQuestionCount) {
        this.totalSubQuestionCount = totalSubQuestionCount;
    }
}
