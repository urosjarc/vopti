package com.urosjarc.vopti.gui.widgets

import com.urosjarc.vopti.app.algos.EuclidianCWS
import com.urosjarc.vopti.core.algos.CWS
import com.urosjarc.vopti.core.domain.Location
import com.urosjarc.vopti.core.domain.TestFunction
import com.urosjarc.vopti.gui.utils.Painter
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Slider
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.KoinComponent
import kotlin.random.Random

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


    fun init(): CWS {
        val heightFun = this.mapHeightFunCB.value

        val depotRand = Random(seed = this.depotsSeedS.value.toInt())
        val depots = arrayOfNulls<Location>(this.depotsSizeS.value.toInt()).map {
            val x = depotRand.nextDouble()
            val y = depotRand.nextDouble()
            val height = heightFun.value(x = x, y = y)
            Location(x = x, y = y, height = height)
        }

        val customerRand = Random(seed = this.depotsSeedS.value.toInt())
        val customers = arrayOfNulls<Location>(this.customersSizeS.value.toInt()).map {
            val x = depotRand.nextDouble()
            val y = depotRand.nextDouble()
            val height = heightFun.value(x = x, y = y)
            Location(x = x, y = y, height = height)
        }

        return EuclidianCWS(
            maxDistance = this.vehiclesRangeS.value,
            depot = depots.first(),
            customers = customers
        )
    }
}
