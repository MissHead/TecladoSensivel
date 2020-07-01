package com.izabela.tecladosensivel.components.keyboard.controllers

import android.view.inputmethod.InputConnection

open class DefaultKeyboardController(inputConnection: InputConnection):
    KeyboardController(inputConnection) {

    companion object {
        private const val MAX_CHARACTERS: Int = Int.MAX_VALUE
    }

    override fun handleKeyStroke(c: Char) {
        addCharacter(c)
    }

    override fun handleKeyStroke(key: KeyboardController.SpecialKey) {
        when(key) {
            SpecialKey.DELETE -> {
                deleteNextCharacter()
            }
            SpecialKey.BACKSPACE -> {
                deletePreviousCharacter()
            }
            SpecialKey.CLEAR -> {
                clearAll()
            }
            SpecialKey.FORWARD -> {
                moveCursorForwardAction()
            }
            SpecialKey.BACK -> {
                moveCursorBackAction()
            }
            else -> {
                return
            }
        }
    }

    override fun maxCharacters(): Int {
        return MAX_CHARACTERS
    }
}