package com.nio.cpu.tracker.parser

import com.nio.cpu.tracker.data.TopItem
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import kotlin.collections.ArrayList

abstract class BaseParser(private val filePath: String, private val listener: ParseListener?) {

    open fun startParse(): List<TopItem> {
        listener?.onParseStart()
        val list = ArrayList<TopItem>()
        val file = File(filePath)
        assert(file.isFile)
        val totalSize = file.length()
        var currentSize = 0L
        val reader = BufferedReader(FileReader(file))
        var line = reader.readLine()
        var preview: TopItem? = null
        while (line != null) {
            currentSize += line.length
            var item = doParse(line)
            if (item != null && item.cpuPercent != preview?.cpuPercent) {
                list.add(item)
                preview = item
                listener?.onParseProgress(currentSize, totalSize, item)
            }
            println(line)
            val temp = reader.readLine() ?: break
            line = temp
        }
        reader.close()
        listener?.onParseEnd(list)
        return list
    }

    abstract fun doParse(line: String) : TopItem?
}