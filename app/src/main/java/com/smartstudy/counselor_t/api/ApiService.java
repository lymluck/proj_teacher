package com.smartstudy.counselor_t.api;

import com.smartstudy.counselor_t.entity.ResponseInfo;
import com.smartstudy.counselor_t.util.HttpUrlUtils;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by louis on 2017/12/27.
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
    Observable<ResponseInfo> getStudentDetailInfo(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> params);

    /**
     * 获取学生信息
     *
     * @param id
     * @return
     */
    @GET(HttpUrlUtils.URL_STUDENT_DETAIL_INFO)
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
    Observable<ResponseInfo> getAuditResult(@HeaderMap Map<String, String> header);


    @GET(HttpUrlUtils.URL_COUNSELLOR_PROFILE)
    Observable<ResponseInfo> getMyInfo(@HeaderMap Map<String, String> header);


    /**
     *
     */

    @POST(HttpUrlUtils.URL_USER_LOGOUT)
    Observable<ResponseInfo> getLogOut(@HeaderMap Map<String, String> header);
}
