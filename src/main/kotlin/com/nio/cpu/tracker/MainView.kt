package com.nio.cpu.tracker

import com.nio.cpu.tracker.data.Process
import com.nio.cpu.tracker.presenter.MainPresenter
import com.nio.cpu.tracker.view.SelectFileView
import com.nio.cpu.tracker.view.SelectGroupView
import com.nio.cpu.tracker.viewmodel.MainSceneViewMode
import javafx.geometry.Pos
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import tornadofx.*

class MainView : View() {
    private val presenter = MainPresenter(this)
    private val chartView = ChartView()
    private val selectFileView = SelectFileView(presenter)
    override val root = borderpane {
        top = selectFileView.root
        left<AllProcessView>()
        bottom<BottomView>()
        center = chartView.root
    }

    init {
        root.titledpane("Hello World")
        root.minWidth = 1200.0
        root.minHeight = 800.0
    }

    fun updateChartView(data: Process) {
        chartView.updateData(data)
    }
}

class AllProcessView: View() {
        override val root = borderpane {
            center<SelectGroupView>()
            style {
                backgroundColor += Color.GREEN
            }
        }
}

class BottomView: View() {
    override val root = vbox {
        useMaxWidth = true
        style {
            backgroundColor += Color.ALICEBLUE
            minHeight = 30.px
            alignment = Pos.CENTER_LEFT
        }
        label("Author: Alex.Liu Email:alex.liu@nio.com")
    }
}

class ChartView : View("Charts") {
    override val root = BorderPane()
    lateinit var lineChart: LineChart<String, Number>;

    init {
        MainSceneViewMode.selectTop.addListener(ChangeListener{ observable, oldValue, newValue ->
            val group = Process(newValue.processId, newValue.processName)
            updateData(group)
        })
    }

    fun updateData(data: Process) {
        val dataList = MainSceneViewMode.topItemList.filter {
            it.processId == data.processId && it.threadId == 0
        }
        with(root) {
            clear()
            lineChart = linechart("CPU占用", CategoryAxis(), NumberAxis()) {
                series(data.process) {
                    for (item in dataList) {
                        data(item.updateTime.toString(), item.cpuPercent)
                    }
                }
            }
            center = lineChart

            right = label {
                text = "Max Cpu Percent ${dataList.maxBy {
                    it.cpuPercent
                }?.cpuPercent} \nMin CpuPercent ${dataList.minBy {
                    it.cpuPercent
                }?.cpuPercent} \nAverage Cpu: ${String.format("%.1f", dataList.sumByDouble { it.cpuPercent.toDouble() } / dataList.size)}"
            }
        }
    }
}
