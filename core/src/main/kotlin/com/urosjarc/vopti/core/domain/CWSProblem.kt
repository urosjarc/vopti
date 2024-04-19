package com.urosjarc.vopti.core.domain

import java.time.Instant
import kotlin.random.Random

data class CWSProblem(
    override val id: Id<CWSProblem> = Id(),

    /** Map */
    val mapHeight: String,
    val mapResolution: Int,

    /** Customers */
    val customersSeed: Int,
    val customersSize: Int,
    val customersDistribution: String,
    val customersGrouping: Double,

    /** Depots */
    val depotsSize: Int,
    val depotsSeed: Int,

    /** Vehicles */
    val vehicleRange: Double,

    val created: Instant = Instant.now()
) : Entity<CWSProblem>() {

    val customers: List<Location>
    var depots: List<Location>
    val mapHeightFun = TestFunction.all.first { it.name == this.mapHeight }
    val customersDistributionFun = TestFunction.all.first { it.name == this.customersDistribution }

    init {
        val customerDistributionMap = this.customersDistributionFun.heightMap(resolution = this.mapResolution)
        val heightMap = this.mapHeightFun.heightMap(resolution = this.mapResolution)

        val depotRand = Random(seed = this.depotsSeed)
        this.depots = arrayOfNulls<Location>(this.depotsSize).map {
            val x = depotRand.nextDouble()
            val y = depotRand.nextDouble()
            val mapX = (x * this.mapResolution).toInt()
            val mapY = (y * this.mapResolution).toInt()
            val height = heightMap[mapY][mapX]
            Location(x = x, y = y, height = height)
        }

        val customerRand = Random(seed = this.customersSeed)
        this.customers = arrayOfNulls<Location>(this.customersSize).map {
            val location: Location
            while (true) {
                val x = customerRand.nextDouble()
                val y = customerRand.nextDouble()
                val mapX = (x * this.mapResolution).toInt()
                val mapY = (y * this.mapResolution).toInt()
                val height = heightMap[mapY][mapX]
                val probability = customerDistributionMap[mapY][mapX]
                val random = customerRand.nextDouble()
                if (random < probability - this.customersGrouping) {
                    location = Location(x = x, y = y, height = height)
                    break
                }
            }
            location
        }
    }
}
