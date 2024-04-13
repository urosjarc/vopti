package com.urosjarc.vopti.algo

import java.util.*

data class Route(var vertices: List<Int>) {
    val id: UUID = UUID.randomUUID()

    constructor(vararg vertex: Int) : this(vertices = vertex.toMutableList())

    fun contains(vertex: Int): Boolean = vertices.contains(vertex)

    fun isOnEdge(vertex: Int): Boolean = vertices.first() == vertex || vertices.last() == vertex

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Route
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int = this.id.hashCode()
}
