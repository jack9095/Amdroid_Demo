package com.kuanquan.weixinedit

import android.annotation.SuppressLint
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import androidx.lifecycle.Observer
import com.kuanquan.weixinedit.adapter.CircleAdapter
import com.kuanquan.weixinedit.model.EventModel
import com.kuanquan.weixinedit.util.CommonUtils
import com.kuanquan.weixinedit.util.KeyboardUtil
import com.kuanquan.weixinedit.util.Utils
import com.kuanquan.weixinedit.viewmodel.MainViewModel
import com.kuanquan.weixinedit.widget.LikePopupWindow
import com.kuanquan.weixinedit.widget.OnPraiseOrCommentClickListener
import com.kuanquan.weixinedit.widget.ScrollSpeedLinearLayoutManger
import com.kuanquan.weixinedit.widget.SpaceDecoration
import com.mouble.baselibrary.base.BaseViewModelActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity: BaseViewModelActivity<MainViewModel>() {

    private var likePopupWindow: LikePopupWindow? = null
    private var isLike = 0
    private var layoutManger: ScrollSpeedLinearLayoutManger? = null
    private var circleAdapter: CircleAdapter? = null

    override fun isBindEventBusHere(): Boolean = true

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initData() {
        viewModel.setResult()
    }

    override fun dataObserver() {
        viewModel.resultLiveData.observe(this, Observer {
            circleAdapter?.setNewData(it)
        })
    }

    override fun providerVMClass(): Class<MainViewModel> = MainViewModel::class.java

    override fun initView() {
        Utils.init(this)
        initAdapter()
        tv_send_comment.setOnClickListener {
            setViewTreeObserver()
        }
    }

    /**
     * 设置adapter
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun initAdapter() {
        circleAdapter = CircleAdapter()
        layoutManger = ScrollSpeedLinearLayoutManger(this)
        recyclerView.layoutManager = layoutManger
        layoutManger?.setSpeedSlow()
        recyclerView.addItemDecoration(SpaceDecoration(this))
        recyclerView.adapter = circleAdapter

        recyclerView.setOnTouchListener { view: View?, motionEvent: MotionEvent? ->
            if (ll_comment.visibility == View.VISIBLE) {
                updateEditTextBodyVisible(View.GONE)
                return@setOnTouchListener true
            }
            false
        }
    }

    /**
     * 获取控件左上顶点Y坐标
     *
     * @param view
     * @return
     */
    private fun getCoordinateY(view: View): Int {
        val coordinate = IntArray(2)
        view.getLocationOnScreen(coordinate)
        return coordinate[1]
    }

    /**
     * 显示评论点赞的 PopupWindow
     */
    private fun showLikePopupWindow(view: View, position: Int) {
        //item 底部y坐标
        val mBottomY: Int = getCoordinateY(view) + view.height
        if (likePopupWindow == null) {
            likePopupWindow = LikePopupWindow(this, isLike)
        }
        likePopupWindow?.setOnPraiseOrCommentClickListener(object : OnPraiseOrCommentClickListener {
            override fun onPraiseClick(position: Int) {
                //调用点赞接口
                likePopupWindow?.dismiss()
            }

            // 评论的点击事件
            override fun onCommentClick(position: Int) {
                ll_comment.visibility = View.VISIBLE
                etComment.requestFocus()
                etComment.hint = "说点什么"
                KeyboardUtil.showSoftInput(this@MainActivity)
                likePopupWindow?.dismiss()
                etComment.setText("")
                view.postDelayed({
                    val y: Int = getCoordinateY(ll_comment) - 20
                    //评论时滑动到对应item底部和输入框顶部对齐
                    recyclerView.smoothScrollBy(0, mBottomY - y)
                }, 100)
            }

            override fun onClickFrendCircleTopBg() {}
            override fun onDeleteItem(id: String?, position: Int) {}
        })?.setTextView(isLike)?.setCurrentPosition(position)
        if (likePopupWindow?.isShowing!!) {
            likePopupWindow?.dismiss()
        } else {
            likePopupWindow?.showPopupWindow(view)
        }
    }

    private var currentKeyboardH = 0
    private var editTextBodyHeight = 0
    private fun setViewTreeObserver() {
        val swipeRefreshLayoutVTO: ViewTreeObserver = ll_scroll.viewTreeObserver
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener {
            val r = Rect()
            ll_scroll.getWindowVisibleDisplayFrame(r)
            val statusBarH = Utils.getStatusBarHeight() // 状态栏高度
            val screenH: Int = ll_scroll.rootView.height
            if (r.top != statusBarH) {
                // r.top代表的是状态栏高度，在沉浸式状态栏时r.top＝0，通过 getStatusBarHeight 获取状态栏高度
                r.top = statusBarH
            }
            val keyboardH = screenH - (r.bottom - r.top)
            Log.d(
                "TAG",
                "screenH＝ " + screenH + " &keyboardH = " + keyboardH + " &r.bottom=" + r.bottom + " &top=" + r.top + " &statusBarH=" + statusBarH
            )
            if (keyboardH == currentKeyboardH) { // 有变化时才处理，否则会陷入死循环
                return@addOnGlobalLayoutListener
            }
            currentKeyboardH = keyboardH
            editTextBodyHeight = ll_comment.height
            if (keyboardH < 150) { //说明是隐藏键盘的情况
                this@MainActivity.updateEditTextBodyVisible(View.GONE)
                return@addOnGlobalLayoutListener
            }
        }
    }

    private fun updateEditTextBodyVisible(visibility: Int) {
        ll_comment.visibility = visibility
        if (View.VISIBLE == visibility) {
            ll_comment.requestFocus()
            //弹出键盘
            CommonUtils.showSoftInput(etComment.context, etComment)
        } else if (View.GONE == visibility) {
            //隐藏键盘
            CommonUtils.hideSoftInput(etComment.context, etComment)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(nodel: EventModel){
        showLikePopupWindow(nodel.view, nodel.position)
    }
}