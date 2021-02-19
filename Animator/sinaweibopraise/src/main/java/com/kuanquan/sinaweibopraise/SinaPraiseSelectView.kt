package com.kuanquan.sinaweibopraise

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.*
import android.view.*
import android.view.accessibility.AccessibilityEvent
import android.widget.PopupWindow
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.kuanquan.sinaweibopraise.glide.GlideApp
import kotlin.math.abs

class SinaPraiseSelectView<T> private constructor(context: Context) : View(context, null) {

    companion object {
        private const val STEP = 4 //emoji每帧变换大小
        private const val BACKGROUND_STEP = 6 //背景每帧变换大小
        private const val RATE = 3L //刷新频率
        private const val NOT_INIT = -2
        private const val HAS_INIT = NOT_INIT + 1 // -1
        private const val NOT_SELECTED = HAS_INIT + 1 // 0
        private const val ON_SELECT = NOT_SELECTED + 1 // 1
        private const val SELECTED = ON_SELECT + 1 // 2
        private const val ON_NOT_SELECT = SELECTED + 1 // 3

        @JvmStatic
        fun <T> attachView(view: View?, data: List<T>?, callback: Callback<T>?, builder: Builder<T>?) {
            if (view == null || data == null || callback == null || builder == null) return
            val pop = PopupWindow(view.context)
            val emojiSelectView = SinaPraiseSelectView<T>(view.context).apply {
                attachView(view)
                    .setData(data)
                    .setBuilder(builder)
                    .build()

                setCallback(object : Callback<T?> {
                    override fun callback(t: T?) {
                        callback.callback(t)
                        pop.dismiss()
                    }
                })
            }
            pop.run {
                contentView = emojiSelectView
                isOutsideTouchable = true
                setBackgroundDrawable(null)
                height = ScreenUtil.getScreenHeight(view.context)
                width = ScreenUtil.getScreenWidth(view.context)
                showAtLocation(view, Gravity.CENTER, 0, 0)
            }
        }

        fun getTextRect(paint: Paint?, str: String): Rect {
            val rect = Rect()
            paint?.getTextBounds(str, 0, str.length - 1, rect)
            return rect
        }
    }

    private val emojiItems = mutableListOf<EmojiItem<T>>()
    private val mData= mutableListOf<T>()
    private val textPadding = ScreenUtil.dp2px(context,2f)
    private val textSidePadding = ScreenUtil.dp2px(context,6f)
    private val itemPaddingWidth = ScreenUtil.dp2px(context,3f)

    private var mStatus = NOT_INIT

    private var mTextBackgroundPaint: Paint? = null // 选中item title text background
    private var mTextPaint: TextPaint? = null //选中item title text
    private var mBackgroundPaint: Paint? = null //背景Paint
    private var mEmojiPaint: Paint? = null // eomjiPaint

    private var positionX = Int.MIN_VALUE //手指X坐标
    private var baseLine = 0 //基准线
    private var notSelectedWidth = 0 //未选中大小
    private var selectedWidth = 0 //选中大小
    private var normalWidth = 0 //正常大小
    private var contentWidth = 0 //内容区域宽度
    private var currentWidth = 0 //当前icon大小
    private var minX = Int.MAX_VALUE //item最左边
    private var maxX = Int.MIN_VALUE //item最右边
    private var triangleX = 0 //小三角指针X位置
    private var isUp = true //是否向上显示view
    private var isLeft = true //是否靠左边
    private var selectedItem: EmojiItem<T>? = null //选中的emoji item

    private var builder: Builder<T>? = null
    private var callback: Callback<T?>? = null //选中回调
    private var mOriginWindowCallback: Window.Callback? = null

    private var isHasSelected = false

    init {
        initView()
    }

    private fun initView() {
        isClickable = true
        setLayerType(LAYER_TYPE_SOFTWARE, null) //阴影需要关闭硬件加速
        initPaints()
        mStatus = HAS_INIT
    }

    private fun initParams() {
        if (mData.size == 0) return
        normalWidth = ScreenUtil.dp2px(context,36f)
        notSelectedWidth = ScreenUtil.dp2px(context,32f)
        selectedWidth = (mData.size - 1) * (normalWidth - notSelectedWidth)
        contentWidth = normalWidth * mData.size + itemPaddingWidth * mData.size * 2

        if (ScreenUtil.getScreenWidth(context) < contentWidth + getInnerSidePadding() + getSidePadding() * 2) {
            //适配小屏手机
            contentWidth = ScreenUtil.getScreenWidth(context) - getInnerSidePadding() * 2 - getSidePadding() * 2
            normalWidth = contentWidth / mData.size - itemPaddingWidth * 2
            notSelectedWidth = normalWidth - ScreenUtil.dp2px(context,4f)
            selectedWidth = normalWidth + ScreenUtil.dp2px(context,4f) * (mData.size - 1)
        }

        currentWidth = normalWidth
        val paddingSum = ScreenUtil.getScreenWidth(context) - contentWidth
        if (isLeft) {
            setPadding(ScreenUtil.dp2px(context,16f), 0, paddingSum - ScreenUtil.dp2px(context,6f), 0)
        } else {
            setPadding(
                paddingSum - ScreenUtil.dp2px(context,6f) - getInnerSidePadding() * 2,
                0,
                ScreenUtil.dp2px(context,16f),
                0
            )
        }
    }

    private fun getSidePadding(): Int = ScreenUtil.dp2px(context,6f)

    private fun getInnerSidePadding(): Int = ScreenUtil.dp2px(context,6f)

    private fun getInnerUpDownPadding(): Int = ScreenUtil.dp2px(context,6f)

    private fun getStart(): Int = paddingStart + getInnerSidePadding() + ScreenUtil.dp2px(context,2f)

    private fun getEnd(): Int = getStart() + contentWidth

    private fun initPaints() {
        mTextPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.LEFT
            textSize = ScreenUtil.dp2px(context,10f).toFloat()
            color = Color.WHITE
        }
        mEmojiPaint = Paint().apply {
            isAntiAlias = true
        }
        mBackgroundPaint = Paint().apply {
            isAntiAlias = true
            color = Color.WHITE
            style = Paint.Style.FILL
        }
        mTextBackgroundPaint = Paint().apply {
            color = Color.BLACK
            alpha = 204
            style = Paint.Style.FILL
        }
    }

    private fun initEmojiItems() {
        emojiItems.clear()
        val rect = Rect().apply {
            bottom = if (isUp) baseLine else baseLine + currentWidth
            top = if (isUp) baseLine - currentWidth else baseLine
            left = getStart()
            right = currentWidth + getStart()
        }

        minX = rect.left
        for (t in mData) {
            if (builder == null) return
            val item = EmojiItem(t)
            builder?.buildModel(item)
            if (!TextUtils.isEmpty(item.bitmapUrl)) {
                item.bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ALPHA_8)
                GlideApp.with(context)
                    .asBitmap()
                    .load(item.bitmapUrl)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            item.bitmap = resource
                            item.bitmapRect.top = 0
                            item.bitmapRect.bottom = item.bitmap?.height ?: 0
                            item.bitmapRect.right = item.bitmap?.width ?: 0
                            postInvalidate()
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })
            }
            item.run {
                bitmapRect.top = 0
                bitmapRect.bottom = item.bitmap?.height ?: 0
                bitmapRect.right = item.bitmap?.width ?: 0
                isSelected = false
                toRect = Rect(rect)
            }
            maxX = rect.right
            rect.left = rect.left + currentWidth + itemPaddingWidth * 2
            rect.right = rect.right + currentWidth + itemPaddingWidth * 2
            item.let {
                emojiItems.add(it)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun attachView(attachedView: View): SinaPraiseSelectView<T> {
        mOriginWindowCallback = AppLifecycleManager.getPresentActivity()?.window?.callback
        AppLifecycleManager.getPresentActivity()?.window?.callback = object : Window.Callback {
            override fun onActionModeFinished(mode: ActionMode?) {}
            override fun onCreatePanelView(featureId: Int): View? { return null }

            override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
                event?.let {
                    onTouchEvent(it)
                }
                return true
            }

            override fun onCreatePanelMenu(featureId: Int, menu: Menu): Boolean { return false }

            override fun onWindowStartingActionMode(callback: ActionMode.Callback?): ActionMode? { return null }

            override fun onWindowStartingActionMode(callback: ActionMode.Callback?, type: Int): ActionMode? { return null }

            override fun onAttachedToWindow() {}

            override fun dispatchGenericMotionEvent(event: MotionEvent?): Boolean { return false }

            override fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent?): Boolean { return false }

            override fun dispatchTrackballEvent(event: MotionEvent?): Boolean { return false}

            override fun dispatchKeyShortcutEvent(event: KeyEvent?): Boolean { return false }

            override fun dispatchKeyEvent(event: KeyEvent?): Boolean { return false }

            override fun onMenuOpened(featureId: Int, menu: Menu): Boolean { return false }

            override fun onPanelClosed(featureId: Int, menu: Menu) {}

            override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean { return false }

            override fun onDetachedFromWindow() {}

            override fun onPreparePanel(featureId: Int, view: View?, menu: Menu): Boolean { return false }

            override fun onWindowAttributesChanged(attrs: WindowManager.LayoutParams?) {}

            override fun onWindowFocusChanged(hasFocus: Boolean) {}

            override fun onContentChanged() {}

            override fun onSearchRequested(): Boolean { return false }

            override fun onSearchRequested(searchEvent: SearchEvent?): Boolean { return false }

            override fun onActionModeStarted(mode: ActionMode?) {}
        }

        val location = IntArray(2)
        attachedView.getLocationOnScreen(location)
        isUp = location[1] > ScreenUtil.dp2px(context,200f) +
                ScreenUtil.getActionBarHeight(context) + ScreenUtil.getStatusBarHeight(context)
        isLeft = ScreenUtil.getScreenWidth(context) / 2 > location[0]
        baseLine = if (isUp)
                location[1] -
                        ScreenUtil.dp2px(context,8f) -
                        ScreenUtil.dp2px(context,22f) -
                        ScreenUtil.dp2px(context,20f) -
                        getInnerUpDownPadding() * 2
            else
                location[1] +
                        attachedView.minimumHeight +
                        getInnerUpDownPadding() * 2
        triangleX = location[0] + attachedView.width / 2
        return this
    }

    fun setCallback(callback: Callback<T?>) {
        this.callback = callback
    }

    fun setBuilder(builder: Builder<T>?): SinaPraiseSelectView<T> {
        this.builder = builder
        return this
    }

    fun setData(data: List<T>?): SinaPraiseSelectView<T> {
        mData.clear()
        data?.let {
            mData.addAll(it)
        }
        return this
    }

    fun build() {
        initParams()
        initEmojiItems()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mStatus < HAS_INIT) return false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isHasSelected = true
                processOnMove(event)
            }
            MotionEvent.ACTION_MOVE -> {
                processOnMove(event)
            }
            MotionEvent.ACTION_UP -> {
                AppLifecycleManager.getPresentActivity()?.window?.callback = mOriginWindowCallback
                if (callback != null && isHasSelected) {
                    if (selectedItem != null) {
                        callback?.callback(selectedItem?.data)
                    } else {
                        callback?.callback(null)
                    }
                }
            }

        }
        return true
    }

    private fun processOnMove(event: MotionEvent): Boolean {
        val tempBaseline =
            if (isUp) baseLine - selectedWidth else baseLine + normalWidth + getInnerUpDownPadding() + selectedWidth
        if (event.rawY < (tempBaseline + selectedWidth * 4 + if (isUp) selectedWidth * 2 else 0) && event.rawY > (tempBaseline - selectedWidth * 4 - if (isUp) 0 else selectedWidth * 2)) {
            val tempX = event.rawX.toInt()
            if (tempX < minX || tempX > maxX) {
                //排除边界
                updateStatusToNotSelected()
                return true
            }
            updateStatusToSelected(tempX)
        } else {
            updateStatusToNotSelected()
        }
        return true
    }

    private fun updateStatusToSelected(currentX: Int) {
        isHasSelected = true
        positionX = currentX
        if (mStatus != ON_SELECT) {
            postInvalidate()
        }
        if (isSelectedItem(currentX)) {
            mStatus = ON_SELECT
        } else {
            ScreenUtil.vibrate(context, 20)
            mStatus = ON_SELECT
            selectedItem = null
        }
    }

    private fun updateStatusToNotSelected() {
        positionX = Int.MIN_VALUE
        if (mStatus != ON_NOT_SELECT) {
            postInvalidate()
        }
        mStatus = if (mStatus > NOT_SELECTED) {
            ON_NOT_SELECT
        } else {
            NOT_SELECTED
        }
    }

    private fun isSelectedItem(tempPositionX: Int): Boolean {
        return if (selectedItem == null) {
            false
        } else selectedItem?.toRect?.left?.minus(itemPaddingWidth)
            ?.compareTo(tempPositionX) ?: 1 <= 0 && selectedItem?.toRect?.right?.plus(
            itemPaddingWidth
        )?.compareTo(tempPositionX) ?: -1 >= 0
    }

    private fun animationToSelect() {
        var maxAdd = 0
        for (item in emojiItems) {
            item.isSelected = updateSelectItem(item)
            if (!item.isSelected) {
                item.diff = getShouldDelete(item, notSelectedWidth)
                maxAdd += item.diff
            }
        }
        if (maxAdd == 0) {
            mStatus = SELECTED
            return
        }
        var laseRect: Rect? = null
        for (item in emojiItems) {
            val toItemWidth =
                if (item.isSelected) item.toRect.width() + maxAdd else item.toRect.width() - item.diff
            if (laseRect == null) {
                item.toRect.left = getStart()
            } else {
                item.toRect.left = laseRect.right + itemPaddingWidth * 2
            }
            item.toRect.right = item.toRect.left + toItemWidth
            if (isUp) {
                item.toRect.top = item.toRect.bottom - toItemWidth
            } else {
                item.toRect.bottom = item.toRect.top + toItemWidth
            }
            laseRect = item.toRect
        }
    }

    private fun updateSelectItem(item: EmojiItem<T>): Boolean {
        if (item.toRect.left - itemPaddingWidth <= positionX && item.toRect.right + itemPaddingWidth > positionX) {
            item.isSelected = true
            selectedItem = item
        } else {
            item.isSelected = false
        }
        return item.isSelected
    }

    private fun getShouldDelete(item: EmojiItem<T>, targetWidth: Int): Int {
        return abs(item.toRect.width() - targetWidth).coerceAtMost(STEP)
    }

    private fun animationToNormal() {
        var maxAdd = 0
        var biggerCount = 0
        selectedItem = null
        for (item in emojiItems) {
            if (item.toRect.width() <= normalWidth) {
                item.diff = getShouldDelete(item, normalWidth)
                maxAdd += item.diff
            } else {
                biggerCount++
            }
            item.isSelected = false
        }
        if (maxAdd == 0) {
            mStatus = NOT_SELECTED
            return
        }
        var laseRect: Rect? = null
        for (item in emojiItems) {
            var toItemWidth: Int
            if (item.toRect.width() > normalWidth) {
                val diff = maxAdd / biggerCount
                maxAdd -= diff
                biggerCount--
                toItemWidth = item.toRect.width() - diff
            } else {
                toItemWidth = item.toRect.width() + item.diff
            }
            if (laseRect == null) {
                item.toRect.left = getStart()
            } else {
                item.toRect.left = laseRect.right + itemPaddingWidth * 2
            }
            item.toRect.right = item.toRect.left + toItemWidth
            if (isUp) {
                item.toRect.top = item.toRect.bottom - toItemWidth
            } else {
                item.toRect.bottom = item.toRect.top + toItemWidth
            }
            laseRect = item.toRect
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mStatus = NOT_INIT
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mStatus < HAS_INIT) return
        //draw background
        drawBackground(canvas)
        when (mStatus) {
            ON_SELECT -> {
                //do some Animation style
                animationToSelect()
                postInvalidateDelayed(RATE)
            }
            ON_NOT_SELECT -> {
                //do some Animation style
                animationToNormal()
                postInvalidateDelayed(RATE)
            }
            SELECTED ->
                //draw selected style
                animationToSelect()
            else ->
                //draw UnSelected style
                animationToNormal()
        }
        for (item in emojiItems) {
            item.bitmap?.let {
                canvas.drawBitmap(it, item.bitmapRect, item.toRect, mEmojiPaint)
            }
            if (item.isSelected) {
                drawSelectItemText(canvas, item)
            }
        }
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawColor(Color.TRANSPARENT)
        if (mStatus == ON_SELECT || mStatus == SELECTED) {
            currentWidth -= BACKGROUND_STEP
            if (currentWidth <= notSelectedWidth) {
                currentWidth = notSelectedWidth
            }
        } else if (mStatus == ON_NOT_SELECT || mStatus == NOT_SELECTED) {
            currentWidth += BACKGROUND_STEP
            if (currentWidth >= normalWidth) {
                currentWidth = normalWidth
            }
        }
        val rectF = RectF()
        rectF.left = paddingLeft.toFloat()
        rectF.right = getEnd().toFloat() + getInnerSidePadding()
        rectF.top =
            if (isUp) (baseLine - currentWidth - getInnerUpDownPadding()).toFloat() else (baseLine - getInnerUpDownPadding()).toFloat()
        rectF.bottom =
            if (isUp) (baseLine + getInnerUpDownPadding()).toFloat() else baseLine + currentWidth + getInnerUpDownPadding().toFloat()
        mBackgroundPaint?.setShadowLayer(
            20.toFloat(),
            0f,
            8f,
            Color.parseColor("#1A000000")
        )
        mBackgroundPaint?.let {
            canvas.drawRoundRect(rectF, rectF.height() / 2, rectF.height() / 2, it)
        }
        drawTriangle(canvas)
    }

    private fun drawTriangle(canvas: Canvas) {
        //画三角形
        val path = Path()
        val innerParams =
            if (isUp) {
                getInnerUpDownPadding()
            } else {
                -getInnerUpDownPadding()
            }

        val triangleBaseLine = if (isUp) baseLine - ScreenUtil.dp2px(context,2f) else baseLine + ScreenUtil.dp2px(context,2f)

        if (isUp) {
            path.moveTo(triangleX.toFloat(), triangleBaseLine + ScreenUtil.dp2px(context,8f).toFloat() + innerParams)
        } else {
            path.moveTo(triangleX.toFloat(), triangleBaseLine - ScreenUtil.dp2px(context,8f).toFloat() + innerParams)
        }
        path.lineTo(triangleX + ScreenUtil.dp2px(context,7f).toFloat(), triangleBaseLine.toFloat() + innerParams)
        path.lineTo(triangleX - ScreenUtil.dp2px(context,7f).toFloat(), triangleBaseLine.toFloat() + innerParams)
        mBackgroundPaint?.setShadowLayer(0f, 0f, 0f, Color.TRANSPARENT)
        mBackgroundPaint?.let {
            canvas.drawPath(path, it)
        }
    }

    private fun drawSelectItemText(canvas: Canvas, item: EmojiItem<T>) {
        drawSelectItemTextBackground(canvas, item)
        val textLength = mTextPaint?.measureText(getText(item))?.toInt() ?: 0
        val height =
            getTextRect(mTextPaint, item.titleText).height().toFloat()
        val startXText = (item.toRect.left + item.toRect.right) / 2 - textLength / 2
        mTextPaint?.let {
            canvas.drawText(
                getText(item),
                startXText.toFloat(),
                if (isUp)
                    (item.toRect.top - ScreenUtil.dp2px(context,3f)).toFloat() - ScreenUtil.dp2px(context,4f)
                else
                    item.toRect.bottom + height + ScreenUtil.dp2px(context,1f) + ScreenUtil.dp2px(context,4f),
                it
            )
        }
    }

    private fun getText(item: EmojiItem<T>): String {
        var text = item.titleText
        if (text.length > 4) {
            text = text.substring(0, 4)
        }
        return text
    }

    private fun drawSelectItemTextBackground(canvas: Canvas, item: EmojiItem<T>) {
        val textLength = mTextPaint?.measureText(getText(item))?.toInt() ?: 0
        val startXText = (item.toRect.left + item.toRect.right) / 2 - textLength / 2
        val height = getTextRect(mTextPaint, item.titleText).height().toFloat()
        val rectF = RectF()
        rectF.left = startXText - textSidePadding.toFloat()
        rectF.right = startXText + textSidePadding.toFloat() + textLength
        rectF.bottom =
            if (isUp) (item.toRect.top).toFloat() - ScreenUtil.dp2px(context,4f) else item.toRect.bottom + height + textPadding * 2 + ScreenUtil.dp2px(context,4f)
        rectF.top =
            if (isUp) item.toRect.top - height - textPadding * 2 - ScreenUtil.dp2px(context,4f) else (item.toRect.bottom).toFloat() + ScreenUtil.dp2px(context,4f)
        mTextBackgroundPaint?.let {
            canvas.drawRoundRect(rectF, rectF.height() / 2, rectF.height() / 2, it)
        }
    }
}

interface Callback<T> {
    fun callback(t: T?)
}

interface Builder<T> {
    fun buildModel(item: EmojiItem<T>): EmojiItem<T>
}

class EmojiItem<T>(val data: T) {
    @JvmField
    var bitmap: Bitmap? = null

    @JvmField
    var titleText = ""

    @JvmField
    var bitmapUrl = ""  //emoji bitmap url

    var bitmapRect = Rect() //emoji bitmap rect
    var toRect = Rect()
    var isSelected = false
    var diff = 0
}