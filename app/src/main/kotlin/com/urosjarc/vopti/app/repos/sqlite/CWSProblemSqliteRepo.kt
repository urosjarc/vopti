package com.urosjarc.vopti.app.repos.sqlite

import com.urosjarc.vopti.core.algos.cws.CWSProblem
import com.urosjarc.vopti.core.domain.Id
import com.urosjarc.vopti.core.repos.CWSProblemRepo

class CWSProblemSqliteRepo : CWSProblemRepo {
    init {
        sqlite.transaction {
            it.table.create<CWSProblem>(throws = false)
        }
    }

    override fun get(): List<CWSProblem> {
        var problems = listOf<CWSProblem>()
        sqlite.transaction {
            problems = it.table.select<CWSProblem>()
        }
        return problems
    }

    override fun get(id: Id<CWSProblem>): CWSProblem {
        var problem: CWSProblem? = null
        sqlite.transaction {
            problem = it.row.select(pk = id)
        }
        return problem!!
    }

    override fun save(data: CWSProblem) {
        sqlite.transaction {
            it.row.insert(row = data)
        }
    }

    override fun save(data: Iterable<CWSProblem>) {
        sqlite.transaction {
            it.table.delete<CWSProblem>()
            it.batch.insert(data)
        }
    }

    override fun update(data: CWSProblem) {
        sqlite.transaction {
            it.row.update(row = data)
        }
    }

    override fun delete(data: CWSProblem) {
        sqlite.transaction {
            it.row.delete(row = data)
        }
    }

    override fun delete() {
        sqlite.transaction {
            it.table.delete<CWSProblem>()
        }
    }
}
