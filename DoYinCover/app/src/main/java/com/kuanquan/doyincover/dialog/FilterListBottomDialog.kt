package com.kuanquan.doyincover.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kuanquan.doyincover.R
import com.kuanquan.doyincover.publisher.view.NoSnapItemAnimator
import com.kuanquan.doyincover.publisher.view.SpaceDecoration
import com.kuanquan.doyincover.utils.CenterLayoutManager
import com.kuanquan.doyincover.utils.ScreenUtils
import com.qiniu.pili.droid.shortvideo.PLBuiltinFilter


/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2018/12/17
 * Description:
 */

class FilterListBottomDialog constructor(context: Context) : Dialog(context, R.style.BottomDialog) {

  private val recycler by lazy { findViewById<RecyclerView>(R.id.recycler)}

  val filterListDialogAdapter = FilterListDialogAdapter()

  var listener: OnFilterDialogListener? = null

  val mCenterLayoutManager: CenterLayoutManager by lazy {
    val layout = CenterLayoutManager(context)
    layout.orientation = RecyclerView.HORIZONTAL
    layout
  }


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    window?.setGravity(Gravity.BOTTOM)
    window?.setWindowAnimations(R.style.BottomDialog_Animation)
    setContentView(R.layout.dialog_filter_list)
    setCanceledOnTouchOutside(true)

    recycler.run{
      layoutManager = mCenterLayoutManager
      addItemDecoration(SpaceDecoration(ScreenUtils.dip2px(this.context, 15f)))
      itemAnimator = NoSnapItemAnimator()
      adapter = filterListDialogAdapter
    }

    filterListDialogAdapter.setOnItemClickListener { adapter, view, position ->
      listener?.onSelectPosition(position)
    }

    isCreated = true
  }

  private var isCreated = false

  fun initData(list: MutableList<PLBuiltinFilter>) {
    filterListDialogAdapter.setNewData(list)
  }

  override fun show() {
    super.show()
    val layoutParams = window?.attributes
    layoutParams?.gravity = Gravity.BOTTOM
    layoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT
    layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT

    window?.decorView?.setPadding(0, 0, 0, 0)

    window?.attributes = layoutParams
  }

  fun setFilterPosition(position: Int) {
    if (isCreated && filterListDialogAdapter.selectPosition != position) {
//      recycler.smoothScrollToPosition(position)
      val temp = filterListDialogAdapter.selectPosition
      filterListDialogAdapter.selectPosition = position
      filterListDialogAdapter.notifyItemChanged(position)
      if (temp > RecyclerView.NO_POSITION) {
        filterListDialogAdapter.notifyItemChanged(temp)
      }
    }
  }

  fun scrollFilter(position: Int){
    recycler.smoothScrollToPosition(position)
  }

  fun getSelectPosition(): Int {
    return filterListDialogAdapter.selectPosition
  }

  interface OnFilterDialogListener {
    fun onSelectPosition(position: Int)
  }


}



