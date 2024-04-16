package com.urosjarc.vopti.gui.widgets

import com.urosjarc.vopti.core.domain.TestFunction
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Slider
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.KoinComponent

abstract class CWSProblemUI : KoinComponent {

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
}

class CWSProblem : CWSProblemUI() {
    private val log = this.logger()

    @FXML
    fun initialize() {
        this.log.info(this)

        this.mapHeightFunCB.items.setAll(TestFunction.all)
        this.mapHeightFunCB.value = TestFunction.all.first()

        this.customersDistributionCB.items.setAll(TestFunction.all)
        this.customersDistributionCB.value = TestFunction.all.first()
        this.customersSizeS.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 250, 10)
    }

}
