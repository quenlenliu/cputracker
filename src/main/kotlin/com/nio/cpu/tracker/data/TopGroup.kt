package com.nio.cpu.tracker.data

class TopGroup(var process: String) {
    var group: MutableList<TopItem> = ArrayList<TopItem>()
    var processId: Int = 0

    constructor(processId: Int) : this("") {
        this.processId = processId
    }

    fun addGroup( data: List<TopItem>){
        group.clear()
        group.addAll(data)
    }

    fun addGroup(item: TopItem) {
        group.add(item)
    }

    companion object {
        fun group(data: List<TopItem>, process: String): TopGroup {
            val group = TopGroup(process)
            for (item in data) {
                if (item.processName.equals(process)) {
                    group.addGroup(item)
                }
            }
            if (group.group.isNotEmpty()) {
                group.processId = group.group.first().processId
            }
            return group
        }

        fun group(data: List<TopItem>, processId: Int): TopGroup {
            val group = TopGroup(processId)
            for (item in data) {
                if (item.processId.equals(processId)) {
                    group.addGroup(item)
                }
            }
            if (group.group.isNotEmpty()) {
                group.process = group.group.first().processName
            }
            return group
        }
    }
}