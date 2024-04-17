package com.urosjarc.vopti.gui.parts

import com.urosjarc.vopti.core.domain.CWSProblem
import com.urosjarc.vopti.core.repos.ProblemRepo
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
    lateinit var problemsTV: TableView<CWSProblem>

    @FXML
    lateinit var mapHeightTC: TableColumn<CWSProblem, String>

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
        this.vehiclesMaxDistanceTC.setCellValueFactory { ReadOnlyObjectWrapper(it.value.vehicleRange) }
    }

}
