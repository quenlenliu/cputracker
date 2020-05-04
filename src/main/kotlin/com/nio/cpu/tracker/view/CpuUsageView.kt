package com.nio.cpu.tracker.view

import com.nio.cpu.tracker.data.CpuProcess
import com.nio.cpu.tracker.data.CpuThread
import com.nio.cpu.tracker.data.TopItem
import com.nio.cpu.tracker.presenter.MainPresenter
import com.nio.cpu.tracker.viewmodel.MainSceneViewMode
import javafx.collections.FXCollections
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.layout.BorderPane
import tornadofx.*

open class CpuUsageView(private val presenter: MainPresenter): View("CpuUsage") {
    override val root = BorderPane()
    lateinit var lineChart: LineChart<String, Number>

    protected val mPropertiesPair = FXCollections.observableArrayList<Pair<String,  String>>()


    init {
        MainSceneViewMode.selectTop.addListener(ChangeListener{ _, _, newValue ->
            when(newValue) {
                is CpuProcess -> updateChartView(newValue)
                is CpuThread -> updateChartView(newValue)
            }
        })

        root.right = tableview(mPropertiesPair) {
            column("Key", String::class) {
                value {
                    it.value.first
                }
            }
            column("Value", String::class) {
                value { it.value.second }
            }
        }
        if (mPropertiesPair.isEmpty()) {
            root.right.isVisible = false
        }
    }

    open fun getPrefName(name: String): String {
        return "CPU: $name"
    }

    private fun updateChartView(data: CpuThread) {
        val dataList = MainSceneViewMode.topItemList.filter {
            it.processId == data.processId && it.threadId == data.threadId
        }
        updateChartView(getPrefName(data.name), dataList)
        updatePropertiesView(dataList)
    }

    private fun updateChartView(data: CpuProcess) {
        val current = System.currentTimeMillis()
        val dataList = MainSceneViewMode.topItemList.filter {
            it.processId == data.processId && it.threadId == 0
        }
        println("Filter Data cost: ${System.currentTimeMillis() - current}ms")
        updateChartView(getPrefName(data.processName), dataList)
        updatePropertiesView(dataList)
        println("Show Data cost: ${System.currentTimeMillis() - current}ms")
    }

    open fun updateChartView(name: String, dataList: List<TopItem>) {
        with(root) {
            lineChart = linechart(name, CategoryAxis(), NumberAxis()) {
                series("Cpu Usage") {
                    for (item in dataList) {
                        data(item.updateTime.toString(), item.cpuPercent)
                    }
                }
            }
            center = lineChart
        }
    }

    open fun updatePropertiesView(dataList: List<TopItem>) {
        val listPair = mutableListOf<Pair<String, String>>()
        val first = dataList.first()
        listPair.add(Pair("ProcessId:", first.processId.toString()))
        listPair.add(Pair("ProcessName", first.processName))
        if (first.threadId != 0) {
            listPair.add(Pair("ThreadId", first.threadId.toString()))
            listPair.add(Pair("ThreadName", first.threadName.toString()))
        }
        listPair.add(Pair("Max Cpu", "${dataList.maxBy {
            it.cpuPercent }?.cpuPercent}"))
        listPair.add(Pair("Min Cpu", "${dataList.minBy {
            it.cpuPercent }?.cpuPercent}"))
        listPair.add(Pair("Ave Cpu", " ${String.format("%.1f", dataList.sumByDouble { it.cpuPercent.toDouble() } / dataList.size)}"))
        mPropertiesPair.setAll(listPair)
        if (mPropertiesPair.isNotEmpty() && !root.right.isVisible) {
            root.right.isVisible = true
        }
    }
}