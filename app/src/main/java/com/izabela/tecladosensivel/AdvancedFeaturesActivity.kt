package com.izabela.tecladosensivel

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.izabela.tecladosensivel.components.keyboard.CustomKeyboardView
import com.izabela.tecladosensivel.components.keyboard.controllers.KeyboardController
import com.izabela.tecladosensivel.components.textFields.CustomTextField
import com.izabela.tecladosensivel.components.utilities.ComponentUtils
import java.util.*


class AdvancedFeaturesActivity: AppCompatActivity() {
    private lateinit var keyboard: CustomKeyboardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advanced_features)

        val customFieldWrapper: LinearLayout = findViewById(R.id.customFieldWrapper)
        customFieldWrapper.addView(
            createCustomTextField(
                "Teclado Numerico",
                CustomKeyboardView.KeyboardType.NUMBER
            )
        )
        customFieldWrapper.addView(
            createCustomTextField(
                "Teclado Numerico Decimal",
                CustomKeyboardView.KeyboardType.NUMBER_DECIMAL
            )
        )
        customFieldWrapper.addView(
            createCustomTextField(
                "Teclado QWERTY",
                CustomKeyboardView.KeyboardType.QWERTY
            )
        )

        keyboard = findViewById(R.id.customKeyboardView)
//        keyboard.autoRegisterEditTexts(customFieldWrapper)

        val switchActivitiesButton: Button = findViewById(R.id.switchActivitiesButton)
        switchActivitiesButton.setOnClickListener {
            startActivity(
                Intent(
                    this@AdvancedFeaturesActivity,
                    MainActivity::class.java
                )
            )
        }
    }

    private fun createCustomTextField(
        hint: String, keyboardType: CustomKeyboardView.KeyboardType
    ): CustomTextField {
        val field = CustomTextField(applicationContext)
        val lp = LinearLayout.LayoutParams(
            ComponentUtils.dpToPx(applicationContext, 400),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val marginBottom = applicationContext.resources.
        getDimension(R.dimen.fieldMarginBottom).toInt()
        lp.setMargins(0, 0, 0, marginBottom)
        field.layoutParams = lp

        field.hint = hint
        field.keyboardType = keyboardType

        return field
    }

    override fun onBackPressed() {
        if (keyboard.isExpanded) {
            keyboard.translateLayout()
        } else {
            super.onBackPressed()
        }
    }
}