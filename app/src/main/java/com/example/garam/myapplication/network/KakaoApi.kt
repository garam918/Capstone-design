package com.example.garam.myapplication.network

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class KakaoApi:Application() {
    val base = "http://dapi.kakao.com"
    val key = "KakaoAK 0feaf684a8da54357cb0ee5c9f341483"
    lateinit var networkService: NetworkService

    companion object{
        var instance : KakaoApi = KakaoApi()
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        buildNetWork()
    }
    fun buildNetWork(){
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(base).addConverterFactory(
            GsonConverterFactory.create()).build()
        networkService = retrofit.create(NetworkService::class.java)
    }
}