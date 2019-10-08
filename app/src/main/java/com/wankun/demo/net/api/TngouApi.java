package com.wankun.demo.net.api;

import com.wankun.demo.model.httpResponse.SuperGalleryResponse;
import com.wankun.demo.model.httpResponse.SuperImageResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TngouApi {

    /**
     * http://image.so.com/zj?ch=beauty&t1=596&sn=30&listtype=new&temp=1
     * 取得 图库 列表，也可以用分类id作为参数
     */
    @GET("zj?ch=beauty&listtype=new&temp=1")
    Observable<SuperGalleryResponse> getGalleryList(@Query("t1") int menu, @Query("sn") int count);

    /**
     * 取得 图库 列表，也可以用分类id作为参数
     */
    @GET("zj?src=srp#&listtype=new&temp=1")
    Observable<SuperGalleryResponse> getGalleryListByName(@Query("q") String name, @Query("sn") int count);




    /**
     * http://image.so.com/zvj?ch=beauty&t1=596&id=77455f9644cda030c8e2a3a6135c0ca9
     * 取得热点图片详情，通过热点id取得该对应详细内容信息
     */
    @GET("zvj?ch=beauty")
    Observable<SuperImageResponse> getGalleryInfo(@Query("t1") int menu, @Query("id") String id);


    /**
     * http://image.so.com/zj?ch=pet&t1=596&sn=30&listtype=new&temp=1
     * 取得 图库 列表，也可以用分类id作为参数
     */
    @GET("zj?ch=pet&listtype=new&temp=1")
    Observable<SuperGalleryResponse> getPetList(@Query("t1") int menu, @Query("sn") int count);

}

