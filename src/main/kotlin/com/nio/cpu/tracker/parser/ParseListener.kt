package com.nio.cpu.tracker.parser

import com.nio.cpu.tracker.data.TopItem

interface ParseListener {
    fun onParseStart()
    fun onParseProgress(current: Long, total: Long, item: TopItem)
    fun onParseEnd(result: List<TopItem>)
}