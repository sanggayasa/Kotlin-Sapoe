package com.akarinti.sapoe.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.NonNull
import androidx.core.content.FileProvider
import com.akarinti.sapoe.BuildConfig
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.cause.ResumeFailedCause
import com.liulishuo.okdownload.core.listener.DownloadListener1
import com.liulishuo.okdownload.core.listener.assist.Listener1Assist
import java.io.File

class DownloadUtils(var activity: Activity, var listener: Listener) {

    private lateinit var taskAPK : DownloadTask


    fun downloadAPK(url: String) {
        var parentfile: File = getParentFile(activity)
        taskAPK = DownloadTask.Builder(url, parentfile)
            .setFilename("update.apk")
            .setMinIntervalMillisCallbackProcess(30)
            .setPassIfAlreadyCompleted(false)
            .setConnectionCount(1)
            .build()

        taskAPK.enqueue(object : DownloadListener1() {
            override fun taskStart(task: DownloadTask, model: Listener1Assist.Listener1Model) {
                //Log.e("DownloadUtils", "taskStart ${task.info}")
            }

            override fun taskEnd(task: DownloadTask, cause: EndCause, realCause: Exception?, model: Listener1Assist.Listener1Model) {
                //Log.e("DownloadUtils", "taskEnd ${cause.name} | ${realCause?.localizedMessage}")
                if (cause == EndCause.COMPLETED && null != task.file) installAPK(task.file!!)
                else listener.errorDownload("Error : ${cause.name} : ${realCause?.localizedMessage?:"Download Fail"}")

                //listener.dismissDownloadLoading()
            }

            override fun progress(task: DownloadTask, currentOffset: Long, totalLength: Long) {
                //Log.e("DownloadUtils", "progress >> $currentOffset / $totalLength")
            }

            override fun connected(task: DownloadTask, blockCount: Int, currentOffset: Long, totalLength: Long) {
                //Log.e("DownloadUtils", "connected")
            }

            override fun retry(task: DownloadTask, cause: ResumeFailedCause) {
                //Log.e("DownloadUtils", "retry ${cause.name}")
                listener.dismissDownloadLoading()
            }
        })
    }

    fun getParentFile(@NonNull context: Context): File {
        val externalSaveDir = context.externalCacheDir
        return externalSaveDir ?: context.cacheDir
    }

    fun installAPK(toInstall: File) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val apkUri: Uri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID.toString() + ".provider", toInstall)
            val intent = Intent(Intent.ACTION_INSTALL_PACKAGE)
            intent.data = apkUri
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            activity.startActivity(intent)
        } else {
            val apkUri = Uri.fromFile(toInstall)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity.startActivity(intent)
        }
    }

    interface Listener {
        fun dismissDownloadLoading()
        fun errorDownload(msg: String)
    }
}