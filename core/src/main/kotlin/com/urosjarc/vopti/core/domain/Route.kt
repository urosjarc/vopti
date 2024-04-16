package com.urosjarc.vopti.core.domain

data class Route(
    override val id: Id<Route> = Id(),
    var vertices: List<Int>
) : Entity<Route>() {

    constructor(vararg vertex: Int) : this(vertices = vertex.toMutableList())

    fun contains(vertex: Int): Boolean = vertices.contains(vertex)

    fun isOnEdge(vertex: Int): Boolean = vertices.first() == vertex || vertices.last() == vertex
}
