package com.smartstudy.counselor_t.api;

import com.smartstudy.counselor_t.entity.ResponseInfo;
import com.smartstudy.counselor_t.util.HttpUrlUtils;

import java.io.File;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    Observable<ResponseInfo> getPhoneCode(@Query("phone") String phone);

    /**
     * 验证码登录
     *
     * @param params
     * @return
     */
    @POST(HttpUrlUtils.URL_CODE_LOGIN)
    Observable<ResponseInfo> phoneCodeLogin(@QueryMap Map<String, String> params);


    /**
     * 获取学生信息
     *
     * @param
     * @return
     */
    @GET(HttpUrlUtils.URL_STUDENT_INFO)
    Observable<ResponseInfo> getStudentInfo(@QueryMap Map<String, String> params);

    /**
     * 获取学生主页详情
     *
     * @param params
     * @return
     */
    @GET(HttpUrlUtils.URL_STUDENT_DETAIL_INFO)
    Observable<ResponseInfo> getStudentDetailInfo(@QueryMap Map<String, String> params);

    /**
     * 重新获取token
     *
     * @return
     */
    @POST(HttpUrlUtils.URL_REFRESH_TOKEN)
    Observable<ResponseInfo> refreshToken();


    /**
     * 获取学生主页详情
     *
     * @param params
     * @return
     */
    @POST()
    Observable<ResponseInfo> postPersonInfo(@Url() String url, @Body RequestBody Body);
}
