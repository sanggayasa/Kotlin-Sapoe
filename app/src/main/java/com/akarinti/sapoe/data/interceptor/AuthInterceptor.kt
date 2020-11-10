package com.akarinti.sapoe.data.interceptor

import android.content.Context
import com.akarinti.sapoe.data.entity.TokenEntity
import com.akarinti.sapoe.data.exception.TokenInvalidException
import com.akarinti.sapoe.data.header.HeaderManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(val headerManager: HeaderManager, val tokenEntity: TokenEntity,
                                          val context: Context):Interceptor{

    val AUTHORIZATION = "Authorization"

    override fun intercept(chain: Interceptor.Chain): Response {
        val oriResponse = chain.request()
        if (headerManager.hasToken()) {
            var response = chain.proceed(addHeaderAuth(oriResponse))

//            if (response.code() != 401) {
                return response
//            } else {
//                newRefreshToken()
//                response = chain.proceed(addHeaderAuth(oriResponse))
//                doWhenForbidden(response)
//            }
            return response
        } else {
            newRequestToken()
            return chain.proceed(addHeaderAuth(oriResponse))
        }
    }

    // TODO: Handle refresh token
    @Throws(IOException::class)
    fun newRefreshToken() {
        synchronized(headerManager) {
            val responseToken =
//                if(headerManager.isLoggedIn()) {
//                    tokenEntity.callRefreshToken().execute()
//                } else {
                    tokenEntity.callRequestNewToken().execute()
//                }
            if (responseToken != null) {
                headerManager.accessToken = responseToken.body()?.data
            }
        }

    }

    @Throws(IOException::class)
    fun newRequestToken(){
        val responseNew = tokenEntity.callRequestNewToken().execute()
        headerManager.authToken = responseNew.body()?.data
    }

    @Throws(TokenInvalidException::class)
    private fun doWhenForbidden(response: Response) {
        val code = response.code()
        if (code == HttpURLConnection.HTTP_FORBIDDEN) {
            throw TokenInvalidException(context)
        } else if (code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            if (headerManager.isLoggedIn()) headerManager.logout()
            else headerManager.clearToken()
        }
    }

    private fun addHeaderAuth(oriRequest: Request): Request {
        return oriRequest.newBuilder()
            .addHeader(AUTHORIZATION,headerManager.getBearerToken())
            .build()
    }

}