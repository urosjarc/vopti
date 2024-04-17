package com.urosjarc.vopti.app.repos.sqlite

import com.urosjarc.vopti.core.domain.Id
import com.urosjarc.vopti.core.domain.CWSProblem
import com.urosjarc.vopti.core.repos.ProblemRepo

class ProblemSqliteRepo : ProblemRepo {
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

    override fun save(problem: CWSProblem) {
        sqlite.autocommit {
            it.row.insert(row = problem)
        }
    }

    override fun update(problem: CWSProblem) {
        sqlite.autocommit {
            it.row.update(row = problem)
        }
    }
}
