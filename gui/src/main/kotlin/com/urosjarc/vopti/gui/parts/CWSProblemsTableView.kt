package com.urosjarc.vopti.gui.parts

import com.urosjarc.vopti.core.algos.cws.CWSProblem
import com.urosjarc.vopti.gui.caches.CWSProblemCache
import com.urosjarc.vopti.gui.utils.UI
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class CWSProblemsTableViewUi : KoinComponent {

    @FXML
    lateinit var self: TableView<CWSProblem>

    @FXML
    lateinit var mapHeightTC: TableColumn<CWSProblem, String>

    @FXML
    lateinit var mapResolutionTC: TableColumn<CWSProblem, Int>

    @FXML
    lateinit var customersSizeTC: TableColumn<CWSProblem, Int>

    @FXML
    lateinit var customersGroupingTC: TableColumn<CWSProblem, Double>

    @FXML
    lateinit var customersDistributionTC: TableColumn<CWSProblem, String>

    @FXML
    lateinit var customersSeedTC: TableColumn<CWSProblem, Int>

    @FXML
    lateinit var depotsSizeTC: TableColumn<CWSProblem, Int>

    @FXML
    lateinit var depotsSeedTC: TableColumn<CWSProblem, Int>

    @FXML
    lateinit var vehiclesMaxDistanceTC: TableColumn<CWSProblem, Double>

    @FXML
    lateinit var vehicleTC: TableColumn<Any, Any>

    @FXML
    lateinit var depotsTC: TableColumn<Any, Any>

    @FXML
    lateinit var clientsTC: TableColumn<Any, Any>

    @FXML
    lateinit var mapTC: TableColumn<Any, Any>
}

class CWSProblemsTableView : CWSProblemsTableViewUi() {
    val log = this.logger()
    val cwsProblemCache: CWSProblemCache by this.inject()

    @FXML
    fun initialize() {
        this.log.info("initialize")

        // Self
        this.self.setOnMouseClicked { this.selectCWSProblem() }

        // Map
        this.mapHeightTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.mapHeight) }
        this.mapResolutionTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.mapResolution) }

        // Customers
        this.customersSeedTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.customersSeed) }
        this.customersSizeTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.customersSize) }
        this.customersGroupingTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.customersGrouping) }
        this.customersDistributionTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.customersDistribution) }

        // Depots
        this.depotsSizeTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.depotsSize) }
        this.depotsSeedTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.depotsSeed) }

        // Vehicles
        this.vehiclesMaxDistanceTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.vehicleRange) }

        // Columns auto size
        this.self.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY;
        UI.setColumnWidth(vehicleTC, 100 / 4)
        UI.setColumnWidth(depotsTC, 100 / 4)
        UI.setColumnWidth(clientsTC, 100 / 4)
        UI.setColumnWidth(mapTC, 100 / 4)

        // Events
        this.cwsProblemCache.onData {
            this.self.items.setAll(it)
        }
    }

    private fun selectCWSProblem() {
        val cwsProblems = this.self.selectionModel.selectedItems ?: return
        if (cwsProblems.isEmpty()) return
        this.cwsProblemCache.select(cwsProblems)
        this.cwsProblemCache.chose(cwsProblems.first())
    }
}
