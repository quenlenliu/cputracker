package com.nio.cpu.tracker.view

import com.nio.cpu.tracker.data.Process
import com.nio.cpu.tracker.data.TopItem
import com.nio.cpu.tracker.viewmodel.MainSceneViewMode
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import tornadofx.*

class SelectGroupView : View("My View") {
    lateinit var mCpuUsageView: TreeView<Any>
    override val root = borderpane() {
        center {
           mCpuUsageView = treeview<Any> {
               style {
                   fontSize = 16.px
               }
               root = TreeItem(Process("All Process"))

               cellFormat {
                   text = when (it) {
                       is Process -> it.process
                       is TopItem -> it.threadName
                       else ->  throw IllegalArgumentException("Invalid value type")
                   }

               }
               onUserSelect {
                   println("onUserSelect: $it")
                   if (it is TopItem) {
                       MainSceneViewMode.selectTop.value = it as TopItem
                   } else if (it is Process){
                       if (it.threadList.isNotEmpty()) {
                           MainSceneViewMode.selectTop.value = (it as Process).threadList.first()
                       }
                   }
               }

               populate {parent ->
                   val value = parent.value
                   when {
                       parent == root -> MainSceneViewMode.processGroup
                       value is Process -> value.threadList
                       else -> null
                   }
               }
           }

            mCpuUsageView.root.expandTo(1)
        }

    }
}
