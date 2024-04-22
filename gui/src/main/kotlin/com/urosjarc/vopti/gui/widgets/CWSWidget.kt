package com.urosjarc.vopti.gui.widgets

import com.urosjarc.vopti.app.algos.EuclidianCWS
import com.urosjarc.vopti.core.algos.cws.CWS
import com.urosjarc.vopti.core.algos.cws.CWSProblemData
import com.urosjarc.vopti.core.domain.Vector
import com.urosjarc.vopti.gui.caches.CWSProblemCache
import com.urosjarc.vopti.gui.parts.CWSProblemsTableView
import com.urosjarc.vopti.gui.utils.Job
import com.urosjarc.vopti.gui.utils.Painter
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


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
    lateinit var resultsTF: TextField

    @FXML
    lateinit var CWSProblemsTV_Controller: CWSProblemsTableView

    @FXML
    lateinit var exportB: Button

    @FXML
    lateinit var importB: Button

    @FXML
    lateinit var saveB: Button

    @FXML
    lateinit var testB: Button

    @FXML
    lateinit var deleteB: Button

}

class CWSWidget : CWSWidgetUI() {
    private val log = this.logger()

    private var cws: CWS? = null
    private var cwsProblemData: CWSProblemData? = null
    private val cwsProblemCache: CWSProblemCache by this.inject()

    private val painter = Painter()
    private var job: Thread? = null

    @FXML
    fun initialize() {
        this.log.info("initialize")

        this.painter.init(pane = this.painterP)

        this.nextB.setOnAction { this.next() }
        this.playB.setOnAction { this.play() }
        this.solveB.setOnAction { this.solve() }

        this.importB.setOnAction { this.import() }
        this.exportB.setOnAction { this.export() }
        this.saveB.setOnAction { this.save() }
        this.testB.setOnAction { this.test() }
        this.deleteB.setOnAction { this.delete() }

        this.cwsProblemCache.onChose {
            this.cws = null
            this.cwsProblemData = CWSProblemData(problem = it)
            this.redraw()
        }
    }

    private fun export() {
        this.resultsTF.text = "EXPORTING: ${this.cwsProblemCache.data}"
    }

    private fun import() {
        this.resultsTF.text = "IMPORTING"
    }

    private fun test() {
        this.resultsTF.clear()
        this.cwsProblemCache.data.forEach {
            this.resultsTF.text += "$it\n"
        }
    }

    private fun initCWS() {
        this.cwsProblemData?.let { data ->
            this.cws = EuclidianCWS(
                maxDistance = data.problem.vehicleRange, depot = data.depots.first(), customers = data.customers
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
        this.cwsProblemData?.let { data ->
            this.painter.heightMap(
                testFunction = data.mapHeightFun, resolution = data.problem.mapResolution
            )
            data.depots.forEach { loc -> this.painter.addSquare(x = loc.x, y = loc.y, color = Color.RED, size = 0.005) }
            data.customers.forEach { loc -> this.painter.addCircle(x = loc.x, y = loc.y, fill = Color.MAGENTA, size = 0.005) }
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
        this.resultsTF.text = "Displaying results for: ${this.cws.toString()}"
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

    private fun save() {
        this.cwsProblemData?.let {
            this.cwsProblemCache.save(it.problem)
        }
    }

    private fun delete() {
        this.cwsProblemData?.let {
            this.cwsProblemCache.delete(it.problem)
        }
    }
}
