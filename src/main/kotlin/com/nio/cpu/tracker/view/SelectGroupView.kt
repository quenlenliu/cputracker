package com.nio.cpu.tracker.view

import com.nio.cpu.tracker.data.TopGroup
import javafx.stage.FileChooser
import tornadofx.*
import java.awt.CheckboxGroup


class SelectGroupView : View("My View") {


    val groupData = ArrayList<TopGroup>().asObservable()

    override val root = borderpane() {
        center {
            for(index in 1 until 10) {
                val item = TopGroup("com.nio.navi-${index}")
                item.processId = index
                groupData.add(item)
            }
                vbox {
                    tableview(groupData) {
                        isEditable = true
                        readonlyColumn("进程号", TopGroup::processId)
                        readonlyColumn("进程名", TopGroup::process)
                    }
                }
            }
        }
}
