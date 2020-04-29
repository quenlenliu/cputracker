package com.nio.cpu.tracker.parser

interface ParseListener {
    fun onParseStart()
    fun onParseProgress(current: Int, total: Int)
    fun onParseEnd()
}