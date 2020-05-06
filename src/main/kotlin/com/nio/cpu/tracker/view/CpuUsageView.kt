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
import javafx.scene.chart.XYChart
import javafx.scene.layout.BorderPane
import tornadofx.*

open class CpuUsageView(private val presenter: MainPresenter): View("CpuUsage") {
    override val root = BorderPane()
    lateinit var lineChart: LineChart<Number, Number>

    protected val mPropertiesPair = FXCollections.observableArrayList<Pair<String,  String>>()
    protected val mChartViewData = FXCollections.observableArrayList<XYChart.Series<Number, Number>>()
    protected val mFirstSeriesData = FXCollections.observableArrayList<XYChart.Data<Number, Number>>()


    init {
        MainSceneViewMode.selectTop.addListener(ChangeListener{ _, _, newValue ->
            when(newValue) {
                is CpuProcess -> updateChartView(newValue)
                is CpuThread -> updateChartView(newValue)
            }
        })
        lineChart = linechart(getPrefName(""), NumberAxis(), NumberAxis())
        lineChart.data = mChartViewData

        val firstSeries = XYChart.Series<Number, Number>()
        firstSeries.name = getPrefName("Usage")
        firstSeries.data = mFirstSeriesData
        mChartViewData.add(firstSeries)

        root.center = lineChart

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
        return "CPU $name"
    }

    private fun updateChartView(data: CpuThread) {
        val dataList = MainSceneViewMode.topItemList.filter {
            it.processId == data.processId && it.threadId == data.threadId
        }
        updateChartView(getPrefName(data.name), dataList)
        updatePropertiesView(dataList)
    }

    private var current = System.currentTimeMillis()
    protected fun resetCostTime() {
        current = System.currentTimeMillis()
    }

    protected fun printCostTime(name: String) {
        println("$name Cost: ${System.currentTimeMillis() - current}ms")
    }

    private fun updateChartView(data: CpuProcess) {
        resetCostTime()
        val dataList = MainSceneViewMode.topItemList.filter {
            it.processId == data.processId && it.threadName == "com.nio.navi"
        }
        printCostTime("Filter Process")
        updateChartView(getPrefName(data.processName), dataList)
        updatePropertiesView(dataList)
    }

    open fun updateChartView(name: String, dataList: List<TopItem>) {
        resetCostTime()
        val dataResult = mutableListOf<XYChart.Data<Number, Number>>()
        for (item in dataList) {
            dataResult.add(XYChart.Data(item.updateTime, item.cpuPercent))
        }
        printCostTime("Generate chart data")
        mFirstSeriesData.setAll(dataResult)
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