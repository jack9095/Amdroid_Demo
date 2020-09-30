package com.kuanquan.weixinedit

import android.annotation.SuppressLint
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import com.kuanquan.weixinedit.adapter.CircleAdapter
import com.kuanquan.weixinedit.model.EventModel
import com.kuanquan.weixinedit.model.ProviderMultiEntity
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
    private var lists = mutableListOf<ProviderMultiEntity>()
    private var images = mutableListOf<String>()

    override fun isBindEventBusHere(): Boolean = true

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initData() {
        images.add("http://pic2.52pk.com/files/allimg/150324/104923F49-12.jpg")
        images.add("http://pic.3h3.com/up/2014-3/20143314140858312456.gif")
        images.add("https://f12.baidu.com/it/u=3294379970,949120920&fm=72")
        images.add("http://pic2.52pk.com/files/allimg/150324/104923F49-12.jpg")
        images.add("http://img.my.csdn.net/uploads/201701/06/1483664741_1378.jpg")
        images.add("http://img.my.csdn.net/uploads/201701/17/1484647897_1978.jpg")
        images.add("http://pic2.52pk.com/files/allimg/150324/104923F49-12.jpg")
        images.add("http://pic.3h3.com/up/2014-3/20143314140858312456.gif")
        images.add("https://f12.baidu.com/it/u=3294379970,949120920&fm=72")

        for (i in 0 .. 5){
            val model = ProviderMultiEntity()
            model.type = 0
            model.createon = "昨天$i"
            model.name = "韦一笑$i"
            model.content = "茫茫的长白大山瀚的草原，浩始森林，大山脚下，原始森林环抱中散落着几十户人家的，" +
                    "一个小山村，茅草房，对面炕，烟筒立在屋后边。在村东头有一个独立的房子，那就是青年点，窗前有一道小溪流过。" +
                    "学子在这里吃饭，由这里出发每天随社员去地里干活。干的活要么上山伐，树，抬树，要么砍柳树毛子开荒种地。" +
                    "在山里，可听那吆呵声：“顺山倒了！”放树谨防回头棒！，树上的枯枝打到别的树上再蹦回来，这回头棒打人最厉害。"

            lists.add(model)
        }

        for (i in 0 .. 5){
            val model = ProviderMultiEntity()
            model.type = 1
            model.createon = "前天$i"
            model.name = "寒冰绵掌$i"
            model.content = "树上的枯枝打到别的树上再蹦回来，这回头棒打人最厉害。"
            model.images = images
            lists.add(model)
        }

        circleAdapter?.setNewData(lists)
    }

    override fun dataObserver() {

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

            override fun onCommentClick(position: Int) {
                ll_comment.visibility = View.VISIBLE
                etComment.requestFocus()
                etComment.hint = "说点什么"
//                to_user_id = null
                KeyboardUtil.showSoftInput(this@MainActivity)
                likePopupWindow?.dismiss()
                etComment.setText("")
                view.postDelayed({
                    val y: Int = getCoordinateY(ll_comment) - 20
                    //评论时滑动到对应item底部和输入框顶部对齐
                    recyclerView.smoothScrollBy(0, mBottomY - y)
                }, 300)
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
        val swipeRefreshLayoutVTO: ViewTreeObserver = ll_scroll.getViewTreeObserver()
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener {
            val r = Rect()
            ll_scroll.getWindowVisibleDisplayFrame(r)
            val statusBarH = Utils.getStatusBarHeight() //状态栏高度
            val screenH: Int = ll_scroll.getRootView().getHeight()
            if (r.top != statusBarH) {
                //r.top代表的是状态栏高度，在沉浸式状态栏时r.top＝0，通过getStatusBarHeight获取状态栏高度
                r.top = statusBarH
            }
            val keyboardH = screenH - (r.bottom - r.top)
            Log.d(
                "TAG",
                "screenH＝ " + screenH + " &keyboardH = " + keyboardH + " &r.bottom=" + r.bottom + " &top=" + r.top + " &statusBarH=" + statusBarH
            )
            if (keyboardH == currentKeyboardH) { //有变化时才处理，否则会陷入死循环
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

    fun updateEditTextBodyVisible(visibility: Int) {
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