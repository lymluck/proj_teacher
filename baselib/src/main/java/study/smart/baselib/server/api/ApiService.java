package study.smart.baselib.server.api;

import com.alibaba.fastjson.JSONObject;

import study.smart.baselib.entity.DataListInfo;
import study.smart.baselib.entity.MyStudentInfo;
import study.smart.baselib.entity.QaDetailInfo;
import study.smart.baselib.entity.ResponseInfo;
import study.smart.baselib.entity.StudentPageInfo;
import study.smart.baselib.entity.TaskDetailInfo;
import study.smart.baselib.entity.TeacherInfo;
import study.smart.baselib.entity.TokenBean;
import study.smart.baselib.entity.VersionInfo;
import study.smart.baselib.utils.ConstantUtils;
import study.smart.baselib.utils.HttpUrlUtils;
import study.smart.baselib.utils.ParameterUtils;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author louis
 * @date on 2017/12/27.
 * @describe get请求按需进行@Headers配置缓存
 * @org com.smartstudy.counselor_t
 * @email luoyongming@innobuddy.com
 */
public interface ApiService {

    /**
     * 获取验证码
     *
     * @param phone 手机号
     * @return
     */
    @POST(HttpUrlUtils.URL_PHONE_CODE)
    Observable<ResponseInfo<String>> getPhoneCode(@HeaderMap Map<String, String> header, @Query("phone") String phone);

    /**
     * 验证码登录
     *
     * @param params
     * @return
     */
    @POST(HttpUrlUtils.URL_CODE_LOGIN)
    Observable<ResponseInfo<JSONObject>> phoneCodeLogin(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);

    /**
     * 获取学生主页详情
     *
     * @param params
     * @return
     */
    @GET(HttpUrlUtils.URL_STUDENT_DETAIL_INFO)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo<StudentPageInfo>> getStudentDetailInfo(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);

    /**
     * 获取学生信息
     *
     * @param id
     * @return
     */
    @GET(HttpUrlUtils.URL_STUDENT_DETAIL_INFO)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo<StudentPageInfo>> getStudentInfo(@HeaderMap Map<String, String> header, @Query("id") String id);

    /**
     * 重新获取token
     *
     * @return
     */
    @POST(HttpUrlUtils.URL_REFRESH_TOKEN)
    Observable<ResponseInfo> refreshToken();


    /**
     * 提交个人信息
     *
     * @return
     */
    @POST()
    Observable<ResponseInfo<String>> postPersonInfo(@HeaderMap Map<String, String> header, @Url() String url, @Body RequestBody Body);


    /**
     * 修改个人信息
     *
     * @return
     */
    @PUT()
    Observable<ResponseInfo<TeacherInfo>> updatePersonInfo(@HeaderMap Map<String, String> header, @Url() String url, @Body RequestBody Body);

    /**
     * 个人信息
     *
     * @return
     */

    @GET(HttpUrlUtils.URL_COUNSELLOR_PROFILE)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo<TeacherInfo>> getAuditResult(@HeaderMap Map<String, String> header);


    /**
     * 个人信息中获取个人选项操作
     *
     * @return
     */

    @GET(HttpUrlUtils.COUNSELLOR_PROFILE_OPTIONS)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo<JSONObject>> getOptions(@HeaderMap Map<String, String> header);


    @GET(HttpUrlUtils.URL_VIDEO_CREDENTIALS)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo<TokenBean>> refreshTacken(@HeaderMap Map<String, String> header);


    @GET(HttpUrlUtils.URL_COUNSELLOR_PROFILE)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo<TeacherInfo>> getMyInfo(@HeaderMap Map<String, String> header);

    /**
     * 获取消息统计列表
     *
     * @param header
     * @return
     */
    @GET(HttpUrlUtils.URL_MESSAGE_STATISTIC_LIST)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo<String>> getMessageList(@HeaderMap Map<String, String> header);


    /**
     * 获取留学中心
     *
     * @param header
     * @return
     */
    @GET(HttpUrlUtils.WORKBENCH_CENTER_LIST)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo<String>> getCenterList(@HeaderMap Map<String, String> header);

    /**
     * 为完成沟通记录
     *
     * @param header
     * @return
     */
    @GET(HttpUrlUtils.WORKBENCH_UNCOMPLETE_LIST)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo<DataListInfo>> getUnTalkRecordList(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);

    /**
     * 获取报告
     *
     * @param header
     * @return
     */
    @GET(HttpUrlUtils.WORKBENCH_UNCOMPLETE_REPORT_LIST)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo<DataListInfo>> getUnCompeleteReportList(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);

    /**
     * 获取消息统计列表
     *
     * @param header
     * @return
     */
    @GET(HttpUrlUtils.WORKBENCH_REPORT_LIST)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo<DataListInfo>> getMyReportList(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);

    /**
     * 获取我发布未沟通列表
     *
     * @param header
     * @return
     */
    @GET(HttpUrlUtils.WORKBENCH_COMPLETE_LIST)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo<String>> getMyTalkRecordList(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);

    /**
     * 获取我发布任务列表
     *
     * @param header
     * @return
     */
    @GET(HttpUrlUtils.WORKBENCH_TASK_LIST)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo<String>> getMyTaskList(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);


    /**
     * 获取消息统计列表
     *
     * @param header
     * @return
     */
    @GET(HttpUrlUtils.WORKBENCH_REPORT_LIST)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo<DataListInfo>> getStudentDetailReport(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);


    /**
     * 我的学员
     *
     * @param header
     * @return
     */
    @GET(HttpUrlUtils.WORKBENCH_USER_LIST)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo<DataListInfo>> getMyStudent(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);


    /**
     * 版本检测
     */
    @GET()
    Observable<ResponseInfo<VersionInfo>> checkVersion(@HeaderMap Map<String, String> header, @Url() String url);

    /**
     * 用户注销
     */
    @POST(HttpUrlUtils.URL_USER_LOGOUT)
    Observable<ResponseInfo<String>> getLogOut(@HeaderMap Map<String, String> header);

    /**
     * 文件下载
     *
     * @param url
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> downLoadFile(@NonNull @Url String url);


    /**
     * 问题列表
     */
    @GET(HttpUrlUtils.URL_QUESTS)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo<DataListInfo>> getQuestions(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);


    /**
     * 消息列表
     */
    @GET(HttpUrlUtils.URL_MESSAGE_LIST)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo<DataListInfo>> getMessageList(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);

    /**
     * 点赞列表
     */
    @GET(HttpUrlUtils.URL_ADD_GOODS_LIST)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo<DataListInfo>> getAddGoodDetail(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);


    /**
     * 我关注问题列表
     */
    @GET(HttpUrlUtils.URL_QUESTS_MARK)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo<DataListInfo>> getMyFocusQuestions(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);


    /**
     * 获取我关注的学生的标签
     */
    @GET()
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo<String>> getMyStudentTag(@HeaderMap Map<String, String> header, @Url() String url);


    /**
     * 获取消息详情
     */
    @GET()
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo<TaskDetailInfo>> getMessageDetail(@HeaderMap Map<String, String> header, @Url() String url);

    /**
     * 获取消息详情
     */
    @GET()
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo<MyStudentInfo>> getStudentDetail(@HeaderMap Map<String, String> header, @Url() String url);


    /**
     * 提交标签
     */
    @PUT()
    Observable<ResponseInfo<String>> submitMyStudentTag(@HeaderMap Map<String, String> header, @Url() String url, @QueryMap Map<String, String> params);


    /**
     * 提交历史标签
     */
    @POST(HttpUrlUtils.URL_QUESTS_HISTORY_TAGS)
    Observable<ResponseInfo<String>> postHistoryTag(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);


    /**
     * 获取历史标签
     */
    @GET(HttpUrlUtils.URL_QUESTS_HISTORY_TAGS)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo<String>> getHisToryTag(@HeaderMap Map<String, String> header);

    /**
     * 问答详情
     *
     * @param header
     * @param url
     * @param params
     * @return
     */
    @GET()
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo<QaDetailInfo>> getQaDetail(@HeaderMap Map<String, String> header, @Url() String url, @QueryMap Map<String, String> params);


    /**
     * 文本消息提交
     *
     * @param header
     * @param params
     * @return
     */
    @POST()
    Observable<ResponseInfo<String>> postQuestion(@HeaderMap Map<String, String> header, @Url() String url, @QueryMap Map<String, String> params);

    /**
     * 重点关注
     *
     * @param header
     * @param url
     * @return
     */
    @POST()
    Observable<ResponseInfo<String>> questionAddMark(@HeaderMap Map<String, String> header, @Url() String url);


    /**
     * 取消重点关注
     *
     * @param header
     * @param url
     * @return
     */
    @DELETE()
    Observable<ResponseInfo<String>> questionDeleteMark(@HeaderMap Map<String, String> header, @Url() String url);


    @POST()
    Observable<ResponseInfo<String>> postAnswerVoice(@HeaderMap Map<String, String> header, @Url() String url, @Body RequestBody Body);

    /**
     * 我的问答
     */
    @GET(HttpUrlUtils.URL_MY_ANSWER)
    Observable<ResponseInfo<DataListInfo>> getMyQuestion(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);

    /**
     * 启动页广告
     */
    @GET(HttpUrlUtils.URL_START_AD)
    Observable<ResponseInfo<String>> getAdInfo(@HeaderMap Map<String, String> header);


    /**
     * 获取未分配中心案子
     */
    @GET(HttpUrlUtils.URL_UNALLOCATED_CENTER)
    Observable<ResponseInfo<DataListInfo>> getUnallocatedCenter(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);

    /**
     * 获取已分配中心案子
     */
    @GET(HttpUrlUtils.URL_ALLOCATED_CENTER)
    Observable<ResponseInfo<DataListInfo>> getAllocatedCenter(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);

    /**
     * 获取被驳回转案
     */
    @GET(HttpUrlUtils.URL_REJECTED_CENTER)
    Observable<ResponseInfo<DataListInfo>> getRejectedCenter(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);


    /**
     * 获取未分配导师
     */
    @GET(HttpUrlUtils.URL_UNALLOCATED_COACH)
    Observable<ResponseInfo<DataListInfo>> getUnallocatedCoach(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);

    /**
     * 获取已分配导师
     */
    @GET(HttpUrlUtils.URL_ALLOCATED_COACH)
    Observable<ResponseInfo<DataListInfo>> getAllocatedCoach(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);

    /**
     * 搜索转案案例
     */
    @GET(HttpUrlUtils.URL_TRANSFER_CASE)
    Observable<ResponseInfo<DataListInfo>> searchTransferCase(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);


    /**
     * 搜索转案案例
     */
    @GET(HttpUrlUtils.URL_MESSAGE_LIST)
    Observable<ResponseInfo<DataListInfo>> searchMsgList(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);


    /**
     * 未分配中心结案
     *
     * @param header
     * @param url
     * @return
     */
    @PUT()
    Observable<ResponseInfo<String>> closeTransferCase(@HeaderMap Map<String, String> header, @Url() String url);

    /**
     * 已分配中心结案
     *
     * @param header
     * @param url
     * @return
     */
    @PUT()
    Observable<ResponseInfo<String>> closeAllocateTransferCase(@HeaderMap Map<String, String> header, @Url() String url);


    /**
     * 未分配中心驳回
     *
     * @param header
     * @param url
     * @return
     */
    @PUT()
    Observable<ResponseInfo<String>> rejectTransferManagerCase(@HeaderMap Map<String, String> header, @Url() String url, @QueryMap Map<String, String> params);

    /**
     * 未分配中心转案重启
     *
     * @param header
     * @param url
     * @return
     */
    @PUT()
    Observable<ResponseInfo<String>> reopenTransferCase(@HeaderMap Map<String, String> header, @Url() String url);


    /**
     * 已分配中心转案重启
     *
     * @param header
     * @param url
     * @return
     */
    @PUT()
    Observable<ResponseInfo<String>> reopenAllocatedCenter(@HeaderMap Map<String, String> header, @Url() String url);

    /**
     * wei分配案子详情
     *
     * @param header
     * @param url
     * @return
     */
    @GET()
    Observable<ResponseInfo<String>> getUnallocated(@HeaderMap Map<String, String> header, @Url() String url);

    /**
     * 已分配案子详情
     *
     * @param header
     * @param url
     * @return
     */
    @GET()
    Observable<ResponseInfo<String>> getAllocated(@HeaderMap Map<String, String> header, @Url() String url);

    /**
     * 被驳回案子详情
     *
     * @param header
     * @param url
     * @return
     */
    @GET()
    Observable<ResponseInfo<String>> getRejectedCenter(@HeaderMap Map<String, String> header, @Url() String url);


    /**
     * 被驳回案子驳回
     *
     * @param header
     * @param url
     * @return
     */
    @PUT()
    Observable<ResponseInfo<String>> rejectRejectCenter(@HeaderMap Map<String, String> header, @Url() String url, @QueryMap Map<String, String> params);

    /**
     * 被驳回案子驳回
     *
     * @param header
     * @param url
     * @return
     */
    @PUT()
    Observable<ResponseInfo<String>> closeRejectCenter(@HeaderMap Map<String, String> header, @Url() String url);


    /**
     * 被驳回案子驳回
     *
     * @param header
     * @param url
     * @return
     */
    @PUT()
    Observable<ResponseInfo<String>> reopenRejectCenter(@HeaderMap Map<String, String> header, @Url() String url);


    /**
     * 未分配导师驳回
     *
     * @param header
     * @param url
     * @return
     */
    @PUT()
    Observable<ResponseInfo<String>> rejectUnallocatedCoach(@HeaderMap Map<String, String> header, @Url() String url, @QueryMap Map<String, String> params);


    /**
     * 未分配导师结案
     *
     * @param header
     * @param url
     * @return
     */
    @PUT()
    Observable<ResponseInfo<String>> closeUnallocatedCoach(@HeaderMap Map<String, String> header, @Url() String url);


    /**
     * 已分配导师结案
     *
     * @param header
     * @param url
     * @return
     */
    @PUT()
    Observable<ResponseInfo<String>> closeAllocatedCoach(@HeaderMap Map<String, String> header, @Url() String url);

    /**
     * 未分配导师重启
     *
     * @param header
     * @param url
     * @return
     */
    @PUT()
    Observable<ResponseInfo<String>> reopenUnallocatedCoach(@HeaderMap Map<String, String> header, @Url() String url);

    /**
     * 已分配导师重启
     *
     * @param header
     * @param url
     * @return
     */
    @PUT()
    Observable<ResponseInfo<String>> reopenAllocatedCoach(@HeaderMap Map<String, String> header, @Url() String url);

    /**
     * 未分配导师案子详情
     *
     * @param header
     * @param url
     * @return
     */
    @GET()
    Observable<ResponseInfo<String>> getUnallocatedCoachl(@HeaderMap Map<String, String> header, @Url() String url);

    /**
     * 已分配导师案子详情
     *
     * @param header
     * @param url
     * @return
     */
    @GET()
    Observable<ResponseInfo<String>> getAllocatedCoachl(@HeaderMap Map<String, String> header, @Url() String url);

    /**
     * 给案子分配中心
     *
     * @param header
     * @param url
     * @return
     */
    @PUT()
    Observable<ResponseInfo<String>> assignTransferCase(@HeaderMap Map<String, String> header, @Url() String url, @QueryMap Map<String, String> params);


    /**
     * 给案子分配老师
     *
     * @param header
     * @param url
     * @return
     */
    @PUT()
    Observable<ResponseInfo<String>> assignTransferCaseTeacher(@HeaderMap Map<String, String> header, @Url() String url, @QueryMap Map<String, String> params);

    /**
     * 重新分配老师
     *
     * @param header
     * @param url
     * @return
     */
    @PUT()
    Observable<ResponseInfo<String>> assignTransferCaseTeacherAgain(@HeaderMap Map<String, String> header, @Url() String url, @QueryMap Map<String, String> params);

}

