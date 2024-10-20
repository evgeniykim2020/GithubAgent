package ru.evgeniykim.githubagent.network.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import ru.evgeniykim.githubagent.model.userrepo.UserRepoModel

interface GitHubApi {
    @GET("users/{username}/repos")
    suspend fun getUserRepo(@Path("username") name: String): Response<UserRepoModel>
}