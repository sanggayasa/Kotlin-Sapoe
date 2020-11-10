package com.akarinti.sapoe.data.exception


import android.content.Context

import java.io.IOException

/**
 * Created by mexanjuadha on 3/9/17.
 */

class TokenInvalidException(private val context: Context) : IOException("Invalid access token.")