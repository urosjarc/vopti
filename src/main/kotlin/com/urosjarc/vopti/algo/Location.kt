package com.urosjarc.vopti.algo

import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

data class Location(
    val id: UUID = UUID.randomUUID(),
    val x: Double,
    val y: Double,
    val height: Double,
) {
    fun distance(location: Location): Double = sqrt((this.x - location.x).pow(2.0) + (this.y - location.y).pow(2.0))

    fun toArray(): DoubleArray = doubleArrayOf(this.x, this.y)
}
