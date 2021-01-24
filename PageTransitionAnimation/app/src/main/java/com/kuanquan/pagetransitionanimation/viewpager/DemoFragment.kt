package com.kuanquan.pagetransitionanimation.viewpager

import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.kuanquan.pagetransitionanimation.AnimationFrameLayout
import com.kuanquan.pagetransitionanimation.R

class DemoFragment: BaseFragment() {
    override val layoutId: Int
        get() = R.layout.fragment_layout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        val mAnimationFrameLayout = view?.findViewById<AnimationFrameLayout>(R.id.frame_layout)
//        val mRelativeLayout = view?.findViewById<RelativeLayout>(R.id.parent)
//        val imageView = view?.findViewById<ImageView>(R.id.image)
//
//        mAnimationFrameLayout?.setFinishListener(object : AnimationFrameLayout.FinishListener {
//            override fun gofinish() {
//                onBackPressed()
//            }
//
//            override fun setBackgroundColor(color: Int) {
//                mRelativeLayout?.setBackgroundColor(color)
//            }
//
//            override fun setRestitution(isRestitution: Boolean) {
//                if (isRestitution) {
////                    view.findViewById<LinearLayout>(R.id.linearLayout).visibility = View.VISIBLE
//                } else {
////                    view.findViewById<LinearLayout>(R.id.linearLayout).visibility = View.INVISIBLE
//                }
//            }
//        })

        val url = arguments?.get("key")
//        if (imageView != null) {
//            Glide.with(this).load(url).into(imageView)
//        }
    }
}