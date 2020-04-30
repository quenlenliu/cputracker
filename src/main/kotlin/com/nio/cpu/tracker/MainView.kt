package com.nio.cpu.tracker

import com.nio.cpu.tracker.data.TopGroup
import com.nio.cpu.tracker.data.TopItem
import com.nio.cpu.tracker.presenter.MainPresenter
import com.nio.cpu.tracker.view.SelectFileView
import com.nio.cpu.tracker.view.SelectGroupView
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import tornadofx.*

class MainView : View() {
    private val presenter = MainPresenter(this)
    private val chartView = ChartView()
    private val selectFileView = SelectFileView(presenter)
    override val root = borderpane {
        top = selectFileView.root
        right<RightView>()
        bottom<BottomView>()
        center = chartView.root
    }

    init {
        root.titledpane("Hello World")
        root.minWidth = 800.0
        root.minHeight = 600.0
    }

    fun updateChartView(data: TopGroup) {
        chartView.updateData(data)
    }
}

class RightView: View() {
        override val root = borderpane {
            minWidth = 300.0
            center<SelectGroupView>()
            style {
                backgroundColor += Color.GREEN
            }
        }
}

class BottomView: View() {
    override val root = label("Bottom View") {
        useMaxWidth = true
        style {
            backgroundColor += Color.GRAY
        }
    }
}

class ChartView : View("Charts") {
    override val root = BorderPane()
    lateinit var lineChart: LineChart<String, Number>;

    init {

    }

    fun updateData(data: TopGroup) {
        with(root) {
            clear()
            lineChart = linechart("CPU占用", CategoryAxis(), NumberAxis()) {
                series(data.process) {
                    for (item in data.group) {
                        data(item.updateTime.toString(), item.cpuPercent)
                    }
                }
            }
            center = lineChart
        }
    }
}
