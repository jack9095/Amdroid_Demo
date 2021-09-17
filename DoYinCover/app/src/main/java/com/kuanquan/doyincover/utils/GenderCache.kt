//package com.kuanquan.doyincover.utils
//
//import androidx.content.edit
//import com.shuashuakan.android.commons.cache.Storage
//import com.kuanquan.doyincover.utils.GenderCache.Gender.None
//import javax.inject.Inject
//import javax.inject.Singleton
//
//@Singleton
//class GenderCache @Inject constructor(val storage: Storage) {
//
//  companion object {
//    private const val GENDER_VALUE = "gender_value"
//  }
//
//  fun getValue(): Gender {
//    val genderValue = storage.appPreference.getInt(GENDER_VALUE, None.value)
//    return Gender.values().filter { it.value == genderValue }[0]
//  }
//
//  fun cacheValue(gender: Gender) {
//    storage.appPreference.edit(commit = true) {
//      putInt(GENDER_VALUE, gender.value)
//    }
//  }
//
//  enum class Gender(val value: Int) {
//    Male(0),
//    Female(1),
//    None(-1)
//  }
//}