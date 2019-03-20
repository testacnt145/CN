package com.chattynotes.swipe.rfit;

import com.chattynotes.constant.keyboard.WSCKeys;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface API {

    //Useful Documentation
    //https://github.com/codepath/android_guides/wiki/Consuming-APIs-with-Retrofit
    //https://futurestud.io/tutorials/retrofit-2-cancel-requests
    //old 2.0 tutorial http://www.vogella.com/tutorials/Retrofit/article.html

//__________________________________________________________________________________________________
    @FormUrlEncoded
    @POST("/apps/cn/api/v1/a/a.php")
    Call<String> androidVersionSupport(@Field(WSCKeys.KEY) String value);
    //response
    /*
    {
        "d":37023,
        "o":37023,
        "s":37023
    }
    */
    //0


    @GET
    Call<String> crawlLink(@Url String url);

    @GET
    Call<String> fourSquareLocation(@Url String url);

}
