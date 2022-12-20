package com.saitawngpha.popularmovie.api

import com.saitawngpha.popularmovie.response.MovieDetailsResponse
import com.saitawngpha.popularmovie.response.MoviesListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @Author: ၸၢႆးတွင်ႉၾႃႉ
 * @Date: 12/19/22
 */
interface ApiServices {
    @GET("movie/popular")
    fun getPopularMoviesList(@Query("page") page: Int) : Call<MoviesListResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") movieId : Int) : Call<MovieDetailsResponse>
}