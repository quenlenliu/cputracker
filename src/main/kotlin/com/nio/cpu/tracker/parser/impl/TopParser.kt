package com.nio.cpu.tracker.parser.impl

import com.nio.cpu.tracker.data.TopItem
import com.nio.cpu.tracker.parser.BaseParser
import com.nio.cpu.tracker.parser.ParseListener
import java.util.regex.Pattern

class TopParser(val filePath: String, val listener: ParseListener): BaseParser(filePath, listener) {
    private var parseCount = 0L
    //top -m 7 -d 2 -t
    /*
     1835  4037  0   4% S 206988K  27320K  fg media    AudioIn_D       /system/bin/mediaserver
     15024 15024  2   3% R  12012K   2964K  fg root     top             top
     2755  3789  2   1% S 1575796K 431060K  fg u0_a34   Tmcom-MapRender com.nio.navi
     3278  4041  2   1% S 3197932K 767456K  fg system   pool-4-thread-1 com.nextev.speech
     2261  2271  2   0% S 2449700K 178068K  fg system   HeapTaskDaemon  system_server
     */
    //                            (1:pid)(2:tid)(3: PR) (4:percent)(5:state)(6:vss) (7:rss)        (8:user)(9:threadName)(10:process)
    private val parserTopStr = " *(\\d*) *(\\d*) *(\\d) *(\\d*)% *([A-Z]) *(\\d*)K *(\\d*)K *.{2} *(\\S*) *(\\S*) *(.*)"

    private val pattern: Pattern = Pattern.compile(parserTopStr)

    override fun doParse(line: String): TopItem? {
        return parserInternal(line)
    }

    private fun parserInternal(line: String): TopItem? {
        //println("parserInternal: $line")
        val matcher = pattern.matcher(line)
        if (matcher.find()) {
            val processId = matcher.group(1).toInt()
            val highThreadId = matcher.group(2).toInt()
            val threadPr = matcher.group(3).toInt()
            val highThreadCpuPercent = matcher.group(4).toFloat()
            val state = matcher.group(5)
            val vss = matcher.group(6).toInt()
            val rss = matcher.group(7).toInt()
            val user = matcher.group(8)
            val threadName = matcher.group(9)
            val processName = matcher.group(10)

            return TopItem(processId, processName, highThreadId, threadName, vss, rss, highThreadCpuPercent, ++parseCount)
        }
        return null
    }

}