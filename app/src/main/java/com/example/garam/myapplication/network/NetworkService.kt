package com.example.garam.myapplication.network

import com.example.garam.myapplication.post.PostTest
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface NetworkService{

    @POST("/count")
    fun menuCount(
       @Body body : JsonObject
    ): Call<JsonObject>

    @POST("/freefood/insertfood")
    fun foodTest(
        @Body body: JsonObject
    ): Call<JsonObject>

    @POST("/manager/signUp/")
    fun manager(
        @Body body: JsonObject
    ): Call<JsonObject>

    @Multipart
    @POST("/login/FVlogin")
    fun imageLogin(
        @Part imagefile : MultipartBody.Part?
    ): Call<JsonObject>

    @Multipart
    @POST("/login/FVlogin")
    fun audioLogin(
        @Part audiofile : MultipartBody.Part?
    ): Call<JsonObject>


    @Multipart
    @POST("/freefood/foodcount")
    fun imageTest2(
        @Part imagefile : MultipartBody.Part?,
        @Part ("name") json: RequestBody
    ): Call<String>

    @Multipart
    @POST("/signup")
    fun imageTest4(
        @Part ("json") json: RequestBody,
        @Part imagefile : MultipartBody.Part?,
        @Part imagefile2 : MultipartBody.Part?,
        @Part audiofile : MultipartBody.Part?,
        @Part("qr") imgFile : RequestBody,
        @Part("face") imgFile2 : RequestBody,
        @Part("audio") audio: RequestBody
    ): Call<ResponseBody>

    @GET("/v2/local/geo/coord2address.json")
    fun address(
        @Header("Authorization") key: String,
        @Query("x") longitude: Double,
        @Query("y") latitude: Double
    ): Call<JsonObject>

    @GET("/v2/local/search/keyword.json")
    fun keywordjob(
        @Header("Authorization") key: String,
        @Query("query") query: String,
        @Query("x") longitude: Double,
        @Query("y") latitude: Double,
        @Query("radius") radius: Int,
        @Query("sort") sort : String,
        @Query("page") page : Int
    ): Call<JsonObject>

    @GET("/v2/local/search/keyword.json")
    fun keyword(
        @Header("Authorization") key: String,
        @Query("query") query: String,
        @Query("x") longitude: Double,
        @Query("y") latitude: Double,
        @Query("sort") sort: String
    ): Call<JsonObject>

    @GET("/freefood/nowcount/")
    fun food(
        @Query("name") name : String
    ) : Call<String>

    @POST("/manager/login")
    fun login(
        @Body loginInfo : JsonObject
    ) : Call<JsonObject>

    @Multipart
    @POST("/foodimg")
    fun fda(
        @Part ("name") json: RequestBody,
        @Part imagefile: MultipartBody.Part?
    ): Call<String>

}
