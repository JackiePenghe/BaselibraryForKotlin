package com.jackiepenghe.baselibraryforkotlin.activities


import android.content.Intent
import android.support.v7.app.AlertDialog
import com.jackiepenghe.baselibraryforkotlin.MainActivity
import com.jackiepenghe.baselibraryforkotlin.activity.BaseWelcomeActivity
import com.jackiepenghe.baselibraryforkotlin.tools.CrashHandler
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import com.yanzhenjie.permission.PermissionListener

/**
 * @author alm
 * @date 2017/10/13
 *
 *
 * 欢迎页 在这个界面可以进行权限请求等
 */

class WelcomeActivity : BaseWelcomeActivity() {
    private val permissionListener = object : PermissionListener {

        override fun onSucceed(requestCode: Int, grantPermissions: List<String>) {
            if (AndPermission.hasPermission(this@WelcomeActivity, grantPermissions)) {
                toNext()
            } else {
                AndPermission.defaultSettingDialog(this@WelcomeActivity, SETTING_REQUEST_CODE)
                    .setTitle("权限请求失败")
                    .setMessage("权限请求失败")
                    .setPositiveButton(android.R.string.yes)
                    .setNegativeButton(android.R.string.no, null)
                    .show()
            }
        }

        override fun onFailed(requestCode: Int, deniedPermissions: List<String>) {
            val settingService = AndPermission.defineSettingDialog(this@WelcomeActivity, SETTING_REQUEST_CODE)
            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(this@WelcomeActivity, deniedPermissions)) {
                //第三种：自定义dialog样式。
                AlertDialog.Builder(this@WelcomeActivity)
                    .setTitle("权限请求失败")
                    .setMessage("权限请求失败")
                    .setPositiveButton(android.R.string.yes) { _, _ ->
                        settingService.execute()
                    }
                    .setNegativeButton(android.R.string.no) { _, _ ->
                        settingService.cancel()
                        toNext()
                    }
                    .setCancelable(false)
                    .show()
            } else {
                AlertDialog.Builder(this@WelcomeActivity)
                    .setTitle("权限请求失败")
                    .setMessage("权限请求失败")
                    .setPositiveButton("继续") { _, _ ->
                        settingService.cancel()
                        toNext()
                    }
                    .setNegativeButton("去设置") { _, _ -> settingService.execute() }
                    .setCancelable(false)
                    .show()
            }
        }
    }
    private val requestCode = 2

    override fun doAfterAnimation() {
        //在动画执行完成之后回调此方法。在此方法中可以执行权限请求等。然后在跳转到主界面.。想要更改本界面的黑白屏的背景，手动在res文件夹下新建一个drawable文件夹，再放入一个名字叫default_main.png的图片即可。

        //可以在这里先执行权限请求等，等权限请求结束或者操作结束，跳转界面

        // 在Activity：
        AndPermission.with(this)
            .requestCode(requestCode)
            .permission(Permission.STORAGE)
            .callback(permissionListener)
            .start()
    }

    /**
     * 设置ImageView的图片资源
     *
     * @return 图片资源ID
     */
    override fun setImageViewSource(): Int {
        return 0
    }

    private fun toNext() {
        CrashHandler.instance.init(this@WelcomeActivity.applicationContext)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val SETTING_REQUEST_CODE = 1
    }
}
