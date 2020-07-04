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

object ApiProviderForApplicationJson {

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
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                                .addHeader("Authorization", AppPreference.loadAccessToken(context)!!)
//                                .addHeader("Content-Type", "application/json")
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