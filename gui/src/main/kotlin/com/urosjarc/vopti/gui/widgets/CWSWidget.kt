package com.urosjarc.vopti.gui.widgets

import com.urosjarc.vopti.app.algos.EuclidianCWS
import com.urosjarc.vopti.core.algos.CWS
import com.urosjarc.vopti.core.domain.CWSProblem
import com.urosjarc.vopti.core.domain.Vector
import com.urosjarc.vopti.gui.utils.Job
import com.urosjarc.vopti.gui.utils.Painter
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.KoinComponent


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

}

class CWSWidget : CWSWidgetUI() {
    private val log = this.logger()

    private var cws: CWS? = null
    private var problem: CWSProblem? = null

    private val painter = Painter()

    private var job: Thread? = null

    @FXML
    fun initialize() {
        this.painter.init(pane = this.painterP)

        this.CWSProblem_Controller.problemCreated.listen {
            this.cws = null
            this.problem = it
            this.redraw()
        }
        this.log.info(this)
        this.nextB.setOnAction { this.next() }
        this.solveB.setOnAction { this.solve() }
        this.playB.setOnAction { this.play() }


        this.CWSProblem_Controller.create()
    }

    private fun initCWS() {
        this.problem?.let { problem ->
            this.cws = EuclidianCWS(
                maxDistance = problem.vehicleRange,
                depot = problem.depots.first(),
                customers = problem.customers
            )

            this.cws!!.init()
        }
    }

    fun redraw() {
        this.painter.clear()
        this.cws?.let { cws ->
            cws.routes.values.forEachIndexed { routeI, route ->
                val routeColor = Color.hsb(360.0 / cws.routes.size * routeI, 1.0, 1.0)
                for (i in 0 until route.vertices.size - 1) {
                    val curr = route.vertices[i] - 1
                    val next = route.vertices[i + 1] - 1
                    this.painter.addSolidLine(
                        start = Vector(x = cws.customers[curr].x, y = cws.customers[curr].y),
                        end = Vector(x = cws.customers[next].x, y = cws.customers[next].y),
                        width = 0.001,
                        color = routeColor
                    )
                }
            }
        }
        this.problem?.let { problem ->
            this.painter.heightMap(
                testFunction = problem.mapHeightFun,
                resolution = problem.mapResolution
            )
            problem.depots.forEach { loc -> this.painter.addSquare(x = loc.x, y = loc.y, color = Color.RED, size = 0.005) }
            problem.customers.forEach { loc -> this.painter.addCircle(x = loc.x, y = loc.y, fill = Color.GREEN, size = 0.005) }
        }
        this.painter.redraw()
    }


    fun next(): Boolean {
        this.cws ?: this.initCWS()
        var count = 10e6
        while (this.cws?.next() == false) {
            count--
            if (count < 0) return false
        }
        Job.start { Platform.runLater { this.redraw() } }
        return true
    }

    private fun solve() {
        this.cws ?: this.initCWS()
        this.cws?.solve()
        this.redraw()
    }

    private fun play() {
        this.job?.interrupt()
        this.cws ?: this.initCWS()

        val interval = this.CWSProblem_Controller.solverPlayIntervalS.value.toLong()
        this.job = Job.start(sleep = 0L, interval = interval, repeat = true) {
            if (!this.next()) {
                this.job?.interrupt()
            }
        }
    }

}
