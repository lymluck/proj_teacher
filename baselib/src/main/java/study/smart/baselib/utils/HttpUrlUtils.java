package study.smart.baselib.utils;

import android.util.Log;

public class HttpUrlUtils {

    /**************************** api ***************************/
    public static final String URL_APP_VERSION = "app/android/version/%1$s/check";  //版本检测
    public static final String URL_QUESTS = "questions/list"; //问答列表
    public static final String URL_ADD_GOODS_LIST = "counsellor/like/likers"; //问答列表
    public static final String URL_QUESTS_MARK = "questions/list/marked"; //关注问答列表
    public static final String URL_QUESTS_OTHER_TAGS = " counsellor/student/%1$s/tags/others"; //学生标签
    public static final String URL_QUESTS_MY_TAGS = "counsellor/student/%1$s/tags"; //学生标签
    public static final String URL_QUESTS_HISTORY_TAGS = "counsellor/student/tags/history";//提交历史的标签
    public static final String URL_QUESTS_LINK = "questions/%1$s"; //问答详情
    public static final String URL_VIDEO_CREDENTIALS = "counsellor/profile/video/credentials"; //上传视频

    public static final String URL_PHONE_CODE = "counsellor/captcha";  //获取验证码
    public static final String URL_CODE_LOGIN = "counsellor/login";  //验证码登录
    public static final String URL_STUDENT_DETAIL_INFO = "counsellor/student/profile";  //学生信息查询
    public static final String URL_COUNSELLOR_VERIFY = "counsellor/verify";  //教师信息提交审核
    public static final String URL_COUNSELLOR_PROFILE = "counsellor/profile";//获取老师个人信息
    public static final String URL_REFRESH_TOKEN = "counsellor/im/refresh";  //获取教师token
    public static final String URL_USER_LOGOUT = "counsellor/logout";  //退出登录
    public static final String COUNSELLOR_PROFILE_OPTIONS = "counsellor/profile/options";//个人信息中获取个人选项操作
    public static final String URL_POST_ANSWER = "questions/%1$s/answer";//提交追问
    public static final String URL_QUESTION_MARK = "questions/%1$s/mark";//关注问题
    public static final String URL_MY_ANSWER = "questions/list/answered_by_me";//我的问答
    public static final String URL_START_AD = "counsellor/app/launchscreen";  // 开机广告页
    public static final String URL_UNALLOCATED_CENTER = "transfer_case/unallocated_center/list";//未分配中心案子的列表
    public static final String URL_ALLOCATED_CENTER = "transfer_case/allocated_center/list";//已分配中心案子的列表
    public static final String URL_REJECTED_CENTER = "transfer_case/rejected_center/list";//被驳回案例列表
    public static final String URL_UNALLOCATED_COACH = "transfer_case/unallocated_coach/list";//获取未分配导师
    public static final String URL_ALLOCATED_COACH = "transfer_case/allocated_coach/list";//获取已分配导师
    public static final String URL_TRANSFER_CASE = "transfer_case/list";//转案搜索
    public static final String URL_CLOSE_TRANSFER_CASE = "transfer_case/close/%1$s/unallocated_center";//未分配中心转案结案
    public static final String URL_REJECT_TRANSFER_CASE = "transfer_case/reject/%1$s/unallocated_center";//未分配中心转案驳回
    public static final String URL_REOPEN_TRANSFER_CASE = "transfer_case/reopen/%1$s/unallocated_center";//未分配中心转案重启
    public static final String URL_UNALLOCATED_TRANSFER_CASE = "transfer_case/%1$s/unallocated";//未分配中心转案获取详情
    public static final String URL_ASSIGN_TRANSFER_CASE = "transfer_case/assign/%1$s/unallocated_center";//给案子分配中心
    public static final String URL_ALLOCATED_TRANSFER_CASE = "transfer_case/%1$s/allocated";//已分配中心转案获取详情
    public static final String URL_REJECTED_CENTER_DETAIL = "transfer_case/%1$s/rejected_center";//被驳回转案获取详情
    public static final String URL_UNALLOCATED_COACH_DETAIL = "transfer_case/%1$s/unallocated_coach";//未分配导师转案获取详情
    public static final String URL_ALLOCATED_COACH_DETAIL = "transfer_case/%1$s/allocated_coach";//已分配导师转案获取详情
    public static final String URL_CLOSE_ALLOCATED_CENTER = "transfer_case/close/%1$s/allocated_center";//已分配中心结案
    public static final String URL_ASSIGN_UNALLOCATED_COACH = "transfer_case/assign/%1$s/unallocated_coach";//给案子分配老师
    public static final String URL_ASSIGN_ALLOCATED_COACH = "transfer_case/assign/%1$s/allocated_coach";//重新分配老师
    public static final String URL_REOPEN_ALLOCATED_CENTER = "transfer_case/reopen/%1$s/allocated_center";//已分配案子重启
    public static final String URL_REJECT_REJECTED_CENTER = " transfer_case/reject/%1$s/rejected_center";//被驳回列表驳回到CRM
    public static final String URL_CLOSE_REJECTED_CENTER = " transfer_case/close/%1$s/rejected_center";//被驳回列表结案
    public static final String URL_REOPEN_REJECTED_CENTER = " transfer_case/reopen/%1$s/rejected_center";//被驳回列表重启
    public static final String URL_REJECT_UNALLOCATED_COACH = "transfer_case/reject/%1$s/unallocated_coach";//未分配导师驳回
    public static final String URL_CLOSE_UNALLOCATED_COACH = "transfer_case/close/%1$s/unallocated_coach";//未分配导师结案
    public static final String URL_REOPEN_UNALLOCATED_COACH = "transfer_case/reopen/%1$s/unallocated_coach";//未分配导师重启
    public static final String URL_CLOSE_ALLOCATED_COACH = "transfer_case/close/%1$s/allocated_coach";//已分配导师结案
    public static final String URL_REOPEN_ALLOCATED_COACH = "transfer_case/reopen/%1$s/allocated_coach";//已分配导师重启
    public static final String URL_MESSAGE_STATISTIC_LIST = "message/statistic/list";//消息统计列表
    public static final String URL_MESSAGE_LIST = "message/list"; //问答列表
    public static final String WORKBENCH_USER_LIST = "workbench/user/list";
    public static final String WORKBENCH_UNCOMPLETE_REPORT_LIST = "workbench/uncomplete/report/list";//待完成报告列表
    public static final String WORKBENCH_REPORT_LIST = "workbench/report/list";//报告中心列表
    public static final String WORKBENCH_UNCOMPLETE_LIST = "workbench/uncomplete/communication/list";//未完成沟通记录
    public static final String WORKBENCH_COMPLETE_LIST = "workbench/communication/list";//我发布的沟通记录
    public static final String WORKBENCH_TASK_LIST = "workbench/task/list";//我发布的任务列表
    public static final String WORKBENCH_CENTER_LIST = "workbench/center/list";//中心
    public static final String MESSAGE_DETAIL = "message/%1$s";//中心

    /**************************** web ***************************/
    public static final String URL_USER_CONTRACT = "/user-agreement.html";  //用户协议


    /*********获取api接口url***********/
    public static String getBaseUrl() {
        String SERVER = "https://slx.smartstudy.com/api/";
//        String SERVER = "http://172.17.7.102:3234/";
//        String SERVER = "http://slx.staging.smartstudy.com/api/"; //test
        String api = (String) SPCacheUtils.get(ConstantUtils.API_SERVER, "");
        switch (api) {
            case "master":
//                SERVER = "https://slx.smartstudy.com/api/";
                SERVER = "http://slx.staging.smartstudy.com/api/";
                break;
            case "test":
//               SERVER = "https://slx.smartstudy.com/api/";
                SERVER = "http://slx.staging.smartstudy.com/api/"; //test
//                SERVER = "http://172.17.7.102:3234/";
                break;
            case "dev":
                SERVER = "http://blog.smartstudy.com:3234/";
                break;
            default:
                break;
        }
        return SERVER;
    }

    /********获取web页面url*********/
    public static String getWebUrl(String url) {
        String WEB_SERVER = "https://xxd.smartstudy.com/";  //master
        String api = (String) SPCacheUtils.get(ConstantUtils.API_SERVER, "");
        switch (api) {
            case "master":
                WEB_SERVER = "https://xxd.smartstudy.com/";
                break;
            case "test":
                WEB_SERVER = "http://xxd.beikaodi.com/";
                break;
            case "dev":
                WEB_SERVER = "http://yongle.smartstudy.com:3100/";
                break;
        }
        return WEB_SERVER + url + "?_from=app_android_" + AppUtils.getVersionName();
    }
}
