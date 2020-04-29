package com.nio.cpu.tracker.view

import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.stage.FileChooser
import tornadofx.*
import java.io.File

class SelectFileView: View("SelectFile") {
    override val root = hbox {
        style {
            alignment = Pos.CENTER
            fontSize = Dimension(18.0, Dimension.LinearUnits.pt)
            spacing = Dimension(8.0, Dimension.LinearUnits.pt)
            paddingTop = 8.0
            paddingBottom = 8.0
        }
        val filePath = label ("请选择你的Top文件")

        button("选择") {
            action {
                val chooseFile = chooseFile("Select File", arrayOf(FileChooser.ExtensionFilter(".txt", mutableListOf("*.txt"))))
                if (chooseFile.isNotEmpty()) {
                    filePath.text = chooseFile.first().absolutePath
                } else {
                    filePath.text = "请重新选择！"
                }
            }
        }


        button("开始解析") {
            action {
                val file = File(filePath.text.trim())
                if (isValidFilePath(file)) {
                    println("Start parse: " + file.absolutePath)
                } else {
                    println("File error, can't parse!!!!!!!!!!!")
                }
            }
        }
    }

    private fun isValidFilePath(file: File): Boolean {
        return file.exists() && file.isFile
    }
}