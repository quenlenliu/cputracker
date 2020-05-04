package com.nio.cpu.tracker.view

import com.nio.cpu.tracker.data.CpuProcess
import com.nio.cpu.tracker.data.CpuThread
import com.nio.cpu.tracker.viewmodel.MainSceneViewMode
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import tornadofx.*

class AllProcessView : View("My View") {
    lateinit var mCpuUsageView: TreeView<Any>
    override val root = borderpane() {
        center {
           mCpuUsageView = treeview<Any> {
               style {
                   fontSize = 16.px
               }
               root = TreeItem(CpuProcess(0, "All Process"))

               cellFormat {
                   text = when (it) {
                       is CpuProcess -> it.processName
                       is CpuThread -> it.name
                       else ->  throw IllegalArgumentException("Invalid value type")
                   }

               }
               onUserSelect {
                   println("onUserSelect: $it")
                   if (it is CpuProcess && it.processId == 0) {
                       return@onUserSelect
                   }
                   MainSceneViewMode.selectTop.value = it
               }

               populate {parent ->
                   val value = parent.value
                   when {
                       parent == root -> MainSceneViewMode.processGroup
                       value is CpuProcess -> value.threadList
                       else -> null
                   }
               }
           }

            mCpuUsageView.root.expandTo(1)
        }

    }
}
