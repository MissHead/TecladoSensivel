package com.izabela.tecladosensivel.components.keyboard

import com.izabela.tecladosensivel.components.keyboard.controllers.KeyboardController

interface KeyboardListener {
    fun characterClicked(c: Char)
    fun specialKeyClicked(key: KeyboardController.SpecialKey)
}