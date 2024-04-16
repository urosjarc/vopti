package com.urosjarc.vopti.core.domain

abstract class Entity<T> {
    abstract val id: Id<T>
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Route
        return id == other.id
    }

    override fun hashCode(): Int = this.id.hashCode()
}
