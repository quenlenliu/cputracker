package com.nio.cpu.tracker

import com.nio.cpu.tracker.data.CpuProcess
import com.nio.cpu.tracker.data.CpuThread
import com.nio.cpu.tracker.data.TopItem
import com.nio.cpu.tracker.presenter.MainPresenter
import com.nio.cpu.tracker.view.*
import com.nio.cpu.tracker.viewmodel.MainSceneViewMode
import javafx.collections.FXCollections
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.layout.BorderPane
import tornadofx.*

class MainView : View() {
    private val presenter = MainPresenter(this)
    private val chartView = vbox {
            style {
                spacing = 40.px
            }
            add(CpuUsageView(presenter))
            add(MemoryUsageView(presenter).root)
    }
    private val selectFileView = SelectFileView(presenter)
    override val root = borderpane {
        top = selectFileView.root
        left<AllProcessView>()
        bottom<AuthorView>()
        center = chartView
    }

    init {
        root.prefWidth = 1200.0
        root.prefHeight = 800.0
    }
}