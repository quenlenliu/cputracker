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

class MemoryUsageView(private val presenter: MainPresenter): CpuUsageView(presenter) {

    override fun updateData(name: String, dataList: List<TopItem>) {
        with(root) {

            lineChart = linechart(name, CategoryAxis(), NumberAxis()) {
                series("Memory Usage") {
                    for (item in dataList) {
                        data(item.updateTime.toString(), item.rss)
                    }
                }
            }

            center = lineChart

            val first = dataList.first()
            val listPair = FXCollections.observableArrayList<Pair<String,  String>>()
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

            right = tableview(listPair) {
                column("Key", String::class) {
                    value {
                        it.value.first
                    }
                }
                column("Value", String::class) {
                    value { it.value.second }
                }
            }
        }

    }

    override fun getPrefName(name: String): String {
        return "Memory: $name"
    }
}