package com.buddy.mykeyboard

import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.view.View
import android.widget.TextView

class MyKeyboardService : InputMethodService(), KeyboardView.OnKeyboardActionListener {

    private lateinit var keyboardView: KeyboardView
    private lateinit var keyboard: Keyboard
    private lateinit var suggestionBar: TextView
    private var capsLock = false

    override fun onCreateInputView(): View {
        val layout = layoutInflater.inflate(R.layout.keyboard_view, null)
        keyboardView = layout.findViewById(R.id.keyboard_view)
        suggestionBar = layout.findViewById(R.id.suggestion_bar)
        keyboard = Keyboard(this, R.xml.qwerty)
        keyboardView.keyboard = keyboard
        keyboardView.setOnKeyboardActionListener(this)
        return layout
    }

    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
        val ic = currentInputConnection ?: return
        when (primaryCode) {
            -5 -> ic.deleteSurroundingText(1, 0) // backspace
            -4 -> ic.commitText("\n", 1) // enter
            -1 -> { // shift
                capsLock = !capsLock
                keyboard.isShifted = capsLock
                keyboardView.invalidateAllKeys()
            }
            32 -> ic.commitText(" ", 1) // space
            else -> {
                var code = primaryCode.toChar()
                if (capsLock) code = code.uppercaseChar()
                ic.commitText(code.toString(), 1)
            }
        }
    }

    override fun onPress(primaryCode: Int) {}
    override fun onRelease(primaryCode: Int) {}
    override fun onText(text: CharSequence?) {}
    override fun swipeLeft() {}
    override fun swipeRight() {}
    override fun swipeDown() {}
    override fun swipeUp() {}
}
