package com.akarinti.sapoe.extension

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import com.akarinti.sapoe.utils.LocationUtils
import io.vrinda.kotlinpermissions.DeviceInfo


/**
 * Created by ridwan on 03/03/2018.
 */
fun externalIntent(uri: Uri, type: String): Intent {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(uri, type)
    return intent
}

fun Context.isIntentAvailable(intent: Intent): Boolean {
    val info = packageManager.queryIntentActivities(intent, 0)
    return info.size > 0
}

fun Context.getColorString(color: Int): String {
    return "#" + String.format("%06X", ResourcesCompat.getColor(resources, color, null) and 0x00ffffff)
}

fun Context.isNetworkAvailable(): Boolean {
    return ((getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo?.isConnected == true) && this.isAirplaneInactive()
//    val runtime = Runtime.getRuntime()
//    try {
//        val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
//        val exitValue = ipProcess.waitFor()
//        return exitValue == 0
//    } catch (e: IOException) {
//        e.printStackTrace()
//    } catch (e: InterruptedException) {
//        e.printStackTrace()
//    }
//    return false
}

fun Context.isAirplaneInactive(): Boolean {
    return Settings.Global.getInt(this.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) == 0
}

fun Context.checkPermissions(permission: String): Boolean {
    return (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED)
}

fun Context.getLat(): Double {
    return DeviceInfo.getLatLong(this)[LocationUtils.LATITUDE_INDEX]
}

fun Context.getLong(): Double {
    return DeviceInfo.getLatLong(this)[LocationUtils.LONGTITUDE_INDEX]
}

fun Context.openMap(lat: Double, long: Double) {
    val uri = "http://maps.google.com/maps?q=loc:$lat,$long"
    this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
}