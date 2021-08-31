package com.kuanquan.dialogfragmentstyle

import android.os.Bundle
import android.view.*
import android.view.animation.AlphaAnimation
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.fragment_browse_history.*

class ADialogFragment: DialogFragment() {
    companion object {
        fun newInstance(goodsId: String?, styleId: String?, skuId: String?): ADialogFragment {
            return ADialogFragment().apply {
                arguments = bundleOf(
                    "" to goodsId,
                    "" to styleId,
                    "" to skuId
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.historyDialog)

    }

    private fun initImmersionBar() {
        ImmersionBar.with(this)
//            .fitsSystemWindows(true)
            .statusBarColor(R.color.color_f2f2f2)
            .statusBarDarkFont(true, 0.2f)
            .init()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_browse_history, container, false).apply {
//            (this as? DetailBrowseHistoryView)?.setItemClick {
//                dismiss()
//            }
        }
    }

    override fun onStart() {
        super.onStart()
        val params = dialog?.window?.attributes
        params?.gravity = Gravity.TOP
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        dialog?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        initImmersionBar()
        initViewModelObservers()

//        space_view

//        val dismissAlphaAnimation = AlphaAnimation(0F, 1F)
//        dismissAlphaAnimation.duration = 300
//        space_view?.startAnimation(dismissAlphaAnimation)
//        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
//        space_view?.visibility = View.VISIBLE
        space_view?.setOnClickListener {
//            val aaa = AlphaAnimation(1F, 0F)
//            aaa.duration = 80
//            space_view?.startAnimation(aaa)
//            dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
            space_view?.visibility = View.GONE
//            space_view?.postDelayed({
                dismiss()
//            },100)

        }
    }

    fun initViewModelObservers() {

    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            super.show(manager, tag)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}