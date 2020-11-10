package com.akarinti.sapoe.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.core.app.ActivityCompat

class LocationUtils {

    companion object {
        const val LATITUDE_INDEX = 0
        const val LONGTITUDE_INDEX = 1
        const val LOCATION_RADIUS_CONST: Double = 1.0

        fun checkLocationPermission(context: Activity, resultCode: Int): Boolean {
            return checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION, resultCode)
        }

        fun getMyLocation(context: Context, locationListener: LocationListener): DoubleArray {
            val doubleArray = DoubleArray(2)
            val locationManager: LocationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            val gpsNotAllowed = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED

            if (!gpsNotAllowed) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    2000,
                    10f,
                    locationListener
                )
                val myLocation =
                    locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
                if (myLocation != null) {
                    doubleArray[LONGTITUDE_INDEX] = myLocation.longitude
                    doubleArray[LATITUDE_INDEX] = myLocation.latitude
                    return doubleArray
                }
            }
            doubleArray[LONGTITUDE_INDEX] = 0.0
            doubleArray[LATITUDE_INDEX] = 0.0
            return doubleArray
        }

        fun checkPermission(activity: Activity, permission: String, resultCode: Int): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val allow = activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
                if (!allow) {
                    activity.requestPermissions(arrayOf(permission), resultCode)
                }
                allow
            } else { true }
        }
    }
}