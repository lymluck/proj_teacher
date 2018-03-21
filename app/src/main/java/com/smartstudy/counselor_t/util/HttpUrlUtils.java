package com.smartstudy.counselor_t.util;

public class HttpUrlUtils {

    /**************************** api ***************************/
    public static final String URL_APP_VERSION = "app/android/version/%1$s/check";  //版本检测
    public static final String URL_PHONE_CODE = "counsellor/captcha";  //获取验证码
    public static final String URL_CODE_LOGIN = "counsellor/login";  //验证码登录
    public static final String URL_STUDENT_DETAIL_INFO = "counsellor/student/profile";  //学生信息查询
    public static final String URL_COUNSELLOR_VERIFY = "counsellor/verify";  //教师信息提交审核
    public static final String URL_COUNSELLOR_PROFILE = "counsellor/profile";//获取老师个人信息
    public static final String URL_REFRESH_TOKEN = "counsellor/im/refresh";  //获取教师token
    public static final String URL_USER_LOGOUT = "counsellor/logout";  //退出登录

    /**************************** web ***************************/
    public static final String URL_USER_CONTRACT = "/user-agreement.html";  //用户协议

    /*********获取api接口url***********/
    public static String getBaseUrl() {
        String SERVER = "http://slx.smartstudy.com/api/";
        String api = (String) SPCacheUtils.get(ConstantUtils.API_SERVER, "");
        switch (api) {
            case "master":
                SERVER = "http://slx.smartstudy.com/api/";
                break;
            case "test":
                //test
                SERVER = "http://slx.staging.smartstudy.com/api/";
                break;
            case "dev":
                //dev
                SERVER = "http://server.tdc.smartstudy.com:3234/";
                break;
            default:
                break;
        }
        return SERVER;
    }

    /********获取web页面url*********/
    public static String getWebUrl(String url) {
        String WEB_SERVER = "https://xxd.smartstudy.com";  //master
        String api = (String) SPCacheUtils.get(ConstantUtils.API_SERVER, "");
        switch (api) {
            case "master":
                WEB_SERVER = "https://xxd.smartstudy.com";
                break;
            case "test":
                WEB_SERVER = "http://xxd.beikaodi.com";
                break;
            case "dev":
                WEB_SERVER = "http://yongle.smartstudy.com:3100";
//                WEB_SERVER = "http://xiaowei.hz.beikaodi.com:3100";
                break;
        }
        return WEB_SERVER + url + "?_from=app_android_" + AppUtils.getVersionName();
    }
}
