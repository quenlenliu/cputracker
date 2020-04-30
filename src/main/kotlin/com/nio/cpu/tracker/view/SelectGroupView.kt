package com.nio.cpu.tracker.view

import com.nio.cpu.tracker.data.TopGroup
import javafx.collections.FXCollections
import javafx.scene.control.SelectionMode
import javafx.scene.control.TableCell
import tornadofx.*

class SelectGroupView : View("My View") {
    private val groupData = FXCollections.observableArrayList<SelectTopGroup>()
    override val root = borderpane() {
        center {
            for(index in 1 until 10) {
                val item = TopGroup("com.nio.navi-${index}")
                item.processId = index
                groupData.add(SelectTopGroup(false, item.processId, item.process))
            }
            vbox {
                tableview(groupData) {
                    isEditable = true
                    readonlyColumn("进程号", SelectTopGroup::processId)
                    readonlyColumn("进程名", SelectTopGroup::processName)
                    selectionModel.selectionMode = SelectionMode.SINGLE
                }
            }
        }
    }

}

class CheckBoxCell() : TableCell<SelectTopGroup, String?>() {
    private val checkBox = checkbox {  }
    override fun updateItem(item: String?, empty: Boolean) {
        super.updateItem(item, empty)
        if (empty) {
            graphic = null
        } else {
            checkBox.isSelected = false
        }
    }
}
