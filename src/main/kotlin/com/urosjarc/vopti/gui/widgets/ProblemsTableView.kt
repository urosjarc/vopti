package com.urosjarc.vopti.gui.widgets

import com.urosjarc.vopti.app.problem.Problem
import com.urosjarc.vopti.app.problem.ProblemRepo
import com.urosjarc.vopti.shared.setColumnWidth
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class ProblemsTableViewUi : KoinComponent {

    @FXML
    lateinit var problemsTV: TableView<Problem>

    @FXML
    lateinit var mapHeightTC: TableColumn<Problem, String>

    @FXML
    lateinit var customersSizeTC: TableColumn<Problem, Int>

    @FXML
    lateinit var customersGroupingTC: TableColumn<Problem, Double>

    @FXML
    lateinit var customersDistributionTC: TableColumn<Problem, String>

    @FXML
    lateinit var customersSeedTC: TableColumn<Problem, Int>

    @FXML
    lateinit var depotsSizeTC: TableColumn<Problem, Int>

    @FXML
    lateinit var depotsSeedTC: TableColumn<Problem, Int>

    @FXML
    lateinit var vehiclesMaxDistanceTC: TableColumn<Problem, Double>
}

class ProblemsTableView : ProblemsTableViewUi() {
    val log = this.logger()
    val problemRepo: ProblemRepo by this.inject()

    @FXML
    fun initialize() {
        this.log.info(this.javaClass)

        // Map
        this.mapHeightTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.mapHeight) }

        // Customers
        this.customersSeedTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.customersSeed) }
        this.customersSizeTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.customersSize) }
        this.customersGroupingTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.customersGrouping) }
        this.customersDistributionTC.setCellValueFactory { ReadOnlyStringWrapper(it.value.customersDistribution) }

        //Depots
        this.depotsSizeTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.depotsSize) }
        this.depotsSeedTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.depotsSeed) }

        //Vehicles
        this.vehiclesMaxDistanceTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.vehicleMaxDistance) }

        this.problemsTV.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY;

        setColumnWidth(this.mapHeightTC, 100 / 7)
        setColumnWidth(this.customersSeedTC, 100 / 7)
        setColumnWidth(this.customersSizeTC, 100 / 7)
        setColumnWidth(this.customersGroupingTC, 100 / 7)
        setColumnWidth(this.customersDistributionTC, 100 / 7)
        setColumnWidth(this.depotsSizeTC, 100 / 7)
        setColumnWidth(this.depotsSeedTC, 100 / 7)
    }

}
