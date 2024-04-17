package com.urosjarc.vopti.core.domain

data class Solution(
    override val id: Id<Solution> = Id(),
    val problem: Id<CWSProblem>
) : Entity<Solution>()
