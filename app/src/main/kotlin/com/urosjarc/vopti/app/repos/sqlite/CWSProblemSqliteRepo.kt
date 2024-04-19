package com.urosjarc.vopti.app.repos.sqlite

import com.urosjarc.vopti.core.algos.cws.CWSProblem
import com.urosjarc.vopti.core.domain.Id
import com.urosjarc.vopti.core.repos.CWSProblemRepo

class CWSProblemSqliteRepo : CWSProblemRepo {
    init {
        sqlite.autocommit {
            it.table.create<CWSProblem>(throws = false)
        }
    }
    override fun getAll(): List<CWSProblem> {
        var problems = listOf<CWSProblem>()
        sqlite.autocommit {
            problems = it.table.select<CWSProblem>()
        }
        return problems
    }

    override fun get(id: Id<CWSProblem>): CWSProblem {
        var problem: CWSProblem? = null
        sqlite.autocommit {
            problem = it.row.select(pk = id)
        }
        return problem!!
    }

    override fun save(data: CWSProblem) {
        sqlite.autocommit {
            it.row.insert(row = data)
        }
    }

    override fun update(data: CWSProblem) {
        sqlite.autocommit {
            it.row.update(row = data)
        }
    }
}
