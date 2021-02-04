package com.kuanquan.pagetransitionanimation

import android.app.SharedElementCallback
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kuanquan.pagetransitionanimation.adapter.MainAdapter
import com.kuanquan.pagetransitionanimation.bean.MainData
import com.kuanquan.pagetransitionanimation.elementspage.ShareElementsActivity
import com.kuanquan.pagetransitionanimation.util.DataUtil.getMainData
import com.kuanquan.pagetransitionanimation.viewpager.BaseFragment
import java.io.Serializable

class MainFragment: BaseFragment(), MainAdapter.OnAdapterItemClickListener {

    var datas: MutableList<MainData> = getMainData()
    private val myAdapter: MainAdapter by lazy { MainAdapter(datas) }
    val linearLayoutManager: LinearLayoutManager by lazy { LinearLayoutManager(requireActivity()) }
    var bundle: Bundle? = null

    override val layoutId = R.layout.fragment_main

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        myAdapter.setOnAdapterItemClickListener(this)
        val recyclerView: RecyclerView = view!!.findViewById(R.id.recycler_view)
        recyclerView.adapter = myAdapter
        recyclerView.layoutManager = linearLayoutManager

        // TODO 2. 共享元素动画  必备的步骤
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.setExitSharedElementCallback(object : SharedElementCallback() {
                override fun onMapSharedElements(names: MutableList<String?>, sharedElements: MutableMap<String, View>) {
                    if (bundle != null) {
                        val position: Int = bundle?.getInt("index", 0) ?: 0
                        Log.e("onMapSharedElements", "position = $position")
                        sharedElements.clear()
                        names.clear()
                        val parentItemView = linearLayoutManager.findViewByPosition(parentPosition)

                       val childRecyclerView = parentItemView?.findViewById<RecyclerView>(R.id.recyclerView)

                        val itemView: View = childRecyclerView?.layoutManager?.findViewByPosition(position)!!

//                        val itemView: View = myAdapter?.gridLayoutManager?.findViewByPosition(position)!!

                        val imageView = itemView.findViewById<ImageView>(R.id.image_iv)
                        names.add(itemView.transitionName)
                        //注意这里第二个参数，如果防止是的条目的item则动画不自然。放置对应的imageView则完美
                        sharedElements[datas[parentPosition].list[position]] = imageView
                        bundle = null
                    }
                }
            })
        }
    }

    var parentPosition = 0
    override fun onClick(position: Int, view: View, parentPosition: Int) {
        this.parentPosition = parentPosition

        val intent = Intent(context, ShareElementsActivity::class.java)
        intent.putExtra("url", datas[parentPosition].list as Serializable?)
        intent.putExtra("index", position)

        // TODO 1. 共享元素动画  必备的步骤
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), view, datas[parentPosition].list[position]).toBundle()
        startActivity(intent, options)
    }
}