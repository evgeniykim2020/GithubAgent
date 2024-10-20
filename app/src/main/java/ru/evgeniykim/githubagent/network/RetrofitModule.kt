package ru.evgeniykim.githubagent.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.evgeniykim.githubagent.network.api.GitHubApi

object RetrofitModule {

    private const val BASE_URL = "https://api.github.com/"
    private val httpClient = OkHttpClient()

    private fun getRetrofit(): Retrofit {
        httpClient
            .newBuilder()
            .addInterceptor(MyInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: GitHubApi = getRetrofit().create(GitHubApi::class.java)
}