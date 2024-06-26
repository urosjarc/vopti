package com.urosjarc.vopti.gui.caches

import com.urosjarc.vopti.core.algos.cws.CWSProblem
import com.urosjarc.vopti.core.repos.CWSProblemRepo
import org.koin.core.component.inject

class CWSProblemCache : Cache<CWSProblem>() {

    val repo by this.inject<CWSProblemRepo>()

    override fun init() {
        this.load()
    }
    override fun save() {
        repo.save(data = this.data)
    }

    override fun load() {
        this.set(repo.get())
    }
}
