package com.nku.moviessearchclient.service

import com.nku.moviessearchclient.data.Movie
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException
import java.util.concurrent.TimeUnit

class MoviesAPI {

    private val protocol: String = "https"
    private val host: String = "<Host Domain>"
    private val client = OkHttpClient()
        .newBuilder()
        .connectTimeout(30, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    fun searchMovies(searchTerm: String): List<Movie> {

        val endpoint = "api/movies/"
        val dynamoEndpoint = "api/dynamodb/movies/"
        val request = Request.Builder()
            .url(String.format(
                "%s://%s/%s?genre=comedy",
                protocol,
                host,
                dynamoEndpoint,
                searchTerm
            ))
            .header(
             "Authorization",
             "Token <Auth Token>"
            )
            .build()

        this.client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            for ((name, value) in response.headers) {
                println("$name: $value")
            }

            println(response.body!!.string())
        }
        return listOf()
    }
}