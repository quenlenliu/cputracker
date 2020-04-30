package com.nio.cpu.tracker.parser

import com.nio.cpu.tracker.data.TopItem
import java.util.regex.Pattern

class ProcessParser(val filePath: String, val listener: ParseListener): BaseParser(filePath, listener) {
    //top -m 7 -d 1
    /*
     3217  2   8% S    28 3083028K 669960K  fg system   com.nextev.speech
     2919  0   3% S   117 1330740K 315820K  fg u0_a53   com.nio.navi
     2260  0   2% S   153 2449504K 172972K  fg system   system_server
     1835  2   1% S    20 198724K  27208K  fg media    /system/bin/mediaserver
     1845  2   1% S     6  18352K    640K  fg shell    /sbin/adbd
    25412  0   1% R     1  12012K   2480K  fg shell    top
     1783  2   0% S    15  98648K  51828K  fg system   /system/bin/surfaceflinger
     */
    //                            (1:pid) (2: PR) (3:percent)(4:state)(5:thr)(6:vss) (7:rss)        (8:user)(9:process)
    private val parserTopStr = " *(\\d*) *(\\d) *(\\d*)% *([A-Z]) *(\\d*) *(\\d*)K *(\\d*)K *.{2} *(\\S*) *(\\S*) *(.*)"

    private val pattern: Pattern = Pattern.compile(parserTopStr)
    override fun doParse(line: String): TopItem? {
        return parserInternal(line)
    }

    private fun parserInternal(line: String): TopItem? {
        //println("parserInternal: $line")
        val matcher = pattern.matcher(line)
        if (matcher.find()) {
            val processId = matcher.group(1).toInt()
            val threadPr = matcher.group(2).toInt()
            val highThreadCpuPercent = matcher.group(3).toFloat()
            val state = matcher.group(4)
            val thr = matcher.group(5).toInt()
            val vss = matcher.group(6).toInt()
            val rss = matcher.group(7).toInt()
            val user = matcher.group(8)
            val processName = matcher.group(9)

            return TopItem(processId, processName, 0, processName, vss, rss, highThreadCpuPercent, System.currentTimeMillis())
        }
        return null
    }
}