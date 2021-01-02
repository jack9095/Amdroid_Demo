package com.kuanquan.botttomsheetdialog

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

class FragmentDelegates(fragment: Fragment?) {

    private var mFragmentRef = WeakReference(fragment)

    var maxHeight: Int = -1

    fun onCreateView(
        view: View?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view?.context ?: return view

        if (maxHeight > 0) {
            return MaxHeightFrameLayout(view.context).apply {
                addView(view)
                maxHeight = this@FragmentDelegates.maxHeight
            }
        }
        return view
    }

    fun getFragment() = mFragmentRef.get()
}

internal class MaxHeightFrameLayout(context: Context) : FrameLayout(context) {

    var maxHeight: Int = -1

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightSpec = if (maxHeight > 0) {
            MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST)
        } else {
            heightMeasureSpec
        }
        super.onMeasure(widthMeasureSpec, heightSpec)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }
}