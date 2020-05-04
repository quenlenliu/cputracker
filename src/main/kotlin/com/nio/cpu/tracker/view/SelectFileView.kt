package com.nio.cpu.tracker.view

import com.nio.cpu.tracker.parser.BaseParser
import com.nio.cpu.tracker.parser.ParseListener
import com.nio.cpu.tracker.parser.impl.ProcessParser
import com.nio.cpu.tracker.parser.impl.ProcessParserApi28
import com.nio.cpu.tracker.presenter.MainPresenter
import javafx.geometry.Pos
import javafx.stage.FileChooser
import tornadofx.*
import java.io.File

class SelectFileView(private val presenter: MainPresenter): View("SelectFile") {
    override val root = hbox {
        style {
            alignment = Pos.CENTER
            fontSize = Dimension(15.0, Dimension.LinearUnits.pt)
            spacing = Dimension(8.0, Dimension.LinearUnits.pt)
            paddingTop = 8.0
            paddingBottom = 8.0
        }
        val filePath = textfield ("Please select your file")

        button("Select File") {
            action {
                val chooseFile = chooseFile("Select File", arrayOf(FileChooser.ExtensionFilter(".txt", mutableListOf("*.txt"))))
                if (chooseFile.isNotEmpty()) {
                    filePath.text = chooseFile.first().absolutePath
                } else {
                    filePath.text = "Please reselect your file"
                }
            }
        }

        button("Start Analyze") {
            action {
                val file = File(filePath.text.trim())
                if (isValidFilePath(file)) {
                    println("Start parse: " + file.absolutePath)
                    val parser = getDesireParser(
                        filePath.text.trim(),
                        presenter
                    )
                    runAsync {
                        parser.startParse()
                    }
                } else {
                    println("File error, can't parse!!!!!!!!!!!")
                }

            }
        }
    }

    private fun getDesireParser(filePath: String, listener: ParseListener): BaseParser {
        return ProcessParserApi28(filePath, listener)
        //return ProcessParser(filePath, listener)
    }

    private fun isValidFilePath(file: File): Boolean {
        return file.exists() && file.isFile
    }
}