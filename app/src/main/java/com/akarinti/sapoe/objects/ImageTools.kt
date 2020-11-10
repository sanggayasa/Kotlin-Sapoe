package com.akarinti.sapoe.objects

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.Base64

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

object ImageTools {

    private val MAX_QUALITY = 80

    fun pathToBitmap(path: String): Bitmap? {
        try {
            val imagefile = File(path)
            return BitmapFactory.decodeStream(FileInputStream(imagefile))
        } catch (e: FileNotFoundException) {
            //e.printStackTrace();
        }
        return null
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArray = bitmapToByte(bitmap)
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun bitmapToByte(bitmap: Bitmap?, imageQuality: Int = MAX_QUALITY): ByteArray? {
        if(null == bitmap) return null
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, imageQuality, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    fun compress(bitmap: Bitmap?, ImageQuality: Int): Bitmap? {
        if(bitmap == null) return null
        val byteArray = bitmapToByte(bitmap, ImageQuality) ?: return null
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    fun getResizedBitmap(image: Bitmap?, maxImageSize: Int): Bitmap? {
        if(image == null) return null
        var width = image.width
        var height = image.height

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxImageSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxImageSize
            width = (height * bitmapRatio).toInt()
        }

        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    fun convert(bitmap: Bitmap?, config: Bitmap.Config): Bitmap? {
        if(bitmap == null) return null
        val convertedBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, config)
        val c = Canvas()
        c.setBitmap(convertedBitmap)
        val p = Paint()
        p.isFilterBitmap = true
        c.drawBitmap(bitmap, 0f, 0f, p)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            bitmap.recycle()
        return convertedBitmap
    }

    fun getFixedFileSizeBitmap(pathUri: String, maxImageSize: Int, maxFileSize: Int): Bitmap? {
        var maxImageSized = maxImageSize
        var fixedFileSizeBitmap = pathToBitmap(pathUri)
        fixedFileSizeBitmap = compress(fixedFileSizeBitmap, 50)
        fixedFileSizeBitmap =
            convert(fixedFileSizeBitmap, Bitmap.Config.RGB_565)
        do {
            fixedFileSizeBitmap =
                getResizedBitmap(fixedFileSizeBitmap, maxImageSized)
            maxImageSized = (maxImageSize - maxImageSize * 0.1).toInt()
        } while (getImageFileSize(fixedFileSizeBitmap) > maxFileSize)
        return fixedFileSizeBitmap
    }

    fun getImageFileSize(bitmap: Bitmap?): Int {
        if(bitmap == null) return 0
        val fileSize = ((bitmapToByte(
            bitmap,
            MAX_QUALITY
        )?.size?:1024) / 1024).toLong()
        return fileSize.toInt()
    }

}
