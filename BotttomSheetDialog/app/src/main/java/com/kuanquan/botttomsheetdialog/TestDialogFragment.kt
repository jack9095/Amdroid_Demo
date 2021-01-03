package com.kuanquan.botttomsheetdialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Outline
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_fragment_layout.*

class TestDialogFragment : BaseDialogFragment() {

    companion object {

        const val PARAMS_COUNT = "params_title"

        @JvmStatic
        fun newInstance() = TestDialogFragment().apply {
            arguments = Bundle().apply {
                putString(PARAMS_COUNT, "测试")
            }
        }

        private val ACTION_SHEET_RATIO = 0.75f
        private val ACTION_DIMAMOUNT = 0.5f
    }

    private var mBehavior: BottomSheetBehavior<*>? = null

    private val list by lazy {
        mutableListOf<TestData>().apply {
            for (item in 0..19) {
                add(TestData("天龙八部", "金庸写的一部小说，当年风靡一时，炙手可热"))
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
       val dialogfragment = BottomSheetDialog(requireContext())
//        val view1 = View.inflate(context, R.layout.dialog_fragment_layout, null)
//        view?.let { dialogfragment.setContentView(it) }
//        mBehavior = BottomSheetBehavior.from(view?.parent as View)
//        mBehavior?.setPeekHeight(height())
        return dialogfragment
    }

    override fun onStart() {
        super.onStart()
        mBehavior = BottomSheetBehavior.from(view?.parent as View)
        mBehavior?.setPeekHeight(height())
        mBehavior?.setState(BottomSheetBehavior.STATE_EXPANDED) //全屏展开
    }

    override val layoutId: Int
        get() = R.layout.dialog_fragment_layout

    override fun canceledOnTouchOutside(): Boolean {
        return true
    }

    private fun width(): Int {
        return WindowManager.LayoutParams.MATCH_PARENT
    }

    private fun height(): Int {
        return (getScreenHeight(context) * ACTION_SHEET_RATIO).toInt()
    }

    private fun gravity(): Int {
        return Gravity.BOTTOM
    }

    override fun setupWindow(window: Window?) {
        window?.attributes?.run {
            dimAmount = ACTION_DIMAMOUNT
            gravity = gravity()
            height = height()
            width = width()
            window.attributes = this
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint
    override fun initViews(view: View) {
//        view?.let { dialogfragment.setContentView(it) }
//        mBehavior = BottomSheetBehavior.from(view.parent as View)
//        mBehavior?.setPeekHeight(height())
        setMaxHeight(height())
//        arguments?.run {
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
            recyclerView.adapter = TestAdapter(list)
//        }
        rootView.run {
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setRoundRect(0, 0, view.width, view.height, dp2px(view.context, 16f).toFloat())
                }
            }
            clipToOutline = true
        }

        titleTv.setOnClickListener {
            dismiss()
        }
    }

}

