package com.nio.cpu.tracker.parser

import com.nio.cpu.tracker.data.TopItem
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

abstract class BaseParser(val filePath: String, val listener: ParseListener?) {

    open fun startParse(): List<TopItem> {
        val list = ArrayList<TopItem>()
        val file = File(filePath)
        if (file.isDirectory) {
            
        }
        return list
    }

    private fun startParse(file: File): List<TopItem> {
        assert(file.isFile)
        val list = ArrayList<TopItem>()

        return list
    }

    abstract fun doParse(line: String) : TopItem
}