package com.kuanquan.pagetransitionanimation.viewpager

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.kuanquan.pagetransitionanimation.AnimationFrameLayout
import com.kuanquan.pagetransitionanimation.R

class PhotoViewerFragment: BaseFragment() {
    override val layoutId: Int
        get() = R.layout.item_linear_layout

    var imageView: ImageView? = null
    var imageUrl: String = null ?: ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mAnimationFrameLayout = view?.findViewById<AnimationFrameLayout>(R.id.frame_layout)
        val mRelativeLayout = view?.findViewById<RelativeLayout>(R.id.parent)
        imageView = view?.findViewById<ImageView>(R.id.image)
        val position = arguments?.get("key") as Int

        mAnimationFrameLayout?.setFinishListener(object : AnimationFrameLayout.FinishListener {
            override fun gofinish() {
                onBackPressed()
            }

            override fun setBackgroundColor(color: Int) {
                mRelativeLayout?.setBackgroundColor(color)
            }

            override fun setRestitution(isRestitution: Boolean) {
                if (isRestitution) {
//                    view.findViewById<LinearLayout>(R.id.linearLayout).visibility = View.VISIBLE
                } else {
//                    view.findViewById<LinearLayout>(R.id.linearLayout).visibility = View.INVISIBLE
                }
            }
        })

        val data = arguments?.get("data") as? ArrayList<String>?

        imageUrl = data?.get(position) ?: ""
        if (imageView != null) {
            Glide.with(this).load(imageUrl).into(imageView!!)
        }

        // TODO 1. 共享元素动画
        view!!.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                imageView!!.viewTreeObserver.removeOnPreDrawListener(this)
                activity!!.supportStartPostponedEnterTransition()
                return true
            }
        })
    }


    // TODO 2. 共享元素动画
    fun getSharedElement(): View {
        return imageView!!
    }

}