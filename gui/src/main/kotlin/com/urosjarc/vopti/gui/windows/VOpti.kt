package com.urosjarc.vopti.gui.windows

import com.urosjarc.vopti.app.algos.EuclidianCWS
import com.urosjarc.vopti.core.algos.CWS
import com.urosjarc.vopti.core.domain.Location
import com.urosjarc.vopti.core.domain.Route
import com.urosjarc.vopti.core.domain.TestFunction
import com.urosjarc.vopti.core.domain.Vector
import com.urosjarc.vopti.gui.utils.Job
import com.urosjarc.vopti.gui.utils.Painter
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.*
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
    lateinit var locationsSizeS: Spinner<Int>

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

    @FXML
    lateinit var locationsDistributionCB: ChoiceBox<TestFunction>

    @FXML
    lateinit var mapCB: ChoiceBox<TestFunction>

    @FXML
    lateinit var resolutionS: Slider

    @FXML
    lateinit var groupingS: Slider
}

class VOpti : VOptiUI() {
    private val painter = Painter()
    private val depots = mutableListOf<Location>()
    private val customers = mutableListOf<Location>()
    private var playJob: Thread? = null
    private var CWS: CWS? = null

    private fun colorRange(size: Int): List<Color> {
        val k = 360.0 / size
        return arrayOfNulls<Color>(size).mapIndexed { x, _ -> Color.hsb(k * x, 1.0, 1.0) }
    }

    @FXML
    fun initialize() {
        this.mapCB.items.setAll(TestFunction.all)
        this.mapCB.value = TestFunction.all.first()
        this.locationsDistributionCB.items.setAll(TestFunction.all)
        this.locationsDistributionCB.value = TestFunction.all.first()
        this.locationsSizeS.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 250, 10)
        this.mapCB.setOnAction { this.clear() }
        this.playS.setOnMouseReleased { this.play() }
        this.createLocationsB.setOnAction { this.createLocations() }
        this.nextB.setOnAction { this.next() }
        this.solveB.setOnAction { this.solve() }
        this.painter.init(pane = this.main)
        this.playB.setOnAction { this.play() }
    }

    private fun play() {
        this.playJob?.interrupt()
        this.playJob = Job.start(sleep = 0, interval = this.playS.value.toLong(), repeat = true) {
            Platform.runLater { if (!this.next()) this.playJob!!.interrupt() }
        }
    }

    private fun clear() {
        this.playJob?.interrupt()
        this.playJob = null
        this.customers.clear()
        this.depots.clear()
        this.painter.clear()
        this.painter.heightMap(this.mapCB.value, resolution = this.resolutionS.value.toInt())
        this.painter.redraw()
    }

    private fun createLocations() {
        this.clear()
        val seed = this.seedS.value.toInt()

        var rand = Random(seed)
        repeat(this.depotsSizeS.value.toInt()) {
            val x = rand.nextDouble()
            val y = rand.nextDouble()
            val height = rand.nextDouble()
            val location = Location(x = x, y = y, height = height)
            this.depots.add(location)
            this.painter.addSquare(x = x, y = y, size = 0.01, color = Color.RED)
        }

        rand = Random(seed + 1)
        val matrix = this.locationsDistributionCB.value.heightMap(resolution = 10)
        repeat(this.locationsSizeS.value.toInt()) {
            while (true) {
                val x = rand.nextDouble()
                val y = rand.nextDouble()
                val probability = matrix[(y * 10).toInt()][(x * 10).toInt()]
                if (rand.nextDouble() + this.groupingS.value > probability) continue
                val location = Location(x = x, y = y, height = this.mapCB.value.value(x = x, y = y))
                this.customers.add(location)
                this.painter.addCircle(x = x, y = y, size = 0.005, fill = Color.YELLOW)
                break
            }
        }

        this.painter.redraw()
    }

    private fun next(): Boolean {
        this.CWS ?: this.start()
        var count = 0
        while (!this.CWS!!.next()) {
            count++
            if (count > 1000) return false
        }
        this.drawRoutes()
        return true
    }

    private fun solve() {
        this.CWS ?: this.start()
        this.CWS!!.solve()
        this.drawRoutes()
    }

    private fun start() {

        /** Initial clark and wright */
        this.CWS = EuclidianCWS(
            depot = this.depots.first(),
            customers = this.customers,
            maxDistance = this.distanceMaxS.value
        )

    }

    private fun drawRoutes() {

        /** Draw routes from clark and wright */
        val colors = this.colorRange(size = this.CWS!!.routes.size)
        this.CWS!!.routes.values.forEachIndexed { routeIndex: Int, route: Route ->
            val color = colors[routeIndex]
            for (i in 0 until route.vertices.size - 1) {
                val startLocation: Location = this.customers[route.vertices[i]]
                val endLocation: Location = this.customers[route.vertices[i + 1]]
                val startVector = Vector(startLocation.x, startLocation.y)
                val endVector = Vector(endLocation.x, endLocation.y)
                painter.addSolidLine(start = startVector, end = endVector, width = 0.001, color = color)
            }
        }
        painter.redraw()
    }

}
