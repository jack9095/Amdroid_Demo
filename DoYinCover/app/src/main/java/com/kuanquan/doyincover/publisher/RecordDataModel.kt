package com.shuashuakan.android.modules.publisher

import android.os.Parcel
import android.os.Parcelable

/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2018/12/18
 * Description:
 */
data class RecordDataModel(var filePath: String?,
                           var coverDuration: Int?,
                           var recordType: Int,
                           var masterFeedId: String? = null,
                           var channelId:String?="",
                           var channelName:String?="") : Parcelable {
  constructor(source: Parcel) : this(
      source.readString(),
      source.readValue(Int::class.java.classLoader) as Int?,
      source.readInt(),
      source.readString(),
      source.readString(),
      source.readString()
  )

  override fun describeContents() = 0

  override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
    writeString(filePath)
    writeValue(coverDuration)
    writeInt(recordType)
    writeString(masterFeedId)
    writeString(channelId)
    writeString(channelName)
  }

  companion object {
    @JvmField
    val CREATOR: Parcelable.Creator<RecordDataModel> = object : Parcelable.Creator<RecordDataModel> {
      override fun createFromParcel(source: Parcel): RecordDataModel = RecordDataModel(source)
      override fun newArray(size: Int): Array<RecordDataModel?> = arrayOfNulls(size)
    }
  }
}