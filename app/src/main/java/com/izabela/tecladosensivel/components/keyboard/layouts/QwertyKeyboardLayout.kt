package com.izabela.tecladosensivel.components.keyboard.layouts

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.izabela.tecladosensivel.components.keyboard.KeyboardListener
import com.izabela.tecladosensivel.components.keyboard.controllers.KeyboardController
import com.izabela.tecladosensivel.components.utilities.ComponentUtils


class QwertyKeyboardLayout(context: Context, controller: KeyboardController?) :
    KeyboardLayout(context, controller) {

    constructor(context: Context): this(context, null)

    private var capsState: CapsState = CapsState.CAPS_DISABLED
    private var symbolsState: SymbolState = SymbolState.SYMBOLS_DISABLED
    private val columnWidth = 0.09f

    private enum class CapsState {
        CAPS_DISABLED,
        CAPS_ENABLED,
        CAPS_LOCK_ENABLED
    }

    private enum class SymbolState {
        SYMBOLS_DISABLED,
        SYMBOL_ONE_DISPLAYED,
        SYMBOL_TWO_DISPLAYED,
    }

    init {
        controller?.registerListener(object: KeyboardListener {
            override fun characterClicked(c: Char) {
                if (capsState === CapsState.CAPS_ENABLED) {
                    capsState = CapsState.CAPS_DISABLED
                    createKeyboard()
                }
            }

            override fun specialKeyClicked(key: KeyboardController.SpecialKey) {
                when(key) {
                    KeyboardController.SpecialKey.CAPS -> {
                        capsState = when(capsState) {
                            CapsState.CAPS_DISABLED -> {
                                CapsState.CAPS_ENABLED
                            }
                            CapsState.CAPS_ENABLED -> {
                                CapsState.CAPS_LOCK_ENABLED
                            }
                            CapsState.CAPS_LOCK_ENABLED -> {
                                CapsState.CAPS_DISABLED
                            }
                        }
                        createKeyboard()
                    }
                    KeyboardController.SpecialKey.SYMBOL -> {
                        symbolsState = when(symbolsState) {
                            SymbolState.SYMBOLS_DISABLED -> {
                                SymbolState.SYMBOL_ONE_DISPLAYED
                            }
                            SymbolState.SYMBOL_ONE_DISPLAYED -> {
                                SymbolState.SYMBOL_TWO_DISPLAYED
                            }
                            SymbolState.SYMBOL_TWO_DISPLAYED -> {
                                SymbolState.SYMBOL_ONE_DISPLAYED
                            }
                        }
                        createKeyboard()
                    }
                    KeyboardController.SpecialKey.ALPHA -> {
                        symbolsState = SymbolState.SYMBOLS_DISABLED
                        createKeyboard()
                    }
                    else -> return
                }
            }
        })
    }

    override fun createRows(): List<LinearLayout> {
        if (symbolsState !== SymbolState.SYMBOLS_DISABLED) {
            return when(symbolsState) {
                SymbolState.SYMBOL_ONE_DISPLAYED -> {
                    createSymbolsOneRows()
                }
                SymbolState.SYMBOL_TWO_DISPLAYED -> {
                    createSymbolsTwoRows()
                }
                else -> {
                    ArrayList() // Isso nunca vai acontecer
                }
            }
        } else {
            return when(capsState) {
                CapsState.CAPS_DISABLED -> {
                    createLowerCaseRows()
                }
                CapsState.CAPS_ENABLED -> {
                    createUpperCaseRows()
                }
                CapsState.CAPS_LOCK_ENABLED -> {
                    createUpperCaseRows()
                }
            }
        }
    }

    private fun createLowerCaseRows(): List<LinearLayout> {
        val rowOne = ArrayList<View>()
        rowOne.add(createButton("1", columnWidth, '1'))
        rowOne.add(createButton("2", columnWidth, '2'))
        rowOne.add(createButton("3", columnWidth, '3'))
        rowOne.add(createButton("4", columnWidth, '4'))
        rowOne.add(createButton("5", columnWidth, '5'))
        rowOne.add(createButton("b", columnWidth, 'b'))
        rowOne.add(createButton("c", columnWidth, 'c'))
        rowOne.add(createButton("d", columnWidth, 'd'))
        rowOne.add(createButton("f", columnWidth, 'f'))
        rowOne.add(createButton("g", columnWidth, 'g'))

        val rowTwo = ArrayList<View>()
        rowTwo.add(createButton("6", columnWidth, '6'))
        rowTwo.add(createButton("7", columnWidth, '7'))
        rowTwo.add(createButton("8", columnWidth, '8'))
        rowTwo.add(createButton("9", columnWidth, '9'))
        rowTwo.add(createButton("0", columnWidth, '0'))
        rowTwo.add(createButton("h", columnWidth, 'h'))
        rowTwo.add(createButton("j", columnWidth, 'j'))
        rowTwo.add(createButton("k", columnWidth, 'k'))
        rowTwo.add(createButton("l", columnWidth, 'l'))
        rowTwo.add(createButton("m", columnWidth, 'm'))

        val rowThree = ArrayList<View>()
        rowThree.add(createButton("a", columnWidth, 'a'))
        rowThree.add(createButton("e", columnWidth, 'e'))
        rowThree.add(createButton("i", columnWidth, 'i'))
        rowThree.add(createButton("o", columnWidth, 'o'))
        rowThree.add(createButton("u", columnWidth, 'u'))
        rowThree.add(createButton("n", columnWidth, 'n'))
        rowThree.add(createButton("p", columnWidth, 'p'))
        rowThree.add(createButton("q", columnWidth, 'q'))
        rowThree.add(createButton("r", columnWidth, 'r'))
        rowThree.add(createButton("s", columnWidth, 's'))

        val rowFour = ArrayList<View>()

        rowFour.add(createButton("", (columnWidth * 4f), ' '))
        rowFour.add(createButton("t", columnWidth, 't'))
        rowFour.add(createButton("v", columnWidth, 'v'))
        rowFour.add(createButton("x", columnWidth, 'x'))
        rowFour.add(createButton("w", columnWidth, 'w'))
        rowFour.add(createButton("y", columnWidth, 'y'))
        rowFour.add(createButton("z", columnWidth, 'z'))

        val rowFive = ArrayList<View>()
        rowFive.add(createButton(
            "...", (columnWidth * 2f), KeyboardController.SpecialKey.SYMBOL))
        rowFive.add(createButton("APAGAR", (columnWidth * 5f), KeyboardController.SpecialKey.BACKSPACE))
        if (hasNextFocus) {
            rowFive.add(createButton(
                "PROX", (columnWidth * 1.2f), KeyboardController.SpecialKey.NEXT))
        } else {
            rowFive.add(createButton(
                "ENTER", (columnWidth * 4f), KeyboardController.SpecialKey.DONE))
        }
        val rows = ArrayList<LinearLayout>()
        rows.add(createRow(rowOne))
        rows.add(createRow(rowTwo))
        rows.add(createRow(rowThree))
        rows.add(createRow(rowFour))
        rows.add(createRow(rowFive))

        return rows
    }

    private fun createUpperCaseRows(): List<LinearLayout> {
        val rowOne = ArrayList<View>()
        rowOne.add(createButton("1", columnWidth, '1'))
        rowOne.add(createButton("2", columnWidth, '2'))
        rowOne.add(createButton("3", columnWidth, '3'))
        rowOne.add(createButton("4", columnWidth, '4'))
        rowOne.add(createButton("5", columnWidth, '5'))
        rowOne.add(createButton("B", columnWidth, 'B'))
        rowOne.add(createButton("C", columnWidth, 'C'))
        rowOne.add(createButton("D", columnWidth, 'D'))
        rowOne.add(createButton("F", columnWidth, 'F'))
        rowOne.add(createButton("G", columnWidth, 'G'))

        val rowTwo = ArrayList<View>()
        rowTwo.add(createButton("6", columnWidth, '6'))
        rowTwo.add(createButton("7", columnWidth, '7'))
        rowTwo.add(createButton("8", columnWidth, '8'))
        rowTwo.add(createButton("9", columnWidth, '9'))
        rowTwo.add(createButton("0", columnWidth, '0'))
        rowTwo.add(createButton("H", columnWidth, 'H'))
        rowTwo.add(createButton("J", columnWidth, 'J'))
        rowTwo.add(createButton("K", columnWidth, 'K'))
        rowTwo.add(createButton("L", columnWidth, 'L'))
        rowTwo.add(createButton("M", columnWidth, 'M'))

        val rowThree = ArrayList<View>()
        rowThree.add(createButton("A", columnWidth, 'A'))
        rowThree.add(createButton("E", columnWidth, 'E'))
        rowThree.add(createButton("I", columnWidth, 'I'))
        rowThree.add(createButton("O", columnWidth, 'O'))
        rowThree.add(createButton("U", columnWidth, 'U'))
        rowThree.add(createButton("N", columnWidth, 'N'))
        rowThree.add(createButton("P", columnWidth, 'P'))
        rowThree.add(createButton("Q", columnWidth, 'Q'))
        rowThree.add(createButton("R", columnWidth, 'R'))
        rowThree.add(createButton("S", columnWidth, 'S'))

        val rowFour = ArrayList<View>()

        rowFour.add(createButton("", (columnWidth * 4f), ' '))
        rowFour.add(createButton("T", columnWidth, 'T'))
        rowFour.add(createButton("V", columnWidth, 'V'))
        rowFour.add(createButton("X", columnWidth, 'X'))
        rowFour.add(createButton("W", columnWidth, 'W'))
        rowFour.add(createButton("Y", columnWidth, 'Y'))
        rowFour.add(createButton("Z", columnWidth, 'Z'))

        val rowFive = ArrayList<View>()
        rowFive.add(createButton(
            "...", (columnWidth * 2f), KeyboardController.SpecialKey.SYMBOL))
        rowFive.add(createButton("APAGAR", (columnWidth * 5f), KeyboardController.SpecialKey.BACKSPACE))
        if (hasNextFocus) {
            rowFive.add(createButton(
                "PROX", (columnWidth * 1.2f), KeyboardController.SpecialKey.NEXT))
        } else {
            rowFive.add(createButton(
                "ENTER", (columnWidth * 4f), KeyboardController.SpecialKey.DONE))
        }
        val rows = ArrayList<LinearLayout>()
        rows.add(createRow(rowOne))
        rows.add(createRow(rowTwo))
        rows.add(createRow(rowThree))
        rows.add(createRow(rowFour))
        rows.add(createRow(rowFive))

        return rows
    }

    private fun createSymbolsOneRows(): List<LinearLayout> {
        val rowTwo = ArrayList<View>()
        rowTwo.add(createButton("+", columnWidth, '+'))
        rowTwo.add(createButton("×", columnWidth, '×'))
        rowTwo.add(createButton("÷", columnWidth, '÷'))
        rowTwo.add(createButton("=", columnWidth, '='))
        rowTwo.add(createButton("%", columnWidth, '%'))
        rowTwo.add(createButton("_", columnWidth, '_'))

        val rowThree = ArrayList<View>()
        rowThree.add(createSpacer((columnWidth * 0.5f)))
        rowThree.add(createButton(",", columnWidth, ','))
        rowThree.add(createButton(".", columnWidth, '.'))
        rowThree.add(createButton("@", columnWidth, '@'))
        rowThree.add(createButton("#", columnWidth, '#'))
        rowThree.add(createButton("$", columnWidth, '$'))
        rowThree.add(createButton("/", columnWidth, '/'))
        rowThree.add(createButton("^", columnWidth, '^'))
        rowThree.add(createButton("&", columnWidth, '&'))
        rowThree.add(createButton("*", columnWidth, '*'))
        rowThree.add(createButton("(", columnWidth, '('))
        rowThree.add(createButton(")", columnWidth, ')'))
        if (hasNextFocus) {
            rowThree.add(createButton(
                "PROX", (columnWidth * 1.2f), KeyboardController.SpecialKey.NEXT))
        } else {
            rowThree.add(createButton(
                "ENTER", (columnWidth * 1.2f), KeyboardController.SpecialKey.DONE))
        }

        val rowFour = ArrayList<View>()

        rowFour.add(createCapsButton())
        rowFour.add(createButton("—", columnWidth, '-'))
        rowFour.add(createButton("'", columnWidth, '\''))
        rowFour.add(createButton("\"", columnWidth, '\"'))
        rowFour.add(createButton(":", columnWidth, ':'))
        rowFour.add(createButton(";", columnWidth, ';'))
        rowFour.add(createButton("!", columnWidth, '!'))
        rowFour.add(createButton("?", columnWidth, '?'))
        rowFour.add(createButton(",", columnWidth, ','))
        rowFour.add(createButton(".", columnWidth, '.'))
        rowFour.add(createSpacer(columnWidth))

        val rowFive = ArrayList<View>()
        rowFive.add(createButton(
            "1/2", (columnWidth *1.8f), KeyboardController.SpecialKey.SYMBOL))
        rowFive.add(createButton("", (columnWidth * 4f), ' '))
        rowFive.add(createButton("APAGAR", columnWidth, KeyboardController.SpecialKey.BACKSPACE))

        val rows = ArrayList<LinearLayout>()
        rows.add(createNumbersRow())
        rows.add(createRow(rowTwo))
        rows.add(createRow(rowThree))
        rows.add(createRow(rowFour))
        rows.add(createRow(rowFive))

        return rows
    }

    private fun createSymbolsTwoRows(): List<LinearLayout> {
        val rowTwo = ArrayList<View>()
        rowTwo.add(createButton(".", columnWidth, '.'))
        rowTwo.add(createButton(",", columnWidth, ','))
        rowTwo.add(createButton("`", columnWidth, '`'))
        rowTwo.add(createButton("~", columnWidth, '~'))
        rowTwo.add(createButton("|", columnWidth, '|'))
        rowTwo.add(createButton("<", columnWidth, '<'))
        rowTwo.add(createButton(">", columnWidth, '>'))
        rowTwo.add(createButton("{", columnWidth, '{'))
        rowTwo.add(createButton("}", columnWidth, '}'))
        rowTwo.add(createButton("[", columnWidth, '['))
        rowTwo.add(createButton("]", columnWidth, ']'))

        val rowThree = ArrayList<View>()
        rowThree.add(createSpacer((columnWidth * 0.4f)))
        if (hasNextFocus) {
            rowThree.add(createButton(
                "PROX", (columnWidth * 1.2f), KeyboardController.SpecialKey.NEXT))
        } else {
            rowThree.add(createButton(
                "ENTER", (columnWidth * 1.2f), KeyboardController.SpecialKey.DONE))
        }

        val rowFour = ArrayList<View>()
        rowFour.add(createButton(
            "2/2", (columnWidth * 1.8f), KeyboardController.SpecialKey.SYMBOL))
        rowFour.add(createButton("", columnWidth * 4.8f, ' '))
        rowFour.add(createButton("APAGAR", columnWidth, KeyboardController.SpecialKey.BACKSPACE))

        val rows = ArrayList<LinearLayout>()
        rows.add(createNumbersRow())
        rows.add(createRow(rowTwo))
        rows.add(createRow(rowThree))
        rows.add(createRow(rowFour))

        return rows
    }

    private fun createNumbersRow(): LinearLayout {
        val row = ArrayList<View>()
        row.add(createButton("1", columnWidth, '1'))
        row.add(createButton("2", columnWidth, '2'))
        row.add(createButton("3", columnWidth, '3'))
        row.add(createButton("4", columnWidth, '4'))
        row.add(createButton("5", columnWidth, '5'))
        row.add(createButton("6", columnWidth, '6'))
        row.add(createButton("7", columnWidth, '7'))
        row.add(createButton("8", columnWidth, '8'))
        row.add(createButton("9", columnWidth, '9'))
        row.add(createButton("0", columnWidth, '0'))
        return createRow(row)
    }

    private fun createCapsButton(): Button {
        val alphaText = "ABC"
        return when(symbolsState) {
            SymbolState.SYMBOLS_DISABLED -> {
                when(capsState) {
                    CapsState.CAPS_DISABLED -> {
                        createButton("CAPS", columnWidth, KeyboardController.SpecialKey.CAPS)
                    }
                    CapsState.CAPS_ENABLED -> {
                        createButton("CAPS", columnWidth, KeyboardController.SpecialKey.CAPS)
                    }
                    CapsState.CAPS_LOCK_ENABLED -> {
                        val button = createButton(
                            "CAPS", columnWidth, KeyboardController.SpecialKey.CAPS)
                        ComponentUtils.setBackgroundTint(
                            button, Color.parseColor("#33CCFF"))
                        return button
                    }
                }
            }
            SymbolState.SYMBOL_ONE_DISPLAYED -> {
                createButton(alphaText, columnWidth, KeyboardController.SpecialKey.ALPHA)
            }
            SymbolState.SYMBOL_TWO_DISPLAYED -> {
                createButton(alphaText, columnWidth, KeyboardController.SpecialKey.ALPHA)
            }
        }
    }
}