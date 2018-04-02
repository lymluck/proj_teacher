package com.smartstudy.counselor_t.util;

public class HttpUrlUtils {

    /**************************** api ***************************/
    public static final String URL_APP_VERSION = "app/android/version/%1$s/check";  //版本检测
    public static final String URL_QUESTS = "questions/list"; //问答列表
    public static final String URL_QUESTS_LINK = "questions/%1$s"; //问答详情

    public static final String URL_PHONE_CODE = "counsellor/captcha";  //获取验证码
    public static final String URL_CODE_LOGIN = "counsellor/login";  //验证码登录
    public static final String URL_STUDENT_DETAIL_INFO = "counsellor/student/profile";  //学生信息查询
    public static final String URL_COUNSELLOR_VERIFY = "counsellor/verify";  //教师信息提交审核
    public static final String URL_COUNSELLOR_PROFILE = "counsellor/profile";//获取老师个人信息
    public static final String URL_REFRESH_TOKEN = "counsellor/im/refresh";  //获取教师token
    public static final String URL_USER_LOGOUT = "counsellor/logout";  //退出登录


    /**************************** web ***************************/
    public static final String URL_USER_CONTRACT = "/user-agreement.html";  //用户协议
    public static final String URL_POST_ANSWER = "questions/%1$s/answer";//提交追问
    public static final String URL_MY_ANSWER = "questions/list/answered_by_me";//我的问答


    /*********获取api接口url***********/
    public static String getBaseUrl() {
//        String SERVER = "http://172.17.7.72:3000/";
//        String SERVER = "https://slx.smartstudy.com/api/";
        String SERVER = "http://slx.staging.smartstudy.com/api/"; //test
        String api = (String) SPCacheUtils.get(ConstantUtils.API_SERVER, "");
        switch (api) {

            case "master":
                SERVER = "https://slx.smartstudy.com/api/";
//                SERVER = "http://slx.sta ging.smartstudy.com/api/";
                break;
            case "test":
//               SERVER = "https://slx.smartstudy.com/api/";
                SERVER = "http://slx.staging.smartstudy.com/api/"; //test
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
