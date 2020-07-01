package com.izabela.tecladosensivel.components.keyboard.controllers

import android.view.inputmethod.InputConnection

class NumberDecimalKeyboardController(inputConnection: InputConnection):
    DefaultKeyboardController(inputConnection) {

    override fun handleKeyStroke(c: Char) {
        if (c == '.') {
            if (!inputText().contains('.')) {
                addCharacter(c)
            }
        } else {
            addCharacter(c)
        }
    }
}