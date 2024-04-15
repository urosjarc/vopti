package com.urosjarc.vopti.app.problem

import com.urosjarc.vopti.shared.Repository
//import com.urosjarc.vopti.shared.sqlite

class ProblemRepo : Repository<Problem>() {

    init {
        this.load()
    }

    override fun load() {
//        sqlite.autocommit {
//            this.set(it.table.select<Problem>())
//        }
    }

    override fun save() {
//        sqlite.autocommit {
//            it.row.update(rows = this.data)
//        }
    }
}
