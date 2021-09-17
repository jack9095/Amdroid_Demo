package com.shuashuakan.android.modules.publisher

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.shuashuakan.android.R
import com.shuashuakan.android.data.RxBus
import com.shuashuakan.android.event.EventPermissionRequestFragmentDismiss
import com.shuashuakan.android.modules.account.activity.LoginActivity
import com.shuashuakan.android.service.PullService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import me.twocities.linker.annotations.Link
import me.twocities.linker.annotations.LinkQuery

/**
 * @author:qingfei.chen
 * @since:2019/5/28  上午10:11
 */
@Link("ssr://post/video?feed_id")
class PublishFilterActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_LOGIN = 100
    }

    @LinkQuery("feed_id")
    @JvmField
    var feedId: String? = null

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindLinkParams()

        RxBus.get().toFlowable().subscribe() { event ->
            when (event) {
                is EventPermissionRequestFragmentDismiss -> {
                    finish()
                }
            }
        }.addTo(compositeDisposable)
        performRequestPermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            this.finish()
            return
        }
        if (requestCode == REQUEST_LOGIN) {
            performRequestPermission()
        }
    }

    private fun performRequestPermission() {
        val accountManager = application.daggerComponent().accountManager()
        if (!accountManager.hasAccount()) {
            LoginActivity.launchForResult(this, REQUEST_LOGIN)
            return
        }

        if (!PullService.canUpload()) {
            showShortToast(getString(R.string.string_publish_wait_edit))
            finish()
            return
        }

        feedId?.let {
            PermissionRequestFragment.create(PullService.UploadEntity.TYPE_ADD_SOLITAIRE, feedId)
                    .show(supportFragmentManager, "chain_feed_fragment")
        } ?: PermissionRequestFragment.create(PullService.UploadEntity.TYPE_ADD_HOME_VIDEO)
                .show(this.supportFragmentManager, "home")
    }
}