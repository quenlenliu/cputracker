package com.nio.cpu.tracker.viewmodel

import com.nio.cpu.tracker.data.CpuProcess
import com.nio.cpu.tracker.data.TopItem
import javafx.beans.InvalidationListener
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableObjectValue
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener

object MainSceneViewMode {

    final val topItemList = FXCollections.observableArrayList<TopItem>()

    final val processGroup = FXCollections.observableArrayList<CpuProcess>()

    final val selectTop = ObservableObject<Any>()

    init {
        topItemList.addListener(ListChangeListener {
            val group = it.list.groupBy { it.processId }
            val list = ArrayList<CpuProcess>(group.size)
            for ((key, item) in group) {
                val process = CpuProcess(key, item.first().processName)
                process.addThread(item)
                list.add(process)
            }
            processGroup.setAll(list)
        })
    }
}

class ObservableObject<T>(): ObservableObjectValue<T> {
    val changeListeners = arrayListOf<ChangeListener<in T>?>()
    val invalidationListener = arrayListOf<InvalidationListener?>()
    private var value: T? = null
    override fun removeListener(p0: ChangeListener<in T>?) {
        changeListeners.remove(p0)
    }

    override fun removeListener(p0: InvalidationListener?) {
        invalidationListener.remove(p0)
    }

    override fun addListener(p0: InvalidationListener?) {
        invalidationListener.add(p0)
    }

    override fun addListener(p0: ChangeListener<in T>?) {
        changeListeners.add(p0)
        if (value != null) {
            p0?.changed(this, value!!, value!!)
        }

    }

    override fun getValue(): T {
        return value!!
    }

    override fun get(): T {
        return value!!
    }

    open fun setValue(data: T) {
        value = data
        changeListeners.forEach {
            it?.changed(this, value, value)
        }
        invalidationListener.forEach {
            it?.invalidated(this)
        }
    }
}
