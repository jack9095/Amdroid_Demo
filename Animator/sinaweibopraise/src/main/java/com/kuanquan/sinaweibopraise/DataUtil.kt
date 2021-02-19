package com.kuanquan.sinaweibopraise

fun getData(): MutableList<PraiseData> {
    val data = mutableListOf<PraiseData>()

    data.add(
        PraiseData(
            "1",
            "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2212937034,3962786012&fm=26&gp=0.jpg",
            "难过"
        )
    )
    data.add(
        PraiseData(
            "2",
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3186741781,1189607048&fm=26&gp=0.jpg",
            "大哭"
        )
    )
    data.add(
        PraiseData(
            "3",
            "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=361282702,4257151640&fm=26&gp=0.jpg",
            "经典微笑"
        )
    )
    data.add(
        PraiseData(
            "4",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3988441745,3058849416&fm=26&gp=0.jpg",
            "流汗"
        )
    )
    data.add(
        PraiseData(
            "5",
            "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2690554165,2425986965&fm=26&gp=0.jpg",
            "惊恐"
        )
    )
    return data
}