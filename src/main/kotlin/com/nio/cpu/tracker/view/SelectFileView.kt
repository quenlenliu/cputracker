package com.nio.cpu.tracker.view

import com.nio.cpu.tracker.parser.BaseParser
import com.nio.cpu.tracker.parser.ParseListener
import com.nio.cpu.tracker.parser.impl.ProcessParser
import com.nio.cpu.tracker.parser.impl.ProcessParserApi28
import com.nio.cpu.tracker.parser.impl.TopParser
import com.nio.cpu.tracker.presenter.MainPresenter
import javafx.geometry.Pos
import javafx.scene.control.RadioButton
import javafx.scene.control.ToggleGroup
import javafx.stage.FileChooser
import tornadofx.*
import java.io.File

class SelectFileView(private val presenter: MainPresenter): View("SelectFile") {

    lateinit var androidVersion: ToggleGroup
    lateinit var analyzeType: ToggleGroup
    companion object {
        const val ANDROID_VERSION_6 = "Android 6.0"
        const val ANDROID_VERSION_8 = "Android 8.0"
        const val ANALYZE_TYPE_PROCESS = "Process"
        const val ANALYZE_TYPE_THREAD = "Thread"
    }

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
                /*val chooseFile = chooseFile("Select File", arrayOf(FileChooser.ExtensionFilter(".txt", mutableListOf("*.txt")))
                        , File("/home/quenlen/Desktop/Top/"), FileChooserMode.Single)*/
                val chooseFile = chooseFile("Select File", arrayOf(FileChooser.ExtensionFilter(".txt", mutableListOf("*.txt")))
                        , null,  FileChooserMode.Single)
                if (chooseFile.isNotEmpty()) {
                    filePath.text = chooseFile.first().absolutePath
                } else {
                    filePath.text = "Please reselect your file"
                }
            }
        }


        vbox {
            style {
                spacing = 8.px
            }
            androidVersion = togglegroup {
                radiobutton(ANDROID_VERSION_6) { isSelected = true  }
                radiobutton(ANDROID_VERSION_8) {  }
            }
        }


        vbox {
            style {
                spacing = 8.px
            }
            analyzeType = togglegroup {
                radiobutton(ANALYZE_TYPE_PROCESS) { isSelected = true }

                radiobutton(ANALYZE_TYPE_THREAD) { }
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
        //
        //return ProcessParser(filePath, listener)
        val androidRadioButton = androidVersion.selectedToggle as RadioButton
        val analyzeTpe = analyzeType.selectedToggle as RadioButton
        return if (androidRadioButton.text == ANDROID_VERSION_6) {
            if (analyzeTpe.text == ANALYZE_TYPE_PROCESS) {
                ProcessParser(filePath, listener)
            } else {
                TopParser(filePath, listener)
            }
        } else {
            if (analyzeTpe.text == ANALYZE_TYPE_PROCESS) {
                ProcessParserApi28(filePath, listener)
            } else {
                TopParser(filePath, listener)
            }
        }
    }

    private fun isValidFilePath(file: File): Boolean {
        return file.exists() && file.isFile
    }
}