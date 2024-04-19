package com.urosjarc.vopti.gui.utils

import javafx.scene.control.TableColumn

object UI {
    fun setColumnWidth(column: TableColumn<*, *>, percent: Int) {
        column.maxWidth = Integer.MAX_VALUE * percent.toDouble()
    }
}
