package com.izabela.tecladosensivel

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.izabela.tecladosensivel.components.keyboard.CustomKeyboardView
import com.izabela.tecladosensivel.components.textFields.CustomTextField
import com.izabela.tecladosensivel.components.utilities.ComponentUtils
import java.lang.Exception
import java.util.*


class AdvancedFeaturesActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var keyboard: CustomKeyboardView

    public var tts: TextToSpeech? = null

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val localeBR = Locale("pt", "BR")

            val result: Int
            result = this.tts!!.setLanguage(localeBR)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "This Language is not supported", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Initilization Failed!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advanced_features)

        tts = TextToSpeech(this, this)

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
        keyboard.autoRegisterEditTexts(customFieldWrapper)

//        keyboard.setOnClickListener{
//            tts!!.speak("EU FIZ FUNFAR", TextToSpeech.QUEUE_ADD, null)
//        }

//        keyboard.keyboardListener.characterClicked(c) {
//            tts!!.speak(c.toString(), TextToSpeech.QUEUE_ADD, null)
//        }

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

    public fun speak(c: Char) {
        tts!!.speak(c.toString(), TextToSpeech.QUEUE_ADD, null)
    }

    private fun createCustomTextField(
        hint: String, keyboardType: CustomKeyboardView.KeyboardType
    ): CustomTextField {
        val field = CustomTextField(applicationContext)
        val lp = LinearLayout.LayoutParams(
            ComponentUtils.dpToPx(applicationContext, 400),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val marginBottom =
            applicationContext.resources.getDimension(R.dimen.fieldMarginBottom).toInt()
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


    override fun onDestroy() {
        // Shut down TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }

        super.onDestroy()
    }
}