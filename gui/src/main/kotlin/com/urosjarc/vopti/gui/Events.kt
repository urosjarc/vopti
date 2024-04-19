package com.urosjarc.vopti.gui

import com.urosjarc.vopti.core.algos.cws.CWSProblem
import com.urosjarc.vopti.gui.utils.Event

object Events {
    val cwsProblemCreated = Event<CWSProblem>()
    val cwsProblemSaved = Event<CWSProblem>()
}
