package com.nio.cpu.tracker.presenter

import com.nio.cpu.tracker.MainView
import com.nio.cpu.tracker.data.TopItem
import com.nio.cpu.tracker.parser.ParseListener
import com.nio.cpu.tracker.viewmodel.MainSceneViewMode

class MainPresenter(val mainView: MainView) : ParseListener {

    override fun onParseStart() {
        println("onParseStart")
    }

    override fun onParseProgress(current: Long, total: Long, item: TopItem) {
        println("onParseProgress: $current, $total, $item")
    }

    override fun onParseEnd(result: List<TopItem>) {
        MainSceneViewMode.topItemList.clear()
        MainSceneViewMode.topItemList.addAll(result)
    }

    fun setFilter(range: LongRange) {
        mainView.updateFilter(range)
    }

}