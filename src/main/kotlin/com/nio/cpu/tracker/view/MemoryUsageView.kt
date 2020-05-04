package com.nio.cpu.tracker.view

import com.nio.cpu.tracker.data.TopItem
import com.nio.cpu.tracker.presenter.MainPresenter
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import tornadofx.*

class MemoryUsageView(private val presenter: MainPresenter): CpuUsageView(presenter) {

    override fun updateChartView(name: String, dataList: List<TopItem>) {
        with(root) {
            lineChart = linechart(name, CategoryAxis(), NumberAxis()) {
                style {
                    size = 2.px
                }
                series("Memory Usage") {
                    for (item in dataList) {
                        data(item.updateTime.toString(), item.rss)
                    }
                }
            }
            center = lineChart
        }
    }

    override fun updatePropertiesView(dataList: List<TopItem>) {
        val listPair = mutableListOf<Pair<String, String>>()
        val first = dataList.first()
        listPair.add(Pair("ProcessId:", first.processId.toString()))
        listPair.add(Pair("ProcessName", first.processName))
        if (first.threadId != 0) {
            listPair.add(Pair("ThreadId", first.threadId.toString()))
            listPair.add(Pair("ThreadName", first.threadName.toString()))
        }
        listPair.add(Pair("Max Memory", "${dataList.maxBy {
            it.rss }?.rss}K"))
        listPair.add(Pair("Min Memory", "${dataList.minBy {
            it.rss}?.rss}K"))
        listPair.add(Pair("Ave Memory", " ${String.format("%.1f", dataList.sumByDouble { it.rss.toDouble() } / dataList.size)}K"))
        mPropertiesPair.setAll(listPair)
        if (mPropertiesPair.isNotEmpty() && !root.right.isVisible) {
            root.right.isVisible = true
        }
    }

    override fun getPrefName(name: String): String {
        return "Memory: $name"
    }
}