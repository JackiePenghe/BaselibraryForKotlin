package com.jackiepenghe.baselibraryforkotlin.activities

import android.content.Intent
import com.jackiepenghe.baselibraryforkotlin.activity.BaseSplashActivity
import com.jackiepenghe.baselibraryforkotlin.tools.Md5Util
import com.jackiepenghe.baselibraryforkotlin.tools.Tool

/**
 * 防止应用启动黑白屏
 *
 * @author alm
 */
class SplashActivity : BaseSplashActivity() {

    override fun onCreate() {

        val cache = "adfafadfetweaf"
        val encrypt = Md5Util.encrypt(cache)
        Tool.warnOut(TAG, "encrypt = $encrypt")

        //本界面仅用于防止程序黑白屏。想要更改本界面的黑白屏的背景，手动在res文件夹下新建一个xml文件夹，再新建一个files_path.xml。在其中配置即可
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private val TAG = SplashActivity::class.java.simpleName
    }
}
