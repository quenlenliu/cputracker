package com.nio.cpu.tracker.data

data class TopItem(val processId: Int, val processName: String, val threadId: Int, val threadName: String,
                   val vss: Int, val rss: Int, val cpuPercent: Float, val updateTime: Long)