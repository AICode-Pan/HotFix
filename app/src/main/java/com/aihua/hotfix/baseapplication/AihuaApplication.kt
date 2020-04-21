package com.aihua.hotfix.baseapplication

import android.app.Application
import android.content.Context
import android.os.Environment
import android.util.Log
import com.aihua.hotfix.utils.HotFix
import java.io.File

class AihuaApplication : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Log.i("AihuaApplication", "attachBaseContext")
        var dexPath = StringBuffer()
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            dexPath.append(Environment.getExternalStorageDirectory().absolutePath)
        } else {
            dexPath.append(Environment.getDownloadCacheDirectory().absolutePath)
        }
        dexPath.append(File.separator)
        dexPath.append("fexPatch.jar")
        Log.i("AihuaApplication", "dexPath=$dexPath")
        var dexFile = File(dexPath.toString())
        if (dexFile.exists()) {
            Log.i("AihuaApplication", "dexFile is exists!")
            HotFix.toFix(this, dexFile)
        } else {
            Log.i("AihuaApplication", "dexFile is not exists!")
        }
    }
}