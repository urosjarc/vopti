package com.urosjarc.vopti.gui.widgets

import com.urosjarc.vopti.app.algos.EuclidianCWS
import com.urosjarc.vopti.core.algos.CWS
import com.urosjarc.vopti.core.domain.CWSProblem
import com.urosjarc.vopti.core.domain.Location
import com.urosjarc.vopti.gui.utils.Painter
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.KoinComponent
import kotlin.random.Random


abstract class CWSWidgetUI : KoinComponent {

    @FXML
    lateinit var painterP: Pane

    @FXML
    lateinit var CWSProblem_Controller: CWSProblemWidget

    @FXML
    lateinit var nextB: Button

    @FXML
    lateinit var playB: Button

    @FXML
    lateinit var solveB: Button

    @FXML
    lateinit var createB: Button
}

class CWSWidget : CWSWidgetUI() {
    private val painter = Painter()
    private val log = this.logger()
    private var problem: CWSProblem? = null
    private var cws: CWS? = null

    @FXML
    fun initialize() {
        this.CWSProblem_Controller.problemCreated.listen { this.create(it) }
        this.log.info(this)
        this.nextB.setOnAction { this.next() }
        this.solveB.setOnAction { this.solve() }
        this.painter.init(pane = this.painterP)
        this.playB.setOnAction { this.play() }
        this.CWSProblem_Controller.create()
    }

    fun redraw() {
        this.painter.clear()

        this.problem?.let {
            this.painter.heightMap(
                testFunction = it.mapHeightFun,
                resolution = it.mapResolution
            )
        }

        this.cws?.let {
            this.painter.addSquare(x = it.depot.x, y = it.depot.y, color = Color.RED, size = 0.005)
            it.customers.forEach { loc -> this.painter.addCircle(x = loc.x, y = loc.y, fill = Color.GRAY, size = 0.005) }
        }

        this.painter.redraw()
    }

    private fun create(problem: CWSProblem) {
        this.problem = problem

        val customerDistributionMap = problem.customersDistributionFun.heightMap(resolution = problem.mapResolution)
        val heightMap = problem.mapHeightFun.heightMap(resolution = problem.mapResolution)

        val depotRand = Random(seed = problem.depotsSeed)
        val depots = arrayOfNulls<Location>(problem.depotsSize).map {
            val x = depotRand.nextDouble()
            val y = depotRand.nextDouble()
            val mapX = (x * problem.mapResolution).toInt()
            val mapY = (y * problem.mapResolution).toInt()
            val height = heightMap[mapY][mapX]
            Location(x = x, y = y, height = height)
        }

        val customerRand = Random(seed = problem.customersSeed)
        val customers: List<Location> = arrayOfNulls<Location>(problem.customersSize).map {
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

        this.cws = EuclidianCWS(
            maxDistance = problem.vehicleRange,
            depot = depots.first(),
            customers = customers
        )

        this.redraw()

        this.cws!!.init()

    }

    fun next() {
        this.cws?.next()
        this.redraw()
    }

    private fun solve() {
        this.cws?.solve()
        this.redraw()
    }

    private fun play() {
        println("play solve")
    }


}
