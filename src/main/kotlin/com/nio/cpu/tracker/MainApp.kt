package com.nio.cpu.tracker

import tornadofx.App
import tornadofx.launch

class MainApp: App(MainView::class) {
}

fun main(args: Array<String>) {
    launch<MainApp>(args)
}