package study.smart.transfer_management.entity;

import java.util.List;

import study.smart.baselib.entity.WorkingSearchInfo;

/**
 * @author yqy
 * @date on 2018/7/30
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class WorkingSearchListInfo {
    private String typeName;
    private String typeTotal;
    private String keyword;
    private int viewType;

    public static final int STUDENT_TYPE = 1;
    public static final int TASK_TYPE = 2;
    public static final int REPORT_TYPE = 3;
    public static final int TALK_TYPE = 4;
    private List<WorkingSearchInfo> workingSearchInfos;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeTotal() {
        return typeTotal;
    }

    public void setTypeTotal(String typeTotal) {
        this.typeTotal = typeTotal;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<WorkingSearchInfo> getWorkingSearchInfos() {
        return workingSearchInfos;
    }

    public void setWorkingSearchInfos(List<WorkingSearchInfo> workingSearchInfos) {
        this.workingSearchInfos = workingSearchInfos;
    }
}
