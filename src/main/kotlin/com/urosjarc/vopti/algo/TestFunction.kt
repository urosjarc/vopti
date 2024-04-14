package com.urosjarc.vopti.algo

import javafx.scene.image.Image
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

sealed class TestFunction(val z: (x: Double, y: Double) -> Double) {

    fun value(x: Double, y: Double): Float {
        return this.z(x - 0.5, y - 0.5).toFloat()
    }

    fun image(): Image {
        val res = 50
        val img = BufferedImage(res, res, 1)
        var maxZ = Float.MIN_VALUE
        var minZ = Float.MIN_VALUE
        val matrix = arrayOfNulls<Array<Float>>(res).mapIndexed { y, _ ->
            arrayOfNulls<Float>(res).mapIndexed { x, _ ->
                val z = this.value(x=x/res.toDouble(), y=y/res.toDouble())
                if (z > minZ) minZ = z
                if (z < maxZ) maxZ = z
                z
            }
        }

        for (y in 0 until res) {
            for (x in 0 until res) {
                val hue = (matrix[y][x] - minZ) / (maxZ - minZ)
                img.setRGB(x, y, Color.HSBtoRGB(0f, 0f, 1 - hue))
            }
        }

        val os = ByteArrayOutputStream()
        ImageIO.write(img, "png", os)
        return Image(ByteArrayInputStream(os.toByteArray()))
    }

    object Sphere : TestFunction(z = { x, y -> x.pow(2) + y.pow(2) })
    object Rosenbrock : TestFunction(z = { x, y -> 100 * (y - x.pow(2)).pow(2) + (1 - x).pow(2) })
    object Beale :
        TestFunction(z = { x, y -> (1.5 - x + x * y).pow(2) + (2.25 - x + x * y.pow(2)).pow(2) + (2.625 - x + x * y.pow(3)).pow(2) })

    object Goldstein : TestFunction(z = { x, y ->
        val a = (x + y + 1).pow(2)
        val b = 19 - 14 * x + 3 * x.pow(2) - 14 * y + 6 * x * y + 3 * y.pow(2)
        val c = (2 * x - 3 * y).pow(2)
        val d = 18 - 32 * x + 12 * x.pow(2) + 48 * y - 36 * x * y + 27 * y.pow(2)
        (1 + a * b) * (30 + c * d)
    })

    object Booth : TestFunction(z = { x, y -> (x + 2 * y - 7).pow(2) + (2 * x + y - 5).pow(2) })
    object BukinN6 : TestFunction(z = { x, y -> 100 * sqrt(abs(y - 0.01 * x.pow(2))) + 0.01 * abs(x + 10) })
    object Matyas : TestFunction(z = { x, y -> 0.26 * (x.pow(2) + y.pow(2)) - 0.48 * x * y })
    object Himmelblau : TestFunction(z = { x, y -> (x.pow(2) + y - 11).pow(2) + (x + y.pow(2) - 7).pow(2) })
    object ThreeHumpCamel : TestFunction(z = { x, y -> 2 * x.pow(2) - 1.05 * x.pow(4) + x.pow(6) / 6 + x * y + y.pow(2) })
    object SixHumpCamel :
        TestFunction(z = { x, y -> (4 - 2.1 * x.pow(2) + x.pow(4) / 3) * x.pow(2) + x * y + (-4 + 4 * y.pow(2)) * y.pow(2) })

    object McCormick : TestFunction(z = { x, y -> sin(x + y) + (x - y).pow(2) - 1.5 * x + 2.5 * y + 1 })
    object StyblinskiTang : TestFunction(z = { x, y -> listOf(x, y).map { it.pow(4) - 16 * it.pow(2) + 5 * it }.sum() / 2 })
    object Shekel : TestFunction(z = { x, y -> listOf(x, y).map { it.pow(4) - 16 * it.pow(2) + 5 * it }.sum() / 2 })
}
