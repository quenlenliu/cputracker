package com.nio.cpu.tracker.presenter

import com.nio.cpu.tracker.MainView
import com.nio.cpu.tracker.data.TopGroup
import com.nio.cpu.tracker.data.TopItem
import com.nio.cpu.tracker.parser.ParseListener
import javafx.application.Platform

class MainPresenter(val mainView: MainView) : ParseListener {

    override fun onParseStart() {
        println("onParseStart")
    }

    override fun onParseProgress(current: Long, total: Long, item: TopItem) {
        println("onParseProgress: $current, $total, $item")
    }

    override fun onParseEnd(result: List<TopItem>) {
        Platform.runLater {
            mainView.updateChartView(TopGroup.group(result, "com.nio.navi"))
        }
    }

}