package com.nio.cpu.tracker.parser

import com.nio.cpu.tracker.data.TopItem
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.*
import kotlin.collections.ArrayList

abstract class BaseParser(val filePath: String, val listener: ParseListener?) {

    open fun startParse(): List<TopItem> {
        listener?.onParseStart()
        val list = ArrayList<TopItem>()
        val file = File(filePath)
        assert(file.isFile)
        val totalSize = file.length()
        var currentSize = 0L
        val reader = BufferedReader(FileReader(file))
        var line: String = ""
        while (({ line = reader.readLine();line }) != null) {
            currentSize += line.length
            var item = doParse(line)
            if (item != null) {
                list.add(item)
            }
            listener?.onParseProgress(currentSize, totalSize)
        }
        listener?.onParseEnd(list)
        return list
    }

    abstract fun doParse(line: String) : TopItem?
}