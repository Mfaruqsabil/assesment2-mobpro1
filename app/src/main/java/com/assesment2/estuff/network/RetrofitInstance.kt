package com.assesment2.estuff.network

import com.assesment2.estuff.model.AboutApp
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl("https://raw.githubusercontent.com/Mfaruqsabil/assesment3/main/")
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

object RetrofitInstance {
    val service : SimpleApiInterface by lazy {
        retrofit.create(SimpleApiInterface::class.java)
    }
}
interface SimpleApiInterface {
    @GET("estuff.json")
    suspend fun getCopyRightText(): AboutApp
}
enum class LoadingIndicator{
    LOADING,SUCCESS,FAILED
}