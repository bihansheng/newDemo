/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.net.api;

import com.wankun.demo.model.httpResponse.BaseGankEntity;
import com.wankun.demo.model.httpResponse.GankDataEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 〈干货网站提供的文章〉
 *
 * @author wankun
 * @create 2019/8/7
 * @since 1.0.0
 */
public interface GankApi {

    //    分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
//
//    数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
//    请求个数： 数字，大于0
//    第几页：数字，大于0

    @GET("data/{type}/20/{page}")
    Observable<BaseGankEntity<List<GankDataEntity>>> getGankList(@Path("type") String type, @Path("page") int page);


    //    每日数据： http://gank.io/api/day/年/月/日
//
//    例：
//    http://gank.io/api/day/2015/08/06


//    随机数据：http://gank.io/api/random/data/分类/个数
//
//    数据类型：福利 | Android | iOS | 休息视频 | 拓展资源 | 前端
//    个数： 数字，大于0
//    例：
//    http://gank.io/api/random/data/Android/20
}