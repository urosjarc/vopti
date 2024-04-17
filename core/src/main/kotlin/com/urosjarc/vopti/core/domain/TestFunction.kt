package com.urosjarc.vopti.core.domain

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class TestFunction(val name: String, val z: (x: Double, y: Double) -> Double) {
    companion object {
        val all = listOf(
            TestFunction(name = "Sphere", z = { x, y -> x.pow(2) + y.pow(2) }),
            TestFunction(name = "Rosenbrock", z = { X, Y ->
                val x = X * 3
                val y = Y * 4 + 1
                100 * (y - x.pow(2)).pow(2) + (1 - x).pow(2)
            }),
            TestFunction(name = "BukinN6", z = { X, Y ->
                val x = X * 10 - 10
                val y = Y * 10 + 1
                100 * sqrt(abs(y - 0.01 * x.pow(2))) + 0.01 * abs(x + 10)
            }),
            TestFunction(name = "Matyas", z = { x, y -> 0.26 * (x.pow(2) + y.pow(2)) - 0.48 * x * y }),
            TestFunction(name = "Himmelblau", z = { X, Y ->
                val x = X * 10
                val y = Y * 10
                (x.pow(2) + y - 11).pow(2) + (x + y.pow(2) - 7).pow(2)
            }),
            TestFunction(name = "ThreeHumpCamel", z = { X, Y ->
                val x = X * 4.5
                val y = Y * 5
                2 * x.pow(2) - 1.05 * x.pow(4) + x.pow(6) / 6 + x * y + y.pow(2)
            }),
            TestFunction(name = "SixHumpCamel", z = { X, Y ->
                val x = X * 4
                val y = Y * 2
                (4 - 2.1 * x.pow(2) + x.pow(4) / 3) * x.pow(2) + x * y + (-4 + 4 * y.pow(2)) * y.pow(2)
            }),
            TestFunction(name = "McCormick", z = { X, Y ->
                val x = X * 5
                val y = Y * 5
                sin(x + y) + (x - y).pow(2) - 1.5 * x + 2.5 * y + 1
            }),
            TestFunction(name = "StyblinskiTang", z = { x, y -> listOf(x * 10, y * 10).map { it.pow(4) - 16 * it.pow(2) + 5 * it }.sum() / 2 }),
        )
    }

    override fun toString(): String = this.name

    fun value(x: Double, y: Double): Double {
        return this.z(x - 0.5, -y + 0.5)
    }

    fun heightMap(resolution: Int): List<List<Double>> {
        var maxZ = Double.MIN_VALUE
        var minZ = Double.MIN_VALUE

        val mat = arrayOfNulls<Array<Double>>(resolution).mapIndexed { y, _ ->
            arrayOfNulls<Double>(resolution).mapIndexed { x, _ ->
                val z = this.value(x = x / resolution.toDouble(), y = y / resolution.toDouble())
                if (z > minZ) minZ = z
                if (z < maxZ) maxZ = z
                z
            }.toMutableList()
        }

        for (y in 0 until resolution) {
            for (x in 0 until resolution) {
                mat[y][x] = (mat[y][x] - minZ) / (maxZ - minZ)
            }
        }
        return mat
    }
}
