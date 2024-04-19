package com.urosjarc.vopti.core.domain

import com.urosjarc.vopti.core.algos.cws.CWSProblem

data class Solution(
    override val id: Id<Solution> = Id(),
    val problem: Id<CWSProblem>
) : Entity<Solution>()
