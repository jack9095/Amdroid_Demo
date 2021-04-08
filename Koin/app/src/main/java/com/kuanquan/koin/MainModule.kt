package com.kuanquan.koin

import org.koin.core.qualifier.named
import org.koin.dsl.module

object MainModule {
    val mainModule = module {
        //单例
        single {
            DbService()
        }

        // 每次都返回新的对象
        factory {
            DbService()
        }

        //指定注入范围
        scope(named<MainActivity>()) {
            scoped {
                MainPresenter(get(), get())
            }
        }
    }
}