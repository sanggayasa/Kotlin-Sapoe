package com.akarinti.sapoe.data.api

import com.akarinti.sapoe.data.body.SaveAnswerBody
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface SaveApi {

    @POST("/questionnaire/submitanswer")
    fun saveAnswer(@Body saveAnswerBody: SaveAnswerBody): Observable<Response<ResponseBody>>
}