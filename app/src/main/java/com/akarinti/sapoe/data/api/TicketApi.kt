package com.akarinti.sapoe.data.api

import com.akarinti.sapoe.data.body.*
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


interface TicketApi {

    @GET("/ticket-list")
    fun getTicketAll(
        @Query("q") q: String,
        @Query("category") category: String,
        @Query("status") status: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("orderId") orderId: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ) : Observable<Response<ResponseBody>>

    @GET("/ticket-list/me")
    fun getTicketMe(
        @Query("category") category: String,
        @Query("status") status: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("orderId") orderId: String,
        @Query("orderType") orderType: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Observable<Response<ResponseBody>>

    @GET("/ticket-type")
    fun getTicketType(): Observable<Response<ResponseBody>>

    @GET("/ticket-sla")
    fun getTicketSla(@Query("type") type: String,
                     @Query("q") q: String): Observable<Response<ResponseBody>>

    @GET("/location-component/{locationId}")
    fun getTicketLocation(@Path("locationId") locationId: String): Observable<Response<ResponseBody>>

    @POST("/ticket-create")
    fun createTicket(@Body ticketCreateBody: TicketCreateBody,
                     @Query("latitude") latitude: Double,
                     @Query("longitude") longitude: Double): Observable<Response<ResponseBody>>

    @PUT("/ticket-change-status/")
    fun changeTicketStatus(@Body body: ChangeStatusBody): Observable<Response<ResponseBody>>

    @GET("/sparepart-list/")
    fun getSpareparts(@Query("categoryId") categoryId: String,
                      @Query("q") q: String,
                      @Query("cityId") cityID: String): Observable<Response<ResponseBody>>

    @GET("/sparepart-category/")
    fun getSparepartsCategory(): Observable<Response<ResponseBody>>

    @PUT("/update-ticket/{ticketId}")
    fun updateTicketStatus(@Path("ticketId") ticketId: String,
                           @Query("latitude") latitude: Double,
                           @Query("longitude") longitude: Double,
                           @Body body: UpdateTicketBody): Observable<Response<ResponseBody>>

    @POST("/ticket-sparepart/{ticketId}")
    fun updateSpareparts(@Path("ticketId") ticketId: String,
                           @Body body: SparepartsBody): Observable<Response<ResponseBody>>

    @GET("/ticket-detail/{ticketID}")
    fun getTicketDetail(@Path("ticketID") ticketID: String) : Observable<Response<ResponseBody>>

    @PUT("/update-ticket/{ticketID}")
    fun takeTicket(@Path("ticketID") ticketID: String,
                   @Query("latitude") latitude: Double,
                   @Query("longitude") longitude: Double,
                   @Body ticketAgentBody: TicketAgentBody) : Observable<Response<ResponseBody>>

    @DELETE("/remove-sparepart/{sparepartID}")
    fun deleteSparepart(@Path("sparepartID") sparepartID: String) : Observable<Response<ResponseBody>>
}