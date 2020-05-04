package com.nio.cpu.tracker.view

import javafx.geometry.Pos
import javafx.scene.paint.Color
import tornadofx.*

class AuthorView: View("Author") {
    override val root = vbox {
        useMaxWidth = true
        style {
            startMargin = 15.px
            minHeight = 50.px
            alignment = Pos.CENTER_LEFT
        }
        label("Author: Alex.Liu Email:alex.liu@nio.com")
    }
}
