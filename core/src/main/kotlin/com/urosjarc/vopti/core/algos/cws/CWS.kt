package com.urosjarc.vopti.core.algos.cws

import com.urosjarc.vopti.core.domain.Id
import com.urosjarc.vopti.core.domain.Location
import com.urosjarc.vopti.core.domain.Route

/**
 * Clark and Wright savings algorithm.
 */
abstract class CWS(val depot: Location, val customers: List<Location>) {
    abstract fun calculateCost(loc0: Location, loc1: Location): Double
    abstract fun isRouteValid(route: List<Int>): Boolean

    val locations = listOf(depot) + customers

    /** Cost matrix */
    private val costs = Array(this.locations.size) { DoubleArray(this.locations.size) { 0.0 } }

    /** List of savings (x, y, saving) */
    private val savings = mutableListOf<Triple<Int, Int, Double>>()

    /** List of constructed routes */
    val routes = mutableMapOf<Id<Route>, Route>()
    val location_route = mutableMapOf<Int, Route>()

    fun init() {
        this.initCosts()
        this.initSavings()
        this.savings.sortByDescending { it.third }
    }

    fun solve() = this.savings.forEach { this.process(loc0 = it.first, loc1 = it.second) }
    fun next(): Boolean {
        val saving = this.savings.removeFirstOrNull() ?: return false
        return this.process(loc0 = saving.first, loc1 = saving.second)
    }

    private fun process(
        loc0: Int,
        loc1: Int,
    ): Boolean {
        val route0 = this.getOrMakeRoute(i = loc0)
        val route1 = this.getOrMakeRoute(i = loc1)

        /** If its the same route or location is not at the edge stop the process */
        if (route0 == route1 || !route0.isOnEdge(loc0) || !route1.isOnEdge(loc1)) return false

        /** R0 = L0-O-O-O-O, R1 = L1-O-O-O-O */
        if (route0.vertices.first() == loc0 && route1.vertices.first() == loc1) {
            return this.validateAndMerge(route0, route1, start = true, reverse = true)
        }
        /** R0 = 0-O-O-O-LO, R1 = L1-O-O-O-O */
        if (route0.vertices.last() == loc0 && route1.vertices.first() == loc1) {
            return this.validateAndMerge(route0, route1, start = false, reverse = false)
        }
        /** R0 = L0-O-O-O-O, R1 = 0-O-O-O-L1 */
        if (route0.vertices.first() == loc0 && route1.vertices.last() == loc1) {
            return this.validateAndMerge(route0, route1, start = true, reverse = false)
        }
        /** R0 = O-O-O-O-LO, R1 = 0-O-O-O-L1 */
        if (route0.vertices.last() == loc0 && route1.vertices.last() == loc1) {
            return this.validateAndMerge(route0, route1, start = false, reverse = true)
        }

        throw Throwable("Should not happen!")
    }

    private fun validateAndMerge(route0: Route, route1: Route, start: Boolean, reverse: Boolean): Boolean {
        val vertices = if (reverse) route1.vertices.reversed() else route1.vertices
        val newRoute = if (start) vertices + route0.vertices else route0.vertices + vertices
        if (this.isRouteValid(newRoute)) {
            route0.vertices = newRoute
            this.routes.remove(route1.id)
            newRoute.forEach { this.location_route[it] = route0 }
            return true
        }
        return false
    }

    private fun getOrMakeRoute(i: Int): Route {
        val route = this.location_route.getOrPut(key = i) { Route(i) }
        this.routes.putIfAbsent(route.id, route)
        return route
    }

    private fun initCosts() {
        for (x in this.locations.indices) {
            for (y in x + 1 until this.locations.size) {
                val dist = this.calculateCost(loc0 = this.locations[x], loc1 = this.locations[y])
                this.costs[y][x] = dist
                this.costs[x][y] = dist
            }
        }
    }

    private fun initSavings() {
        val depot = this.locations.first()
        for (y in 1 until locations.size) {
            for (x in y + 1 until locations.size) {
                val loc0 = locations[y]
                val loc1 = locations[x]
                val saving = this.calculateCost(loc0, depot) + this.calculateCost(loc1, depot) - this.calculateCost(loc0, loc1)
                this.savings.add(Triple(x, y, saving))
            }
        }
    }

}
