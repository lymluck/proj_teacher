package study.smart.baselib.utils;

public class ParameterUtils {
    public static final long SMS_TIMEOUT = 30 * 1000;  //短信验证倒计时

    public static final String CACHE_NULL = "xxd_null";
    // / 没有连接
    public static final String NETWORN_NONE = "NETWORN_NONE";
    // / wifi连接
    public static final String NETWORN_WIFI = "NETWORN_WIFI";
    // / 手机网络数据连接
    public static final String NETWORN_2G = "NETWORN_2G";
    public static final String NETWORN_3G = "NETWORN_3G";
    public static final String NETWORN_4G = "NETWORN_4G";
    public static final String NETWORN_MOBILE = "NETWORN_MOBILE";
    public static final String API_SERVER = "api_server";
    public static final String GET_DATA_FAILED = "获取数据失败,请稍候重试!";
    public static final String NET_ERR = "请检查网络连接状态!";
    public static final String NEWEST_VERSION = "当前已是最新版本!";
    public static final String UPLOAD_ERR = "上传失败!";
    public static final String DOWNLOAD_ERR = "下载失败!";
    public static final String ALL_PICS = "所有图片";
    public static final String ALL_VIDEOS = "所有视频";
    public static final String MEIQIA_KEY = "11342702eacdcfdc64e67be582aebbf5";
    //无网络连接错误码
    public static final String RESPONE_CODE_NETERR = "net_err";
    // 应用更新
    public static final int FLAG_UPDATE = 1;
    // 应用强制更新
    public static final int FLAG_UPDATE_NOW = 2;
    // 通知栏更新
    public static final int MSG_WHAT_PROGRESS = 3;
    public static final int MSG_WHAT_ERR = 4;
    public static final int MSG_WHAT_FINISH = 5;
    public static final int MSG_WHAT_REFRESH = 6;

    //message empty what
    public static final int EMPTY_WHAT = 7;


    public static final int REQUEST_CODE_CHANGEPHOTO = 8; //更换照片请求码
    public static final int REQUEST_CODE_CAMERA = 9; //拍摄照片请求码
    public static final int REQUEST_CODE_CLIP_OVER = 10; //剪裁照片请求码
    public static final int REQUEST_CODE_EDIT_MYINFO = 11; //查看我的资料请求码
    public static final int REQUEST_CODE_STORAGE = 12; //申请读写存储请求码
    public static final int REQUEST_CODE_ADD_SCHOOL = 13; //添加选校请求码
    public static final int REQUEST_CODE_PERMISSIONS = 14; //添加权限请求码
    public static final int REQUEST_CODE_LOGIN = 15; //登录请求码
    public static final int REQUEST_CODE_SMART_CHOOSE = 16; //智能选校请求码

    public static final int ONLY_NETWORK = 17; //只查询网络数据
    public static final int ONLY_CACHED = 18; //只查询本地缓存
    public static final int CACHED_ELSE_NETWORK = 19; //先查询本地缓存，如果本地没有，再查询网络数据
    public static final int NETWORK_ELSE_CACHED = 20; //先查询网络数据，如果没有，再查询本地缓

    public static final int PULL_DOWN = 21; //下拉刷新
    public static final int PULL_UP = 22; //上拉加载
    public static final int FRAGMENT_ONE = 23; //首页第一个fragment标识
    public static final int FRAGMENT_TWO = 24;//首页第二个fragment标识
    public static final int FRAGMENT_THREE = 25;//首页第三个fragment标识
    public static final int FRAGMENT_THOUR = 26;//首页第四个fragment标识
    public static final int FRAGMENT_FIVE = 34;//首页第五个fragment标识
    public static final String FRAGMENT_TAG = "frgmt_tag";
    public static final int GL_FLAG = 27;
    public static final int US_FLAG = 28;
    public static final int ACTION_FROM_WEB = 29;
    public static final int REQUEST_CODE_WEBVIEW = 30;
    public static final int REQUEST_CODE_SPECIAL = 31;
    public static final int MSG_WHAT_SMOOTH = 32;
    public static final int MSG_WHAT_REPOSITION = 33;
    public static final int GET_ORDER_DATE = 34;
    public static final int GET_ORDER_TIME = 35;
    public static final int REQUEST_CODE_CARD_QA = 36;
    public static final int REQUEST_VIDEO = 37;
    public static final String QUARTERLY = "QUARTERLY";//季度报告
    public static final String SUMMARY = "SUMMARY";//总结报告
    public static final String CLOSE_CASE = "CLOSE_CASE";//结案报告

    public static final String TRANSITION_FLAG = "trans_flag";  //搜索flag名
    public static final String TRANSFER_MANAGER = "transfer_manager";  //flag
    public static final String MSG_DETAIL = "msg_detail";  //flag·
    public static final String MY_ALL_STUDENT = "my_all_student";//学员管理查询
    public static final String MYSCHOOL_FLAG = "mySchool";  //flag
    public static final String RECENTUSER_FLAG = "recent_user";  //flag
    public static final String EDIT_NAME = "edit_name";  //flag
    public static final String EDIT_SHEHUI_EVENT = "edit_shehui_event";  //flag
    public static final String WORK_CITY = "work_city";  //flag
    public static final String WORK_BUSSIENSS = "work_bussienss";  //flag
    public static final String EDIT_WORK_NAME = "edit_work_name";//工作职称
    public static final String EDIT_WORK_EXPERIENCE = "edit_work_experience";//工作经验
    public static final String EDIT_GRADUATED_SCHOOL = "edit_graduated_school";//毕业学校
    public static final String EDIT_REAL_NAME = "edit_real_name";//修改真实姓名
    public static final String EDIT_REMARK = "edit_remark";//修改真实姓名
    public static final String EDIT_EMAIL = "edit_email";//修改邮箱
    public static final String STUDENT_TRANSFER_MANAGER = "student_transfer_manager";//学员管理
    public static final String COMPELETE_STUDENT = "compelete_student";//学员信息完善
    public static final String TASK_TRANSFER_MANAGER = "task_transfer_manager";//学员信息完善
    public static final String REPORT_TRANSFER_MANAGER = "report_transfer_manager";//报告管理
    public static final String TALK_TRANSFER_MANAGER = "talk_transfer_manager";//沟通管理

}
