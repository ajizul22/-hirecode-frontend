package com.example.hirecodeandroid.project

import retrofit2.http.GET

interface ProjectApiService {

    @GET("project/2")
    suspend fun getAllProject(): ProjectResponse
}