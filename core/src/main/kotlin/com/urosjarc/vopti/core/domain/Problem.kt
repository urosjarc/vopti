package com.urosjarc.vopti.core.domain

import java.time.Instant

data class Problem(
    override val id: Id<Problem> = Id(),

    /** Map */
    val mapHeight: String,

    /** Customers */
    val customersSeed: Int,
    val customersSize: Int,
    val customersDistribution: String,
    val customersGrouping: Double,

    /** Depots */
    val depotsSize: Int,
    val depotsSeed: Int,

    /** Vehicles */
    val vehicleMaxDistance: Double,

    val created: Instant = Instant.now()
) : Entity<Problem>()
