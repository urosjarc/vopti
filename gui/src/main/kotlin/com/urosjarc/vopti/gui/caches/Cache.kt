package com.urosjarc.vopti.gui.caches

import javafx.application.Platform
import org.apache.logging.log4j.kotlin.logger
import org.koin.core.component.KoinComponent

abstract class Cache<T : Any> : KoinComponent {
    val log = this.logger()

    val data = mutableListOf<T>()
    var selected = mutableListOf<T>()
    var history = mutableListOf<T>()
    var future = mutableListOf<T>()
    var chosen: T? = null

    private val onDataCb = mutableListOf<Watcher<List<T>>>()
    private val onSelectCb = mutableListOf<Watcher<List<T>>>()
    private val onChoseCb = mutableListOf<Watcher<T>>()
    private val onResetCb = mutableListOf<Watcher<List<T>>>()

    private class Watcher<CT>(
        val cb: (t: CT) -> Unit,
        val runLater: Boolean
    ) {
        fun run(t: CT) = if (this.runLater) Platform.runLater { this.cb(t) } else this.cb(t)
    }

    fun onReset(runLater: Boolean = false, cb: (t: List<T>) -> Unit) {
        this.onResetCb.add(Watcher(cb = cb, runLater = runLater))
    }

    fun onResetNotify() {
        this.onResetCb.forEach { it.run(this.data) }
    }

    fun onData(runLater: Boolean = false, cb: (t: List<T>) -> Unit) {
        this.onDataCb.add(Watcher(cb = cb, runLater = runLater))
    }

    fun onDataNotify() {
        this.onDataCb.forEach { it.run(this.data) }
    }

    fun onSelect(runLater: Boolean = false, cb: (t: List<T>) -> Unit) {
        this.onSelectCb.add(Watcher(cb = cb, runLater = runLater))
    }

    fun onSelectNotify() {
        this.onSelectCb.forEach { it.run(this.selected) }
    }

    fun onChose(runLater: Boolean = false, cb: (t: T) -> Unit) {
        this.onChoseCb.add(Watcher(cb = cb, runLater = runLater))
    }

    fun onChoseNotify() {
        val chosen = this.chosen ?: return
        this.onChoseCb.forEach { it.run(chosen) }
    }

    fun set(t: List<T>) {
        this.log.info("set: $t")
        if (!t.contains(this.chosen)) this.chose(null)
        this.data.clear()
        this.data.addAll(t)
        this.resetHistory(all = true)
        this.onDataNotify()
        this.save()
    }

    open fun save(t: T): T {
        this.log.info("save: $t")
        val old = this.data.firstOrNull { t == it }
        if (old == null) this.data.add(t) else return old
        this.onDataNotify()
        this.save()
        return t
    }

    fun select(t: List<T>) {
        this.log.info("select: $t")
        this.selected = t.toMutableList()
        this.onSelectNotify()
    }

    fun chose(t: T? = null) {
        this.log.info("chose: $t")
        this.resetHistory(all = false)
        this.chosen?.let { this.history.add(it) }
        this.chosen = t
        this.onChoseNotify()
    }

    fun find(t: T): T? {
        this.log.info("find: $t")
        return this.data.firstOrNull { it == t }
    }

    fun reset() {
        this.log.info("reset")
        this.set(listOf())
        this.onResetNotify()
    }

    open fun delete(t: T) {
        this.log.info("delete: $t")
        this.resetHistory(all = false)
        this.history.removeAll { it == t }
        this.future.removeAll { it == t }

        if (this.data.removeAll { it == t }) {
            this.onDataNotify()
            this.save()
        }
        if (this.selected.removeAll { it == t }) {
            this.onSelectNotify()
        }
        if (this.chosen == t) {
            this.chose(null)
        }
    }

    private fun resetHistory(all: Boolean) {
        this.log.info("resetHistory: $all")
        if (all) this.history.clear()
        else this.history += this.future
        this.future.clear()
    }

    // data: [1,2,3,4,5,6]
    // history, chosen, future: [1,2,3], 4, [5,6]
    fun undo() {
        this.log.info("undo")
        this.history.removeLastOrNull()?.let { newChosen ->
            this.chosen?.let { oldChosen -> this.future.add(0, oldChosen) }
            this.chosen = newChosen
            this.onChoseNotify()
        }
    }

    fun redo() {
        this.log.info("redo")
        this.future.removeFirstOrNull()?.let { newChosen ->
            this.chosen?.let { oldChosen -> this.history.add(oldChosen) }
            this.chosen = newChosen
            this.onChoseNotify()
        }
    }

    abstract fun init()
    abstract fun load()

    abstract fun save()
}
