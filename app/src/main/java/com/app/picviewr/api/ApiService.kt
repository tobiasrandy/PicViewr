package com.app.picviewr.api

import Photo
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {

    companion object {
        private const val DOMAIN_PHOTO = "https://jsonplaceholder.typicode.com"

        fun create(): ApiService{
            val okHttpClientBuilder = OkHttpClient.Builder()

            var interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpClientBuilder.addInterceptor(interceptor)

            val authInterceptor = Interceptor { chain ->
                val originalRequest: Request = chain.request()
                val newRequest: Request = originalRequest.newBuilder()
                    .header("accept", "application/json")
                    .build()
                chain.proceed(newRequest)
            }
            okHttpClientBuilder.addInterceptor(authInterceptor)

            val client = okHttpClientBuilder
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(DOMAIN_PHOTO)
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }

    @GET("/photos")
    fun getPhotoList(@Query("_page") page: Int, @Query("_limit") limit: Int): Observable<List<Photo>>
}