package com.kuanquan.videocover

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kuanquan.videocover.bean.LocalMedia
import com.kuanquan.videocover.util.PermissionChecker
import kotlinx.coroutines.MainScope
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.ArrayList

/**
 * 协程好文
 * https://juejin.cn/post/6950616789390721037
 *
 * rebase 会把你当前分支的 commit 放到公共分支的最后面，所以叫变基。就好像你从公共分支又重新拉出来这个分支一样。（不会改变节点）
 *   merge 会把公共分支和你当前的 commit 合并在一起，形成一个新的 commit 节点提交。（会改变节点）
 */
class MainActivity : AppCompatActivity() {

    companion object{
        private const val APPLY_STORAGE_PERMISSIONS_CODE = 1
    }

    private var mImageView: ImageView? = null
    private val mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        lifecycleScope

        val list = ArrayList<String>()

        for (i in 0..10) {
            list.add("Alex")
        }

        Log.e("wangfei -> ", "size -> ${list.size}")
        for (i in list.indices) {
            Log.e("wangfei -> ", "i -> $i")
        }

        mImageView = findViewById(R.id.image_view)
        EventBus.getDefault().register(this)
        if (PermissionChecker
                .checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) &&
            PermissionChecker
                .checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ) {

        } else {
            PermissionChecker.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), APPLY_STORAGE_PERMISSIONS_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == APPLY_STORAGE_PERMISSIONS_CODE) {
            // 同意权限
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

    fun onClick(view: View) {
        val videoPath = "/storage/emulated/0/DCIM/Video/V11021-153429.mp4"
        val fileName = "V11021-153429.mp4"
        val videoDuration = 20171L // 毫秒

//        val videoPath = "/storage/emulated/0/Movies/QQ视频_9fdd2348b2c3d6f703ab84ae010b7c891634795766.mp4"
//        val fileName = "QQ视频_9fdd2348b2c3d6f703ab84ae010b7c891634795766.mp4"
//        val videoDuration = 10067L // 毫秒

//        val videoPath = "/storage/emulated/0/DCIM/Camera/VID_20210929_18262509.mp4"
//        val fileName = "VID_20210929_18262509.mp4"
//        val videoDuration = 17024L // 毫秒

        val localMedia = LocalMedia(videoPath,"",fileName,videoDuration)

        InstagramMediaProcessActivity.launchActivity(this, localMedia)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(str: String?) {
        Glide.with(mImageView!!)
            .load(str)
            .into(mImageView!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}