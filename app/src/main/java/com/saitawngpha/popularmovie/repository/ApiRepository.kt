package com.saitawngpha.popularmovie.repository

import com.saitawngpha.popularmovie.api.ApiServices
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

/**
 * @Author: ၸၢႆးတွင်ႉၾႃႉ
 * @Date: 12/19/22
 */

@ActivityScoped
class ApiRepository @Inject constructor(private  val apiServices: ApiServices) {

    fun getPopularMoviesList(page: Int) = apiServices.getPopularMoviesList(page)

    fun getMovieDetails(id: Int) = apiServices.getMovieDetails(id)
}