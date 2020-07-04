package com.edusunsoft.transport.orataro.network

import android.content.Context
import com.edusunsoft.transport.orataro.BuildConfig
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.utils.AppPreference
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiProvider {

    var gson: Gson = GsonBuilder()
            .setLenient()
            .create()

    fun provideApi(context: Context): RetrofitService = Retrofit.Builder()
            .baseUrl(BuildConfig.APP_URL)
            .client(provideOkHttpClient(provideLoggingInterceptor(), context))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(RetrofitService::class.java)

    private fun provideOkHttpClient(
            interceptor: HttpLoggingInterceptor,
            context: Context
    ): OkHttpClient =
            OkHttpClient().newBuilder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                                .addHeader("access-token", AppPreference.loadAccessToken(context)!!)
                                .addHeader(context.resources.getString(R.string.authorization), AppPreference.loadAccessToken(context)!!)
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .build()
                        chain.proceed(request)
                    }
                    .addInterceptor(interceptor)
                    .build()

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor =
            HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG)
                    HttpLoggingInterceptor.Level.BODY
                else
                    HttpLoggingInterceptor.Level.NONE
            }
    
}