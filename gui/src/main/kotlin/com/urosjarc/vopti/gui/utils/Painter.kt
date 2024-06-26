package com.urosjarc.vopti.gui.utils

import com.urosjarc.vopti.core.domain.TestFunction
import com.urosjarc.vopti.core.domain.Vector
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.shape.Rectangle
import javafx.scene.shape.Shape
import java.awt.Color.HSBtoRGB
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import kotlin.math.min


class Painter {
    private lateinit var pane: Pane
    private var mouseX = 0.0
    private var mouseY = 0.0
    private val staticShapes = mutableListOf<Shape>()
    private val shapes = mutableListOf<Shape>()
    private val background = ImageView()

    fun init(pane: Pane) {
        this.pane = pane
        this.pane.widthProperty().addListener { _, _, _ -> this.redraw() }
        this.pane.heightProperty().addListener { _, _, _ -> this.redraw() }

        this.pane.setOnScroll { event: ScrollEvent ->
            val zoomFactor = if (event.deltaY < 0) 0.95 else 1.03;
            this.pane.scaleX *= zoomFactor
            this.pane.scaleY *= zoomFactor
        }
        this.pane.setOnMousePressed {
            if (it.isPrimaryButtonDown) {
                this.mouseX = it.x
                this.mouseY = it.y
            }
        }
        this.pane.setOnMouseDragged {
            if (it.isPrimaryButtonDown) {
                this.pane.translateX += it.x - this.mouseX
                this.pane.translateY += it.y - this.mouseY
            }
        }

        this.staticShapes.add(this.addSquareBox(0.5, 0.5, 1.0))
        this.shapes.clear()
    }

    fun addCircle(
        x: Double,
        y: Double,
        size: Double = 0.05,
        lineWidth: Double = 0.01,
        fill: Color = Color.BLACK,
        stroke: Color = Color.BLACK
    ): Circle {
        val shape = Circle()
        shape.centerX = x
        shape.centerY = y
        shape.radius = size / 2
        shape.fill = fill
        shape.stroke = stroke
        shape.strokeWidth = lineWidth
        this.shapes.add(shape)
        return shape
    }

    private fun addRectangle(x: Double, y: Double, size: Double, lineWidth: Double, fill: Color, stroke: Color): Rectangle {
        val shape = Rectangle()
        shape.height = size
        shape.width = size
        shape.x = x - shape.width / 2
        shape.y = y - shape.height / 2
        shape.stroke = stroke
        shape.fill = fill
        shape.strokeWidth = lineWidth
        this.shapes.add(shape)
        return shape
    }

    fun addSquare(x: Double, y: Double, size: Double = 0.05, color: Color = Color.BLACK): Rectangle =
        this.addRectangle(x = x, y = y, size = size, lineWidth = 0.0, fill = color, stroke = Color.TRANSPARENT)

    fun heightMap(testFunction: TestFunction, resolution: Int) {
        val img = BufferedImage(resolution, resolution, 1)
        val matrix = testFunction.heightMap(resolution = resolution)

        for (y in 0 until resolution) {
            for (x in 0 until resolution) {
                img.setRGB(x, y, HSBtoRGB(0f, 0f, 1 - matrix[y][x].toFloat()))
            }
        }

        val os = ByteArrayOutputStream()
        ImageIO.write(img, "png", os)
        this.setBackground(Image(ByteArrayInputStream(os.toByteArray())))
    }

    fun setBackground(image: Image) {
        this.background.image = image
        val minDim = min(this.pane.height, this.pane.width)
        this.background.minWidth(minDim)
        this.background.minHeight(minDim)
        this.background.maxWidth(minDim)
        this.background.maxHeight(minDim)
    }

    fun addSquareBox(x: Double, y: Double, size: Double = 0.05, color: Color = Color.BLACK): Rectangle =
        this.addRectangle(x = x, y = y, size = size, lineWidth = 1.0, fill = Color.TRANSPARENT, stroke = color)

    private fun addLine(start: Vector, end: Vector, width: Double, color: Color, strokeDashArray: Array<Double>): Line {
        val shape = Line()
        shape.strokeDashArray.setAll(*strokeDashArray)
        shape.startX = start.x
        shape.startY = start.y
        shape.endX = end.x
        shape.endY = end.y
        shape.strokeWidth = width
        shape.stroke = color
        this.shapes.add(shape)
        return shape
    }

    fun addDashedLine(
        start: Vector,
        end: Vector,
        width: Double = 0.005,
        color: Color = Color.BLACK
    ) = this.addLine(start = start, end = end, width = width, color = color, strokeDashArray = arrayOf(0.01, 0.02))

    fun addSolidLine(
        start: Vector,
        end: Vector,
        width: Double = 0.005,
        color: Color = Color.BLACK
    ) = this.addLine(start = start, end = end, width = width, color = color, strokeDashArray = arrayOf())

    fun redraw() {
        val minDim = min(this.pane.height, this.pane.width)
        this.pane.children.clear()

        /** BACKGROUND */
        this.background.fitWidth = minDim
        this.background.fitHeight = minDim
        this.pane.children.add(this.background)

        /** DYNAMIC AND STATIC SHAPES */
        (this.shapes + this.staticShapes).forEach {
            when (it) {
                is Circle -> {
                    val shape = Circle()
                    shape.centerX = minDim * it.centerX
                    shape.centerY = minDim * it.centerY
                    shape.radius = minDim * it.radius
                    shape.fill = it.fill
                    shape.stroke = it.stroke
                    shape.strokeWidth = it.strokeWidth
                    this.pane.children.add(shape)
                }

                is Rectangle -> {
                    val shape = Rectangle()
                    shape.width = minDim * it.width
                    shape.height = minDim * it.height
                    shape.x = minDim * it.x
                    shape.y = minDim * it.y
                    shape.fill = it.fill
                    shape.stroke = it.stroke
                    shape.strokeWidth = it.strokeWidth
                    this.pane.children.add(shape)
                }

                is Line -> {
                    val shape = Line()
                    shape.startX = minDim * it.startX
                    shape.startY = minDim * it.startY
                    shape.endX = minDim * it.endX
                    shape.endY = minDim * it.endY
                    shape.strokeWidth = minDim * it.strokeWidth
                    shape.strokeDashArray.setAll(it.strokeDashArray.map { minDim * it })
                    shape.stroke = it.stroke
                    this.pane.children.add(shape)
                }

                else -> throw Error("Unknown shape to draw!")
            }
        }
    }

    fun clear() {
        this.shapes.clear()
        this.redraw()
    }
}
