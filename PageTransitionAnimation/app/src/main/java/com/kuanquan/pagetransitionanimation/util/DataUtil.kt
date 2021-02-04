package com.kuanquan.pagetransitionanimation.util

import com.kuanquan.pagetransitionanimation.bean.MainData
import java.util.*

object DataUtil {

    fun getData(): ArrayList<String>{
        val datas =  ArrayList<String>()
        datas.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2478350582,3338695212&fm=26&gp=0.jpg")
        datas.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3485183293,847227336&fm=26&gp=0.jpg")
        datas.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2037944750,611917901&fm=26&gp=0.jpg")
        datas.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1745933341,3133887881&fm=26&gp=0.jpg")
        datas.add("http://b162.photo.store.qq.com/psb?/V14EhGon4cZvmh/z2WukT5EhNE76WtOcbqPIgwM2Wxz4Tb7Nub.rDpsDgo!/b/dOaanmAaKQAA")
        datas.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3576736246,2692877583&fm=26&gp=0.jpg")
        datas.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2354651587,2307656983&fm=26&gp=0.jpg")
        datas.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3903227663,309999411&fm=26&gp=0.jpg")
        datas.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2233497321,4233551410&fm=26&gp=0.jpg")
        datas.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3355332996,90856357&fm=26&gp=0.jpg")

        return datas
    }

    fun getMainData(): MutableList<MainData>{
        val data =  mutableListOf<MainData>()
        data.add(MainData("笑傲江湖", getData()))
        data.add(MainData("天龙八部", getData1()))
        data.add(MainData("碧血剑", getData()))
        data.add(MainData("书剑恩仇录", getData()))
        data.add(MainData("鹿鼎记", getData()))
        data.add(MainData("飞狐外传", getData()))
        data.add(MainData("雪鹰领主", getData()))
        data.add(MainData("遮天", getData()))
        data.add(MainData("完美世界", getData()))
        data.add(MainData("修罗到底", getData()))
        data.add(MainData("侠客行", getData()))

        return data
    }


    fun getData1(): ArrayList<String>{
        val datas =  ArrayList<String>()
        datas.add("https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3363295869,2467511306&fm=26&gp=0.jpg")
        datas.add("https://dss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1603365312,3218205429&fm=26&gp=0.jpg")
        datas.add("https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2771978851,2906984932&fm=26&gp=0.jpg")
        datas.add("https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2583035764,1571388243&fm=26&gp=0.jpg")
        datas.add("https://dss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1089874897,1268118658&fm=26&gp=0.jpg")
        datas.add("https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1963304009,2816364381&fm=26&gp=0.jpg")
        datas.add("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=381858117,588155436&fm=26&gp=0.jpg")
        datas.add("https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1021698570,4055706443&fm=26&gp=0.jpg")
        datas.add("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1946764445,117598738&fm=26&gp=0.jpg")
        datas.add("https://dss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1921096633,3188797021&fm=26&gp=0.jpg")

        return datas
    }
}