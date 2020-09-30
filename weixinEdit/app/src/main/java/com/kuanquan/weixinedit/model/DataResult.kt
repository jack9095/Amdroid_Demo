package com.kuanquan.weixinedit.model

import java.io.Serializable

class DataResult{
//    var results: List<DataModel>? = null

    inner class DataModel : Serializable {
        var createdAt: String? = null
        var publishedAt: String? = null
        var _id: String? = null
        var source: String? = null
        var used: Boolean = false
        var type: String? = null
        var url: String? = null
        var desc: String? = null
        var who: String? = null
    }
}