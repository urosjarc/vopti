package com.urosjarc.vopti.gui.widgets

import com.urosjarc.vopti.core.algos.CWS
import com.urosjarc.vopti.gui.utils.Painter
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
    lateinit var CWSProblem_Controller: CWSProblem

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
    private var cws: CWS? = null

    @FXML
    fun initialize() {
        this.log.info(this)
        this.nextB.setOnAction { this.next() }
        this.solveB.setOnAction { this.solve() }
        this.painter.init(pane = this.painterP)
        this.playB.setOnAction { this.play() }
        this.redraw()
    }
    fun redraw() {
        this.painter.clear()
        this.painter.heightMap(
            testFunction = this.CWSProblem_Controller.mapHeightFunCB.value,
            resolution = this.CWSProblem_Controller.mapResolutionS.value.toInt()
        )
        this.cws?.let {
            this.painter.addSquare(x = it.depot.x, y = it.depot.y, color = Color.RED)
            it.customers.forEach { loc -> this.painter.addSquare(x = loc.x, y = loc.y, color = Color.RED) }
        }
        this.painter.redraw()
    }

    fun create() {
        this.cws = this.CWSProblem_Controller.init()
        this.cws!!.init()
        this.redraw()
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
