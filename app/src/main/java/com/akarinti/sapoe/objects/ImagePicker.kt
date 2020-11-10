package com.akarinti.sapoe.objects

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
import android.util.Base64
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import com.akarinti.sapoe.BuildConfig
import com.akarinti.sapoe.R
import java.io.*
import java.util.*


object ImagePicker {

    private val DEFAULT_MIN_WIDTH_QUALITY = 400

    val TEMP_IMAGE_NAME = "tempImage"

    private val IMAGE_TYPE = "image/*"

    @Throws(NullPointerException::class)
    fun getPickImageIntent(
        context: Context, isReadStorage: Boolean,
        isCamera: Boolean
    ): Intent? {
        var chooserIntent: Intent? = null

        var intentList: MutableList<Intent> = ArrayList()

      /*  if (isReadStorage) {
            val pickGalleryIntent = createPickGalleryIntent()
            intentList =
                addIntentsToList(context, intentList, pickGalleryIntent)
        }*/

        if (isCamera) {
            intentList = addIntentsToList(
                context,
                intentList,
                createTakePhotoIntent(context)
            )
        }

        if (intentList.size > 0) {
            chooserIntent = Intent.createChooser(
                intentList.removeAt(intentList.size - 1),
                context.getString(R.string.image_after)
            )
            chooserIntent?.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toTypedArray<Parcelable>())
        }

        return chooserIntent
    }

    fun getCaptureImageIntent(context: Context, isCamera: Boolean): Intent? {
        return getPickImageIntent(context, false, isCamera)
    }

    fun createTakePhotoIntent(context: Context): Intent {
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePhotoIntent.putExtra("return-data", true)
        takePhotoIntent.putExtra(
            MediaStore.EXTRA_OUTPUT,
            FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider",
                getTempFile(context)
            )
        )
        //            Uri.fromFile(getTempFile(context)));
        return takePhotoIntent
    }

    private fun createPickGalleryIntent(): Intent {
        val pickGalleryIntent = Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI)
        pickGalleryIntent.type = IMAGE_TYPE
        return pickGalleryIntent
    }

    private fun addIntentsToList(
        context: Context, list: MutableList<Intent>,
        intent: Intent
    ): MutableList<Intent> {
        val resInfo = context.packageManager.queryIntentActivities(intent, 0)
        for (resolveInfo in resInfo) {
            val packageName = resolveInfo.activityInfo.packageName
            val targetedIntent = Intent(intent)
            targetedIntent.setPackage(packageName)
            list.add(targetedIntent)
        }
        return list
    }

    fun saveSelectedImage(source: String, destination: String, context: Context): Boolean {
        var inputStream: FileInputStream? = null
        var outputStream: FileOutputStream? = null
        try {
            outputStream = context.openFileOutput(destination, Context.MODE_PRIVATE)
            inputStream = context.openFileInput(source)

            val buf = ByteArray(1024)
            var len: Int
            do {
                len = inputStream!!.read(buf)
                if(len > 0) outputStream!!.write(buf, 0, len)
            } while (len > 0)

        } catch (e: Exception) {
            //e.printStackTrace();
            return false
        } finally {
            if (inputStream != null && outputStream != null) {
                try {
                    inputStream.close()
                    outputStream.close()
                } catch (e: IOException) {
                    //e.printStackTrace();
                }

            }
        }
        return true
    }

    fun getImagePath(context: Context, imageName: String): String {
        return context.filesDir.absolutePath + "/" + imageName
    }

    private fun getTempFile(context: Context): File {
        val imageFile: File
        if (hasExternalStorage()) {
            imageFile = File(context.externalCacheDir, TEMP_IMAGE_NAME)
        } else {
            imageFile = context.filesDir
        }
        imageFile.parentFile.mkdirs()
        return imageFile
    }

    private fun hasExternalStorage(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    @Throws(IOException::class)
    private fun decodeBitmap(
        context: Context, theUri: Uri,
        sampleSize: Int? = null
    ): Bitmap? {
        var inputStream = context.contentResolver.openInputStream(theUri)
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(inputStream, null, options)
        if(null != sampleSize) options.inSampleSize = sampleSize
        inputStream!!.close()
        inputStream = context.contentResolver.openInputStream(theUri)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeStream(inputStream, null, options)
    }

    @Throws(IOException::class)
    private fun getImageResized(context: Context, selectedImage: Uri): Bitmap {
        var bm: Bitmap? = null
        val sampleSizes = intArrayOf(10, 8, 5, 3, 2, 1)
        var i = 0
        val minWidthQuality = DEFAULT_MIN_WIDTH_QUALITY
        do {
            bm = decodeBitmap(context, selectedImage, sampleSizes[i])
            i++
        } while (bm!!.width < minWidthQuality && i < sampleSizes.size)
        return bm
    }

    private fun getRotation(context: Context, imageUri: Uri): Int {
        return getRotationFromCamera(context, imageUri)
    }

    private fun getRotationFromCamera(context: Context, imageFile: Uri): Int {
        var rotate = 0
        try {

            context.contentResolver.notifyChange(imageFile, null)
            val exif = ExifInterface(context.contentResolver.openInputStream(imageFile)!!)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )

            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
                ExifInterface.ORIENTATION_UNDEFINED -> rotate =
                    getRotationFromGallery(context, imageFile)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return rotate
    }

    fun getRotationFromGallery(context: Context, imageUri: Uri): Int {
        var result = 0
        val columns = arrayOf(MediaStore.Images.Media.ORIENTATION)
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(imageUri, columns, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val orientationColumnIndex = cursor.getColumnIndex(columns[0])
                result = cursor.getInt(orientationColumnIndex)
            }
        } catch (e: Exception) {
            //
        } finally {
            cursor?.close()
        }
        return result
    }

    private fun rotate(bm: Bitmap?, rotation: Int): Bitmap? {
        if (rotation != 0) {
            val matrix = Matrix()
            matrix.postRotate(rotation.toFloat())
            return Bitmap
                .createBitmap(bm!!, 0, 0, bm.width, bm.height, matrix, true)
        }
        return bm
    }

    fun getBitmapFromResult(
        context: Context, resultCode: Int,
        imageReturnedIntent: Intent?
    ): Bitmap? {
        var isRetrieveImage = false
        var bitmap: Bitmap? = null
        val imageFile = getTempFile(context)
        if (resultCode == Activity.RESULT_OK) {
            val selectedImage: Uri?
            val isCamera = imageReturnedIntent == null ||
                    imageReturnedIntent.data == null ||
                    imageReturnedIntent.data!!.toString().contains(imageFile.toString())

            if (isCamera) {
                selectedImage = FileProvider.getUriForFile(
                    context,
                    BuildConfig.APPLICATION_ID + ".provider",
                    imageFile
                )//Uri.fromFile(imageFile);
            } else {
                selectedImage = imageReturnedIntent!!.data
            }
            try {
                bitmap = decodeBitmap(context, selectedImage!!)
            } catch (e: IOException) {
                // e.printStackTrace();
            }
        }
        return bitmap
    }

    fun getImageFromResult(
        context: Context, resultCode: Int,
        imageReturnedIntent: Intent?, imageName: String
    ): Boolean {
        var isRetrieveImage = false
        var bitmap: Bitmap? = null
        val imageFile = getTempFile(context)
        if (resultCode == Activity.RESULT_OK) {
            val selectedImage: Uri?
            val isCamera = imageReturnedIntent == null ||
                    imageReturnedIntent.data == null ||
                    imageReturnedIntent.data!!.toString().contains(imageFile.toString())

            if (isCamera) {
                selectedImage = FileProvider.getUriForFile(
                    context,
                    BuildConfig.APPLICATION_ID + ".provider",
                    imageFile
                )//Uri.fromFile(imageFile);
            } else {
                selectedImage = imageReturnedIntent!!.data
            }

            try {
                bitmap = getImageResized(context, selectedImage!!)
                val rotation = getRotation(context, selectedImage)
                bitmap = rotate(bitmap, rotation)
                isRetrieveImage = saveBitmapToFile(bitmap, context, imageName)
            } catch (e: IOException) {
                // e.printStackTrace();
            }

        }
        return isRetrieveImage
    }

    private fun saveBitmapToFile(bitmap: Bitmap?, context: Context, imageName: String): Boolean {
        val outputFile: FileOutputStream
        var isSaved = false
        try {
            outputFile = context.openFileOutput(imageName, Context.MODE_PRIVATE)
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, outputFile)
            outputFile.close()
            isSaved = true
        } catch (e: Exception) {
            //e.printStackTrace();
        }

        return isSaved
    }

    fun imageToBase64(image: Bitmap, compressFormat: Bitmap.CompressFormat, quality: Int): String {
        val byteArrayOS = ByteArrayOutputStream()
        image.compress(compressFormat, quality, byteArrayOS)
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.NO_WRAP)
    }

    fun base64ToImage(input: String): Bitmap {
        val decodedBytes = Base64.decode(input, 0)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

}
