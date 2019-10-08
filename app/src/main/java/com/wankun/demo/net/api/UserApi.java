/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.net.api;

import com.wankun.demo.model.httpResponse.UpdateVersion;
import com.wankun.demo.net.HttpResult;
import com.wankun.demo.model.UserInfo;
import com.wankun.demo.model.httpResponse.UploadFiles;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 〈具体网络请求〉
 * <p>
 * * <p>
 * * *@Query、@QueryMap：用于Http Get请求传递参数
 * * *@Field：用于Post方式传递参数,需要在请求接口方法上添加@FormUrlEncoded,即以表单的方式传递参数
 * * *@Body：用于Post,根据转换方式将实例对象转化为对应字符串传递参数.比如Retrofit添加GsonConverterFactory则是将body转化为gson字符串进行传递
 * * *@Path：用于URL上占位符
 * * *@Part：配合@Multipart使用,一般用于文件上传
 * * *@Header：添加http header
 * * *@Headers：跟@Header作用一样,只是使用方式不一样,@Header是作为请求方法的参数传入,@Headers是以固定方式直接添加到请求方法上
 * *
 *
 * @author wankun
 * @create 2019/5/10
 * @since 1.0.0
 */
public interface UserApi {

    static final public String TYP_ANDROID = "1";

    /**
     * 检查更新
     *
     * @param  type  1  android   2 ios
     */
    @POST("getConfigs")
    @FormUrlEncoded
    Observable<HttpResult<UpdateVersion>> getApkVersion(@Field("type") String type);





    // ===================================================================================用户相关

    /**
     * 登录
     *
     * @param username 账号
     * @param password 密码
     * @return
     */
    @POST("getUser")
    @FormUrlEncoded
    Observable<HttpResult<UserInfo>> login(
            @Field("username") String username,
            @Field("password") String password);

    /**
     * 上传文件
     */
    @Multipart
    @POST("uploadFile")
    Observable<HttpResult<UploadFiles>> uploadImage(
            @Part("uid") RequestBody uid,
            @Part("auth_key") RequestBody authKey,
            @Part MultipartBody.Part file);


    /**
     * 上传文件*
     *
     */
    @Multipart
    @POST("/upload/MoreImages")
    Observable<HttpResult<UploadFiles>> uploadFiles(
            @Part("uid") RequestBody uid,
            @Part("auth_key") RequestBody authKey,
            @Part MultipartBody.Part[] parts);

}