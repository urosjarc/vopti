package com.urosjarc.vopti.shared

import javafx.concurrent.Task

fun startThread(sleep: Long = 0, interval: Long = 0, repeat: Boolean = false, workCb: () -> Unit): Thread {

    val task: Task<Unit> = object : Task<Unit>() {

        @Throws(Exception::class)
        override fun call() {
            Thread.sleep(sleep)
            this.work()
            while (repeat) {
                Thread.sleep(interval)
                this.work()
            }
        }

        fun work() = try {
            workCb()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
    return Thread(task).also {
        it.isDaemon = true
        it.start()
    }
}
