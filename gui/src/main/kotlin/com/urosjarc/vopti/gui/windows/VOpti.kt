package com.urosjarc.vopti.gui.windows

import javafx.fxml.FXML
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.KoinComponent


abstract class VOptiUI : KoinComponent {}

class VOpti : VOptiUI() {

    private val log = this.logger()

    @FXML
    fun initialize() {
        log.info(this)
    }
}
