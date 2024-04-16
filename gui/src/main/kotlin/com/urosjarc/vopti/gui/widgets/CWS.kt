package com.urosjarc.vopti.gui.widgets

import com.urosjarc.vopti.gui.utils.Painter
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.layout.Pane
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.KoinComponent


abstract class CWSUI : KoinComponent {

    @FXML
    lateinit var painterP: Pane

    @FXML
    lateinit var nextB: Button

    @FXML
    lateinit var createB: Button

    @FXML
    lateinit var playB: Button

    @FXML
    lateinit var solveB: Button
}

class CWS : CWSUI() {
    private val painter = Painter()
    private val log = this.logger()

    @FXML
    fun initialize() {
        this.log.info(this)
        this.nextB.setOnAction { this.next() }
        this.solveB.setOnAction { this.solve() }
        this.painter.init(pane = this.painterP)
        this.playB.setOnAction { this.play() }
    }

    fun next() {
        println("next")
    }

    private fun solve() {
        println("solve")
    }

    private fun play() {
        println("play")
    }


}
