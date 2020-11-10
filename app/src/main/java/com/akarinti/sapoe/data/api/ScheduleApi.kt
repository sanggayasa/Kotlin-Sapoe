package com.akarinti.sapoe.data.api

import com.akarinti.sapoe.data.body.ChangeStatusBody
import com.akarinti.sapoe.data.body.LogOrderBody
import com.akarinti.sapoe.data.body.SaveAnswerBody
import com.akarinti.sapoe.data.body.SuhuBody
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ScheduleApi {

    @GET("/schedule/agent")
    fun getListSchedule(
        @Query("type") type: String,
        @Query("status") status: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ) : Observable<Response<ResponseBody>>

    @GET("/schedule/agent")
    fun getListScheduleDistance(
        @Query("type") type: String,
        @Query("status") status: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ) : Observable<Response<ResponseBody>>

    @GET("/questionnaire/order/{orderID}")
    fun getQuestionnaire(@Path("orderID") orderID: String,
                        @Query("category") category: String,
                        @Query("itemId") itemId: String? = "",
                        @Query("formId") formId: String? = "") : Observable<Response<ResponseBody>>

    @GET("/order/{orderID}")
    fun getQuestionnaireOffline(@Path("orderID") orderID: String,
                                @Query("category") category: String,
                                @Query("latitude") latitude: Double,
                                @Query("longitude") longitude: Double
    ) : Observable<Response<ResponseBody>>

    @GET("/order/{orderID}")
    fun getAnswer(@Path("orderID") orderID: String,
                  @Query("category") category: String
    ) : Observable<Response<ResponseBody>>

    @POST("/questionnaire/submittemperature")
    fun createSuhu(@Body suhuBody: SuhuBody): Observable<Response<ResponseBody>>

    @GET("/questionnaire/question/{questionID}")
    fun getQuestionDetail(@Path("questionID") questionID: String) : Observable<Response<ResponseBody>>

    @PUT("/order/changestatus")
    fun changeScheduleStatusValidated(@Body body: ChangeStatusBody,
                                      @Query("latitude") latitude: Double,
                                      @Query("longitude") longitude: Double): Observable<Response<ResponseBody>>

    @POST("/questionnaire/submitanswer")
    fun submitAnswer(@Body body: SaveAnswerBody): Observable<Response<ResponseBody>>

    @POST("/history/submit")
    fun submitHistory(@Body input: LogOrderBody): Observable<Response<ResponseBody>>
}