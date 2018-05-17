package com.smartstudy.counselor_t.server.api;

import com.smartstudy.counselor_t.entity.ResponseInfo;
import com.smartstudy.counselor_t.util.ConstantUtils;
import com.smartstudy.counselor_t.util.HttpUrlUtils;
import com.smartstudy.counselor_t.util.ParameterUtils;

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
    Observable<ResponseInfo> getPhoneCode(@HeaderMap Map<String, String> header, @Query("phone") String phone);

    /**
     * 验证码登录
     *
     * @param params
     * @return
     */
    @POST(HttpUrlUtils.URL_CODE_LOGIN)
    Observable<ResponseInfo> phoneCodeLogin(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);

    /**
     * 获取学生主页详情
     *
     * @param params
     * @return
     */
    @GET(HttpUrlUtils.URL_STUDENT_DETAIL_INFO)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo> getStudentDetailInfo(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);

    /**
     * 获取学生信息
     *
     * @param id
     * @return
     */
    @GET(HttpUrlUtils.URL_STUDENT_DETAIL_INFO)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo> getStudentInfo(@HeaderMap Map<String, String> header, @Query("id") String id);

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
    Observable<ResponseInfo> postPersonInfo(@HeaderMap Map<String, String> header, @Url() String url, @Body RequestBody Body);


    /**
     * 修改个人信息
     *
     * @return
     */
    @PUT()
    Observable<ResponseInfo> updatePersonInfo(@HeaderMap Map<String, String> header, @Url() String url, @Body RequestBody Body);

    /**
     * 个人信息
     *
     * @return
     */

    @GET(HttpUrlUtils.URL_COUNSELLOR_PROFILE)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo> getAuditResult(@HeaderMap Map<String, String> header);


    /**
     * 个人信息中获取个人选项操作
     *
     * @return
     */

    @GET(HttpUrlUtils.COUNSELLOR_PROFILE_OPTIONS)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo> getOptions(@HeaderMap Map<String, String> header);


    @GET(HttpUrlUtils.URL_COUNSELLOR_PROFILE)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo> getMyInfo(@HeaderMap Map<String, String> header);


    /**
     * 版本检测
     */
    @GET()
    Observable<ResponseInfo> checkVersion(@HeaderMap Map<String, String> header, @Url() String url);

    /**
     * 用户注销
     */
    @POST(HttpUrlUtils.URL_USER_LOGOUT)
    Observable<ResponseInfo> getLogOut(@HeaderMap Map<String, String> header);

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
    Observable<ResponseInfo> getQuestions(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);


    /**
     * 点赞列表
     */
    @GET(HttpUrlUtils.URL_ADD_GOODS_LIST)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo> getAddGoodDetail(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);


    /**
     * 我关注问题列表
     */
    @GET(HttpUrlUtils.URL_QUESTS_MARK)
    Observable<ResponseInfo> getMyFocusQuestions(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);


    /**
     * 获取我关注的学生的标签
     */
    @GET()
    Observable<ResponseInfo> getMyStudentTag(@HeaderMap Map<String, String> header, @Url() String url);


    /**
     * 提交标签
     */
    @PUT()
    Observable<ResponseInfo> submitMyStudentTag(@HeaderMap Map<String, String> header, @Url() String url, @QueryMap Map<String, String> params);


    /**
     * 提交历史标签
     */
    @POST(HttpUrlUtils.URL_QUESTS_HISTORY_TAGS)
    Observable<ResponseInfo> postHistoryTag(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);


    /**
     * 获取历史标签
     */
    @GET(HttpUrlUtils.URL_QUESTS_HISTORY_TAGS)
    @Headers(ConstantUtils.REQUEST_CACHE_TYPE_HEAD + ":" + ParameterUtils.NETWORK_ELSE_CACHED)
    Observable<ResponseInfo> getHisToryTag(@HeaderMap Map<String, String> header);

    /**
     * 问答详情
     *
     * @param header
     * @param url
     * @param params
     * @return
     */
    @GET()
    Observable<ResponseInfo> getQaDetail(@HeaderMap Map<String, String> header, @Url() String url, @QueryMap Map<String, String> params);


    /**
     * 文本消息提交
     *
     * @param header
     * @param params
     * @return
     */
    @POST()
    Observable<ResponseInfo> postQuestion(@HeaderMap Map<String, String> header, @Url() String url, @QueryMap Map<String, String> params);

    /**
     * 重点关注
     *
     * @param header
     * @param url
     * @return
     */
    @POST()
    Observable<ResponseInfo> questionAddMark(@HeaderMap Map<String, String> header, @Url() String url);


    /**
     * 取消重点关注
     *
     * @param header
     * @param url
     * @return
     */
    @DELETE()
    Observable<ResponseInfo> questionDeleteMark(@HeaderMap Map<String, String> header, @Url() String url);


    @POST()
    Observable<ResponseInfo> postAnswerVoice(@HeaderMap Map<String, String> header, @Url() String url, @Body RequestBody Body);

    /**
     * 用户注销
     */
    @GET(HttpUrlUtils.URL_MY_ANSWER)
    Observable<ResponseInfo> getMyQuestion(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);

    /**
     * 用户注销
     */
    @GET(HttpUrlUtils.URL_START_AD)
    Observable<ResponseInfo> getAdInfo(@HeaderMap Map<String, String> header);

}
