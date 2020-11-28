package com.izabela.tecladosensivel.components.keyboard.controllers

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.speech.tts.TextToSpeech
import android.view.KeyEvent
import android.view.inputmethod.InputConnection
import com.izabela.tecladosensivel.components.keyboard.KeyboardListener
import java.util.*
import kotlin.collections.ArrayList

abstract class KeyboardController(private val inputConnection: InputConnection) {

    private val listeners = ArrayList<KeyboardListener>()
    private var cursorPosition: Int = 0
    private var inputText = ""

    companion object {
        private fun deleteCharacter(text: String, index: Int): String {
            return StringBuilder(text).deleteCharAt(index).toString()
        }

        private fun addCharacter(text: String, addition: Char, index: Int): String {
            return text.substring(0, index) + addition + text.substring(index)
        }

        private val textToSpeechEngine: TextToSpeech by lazy {
            TextToSpeech(Activity., TextToSpeech.OnInitListener { status ->
                    if (status == TextToSpeech.SUCCESS) {
                        textToSpeechEngine.language = Locale.UK
                    }
                })
        }

        private fun textToSpeech(text: String) {
            TextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null)
        }
    }

    @Synchronized fun onKeyStroke(c: Char) {
        updateMembers()
        handleKeyStroke(c)
        for (listener in listeners) {
            listener.characterClicked(c)
        }
    }

    @Synchronized fun onKeyStroke(key: SpecialKey) {
        updateMembers()
        handleKeyStroke(key)
        for (listener in listeners) {
            listener.specialKeyClicked(key)
        }
    }

    fun registerListener(listener: KeyboardListener) {
        listeners.add(listener)
    }

    private fun updateMembers() {
        val beforeText = beforeCursor()
        val afterText = afterCursor()
        cursorPosition = beforeText.length
        inputText = beforeText + afterText
    }

    private fun beforeCursor(): String {
        return inputConnection.getTextBeforeCursor(100, 0).toString()
    }

    private fun afterCursor(): String {
        return inputConnection.getTextAfterCursor(100, 0).toString()
    }

    internal fun cursorPosition(): Int {
        return cursorPosition
    }

    internal fun inputText(): String {
        return inputText
    }

    internal fun deleteNextCharacter() {
        if (cursorPosition >= inputText.length) {
            return
        }
        inputConnection.deleteSurroundingText(0, 1)
        inputText = deleteCharacter(inputText, cursorPosition)
    }

    internal fun deletePreviousCharacter() {
        if (cursorPosition == 0) {
            return
        }
        inputConnection.deleteSurroundingText(1, 0)
        inputText = deleteCharacter(inputText, --cursorPosition)
    }

    internal fun addCharacter(c: Char) {
        if (cursorPosition >= maxCharacters()) {
            return
        }
        inputConnection.commitText(c.toString(), 1)
        inputText = if (cursorPosition++ >= inputText.length) { inputText + c } else {
            addCharacter(inputText, c, (cursorPosition - 1))
        }
    }

    internal fun replaceNextCharacter(c: Char) {
        deleteNextCharacter()
        addCharacter(c)
    }

    internal fun synchronousMoveCursorForward() {
        replaceNextCharacter(inputText()[cursorPosition()])
    }

    internal fun moveCursorForwardAction() {
        if (cursorPosition >= inputText.length) {
            return
        }
        inputConnection.sendKeyEvent(
            KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT)
        )
        inputConnection.sendKeyEvent(
            KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_RIGHT)
        )
    }

    internal fun moveCursorBackAction() {
        if (cursorPosition == 0) {
            return
        }
        inputConnection.sendKeyEvent(
            KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT)
        )
        inputConnection.sendKeyEvent(
            KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_LEFT)
        )
    }

    internal fun clearAll() {
        while (cursorPosition < inputText.length) {
            deleteNextCharacter()
        }
        while (cursorPosition > 0) {
            deletePreviousCharacter()
        }
    }

    internal abstract fun handleKeyStroke(c: Char)

    internal abstract fun handleKeyStroke(key: SpecialKey)

    internal abstract fun maxCharacters(): Int

    enum class SpecialKey {
        DELETE,
        BACKSPACE,
        CLEAR,
        FORWARD,
        BACK,
        NEXT,
        CAPS,
        SYMBOL,
        ALPHA,
        DONE
    }
}