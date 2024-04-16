package com.urosjarc.vopti.app.algos

import com.urosjarc.vopti.core.algos.CWS
import com.urosjarc.vopti.core.domain.Location

/**
 * Euclidean distance base Clark and Wright savings algorithm.
 */
class EuclidianCWS(
    private val maxDistance: Double,
    depot: Location,
    customers: List<Location>
) : CWS(depot = depot, customers = customers) {
    override fun calculateCost(loc0: Location, loc1: Location): Double = loc0.distance(loc1)
    override fun isRouteValid(route: List<Int>): Boolean {
        var curr = this.locations.first()
        val last = this.locations.last()
        var dist = 0.0

        route.forEach {
            val next = this.locations[it]
            dist += curr.distance(next)
            curr = next
        }

        dist += curr.distance(last)
        return dist < this.maxDistance
    }
}
