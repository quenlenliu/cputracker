package com.nio.cpu.tracker.parser.impl

import com.nio.cpu.tracker.data.TopItem
import com.nio.cpu.tracker.parser.BaseParser
import com.nio.cpu.tracker.parser.ParseListener
import java.util.regex.Pattern

class ProcessParserApi28(val filePath: String, val listener: ParseListener): BaseParser(filePath, listener) {
    private var parseCount = 0L
    //top -m 7 -d 1
    /*
  PID USER         PR  NI VIRT  RES  SHR S[%CPU] %MEM     TIME+ ARGS
 1800 system       -3  -8 174M  14M 6.4M S 15.3   0.6  84:23.41 android.hardware.graphics.composer@2.1-service
 4040 u0_a129      10 -10 1.9G 181M 135M S  7.6   9.0  18:11.93 com.google.android.apps.messaging
12386 root         20   0  12M 3.9M 3.3M R  3.8   0.1   0:00.01 top -n 10
 1812 system       -2  -8 247M  33M  10M S  3.8   1.6  15:02.90 surfaceflinger
12380 root         20   0 9.1M 2.4M 2.1M S  0.0   0.1   0:00.00 sh
     */
    //                            (1:pid) (2:user) (3: PR)      (4:NI)   (5:VIRT)(6:RES) (7:SHR) (8:state)(9:cpu%)(10:mem%)(11:time)(12:process)
    private val parserTopStr = " *(\\d+) +(\\S+) +(\\-?\\d+) +(\\-?\\d+) +(\\S+) +(\\S+) +(\\S*) +([A-Z]) +(\\S+) +(\\S+) +(\\S+) +(\\S+) *(.*)"


    private val pattern: Pattern = Pattern.compile(parserTopStr)
    override fun doParse(line: String): TopItem? {
        return parserInternal(line)
    }

    private fun parserInternal(line: String): TopItem? {
        //println("parserInternal: $line")
        val matcher = pattern.matcher(line)
        if (matcher.find()) {
            val processId = matcher.group(1).toInt()
            val user = matcher.group(2)
            val pr = matcher.group(3).toInt()
            val ni = matcher.group(4).toInt()
            val virt = matcher.group(5)
            val res = matcher.group(6)
            val shr = matcher.group(7)
            val state = matcher.group(8)
            val cpuPercent = matcher.group(9).toFloat()
            val memPercent = matcher.group(10).toFloat()
            val processName = matcher.group(12)
            return TopItem(processId, processName, 0, processName, getByteSize(virt), getByteSize(res), cpuPercent, ++parseCount)
        }
        return null
    }

    private fun getByteSize(sizeLine: String): Int {
        if (sizeLine.length <= 1) {
            return 0
        }
        val last = sizeLine.last()
        val size = sizeLine.substring(0, sizeLine.length - 1).toFloat()
        if (last == 'G') {
            return (size * 1024 * 1024).toInt()
        } else if (last == 'M') {
            return (size * 1024).toInt()
        } else if (last == 'K') {
            return size.toInt()
        } else {
            return 0
        }
    }

}