package com.akarinti.sapoe.data.base

import retrofit2.Retrofit

class RetrofitHelper(){

    companion object {
        var retrofit:Retrofit ?= null

        fun init(retrofit: Retrofit){
            RetrofitHelper.retrofit = retrofit
        }
    }
}