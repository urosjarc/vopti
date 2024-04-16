package com.urosjarc.vopti.app.repos.sqlite

import com.urosjarc.vopti.core.domain.Id
import com.urosjarc.vopti.core.domain.Problem
import com.urosjarc.vopti.core.repos.ProblemRepo

class ProblemSqliteRepo : ProblemRepo {
    override fun getAll(): List<Problem> {
        var problems = listOf<Problem>()
        sqlite.autocommit {
            problems = it.table.select<Problem>()
        }
        return problems
    }

    override fun get(id: Id<Problem>): Problem {
        var problem: Problem? = null
        sqlite.autocommit {
            problem = it.row.select(pk = id)
        }
        return problem!!
    }

    override fun save(problem: Problem) {
        sqlite.autocommit {
            it.row.insert(row = problem)
        }
    }

    override fun update(problem: Problem) {
        sqlite.autocommit {
            it.row.update(row = problem)
        }
    }
}
