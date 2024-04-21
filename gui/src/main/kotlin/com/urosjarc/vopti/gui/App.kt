package com.urosjarc.vopti.gui

import com.urosjarc.vopti.app.repos.sqlite.CWSProblemSqliteRepo
import com.urosjarc.vopti.core.repos.CWSProblemRepo
import com.urosjarc.vopti.gui.caches.CWSProblemCache
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

object App {
    fun modul() = module {
        this.single<CWSProblemRepo> { CWSProblemSqliteRepo() }
        this.single<CWSProblemCache> { CWSProblemCache() }
    }

    fun init() {
        startKoin { this.modules(modul()) }
    }

    fun reset() = stopKoin()
}
