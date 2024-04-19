package com.urosjarc.vopti.core.algos.cws

import com.urosjarc.vopti.core.domain.Location
import com.urosjarc.vopti.core.domain.TestFunction
import kotlin.random.Random

class CWSProblemData(val problem: CWSProblem) {

    val customers: List<Location>
    var depots: List<Location>
    val mapHeightFun = TestFunction.all.first { it.name == problem.mapHeight }
    val customersDistributionFun = TestFunction.all.first { it.name == problem.customersDistribution }

    init {
        val customerDistributionMap = this.customersDistributionFun.heightMap(resolution = problem.mapResolution)
        val heightMap = this.mapHeightFun.heightMap(resolution = problem.mapResolution)

        val depotRand = Random(seed = problem.depotsSeed)
        this.depots = arrayOfNulls<Location>(problem.depotsSize).map {
            val x = depotRand.nextDouble()
            val y = depotRand.nextDouble()
            val mapX = (x * problem.mapResolution).toInt()
            val mapY = (y * problem.mapResolution).toInt()
            val height = heightMap[mapY][mapX]
            Location(x = x, y = y, height = height)
        }

        val customerRand = Random(seed = problem.customersSeed)
        this.customers = arrayOfNulls<Location>(problem.customersSize).map {
            val location: Location
            while (true) {
                val x = customerRand.nextDouble()
                val y = customerRand.nextDouble()
                val mapX = (x * problem.mapResolution).toInt()
                val mapY = (y * problem.mapResolution).toInt()
                val height = heightMap[mapY][mapX]
                val probability = customerDistributionMap[mapY][mapX]
                val random = customerRand.nextDouble()
                if (random < probability - problem.customersGrouping) {
                    location = Location(x = x, y = y, height = height)
                    break
                }
            }
            location
        }
    }
}
