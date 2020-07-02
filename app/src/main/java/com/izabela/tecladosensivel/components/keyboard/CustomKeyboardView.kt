package com.izabela.tecladosensivel.components.keyboard

import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import com.izabela.tecladosensivel.components.expandableView.ExpandableState
import com.izabela.tecladosensivel.components.expandableView.ExpandableStateListener
import com.izabela.tecladosensivel.components.expandableView.ExpandableView
import com.izabela.tecladosensivel.components.keyboard.controllers.DefaultKeyboardController
import com.izabela.tecladosensivel.components.keyboard.controllers.KeyboardController
import com.izabela.tecladosensivel.components.keyboard.controllers.NumberDecimalKeyboardController
import com.izabela.tecladosensivel.components.keyboard.layouts.KeyboardLayout
import com.izabela.tecladosensivel.components.keyboard.layouts.NumberDecimalKeyboardLayout
import com.izabela.tecladosensivel.components.keyboard.layouts.NumberKeyboardLayout
import com.izabela.tecladosensivel.components.keyboard.layouts.QwertyKeyboardLayout
import com.izabela.tecladosensivel.components.textFields.CustomTextField
import com.izabela.tecladosensivel.components.utilities.ComponentUtils
import java.util.*

class CustomKeyboardView(context: Context, attr: AttributeSet) : ExpandableView(context, attr) {
    private var fieldInFocus: EditText? = null
    private val keyboards = HashMap<EditText, KeyboardLayout?>()
    private val keyboardListener: KeyboardListener

    init {
        setBackgroundColor(Color.WHITE)

        keyboardListener = object: KeyboardListener {
            override fun characterClicked(c: Char) {
            }

            override fun specialKeyClicked(key: KeyboardController.SpecialKey) {
                if (key === KeyboardController.SpecialKey.DONE) {
                    translateLayout()
                } else if (key === KeyboardController.SpecialKey.NEXT) {
                    fieldInFocus?.focusSearch(View.FOCUS_DOWN)?.let {
                        it.requestFocus()
                        checkLocationOnScreen()
                        return
                    }
                }
            }
        }

        registerListener(object: ExpandableStateListener {
            override fun onStateChange(state: ExpandableState) {
                if (state === ExpandableState.EXPANDED) {
                    checkLocationOnScreen()
                }
            }
        })

        setOnClickListener({})
        isSoundEffectsEnabled = false
    }

    fun registerEditText(type: KeyboardType, field: EditText) {
        if (!field.isEnabled) {
            return  // campo inativo nao tem input
        }

        field.setRawInputType(InputType.TYPE_CLASS_TEXT)
        field.setTextIsSelectable(true)
        field.showSoftInputOnFocus = false
        field.isSoundEffectsEnabled = false
        field.isLongClickable = false

        val inputConnection = field.onCreateInputConnection(EditorInfo())
        keyboards[field] = createKeyboardLayout(type, inputConnection)
        keyboards[field]?.registerListener(keyboardListener)

        field.onFocusChangeListener = OnFocusChangeListener { _: View, hasFocus: Boolean ->
            if (hasFocus) {
                ComponentUtils.hideSystemKeyboard(context, field)

                field.focusSearch(View.FOCUS_DOWN)?.run {
                    if (this is EditText) keyboards[field]?.hasNextFocus = true
                }
                fieldInFocus = field

                renderKeyboard()
                if (!isExpanded) {
                    translateLayout()
                }
            } else if (!hasFocus && isExpanded) {
                for (editText in keyboards.keys) {
                    if (editText.hasFocus()) {
                        return@OnFocusChangeListener
                    }
                }
                translateLayout()
            }
        }

        field.setOnClickListener {
            if (!isExpanded) {
                translateLayout()
            }
        }
    }

    fun autoRegisterEditTexts(rootView: ViewGroup) {
        registerEditTextsRecursive(rootView)
    }

    private fun registerEditTextsRecursive(view: View) {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                registerEditTextsRecursive(view.getChildAt(i))
            }
        } else {
            if (view is CustomTextField) {
                registerEditText(view.keyboardType, view)
            } else if (view is EditText) {
                when (view.inputType) {
                    InputType.TYPE_CLASS_NUMBER -> {
                        registerEditText(CustomKeyboardView.KeyboardType.NUMBER, view)
                    }
                    InputType.TYPE_NUMBER_FLAG_DECIMAL -> {
                        registerEditText(CustomKeyboardView.KeyboardType.NUMBER_DECIMAL, view)
                    }
                    else -> {
                        registerEditText(CustomKeyboardView.KeyboardType.QWERTY, view)
                    }
                }
            }
        }
    }

    fun unregisterEditText(field: EditText?) {
        keyboards.remove(field)
    }

    fun clearEditTextCache() {
        keyboards.clear()
    }

    private fun renderKeyboard() {
        removeAllViews()
        val keyboard: KeyboardLayout? = keyboards[fieldInFocus]
        keyboard?.let {
            it.orientation = LinearLayout.VERTICAL
            it.createKeyboard(measuredWidth.toFloat())
            addView(keyboard)
        }
    }

    private fun createKeyboardLayout(type: KeyboardType, ic: InputConnection): KeyboardLayout? {
        when(type) {
            KeyboardType.NUMBER -> {
                return NumberKeyboardLayout(context, createKeyboardController(type, ic))
            }
            KeyboardType.NUMBER_DECIMAL -> {
                return NumberDecimalKeyboardLayout(context, createKeyboardController(type, ic))
            }
            KeyboardType.QWERTY -> {
                return QwertyKeyboardLayout(context, createKeyboardController(type, ic))
            }
            else -> return@createKeyboardLayout null // a ideia é que nunca ocorra
        }
    }

    private fun createKeyboardController(type: KeyboardType, ic: InputConnection): KeyboardController? {
        return when(type) {
            KeyboardType.NUMBER_DECIMAL -> {
                NumberDecimalKeyboardController(ic)
            }
            else -> {
                DefaultKeyboardController(ic)
            }
        }
    }

    override fun configureSelf() {
        renderKeyboard()
        checkLocationOnScreen()
    }

    private fun checkLocationOnScreen() {
        fieldInFocus?.run {
            var fieldParent = this.parent
            while (fieldParent !== null) {
                if (fieldParent is ScrollView) {
                    if (!fieldParent.isSmoothScrollingEnabled) {
                        break
                    }

                    val fieldLocation = IntArray(2)
                    this.getLocationOnScreen(fieldLocation)

                    val keyboardLocation = IntArray(2)
                    this@CustomKeyboardView.getLocationOnScreen(keyboardLocation)

                    val fieldY = fieldLocation[1]
                    val keyboardY = keyboardLocation[1]

                    if (fieldY > keyboardY) {
                        val deltaY = (fieldY - keyboardY)
                        val scrollTo = (fieldParent.scrollY + deltaY + this.measuredHeight + 10.toDp)
                        fieldParent.smoothScrollTo(0, scrollTo)
                    }
                    break
                }
                fieldParent = fieldParent.parent
            }
        }
    }

    enum class KeyboardType {
        NUMBER,
        NUMBER_DECIMAL,
        QWERTY
    }
}