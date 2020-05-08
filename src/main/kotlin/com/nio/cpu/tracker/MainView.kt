package com.nio.cpu.tracker

import com.nio.cpu.tracker.presenter.MainPresenter
import com.nio.cpu.tracker.view.*
import javafx.geometry.Pos
import tornadofx.*
import java.awt.TextField

class MainView : View() {
    private val presenter = MainPresenter(this)
    val mCpuUsageView = CpuUsageView(presenter)
    val mMemoryUsageView = MemoryUsageView(presenter)
    lateinit var from: javafx.scene.control.TextField
    lateinit var to: javafx.scene.control.TextField
    private val chartView = vbox {
            style {
                spacing = 40.px
            }
            hbox {
                style {
                    paddingLeft = 20
                    spacing = 8.px
                    alignment = Pos.CENTER_LEFT
                }
                label("From")
                from = textfield(0.toString()) {
                    action {
                        notifyRangeChange()
                    }
                }
                label("To")
                to = textfield(Long.MAX_VALUE.toString()) {
                    action {
                        notifyRangeChange()
                    }
                }
            }
            add(mCpuUsageView)
            add(mMemoryUsageView)
    }

    private fun notifyRangeChange() {
        val range = LongRange(from.text.toLong(), to.text.toLong())
        presenter.setFilter(range)
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

    fun updateFilter(range: LongRange) {
        mCpuUsageView.setFilter(range)
        mMemoryUsageView.setFilter(range)
    }
}