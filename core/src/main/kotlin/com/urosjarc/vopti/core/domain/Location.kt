package com.urosjarc.vopti.core.domain

import kotlin.math.pow
import kotlin.math.sqrt

data class Location(
    override val id: Id<Location> = Id(),
    val x: Double,
    val y: Double,
    val height: Double,
) : Entity<Location>() {
    fun distance(location: Location): Double = sqrt((this.x - location.x).pow(2.0) + (this.y - location.y).pow(2.0))

}
