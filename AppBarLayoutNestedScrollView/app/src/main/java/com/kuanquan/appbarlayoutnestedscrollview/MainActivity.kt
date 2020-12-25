package com.kuanquan.appbarlayoutnestedscrollview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.google.android.material.appbar.AppBarLayout
import com.kuanquan.appbarlayoutnestedscrollview.databinding.ActivityMainBinding
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding.root)
        viewBinding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                  val realDistance = (appBarLayout?.totalScrollRange ?: 0).toInt() - abs(verticalOffset)

            val lp = viewBinding.imageView.layoutParams as RelativeLayout.LayoutParams
               lp.setMargins(0, 0, 0, realDistance)
            viewBinding.imageView.layoutParams = lp
        })
    }
}
