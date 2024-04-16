package com.urosjarc.vopti.core.repos

import com.urosjarc.vopti.core.domain.Id

interface Repo<T> {
    fun getAll(): List<T>
    fun get(id: Id<T>): T
    fun save(problem: T)
    fun update(problem: T)
}