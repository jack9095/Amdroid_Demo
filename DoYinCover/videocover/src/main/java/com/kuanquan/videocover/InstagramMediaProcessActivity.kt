package com.kuanquan.videocover

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.kuanquan.videocover.bean.LocalMedia
import com.kuanquan.videocover.callback.ProcessStateCallBack
import com.kuanquan.videocover.util.InstagramTitleBar
import com.kuanquan.videocover.widget.InstagramMediaSingleVideoContainer

class InstagramMediaProcessActivity: AppCompatActivity() {

    private var container: View? = null
    private var mLocalMedia: LocalMedia? = null
    private var mTitleBar: InstagramTitleBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLocalMedia = intent?.getSerializableExtra(EXTRA_SELECT_LIST) as? LocalMedia
        initView()
    }

    private fun initView() {
        val contentView: FrameLayout = object : FrameLayout(this) {
            override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
                val width = MeasureSpec.getSize(widthMeasureSpec)
                val height = MeasureSpec.getSize(heightMeasureSpec)
                measureChild(mTitleBar, widthMeasureSpec, heightMeasureSpec)
                getChildAt(0).measure(
                    MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(
                        height - mTitleBar!!.measuredHeight,
                        MeasureSpec.EXACTLY
                    )
                )
                setMeasuredDimension(width, height)
            }

            override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
                mTitleBar?.layout(0, 0, mTitleBar!!.measuredWidth, mTitleBar!!.measuredHeight)
                val child = getChildAt(0)
                child.layout(
                    0,
                    mTitleBar!!.measuredHeight,
                    child.measuredWidth,
                    mTitleBar!!.measuredHeight + child.measuredHeight
                )
            }
        }
        container = contentView
        container?.setBackgroundColor(Color.parseColor("#000000"))
        setContentView(contentView)

        // 视频控件
        val singleVideoContainer =
            InstagramMediaSingleVideoContainer(this, mLocalMedia!!, false)
        singleVideoContainer.addLifecycleObserver(this)
        contentView.addView(singleVideoContainer,
            FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
        )
        singleVideoContainer.mCoverView?.mLiveData?.observe(this, Observer { finish() })
        mTitleBar = InstagramTitleBar(this)
        contentView.addView(mTitleBar)
        mTitleBar!!.setClickListener(object : InstagramTitleBar.OnTitleBarItemOnClickListener {
            override fun onLeftViewClick() {
                finish()
            }

            override fun onRightViewClick() {
                if (contentView.getChildAt(0) is ProcessStateCallBack) {
                    (contentView.getChildAt(0) as ProcessStateCallBack).onProcess(this@InstagramMediaProcessActivity)
                }
            }
        })
    }

    companion object{
        private const val EXTRA_SELECT_LIST = "selectList"
        fun launchActivity(activity: Activity, localMedia: LocalMedia) {
            val intent = Intent(activity.applicationContext, InstagramMediaProcessActivity::class.java)
            intent.putExtra(EXTRA_SELECT_LIST,localMedia)
            activity.startActivity(intent)
            activity.overridePendingTransition(0, 0)
        }
    }
}