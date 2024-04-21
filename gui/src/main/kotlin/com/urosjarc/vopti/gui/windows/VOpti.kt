package com.urosjarc.vopti.gui.windows

import com.urosjarc.vopti.gui.caches.CWSProblemCache
import javafx.fxml.FXML
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


abstract class VOptiUI : KoinComponent {}

class VOpti : VOptiUI() {

    private val log = this.logger()
    private val cwsProblemCatche: CWSProblemCache by this.inject()

    @FXML
    fun initialize() {
        log.info("initialize")
        cwsProblemCatche.init()
    }
}
