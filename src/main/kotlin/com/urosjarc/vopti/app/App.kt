package com.urosjarc.vopti.app

import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

object App {
    fun modul() = module {
    }

    fun init() {
        startKoin { this.modules(modul()) }
    }

    fun reset() = stopKoin()
}
