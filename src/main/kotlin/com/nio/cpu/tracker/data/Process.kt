package com.nio.cpu.tracker.data

class Process(var process: String) {
    var threadList: MutableList<TopItem> = ArrayList<TopItem>()
    var processId: Int = 0
    private val map = HashMap<Int, Boolean>()

    constructor(processId: Int) : this("") {
        this.processId = processId
    }

    constructor(processId: Int, processName: String): this(processName) {
        this.processId = processId;
    }

    fun addThread(data: List<TopItem>){
        threadList.clear()
        map.clear();
        data.forEach {
            if (it.processId.equals(processId)) {
                if (!map.containsKey(it.threadId)) {
                    map.put(it.threadId, true)
                    threadList.add(it)
                }
            }
        }
        if (threadList.isNotEmpty()) {
            processId = threadList.first().processId
            process = threadList.first().processName
        }
    }

    fun addThread(item: TopItem) {
        if (item.processId.equals(processId)) {
            if (!map.containsKey(item.threadId)) {
                map.put(item.threadId, true)
                threadList.add(item)
            }
        }
    }

    companion object {
        fun group(data: List<TopItem>, process: String): Process {
            val group = Process(process)
            for (item in data) {
                if (item.processName.equals(process)) {
                    group.addThread(item)
                }
            }
            if (group.threadList.isNotEmpty()) {
                group.processId = group.threadList.first().processId
            }
            return group
        }

        fun group(data: List<TopItem>, processId: Int): Process {
            val group = Process(processId)
            for (item in data) {
                if (item.processId.equals(processId)) {
                    group.addThread(item)
                }
            }
            if (group.threadList.isNotEmpty()) {
                group.process = group.threadList.first().processName
            }
            return group
        }
    }
}