package com.example.flickrsearch

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface FlickrApi {
    @GET("services/feeds/photos_public.gne?format=json&nojsoncallback=1")
    suspend fun searchImages(@Query("tags") tags: String): FlickrResponse

    companion object {
        fun create(): FlickrApi {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.flickr.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(FlickrApi::class.java)
        }
    }
}
