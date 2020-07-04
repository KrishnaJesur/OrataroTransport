package com.edusunsoft.transport.orataro.network

import com.edusunsoft.transport.orataro.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.simpleframework.xml.convert.AnnotationStrategy
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitAdapter {
    private val strategy = AnnotationStrategy()
    //    private val serializer = Persister(strategy)
    private val okHttpClient = OkHttpClient.Builder()
    private val retrofitBuilder = Retrofit.Builder()
//            .addConverterFactory(SimpleXmlConverterFactory.create(serializer))
//            .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(Persister(AnnotationStrategy())))
            .addConverterFactory(GsonConverterFactory.create())

    private fun <S> createService(serviceClass: Class<S>, baseUrl: String): S {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = okHttpClient
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .addInterceptor(interceptor)
                .build()

        val retrofit = retrofitBuilder
                .baseUrl(baseUrl)
                .client(client)
                .build()

        return retrofit.create(serviceClass)
    }

    private var orataroRetrofitService: RetrofitService? = null

    fun getOrataroCommonApi(): RetrofitService {
        if (orataroRetrofitService == null) {
            orataroRetrofitService = createService(RetrofitService::class.java, BuildConfig.SERVER_URL)
        }
        return orataroRetrofitService as RetrofitService
    }
}