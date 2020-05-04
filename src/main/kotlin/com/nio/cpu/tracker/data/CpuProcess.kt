package com.nio.cpu.tracker.data

data class CpuProcess(val processId: Int, val processName: String) {
    var threadList: MutableList<CpuThread> = ArrayList<CpuThread>()
    private val map = HashMap<Int, Boolean>()


    fun addThread(data: List<TopItem>){
        threadList.clear()
        map.clear();
        data.forEach {
            if (it.processId.equals(processId)) {
                if (!map.containsKey(it.threadId)) {
                    map.put(it.threadId, true)
                    threadList.add(CpuThread(processId, it.threadName, it.threadId))
                }
            }
        }
    }

    fun addThread(item: TopItem) {
        if (item.processId.equals(processId)) {
            if (!map.containsKey(item.threadId)) {
                map.put(item.threadId, true)
                threadList.add(CpuThread(processId, item.threadName, item.threadId))
            }
        }
    }
}