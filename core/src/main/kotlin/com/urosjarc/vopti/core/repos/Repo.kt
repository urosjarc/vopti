package com.urosjarc.vopti.core.repos

import com.urosjarc.vopti.core.algos.cws.CWSProblem
import com.urosjarc.vopti.core.domain.Id

interface Repo<T> {
    fun get(): List<T>
    fun get(id: Id<T>): T
    fun save(data: T)
    fun update(data: T)
    fun delete(data: T)
    fun delete()
    fun save(data: Iterable<T>)
}
