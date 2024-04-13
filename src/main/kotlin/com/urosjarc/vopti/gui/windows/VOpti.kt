package com.urosjarc.vopti.gui.windows

import com.urosjarc.vopti.shared.Painter
import javafx.fxml.FXML
import javafx.scene.layout.Pane
import org.koin.core.component.KoinComponent


abstract class VOptiUI : KoinComponent {
    @FXML
    lateinit var downPane: Pane

    @FXML
    lateinit var upPane: Pane
}

class VOpti : VOptiUI() {

    private val upPainter = Painter()
    private val downPainter = Painter()

    @FXML
    fun initialize() {
        this.upPainter.init(pane = this.upPane)
        this.downPainter.init(pane = this.downPane)
        this.upPainter.addCircle(0.5, 0.5)
        this.downPainter.addSquare(0.5, 0.5)
        this.downPainter.addSquareBox(0.5, 0.5, size = 1.0)
    }

}
