package com.urosjarc.vopti.gui.widgets

import com.urosjarc.vopti.core.domain.CWSProblem
import com.urosjarc.vopti.core.domain.TestFunction
import com.urosjarc.vopti.gui.utils.Event
import javafx.fxml.FXML
import javafx.scene.control.*
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.KoinComponent

abstract class CWSProblemWidgetUI : KoinComponent {
    /**
     * MAP
     */

    @FXML
    lateinit var mapHeightFunCB: ChoiceBox<TestFunction>

    @FXML
    lateinit var mapResolutionS: Slider

    /**
     * Depots
     */
    @FXML
    lateinit var depotsSizeS: Slider

    @FXML
    lateinit var depotsSeedS: Slider

    /**
     * Customers
     */

    @FXML
    lateinit var customersDistributionCB: ChoiceBox<TestFunction>

    @FXML
    lateinit var customersGroupingS: Slider

    @FXML
    lateinit var customersSizeS: Spinner<Int>

    @FXML
    lateinit var customersSeedS: Slider

    /**
     * Vehicles
     */

    @FXML
    lateinit var vehiclesRangeS: Slider

    /**
     * Solver
     */

    @FXML
    lateinit var solverPlayIntervalS: Slider

    /**
     * Controls
     */
    @FXML
    lateinit var createB: Button
}

class CWSProblemWidget : CWSProblemWidgetUI() {
    private val log = this.logger()
    val problemCreated = Event<CWSProblem>()

    @FXML
    fun initialize() {
        this.log.info(this)

        this.mapHeightFunCB.items.setAll(TestFunction.all)
        this.mapHeightFunCB.value = TestFunction.all.first()

        this.customersDistributionCB.items.setAll(TestFunction.all)
        this.customersDistributionCB.value = TestFunction.all.first()
        this.customersSizeS.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 250, 10)

        this.createB.setOnAction { this.create() }
        listOf(this.mapHeightFunCB, this.customersDistributionCB).forEach { it.setOnAction { this.create() } }
    }

    fun create() {
        val problem = CWSProblem(
            mapHeight = this.mapHeightFunCB.value.toString(),
            mapResolution = this.mapResolutionS.value.toInt(),
            customersSeed = this.customersSeedS.value.toInt(),
            customersSize = this.customersSizeS.value.toInt(),
            customersDistribution = this.customersDistributionCB.value.toString(),
            customersGrouping = this.customersGroupingS.value,
            depotsSize = this.depotsSizeS.value.toInt(),
            depotsSeed = this.depotsSeedS.value.toInt(),
            vehicleRange = this.vehiclesRangeS.value
        )
        this.problemCreated.trigger(problem)
    }
}
