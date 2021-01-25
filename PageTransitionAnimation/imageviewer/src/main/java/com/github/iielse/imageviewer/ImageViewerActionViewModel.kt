package com.github.iielse.imageviewer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

object ViewerActions {
    const val SET_CURRENT_ITEM = "setCurrentItem"
    const val DISMISS = "dismiss"
}

class ImageViewerActionViewModel : ViewModel() {

    val actionEvent = MutableLiveData<Pair<String, Any?>>()

    fun setCurrentItem(pos: Int) = internalHandle(ViewerActions.SET_CURRENT_ITEM, pos)
    fun dismiss() = internalHandle(ViewerActions.DISMISS, null)

    private fun internalHandle(action: String, extra: Any?) {
        actionEvent.value = Pair(action, extra)
        actionEvent.value = null
    }
}