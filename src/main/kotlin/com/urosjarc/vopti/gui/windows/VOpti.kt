package com.urosjarc.vopti.gui.windows

import javafx.fxml.FXML
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import org.koin.core.component.KoinComponent

abstract class VOptiUI : KoinComponent {
    @FXML
    lateinit var tablesT: Tab

    @FXML
    lateinit var columnsT: Tab

    @FXML
    lateinit var connectionsT: Tab

    @FXML
    lateinit var schemasT: Tab

    @FXML
    lateinit var queriesT: Tab

    @FXML
    lateinit var tablesParentT: Tab

    @FXML
    lateinit var dbT: Tab

    @FXML
    lateinit var tablesTP: TabPane

    @FXML
    lateinit var commitsT: Tab
}

class VOpti : VOptiUI() {


}
