package com.kuanquan.pagetransitionanimation.elementspage

import android.annotation.TargetApi
import android.app.Activity
import android.app.SharedElementCallback
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.kuanquan.pagetransitionanimation.databinding.ActivityShareElementsBinding
import com.kuanquan.pagetransitionanimation.viewpager.PhotoViewerFragment


class ShareElementsActivityOther : AppCompatActivity() {
    lateinit var viewBinding: ActivityShareElementsBinding
    lateinit var rootView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        super.onCreate(savedInstanceState)

        viewBinding = ActivityShareElementsBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding.root)

        supportPostponeEnterTransition() //延缓执行 然后在fragment里面的控件加载完成后start


        //将内部的布局 item_linear_layout，放入 AnimationFrameLayout 中
//        val view = LayoutInflater.from(this).inflate(R.layout.item_linear_layout, viewBinding.frameLayout)
//        view.setBackgroundColor(Color.TRANSPARENT)
//        rootView = findViewById<LinearLayout>(R.id.parent)

        val urls = intent.getSerializableExtra("url") as? ArrayList<String>

        val position = intent.getIntExtra("index",0)

//        viewBinding.frameLayout.setFinishListener(object : AnimationFrameLayout.FinishListener {
//            override fun gofinish() {
//                onBackPressed()
//            }
//
//            override fun setBackgroundColor(color: Int) {
//                viewBinding.viewpager.setBackgroundColor(color)
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

        val mInnerAdapter = InnerAdapter(supportFragmentManager, urls)

        viewBinding.viewpager.adapter = mInnerAdapter

        viewBinding.viewpager.currentItem = position


        //这个可以看做个管道  每次进入和退出的时候都会进行调用  进入的时候获取到前面传来的共享元素的信息
        //退出的时候 把这些信息传递给前面的activity
        //同时向sharedElements里面put view,跟对view添加transitionname作用一样
        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: List<String>, sharedElements: MutableMap<String, View>) {
                val url: String = urls?.get(viewBinding.viewpager.currentItem) ?: ""
                val fragment: PhotoViewerFragment = mInnerAdapter.instantiateItem(viewBinding.viewpager, viewBinding.viewpager.currentItem) as PhotoViewerFragment
                sharedElements.clear()
                sharedElements[url] = fragment.getSharedElement()
            }
        })
    }

    @TargetApi(22)
    override fun supportFinishAfterTransition() {
        val data = Intent()
        data.putExtra("index", viewBinding.viewpager.currentItem)
        setResult(Activity.RESULT_OK, data)
        super.supportFinishAfterTransition()
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        val data = Intent()
        data.putExtra("index", viewBinding.viewpager.currentItem)
        setResult(Activity.RESULT_OK, data)
        super.supportFinishAfterTransition()
    }

    private class InnerAdapter(fm: FragmentManager, val url: ArrayList<String>?) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
//            if (position == 1) {
//                return DemoFragment()
//            } else {
                val fragment = PhotoViewerFragment()
                val bundle = Bundle()
                bundle.putInt("key", position)
                bundle.putSerializable("data", url)
                fragment.arguments = bundle
                return fragment
//            }
        }

        override fun getCount(): Int {
            return 10
        }

    }
}