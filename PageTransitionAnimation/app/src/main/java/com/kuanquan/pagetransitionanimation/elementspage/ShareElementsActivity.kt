package com.kuanquan.pagetransitionanimation.elementspage

import android.annotation.TargetApi
import android.app.Activity
import android.app.SharedElementCallback
import android.content.Intent
import android.os.Bundle
import android.transition.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.kuanquan.pagetransitionanimation.databinding.ActivityShareElementsBinding
import com.kuanquan.pagetransitionanimation.viewpager.PhotoViewerFragment


class ShareElementsActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityShareElementsBinding
    private var mInnerAdapter: InnerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        super.onCreate(savedInstanceState)

        viewBinding = ActivityShareElementsBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding.root)

        // TODO 1. 共享元素动画
//        supportPostponeEnterTransition() //延缓执行 然后在fragment里面的控件加载完成后start

        val urls = intent.getSerializableExtra("url") as? ArrayList<String>

        val position = intent.getIntExtra("index",0)

        mInnerAdapter = InnerAdapter(supportFragmentManager, urls)

        viewBinding.viewpager.adapter = mInnerAdapter

        viewBinding.viewpager.currentItem = position

        window.sharedElementEnterTransition.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {
                Log.e("共享元素动画的实现 Enter -> ", "onTransitionStart")
            }

            override fun onTransitionEnd(transition: Transition) {
                Log.e("共享元素动画的实现 Enter -> ", "onTransitionEnd")
            }

            override fun onTransitionCancel(transition: Transition) {
                Log.e("共享元素动画的实现 Enter -> ", "onTransitionCancel")
            }

            override fun onTransitionPause(transition: Transition) {
                Log.e("共享元素动画的实现 Enter -> ", "onTransitionPause")
            }

            override fun onTransitionResume(transition: Transition) {
                Log.e("共享元素动画的实现 Enter -> ", "onTransitionResume")
            }
        })

        window.sharedElementExitTransition.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {
                Log.e("共享元素动画的实现 exit -> ", "onTransitionStart")
            }

            override fun onTransitionEnd(transition: Transition) {
                Log.e("共享元素动画的实现 exit -> ", "onTransitionEnd")
            }

            override fun onTransitionCancel(transition: Transition) {
                Log.e("共享元素动画的实现 exit -> ", "onTransitionCancel")
            }

            override fun onTransitionPause(transition: Transition) {
                Log.e("共享元素动画的实现 exit -> ", "onTransitionPause")
            }

            override fun onTransitionResume(transition: Transition) {
                Log.e("共享元素动画的实现 exit -> ", "onTransitionResume")
            }
        })


        // TODO 2. 共享元素动画
        //这个可以看做个管道  每次进入和退出的时候都会进行调用  进入的时候获取到前面传来的共享元素的信息
        //退出的时候 把这些信息传递给前面的activity
        //同时向sharedElements里面put view,跟对view添加transitionname作用一样
        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: List<String>, sharedElements: MutableMap<String, View>) {
                val url: String = urls?.get(viewBinding.viewpager.currentItem) ?: ""
                val fragment: PhotoViewerFragment = mInnerAdapter?.instantiateItem(viewBinding.viewpager, viewBinding.viewpager.currentItem) as PhotoViewerFragment
                sharedElements.clear()
                sharedElements[url] = fragment.getSharedElement()
            }
        })

//        setExitSharedElementCallback(object : SharedElementCallback() {
//            override fun onMapSharedElements(names: List<String>, sharedElements: MutableMap<String, View>) {
//                val url: String = urls?.get(viewBinding.viewpager.currentItem) ?: ""
//                val fragment: PhotoViewerFragment = mInnerAdapter?.instantiateItem(viewBinding.viewpager, viewBinding.viewpager.currentItem) as PhotoViewerFragment
//                sharedElements.clear()
//                sharedElements[url] = fragment.getSharedElement()
//            }
//        })

        val mtransitionset = TransitionSet() //制定过度动画set

        mtransitionset.addTransition(ChangeBounds()) //改变表框大小

//        mtransitionset.addTransition(null) //改变表框大小

        mtransitionset.addTransition(ChangeImageTransform()) //图片移动，还可以是其他的，要什么效果自己添加
//        mtransitionset.addTransition(null) //图片移动，还可以是其他的，要什么效果自己添加

//        mtransitionset.duration = 500

        //注意，下面是必须的
//        getWindow().setEnterTransition(mtransitionset)
//        getWindow().setExitTransition(mtransitionset)
//        getWindow().setSharedElementEnterTransition(mtransitionset)
//        getWindow().setSharedElementExitTransition(mtransitionset)


//        分解效果
//        window.enterTransition = Explode().setDuration(0)
//        window.exitTransition = Explode().setDuration(0)

//        滑动效果
//        window.enterTransition = Slide().setDuration(0)
//        window.exitTransition = Slide().setDuration(0)

//        淡入淡出效果
//        window.enterTransition = Fade().setDuration(0)
//        window.exitTransition = Fade().setDuration(0)
    }

//    fun setElementCallback(fragment: PhotoViewerFragment, url: String){
//        // TODO 2. 共享元素动画
//        //这个可以看做个管道  每次进入和退出的时候都会进行调用  进入的时候获取到前面传来的共享元素的信息
//        //退出的时候 把这些信息传递给前面的activity
//        //同时向sharedElements里面put view,跟对view添加transitionname作用一样
//        setEnterSharedElementCallback(object : SharedElementCallback() {
//            override fun onMapSharedElements(names: List<String>, sharedElements: MutableMap<String, View>) {
////                val url: String = urls?.get(viewBinding.viewpager.currentItem) ?: ""
////                val fragment: PhotoViewerFragment = mInnerAdapter.instantiateItem(viewBinding.viewpager, viewBinding.viewpager.currentItem) as PhotoViewerFragment
//                sharedElements.clear()
//                sharedElements[url] = fragment.getSharedElement()
//            }
//        })
//    }

    @TargetApi(22)
    override fun supportFinishAfterTransition() {
        val data = Intent()
        data.putExtra("index", viewBinding.viewpager.currentItem)
        setResult(Activity.RESULT_OK, data)
        super.supportFinishAfterTransition()
    }

    override fun onBackPressed() {
//        super.onBackPressed()
//        val data = Intent()
//        data.putExtra("index", viewBinding.viewpager.currentItem)
//        setResult(Activity.RESULT_OK, data)

        // TODO 3. 共享元素动画
        supportFinishAfterTransition()
//        super.supportFinishAfterTransition()
    }

    private class InnerAdapter(fm: FragmentManager, val url: ArrayList<String>?) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
                val fragment = PhotoViewerFragment()
                val bundle = Bundle()
                bundle.putInt("key", position)
                bundle.putSerializable("data", url)
                fragment.arguments = bundle
                return fragment
        }

        override fun getCount(): Int {
            return 10
        }

    }

    override fun onPostResume() {
        super.onPostResume()
//        val fragment: PhotoViewerFragment = mInnerAdapter?.instantiateItem(viewBinding.viewpager, viewBinding.viewpager.currentItem) as PhotoViewerFragment
//        fragment.imageView?.visibility = View.GONE
//        fragment.image_other?.bringToFront()
    }
}