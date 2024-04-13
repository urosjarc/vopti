package com.urosjarc.vopti.gui.windows

import com.urosjarc.vopti.algo.ClarkAndWright
import com.urosjarc.vopti.algo.Location
import com.urosjarc.vopti.shared.Painter
import com.urosjarc.vopti.shared.Vector
import com.urosjarc.vopti.shared.startThread
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Slider
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import org.koin.core.component.KoinComponent
import kotlin.random.Random


abstract class VOptiUI : KoinComponent {

    @FXML
    lateinit var main: Pane

    @FXML
    lateinit var nextB: Button

    @FXML
    lateinit var solveB: Button

    @FXML
    lateinit var createLocationsB: Button

    @FXML
    lateinit var locationsSizeS: Slider

    @FXML
    lateinit var seedS: Slider

    @FXML
    lateinit var depotsSizeS: Slider

    @FXML
    lateinit var distanceMaxS: Slider

    @FXML
    lateinit var playB: Button

    @FXML
    lateinit var playS: Slider
}

class VOpti : VOptiUI() {
    private val painter = Painter()
    private val customers = mutableListOf<Location>()
    private val depots = mutableListOf<Location>()
    private val locations get() = this.depots.subList(0, 1) + this.customers

    private var clarkAndWright: ClarkAndWright? = null
    private var playThread: Thread? = null

    private fun colorRange(size: Int): List<Color> {
        val k = 360.0 / size
        return arrayOfNulls<Color>(size).mapIndexed { x, _ -> Color.hsb(k * x, 1.0, 1.0) }
    }

    @FXML
    fun initialize() {
        this.playS.setOnMouseReleased { this.play() }
        this.createLocationsB.setOnAction { this.createLocations() }
        this.nextB.setOnAction { this.next() }
        this.solveB.setOnAction { this.solve() }
        this.painter.init(pane = this.main)
        this.playB.setOnAction { this.play() }
    }

    private fun play() {
        this.playThread?.interrupt()
        this.playThread = startThread(sleep = 0, interval = this.playS.value.toLong(), repeat = true) {
            Platform.runLater { if(!this.next()) this.playThread!!.interrupt() }
        }
    }

    private fun clear() {
        this.playThread?.interrupt()
        this.playThread = null
        this.clarkAndWright = null
        this.customers.clear()
        this.depots.clear()
        this.painter.clear()
    }

    private fun createLocations() {
        this.clear()

        val rand = Random(this.seedS.value.toInt())

        repeat(this.locationsSizeS.value.toInt()) {
            val x = rand.nextDouble()
            val y = rand.nextDouble()
            val height = rand.nextDouble()
            val location = Location(x = x, y = y, height = height)
            this.customers.add(location)
            this.painter.addCircle(x = x, y = y, size = 0.005, fill = Color.GRAY)
        }

        repeat(this.depotsSizeS.value.toInt()) {
            val x = rand.nextDouble()
            val y = rand.nextDouble()
            val height = rand.nextDouble()
            val location = Location(x = x, y = y, height = height)
            this.depots.add(location)
            this.painter.addSquare(x = x, y = y, size = 0.01, color = Color.RED)
        }

        this.painter.redraw()
    }

    private fun next(): Boolean {
        this.clarkAndWright ?: this.start()
        var count = 0
        while (!this.clarkAndWright!!.next()) {
            count++
            if(count > 1000) return false
        }
        this.drawRoutes()
        return true
    }

    private fun solve() {
        this.clarkAndWright ?: this.start()
        this.clarkAndWright!!.solve()
        this.drawRoutes()
    }

    private fun start() {

        /** Initial clark and wright */
        this.clarkAndWright = ClarkAndWright(
            locations = this.depots.subList(0, 1) + this.customers,
            isRouteValid = { indexes ->
                var curr = this.locations.first()
                val last = this.locations.last()
                var dist = 0.0

                indexes.forEach {
                    val next = this.locations[it]
                    dist += curr.distance(next)
                    curr = next
                }

                dist += curr.distance(last)
                dist < this.distanceMaxS.value
            }
        )

    }

    private fun drawRoutes() {

        /** Draw routes from clark and wright */
        val colors = this.colorRange(size = this.clarkAndWright!!.routes.size)
        this.clarkAndWright!!.routes.values.forEachIndexed { routeIndex, route ->
            val color = colors[routeIndex]
            for (i in 0 until route.vertices.size - 1) {
                val startLocation: Location = this.locations[route.vertices[i]]
                val endLocation: Location = this.locations[route.vertices[i + 1]]
                val startVector = Vector(startLocation.x, startLocation.y)
                val endVector = Vector(endLocation.x, endLocation.y)
                painter.addSolidLine(start = startVector, end = endVector, width = 0.001, color = color)
            }
        }
        painter.redraw()
    }

}
