package com.urosjarc.vopti.core.algos.cws

import com.urosjarc.vopti.core.domain.Entity
import com.urosjarc.vopti.core.domain.Id
import java.time.Instant

data class CWSProblem(
    override val id: Id<CWSProblem> = Id(),

    /** Map */
    val mapHeight: String,
    val mapResolution: Int,

    /** Customers */
    val customersSeed: Int,
    val customersSize: Int,
    val customersDistribution: String,
    val customersGrouping: Double,

    /** Depots */
    val depotsSize: Int,
    val depotsSeed: Int,

    /** Vehicles */
    val vehicleRange: Double,

    val created: Instant = Instant.now()
) : Entity<CWSProblem>()
