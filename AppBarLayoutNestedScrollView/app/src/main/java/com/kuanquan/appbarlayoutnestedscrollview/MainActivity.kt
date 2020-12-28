package com.kuanquan.appbarlayoutnestedscrollview

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.kuanquan.appbarlayoutnestedscrollview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding.root)
        viewBinding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
//            val realDistance = (appBarLayout?.totalScrollRange ?: 0).toInt() - abs(verticalOffset)

//            val lp = viewBinding.imageView.layoutParams as RelativeLayout.LayoutParams
//               lp.setMargins(0, 0, 0, realDistance)
//            viewBinding.imageView.layoutParams = lp
        })

        topView()

        doubleFeedView()
    }

    private fun topView() {
        val myAdapter = MyAdapter(this@MainActivity, getDefaultData())
        viewBinding.gridView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 3)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    super.getItemOffsets(outRect, view, parent, state)
                    val position = parent.getChildAdapterPosition(view)
                    val column = position % 3
                    outRect.left = column * 18 / 3
                    outRect.right = 18 - (column + 1) * 18 / 3
                    outRect.bottom = 18
                }
            })
            adapter = myAdapter
        }

        myAdapter.setOnClickListener(object : MyAdapter.OnClickListener {
            override fun onClick(v: View?, position: Int) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("url", myAdapter.getData()?.get(position))
                startActivity(
                    intent,
                    ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, v, "sharedView")
                        .toBundle()
                )
            }
        })
    }

    private fun doubleFeedView() {
        val myAdapter = MyAdapter(this@MainActivity, getDefaultData())
        viewBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect.bottom = 18
                }
            })
            adapter = myAdapter
        }

        myAdapter.setOnClickListener(object : MyAdapter.OnClickListener {
            override fun onClick(v: View?, position: Int) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("url", myAdapter.getData()?.get(position))
                startActivity(
                    intent,
                    ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, v, "sharedView")
                        .toBundle()
                )
            }
        })
    }

}
