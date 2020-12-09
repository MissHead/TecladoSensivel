package com.izabela.tecladosensivel

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.speech.tts.TextToSpeech
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.izabela.tecladosensivel.components.keyboard.CustomKeyboardView
import com.izabela.tecladosensivel.components.keyboard.KeyboardListener
import java.util.*


class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var keyboard: CustomKeyboardView
    var tts: TextToSpeech? = null
    var vibrator: Vibrator? = null

    override fun onInit(status: Int) {
        setupTTS(status)
    }

    private fun setupTTS(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val localeBR = Locale("pt", "BR")

            val result: Int
            result = this.tts!!.setLanguage(localeBR)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "This Language is not supported", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Initialization Failed!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val numberField: EditText = findViewById(R.id.testNumberField)
        val numberDecimalField: EditText = findViewById(R.id.testNumberDecimalField)
        val qwertyField: EditText = findViewById(R.id.testQwertyField)

        tts = TextToSpeech(this, this)
        vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        val listener = object : KeyboardListener {
            override fun characterClicked(c: Char) {
                tts!!.speak(c.toString(), TextToSpeech.QUEUE_ADD, null)
            }
        }

        keyboard = findViewById(R.id.customKeyboardView)
        keyboard.registerEditText(CustomKeyboardView.KeyboardType.NUMBER, numberField, listener)
        keyboard.registerEditText(CustomKeyboardView.KeyboardType.NUMBER_DECIMAL, numberDecimalField, listener)
        keyboard.registerEditText(CustomKeyboardView.KeyboardType.QWERTY, qwertyField, listener)

        keyboard.setOnClickListener {
            this@MainActivity.vibrate(this@MainActivity.vibrator!!, 300)
        }

        val switchActivitiesButton: Button = findViewById(R.id.switchActivitiesButton)
        switchActivitiesButton.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    AdvancedFeaturesActivity::class.java
                )
            )
        }
    }

    private fun vibrate(vibrator: Vibrator, time: Long = 150) {
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(time, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(time)
        }
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