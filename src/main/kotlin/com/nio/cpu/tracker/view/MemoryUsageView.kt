package com.nio.cpu.tracker.view

import com.nio.cpu.tracker.data.TopItem
import com.nio.cpu.tracker.presenter.MainPresenter
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import tornadofx.*

class MemoryUsageView(private val presenter: MainPresenter): CpuUsageView(presenter) {

    override fun updateChartView(name: String, dataList: List<TopItem>) {
        val dataResult = mutableListOf<XYChart.Data<Number, Number>>()
        for (item in dataList) {
            dataResult.add(XYChart.Data(item.updateTime, item.rss))
        }
        mFirstSeriesData.setAll(dataResult)
    }

    override fun updatePropertiesView(dataList: List<TopItem>) {
        val listPair = mutableListOf<Pair<String, String>>()
        val first = dataList.first()
        listPair.add(Pair("ProcessId", first.processId.toString()))
        listPair.add(Pair("ProcessName", first.processName))
        if (first.threadId != 0) {
            listPair.add(Pair("ThreadId", first.threadId.toString()))
            listPair.add(Pair("ThreadName", first.threadName.toString()))
        }
        listPair.add(Pair("Max Memory", "${dataList.maxBy {
            it.rss }?.rss?.div(1024)}MB"))
        listPair.add(Pair("Min Memory", "${dataList.minBy {
            it.rss}?.rss?.div(1024)}MB"))
        listPair.add(Pair("Ave Memory", " ${String.format("%.1f", dataList.sumByDouble { it.rss.toDouble() } / 1024 / dataList.size)}MB"))
        mPropertiesPair.setAll(listPair)
        if (mPropertiesPair.isNotEmpty() && !root.right.isVisible) {
            root.right.isVisible = true
        }
    }

    override fun getPrefName(name: String): String {
        return "Memory $name"
    }
}