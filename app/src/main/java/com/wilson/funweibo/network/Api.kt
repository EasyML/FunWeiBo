package com.wilson.funweibo.network

import com.wilson.funweibo.BuildConfig
import com.wilson.funweibo.base.Constant.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 *
 * @Author： chezhou
 * @Create： $date$
 * @Description：
 *
 */
enum class Api {

    INSTANCE;

    private val okhttp = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE)).build()

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttp)
            .build()

    val service = retrofit.create(ApiService::class.java)
}
