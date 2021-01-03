package com.kuanquan.botttomsheetdialog

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager

/**
 * 对页面的各种事件和 UI 操作转换为对 Window 和 Dialog 的操作，更加灵活。
 * 常用方法抽象出来，方便使用。
 * 如无必要，不必新增方法，可以考虑通过调用 [setupWindow]、[setupDialog] 直接对 Window 和 Dialog 进行设置。
 * 根布局用 [getView] 或 [initViews] 的入参 view 获取，Context 用 [getContext] 获取。
 * 如果想要构建自定义 Dialog，可用 [createDialog] 构建。
 */
abstract class BaseDialogFragment : AppCompatDialogFragment() {

    companion object {

        const val WINDOW_ANIM_DEFAULT = -1

        @JvmField
        val WINDOW_ANIM_BOTTOM_IN_BOTTOM_OUT = R.style.DialogBottomStyle

        @JvmField
        val WINDOW_ANIM_LEFT_IN_LEFT_OUT = R.style.DialogLeftStyle

        @JvmField
        val WINDOW_ANIM_CENTER_SCALE = R.style.DialogCenterScale

        @JvmField
        val WINDOW_ANIM_RIGHT_IN_RIGHT_OUT = R.style.DialogRightStyle

        @JvmField
        val WINDOW_ANIM_TOP_IN_TOP_OUT = R.style.DialogTopStyle

        private const val SAVED_MARGIN_ARRAY = "BaseDialogFragment:marginArray"

        private const val SAVED_MAX_HEIGHT = "BaseDialogFragment:maxHeight"
    }

    protected abstract val layoutId: Int

    private val mMarginArray by lazy { intArrayOf(0, 0, 0, 0) }

    private val mDelegates by lazy { FragmentDelegates(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showsDialog = true
        initData()
        initViewModel()
    }

    protected open fun initViewModel() {

    }

    protected open fun initData() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutId, null, false)
        return mDelegates.onCreateView(view, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    protected abstract fun initViews(view: View)

    protected open fun setupDialog(dialog: Dialog?) {
        dialog?.setCanceledOnTouchOutside(canceledOnTouchOutside())
    }

    protected open fun setupWindow(window: Window?) {

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val d = createDialog().takeIf { it != null } ?: let {
            super.onCreateDialog(savedInstanceState)
        }
        return d.apply { setupDialog(this) }
    }

    protected open fun createDialog(): Dialog? = null

    open fun show(manager: FragmentManager?) {
        manager ?: return

        if (isAdded) {
            return
        }

        if (manager.findFragmentByTag(javaClass.canonicalName) != null) {
            return
        }
        showInternal(manager)
    }

    private fun showInternal(manager: FragmentManager) {
        try {
            show(manager, javaClass.canonicalName)
        } catch (e: Exception) {
        }
    }

    override fun dismiss() {
        try {
            dismissAllowingStateLoss()
        } catch (e: Exception) {
        }
    }

    private fun setDefaultWindowAttrs(window: Window?) {
        window?.run {
            setBackgroundDrawableResource(android.R.color.transparent)
            addFlags(windowFlags())
            setWindowAnimations(windowAnimations())

            window.attributes = attributes?.apply {
                setWindowAttrs(this)
            }
        }
    }

    protected open fun windowFlags() = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
    protected open fun windowAnimations(): Int = WINDOW_ANIM_DEFAULT
    protected open fun canceledOnTouchOutside() = false

    private fun setWindowAttrs(attrs: WindowManager.LayoutParams) {
        with(attrs) {
            dimAmount = 0.5f
            gravity = Gravity.CENTER
            width = WindowManager.LayoutParams.MATCH_PARENT
        }
    }

    override fun onStart() {
        super.onStart()
        setDefaultWindowAttrs(dialog?.window)
        setupWindow(dialog?.window)
        setContentViewMargin()
    }

    private fun setContentViewMargin() {
        view?.startMargin = mMarginArray[0]
        view?.topMargin = mMarginArray[1]
        view?.endMargin = mMarginArray[2]
        view?.bottomMargin = mMarginArray[3]
    }

    protected fun setMargin(
        left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
        mMarginArray[0] = left
        mMarginArray[1] = top
        mMarginArray[2] = right
        mMarginArray[3] = bottom
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray(SAVED_MARGIN_ARRAY, mMarginArray)
        outState.putInt(SAVED_MAX_HEIGHT, mDelegates.maxHeight)
    }

    fun setMaxHeight(height: Int) {
        mDelegates.maxHeight = height
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        savedInstanceState?.run {
            val array = getIntArray(SAVED_MARGIN_ARRAY)
            array?.let {
                setMargin(array[0], array[1], array[2], array[3])
            }

            mDelegates.maxHeight = getInt(SAVED_MAX_HEIGHT)
        }
    }

}