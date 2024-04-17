package com.urosjarc.vopti.gui.utils

import org.apache.logging.log4j.kotlin.logger

data class Event<T>(val listeners: MutableList<(T) -> Unit> = mutableListOf()) {

    val log = this.logger()
    fun listen(cb: (T) -> Unit) = this.listeners.add(cb)
    fun trigger(data: T) = listeners.forEach {
        this.log.info("Event trigger")
        it(data)
    }
}
