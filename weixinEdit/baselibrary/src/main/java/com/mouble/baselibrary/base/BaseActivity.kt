package com.mouble.baselibrary.base

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.mouble.baselibrary.util.LogUtil
import org.greenrobot.eventbus.EventBus
import kotlin.system.exitProcess

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        setStatusBar(Color.WHITE)
        if (isBindEventBusHere()) {
            EventBus.getDefault().register(this)
        }
        initView(savedInstanceState)
        initData()
    }

    abstract fun isBindEventBusHere(): Boolean
    abstract fun getLayoutId(): Int
    abstract fun initView(savedInstanceState: Bundle?)
    abstract fun initData()
    abstract fun dataObserver()

    // TODO 型参数组 修饰符 vararg
    protected fun addOnClickListeners(listener: View.OnClickListener?, @IdRes vararg ids: Int) {
        for (@IdRes id in ids) {
            findViewById<View>(id).setOnClickListener(listener)
        }
    }

    protected fun showToast(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    private var progressDialog: LoadDialog? = null
    fun showProgressDialog() {
        progressDialog = LoadDialog(this).apply {
            //点击外部都不可取消
            setCanceledOnTouchOutside(false)
            //点击返回键可取消
            setCancelable(true)
        }
        progressDialog?.run {
            try {
                if (isShowing)
                    dismiss()
                else
                    show()
            } catch (e: Exception) {
                LogUtil.e("异常 = ", e)
            }
        }
    }

    fun dismissProgressDialog() {
        try {
            progressDialog?.run {
                // progressDialog.hide();会导致android.view.WindowLeaked
                if (isShowing) dismiss()
            }
        } catch (e: Exception) {
            LogUtil.e("进度弹框取消异常 = ", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBindEventBusHere()) EventBus.getDefault().unregister(this)
    }

    /*************************** 顶部状态栏 start ************************************/
    // 设置顶部状态栏颜色
    private fun setStatusBar(@ColorInt color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = color
            if (isLightColor(color)) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        }
    }

    private fun isLightColor(@ColorInt color: Int): Boolean = ColorUtils.calculateLuminance(color) >= 0.5

    @ColorInt
    protected fun getStatusBarColor(): Int = Color.WHITE
    /*************************** 顶部状态栏 end ************************************/


    private var mExitTime: Long = 0
    open fun oDoubleCheck() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show()
            mExitTime = System.currentTimeMillis()
        } else {
            finish()
            exitProcess(0)
        }
    }

    /*************************** 权限相关 start ************************************/

    // 检查是否获取到权限 true 已经有权限了
    open fun hasPermissions(vararg permissions: String?): Boolean {
        for (permission in permissions) {
            if (permission?.let { ContextCompat.checkSelfPermission(this, it) } != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    // 请求相关权限
    open fun requestPermissions(requestCode: Int, vararg permissions: String?) {
        ActivityCompat.requestPermissions(this, permissions, requestCode)
    }

    // 请求权限结果回调
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
//        when (requestCode) {
//            0 -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getRelatedPermissions(requestCode)
//            } else {
//                Toast.makeText(this, "你拒绝了权限", Toast.LENGTH_SHORT).show()
//            }
//        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getRelatedPermissions(requestCode)
        } else {
            Toast.makeText(this, "你拒绝了权限", Toast.LENGTH_SHORT).show()
        }
    }

    // 获取到相关权限
    open fun getRelatedPermissions(requestCode: Int) {}

    /*
    使用
    val hasPermissions = hasPermissions(Manifest.permission.CAMERA)
        if (!hasPermissions) {
            requestPermissions(0, Manifest.permission.CAMERA)
        } else {

        }

        fun getRelatedPermissions(requestCode: Int) {
            when (requestCode) {

            }
        }

     */

    /*************************** 权限相关 end ************************************/
}