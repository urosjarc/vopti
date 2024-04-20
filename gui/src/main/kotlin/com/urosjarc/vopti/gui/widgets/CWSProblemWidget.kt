package com.urosjarc.vopti.gui.widgets

import com.urosjarc.vopti.gui.caches.CWSProblemCache
import com.urosjarc.vopti.core.algos.cws.CWSProblem
import com.urosjarc.vopti.core.domain.Id
import com.urosjarc.vopti.core.domain.TestFunction
import javafx.fxml.FXML
import javafx.scene.control.*
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

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
    lateinit var cwsProblemConfirmed: Button
}

class CWSProblemWidget : CWSProblemWidgetUI() {
    private val log = this.logger()
    private val cwsProblemCache: CWSProblemCache by this.inject()
    private var id: Id<CWSProblem>? = null

    @FXML
    fun initialize() {
        this.log.info(this)

        // Map
        this.mapHeightFunCB.items.setAll(TestFunction.all)
        this.mapHeightFunCB.value = TestFunction.all.first()

        // Customers
        this.customersDistributionCB.items.setAll(TestFunction.all)
        this.customersDistributionCB.value = TestFunction.all.first()
        this.customersSizeS.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 250, 10)

        // Confirmation
        this.cwsProblemConfirmed.setOnAction { this.cwsProblemConfirmed() }

        // Events
        listOf(this.mapHeightFunCB, this.customersDistributionCB).forEach { it.setOnAction { this.cwsProblemConfirmed() } }

        this.cwsProblemCache.onChose { cwsp ->
            cwsp?.let {
                this.mapHeightFunCB.value = TestFunction.all.first { it.name == cwsp.mapHeight }
                this.mapResolutionS.value = cwsp.mapResolution.toDouble()
                this.customersSeedS.value = cwsp.customersSeed.toDouble()
                this.customersSizeS.valueFactory.value = cwsp.customersSize
                this.customersDistributionCB.value = TestFunction.all.first { it.name == cwsp.customersDistribution }
                this.customersGroupingS.value = cwsp.customersGrouping
                this.depotsSizeS.value = cwsp.depotsSize.toDouble()
                this.depotsSeedS.value = cwsp.depotsSeed.toDouble()
                this.vehiclesRangeS.value = cwsp.vehicleRange
            }
        }
    }

    fun cwsProblemConfirmed() {
        val problem = CWSProblem(
            id = this.id ?: Id(),
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
        this.cwsProblemCache.chose(problem)
    }
}
