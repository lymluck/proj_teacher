package study.smart.baselib.entity;

/**
 * @author yqy
 * @date on 2018/6/27
 * @describe 权限判断类
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class Privileges {
    // 转案
    private boolean transferCase;
    // 未分配中心
    private boolean unallocated;
    // 未分配中心下的分配中心权限
    private boolean unallocatedAllocateCenter;
    // 未分配中心下的驳回转案权限
    private boolean unallocatedRejected;
    // 未分配中心下的结案权限
    private boolean unallocatedClose;
    // 未分配中心下的重启转案权限
    private boolean unallocatedReopen;
    // 已经分配中心
    private boolean allocated;
    // 已经分配中心下的驳回转案权限
    private boolean allocatedRejected;
    // 已经分配中心下的分配导师权限
    private boolean allocatedAssigned;
    // 已经分配中心下的结案权限
    private boolean allocatedClose;
    // 已经分配中心下的结案权限
    private boolean allocatedReopen;
    // 被驳回转案
    private boolean rejected;
    // 被驳回转案下的分配中心权限
    private boolean rejectedAllocateCenter;
    // 被驳回转案下的驳回转案权限
    private boolean rejectedRejected;
    // 被驳回转案下的结案权限
    private boolean rejectedClose;
    // 被驳回转案下的重启转案权限
    private boolean rejectedReopen;
    // 未分配导师
    private boolean unassigned;
    // 未分配导师下的驳回转案权限
    private boolean unassignedRejected;
    // 未分配导师下的分配导师权限
    private boolean unassignedAssigned;
    // 未分配导师下的结案权限
    private boolean unassignedClose;
    // 未分配导师下的重启转案权限
    private boolean unassignedReopen;
    // 已分配导师
    private boolean assigned;
    // 已分配导师下的分配导师权限
    private boolean assignedAssigned;
    // 已分配导师下的结案权限
    private boolean assignedClose;
    // 已分配导师下的重启转案权限
    private boolean assignedReopen;

    public boolean isTransferCase() {
        return transferCase;
    }

    public void setTransferCase(boolean transferCase) {
        this.transferCase = transferCase;
    }

    public boolean isUnallocated() {
        return unallocated;
    }

    public void setUnallocated(boolean unallocated) {
        this.unallocated = unallocated;
    }

    public boolean isUnallocatedAllocateCenter() {
        return unallocatedAllocateCenter;
    }

    public void setUnallocatedAllocateCenter(boolean unallocatedAllocateCenter) {
        this.unallocatedAllocateCenter = unallocatedAllocateCenter;
    }

    public boolean isUnallocatedRejected() {
        return unallocatedRejected;
    }

    public void setUnallocatedRejected(boolean unallocatedRejected) {
        this.unallocatedRejected = unallocatedRejected;
    }

    public boolean isUnallocatedClose() {
        return unallocatedClose;
    }

    public void setUnallocatedClose(boolean unallocatedClose) {
        this.unallocatedClose = unallocatedClose;
    }

    public boolean isUnallocatedReopen() {
        return unallocatedReopen;
    }

    public void setUnallocatedReopen(boolean unallocatedReopen) {
        this.unallocatedReopen = unallocatedReopen;
    }

    public boolean isAllocated() {
        return allocated;
    }

    public void setAllocated(boolean allocated) {
        this.allocated = allocated;
    }

    public boolean isAllocatedAssigned() {
        return allocatedAssigned;
    }

    public void setAllocatedAssigned(boolean allocatedAssigned) {
        this.allocatedAssigned = allocatedAssigned;
    }

    public boolean isAllocatedClose() {
        return allocatedClose;
    }

    public void setAllocatedClose(boolean allocatedClose) {
        this.allocatedClose = allocatedClose;
    }

    public boolean isAllocatedReopen() {
        return allocatedReopen;
    }

    public void setAllocatedReopen(boolean allocatedReopen) {
        this.allocatedReopen = allocatedReopen;
    }

    public boolean isRejected() {
        return rejected;
    }

    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }

    public boolean isRejectedAllocateCenter() {
        return rejectedAllocateCenter;
    }

    public void setRejectedAllocateCenter(boolean rejectedAllocateCenter) {
        this.rejectedAllocateCenter = rejectedAllocateCenter;
    }

    public boolean isRejectedRejected() {
        return rejectedRejected;
    }

    public void setRejectedRejected(boolean rejectedRejected) {
        this.rejectedRejected = rejectedRejected;
    }

    public boolean isRejectedClose() {
        return rejectedClose;
    }

    public void setRejectedClose(boolean rejectedClose) {
        this.rejectedClose = rejectedClose;
    }

    public boolean isRejectedReopen() {
        return rejectedReopen;
    }

    public void setRejectedReopen(boolean rejectedReopen) {
        this.rejectedReopen = rejectedReopen;
    }

    public boolean isUnassigned() {
        return unassigned;
    }

    public void setUnassigned(boolean unassigned) {
        this.unassigned = unassigned;
    }

    public boolean isUnassignedRejected() {
        return unassignedRejected;
    }

    public void setUnassignedRejected(boolean unassignedRejected) {
        this.unassignedRejected = unassignedRejected;
    }

    public boolean isUnassignedAssigned() {
        return unassignedAssigned;
    }

    public void setUnassignedAssigned(boolean unassignedAssigned) {
        this.unassignedAssigned = unassignedAssigned;
    }

    public boolean isUnassignedClose() {
        return unassignedClose;
    }

    public void setUnassignedClose(boolean unassignedClose) {
        this.unassignedClose = unassignedClose;
    }

    public boolean isUnassignedReopen() {
        return unassignedReopen;
    }

    public void setUnassignedReopen(boolean unassignedReopen) {
        this.unassignedReopen = unassignedReopen;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public boolean isAssignedAssigned() {
        return assignedAssigned;
    }

    public void setAssignedAssigned(boolean assignedAssigned) {
        this.assignedAssigned = assignedAssigned;
    }

    public boolean isAssignedClose() {
        return assignedClose;
    }

    public void setAssignedClose(boolean assignedClose) {
        this.assignedClose = assignedClose;
    }

    public boolean isAssignedReopen() {
        return assignedReopen;
    }

    public void setAssignedReopen(boolean assignedReopen) {
        this.assignedReopen = assignedReopen;
    }

    public boolean isAllocatedRejected() {
        return allocatedRejected;
    }

    public void setAllocatedRejected(boolean allocatedRejected) {
        this.allocatedRejected = allocatedRejected;
    }
}
