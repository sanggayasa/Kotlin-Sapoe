package com.akarinti.sapoe.extension

import android.widget.EditText

fun EditText.isValidEmail(): Boolean {
    return this.text.isValidEmail()
}

fun EditText.isEmpty(): Boolean {
    return this.text.isNullOrEmpty()
}

fun EditText.isFilled(): Boolean {
    return !this.text.isNullOrEmpty()
}

fun EditText.isMinimum(num: Int): Boolean {
    return this.text.isMinimum(num)
}

fun EditText.isMaximum(num: Int): Boolean {
    return this.text.isMaximum(num)
}

fun EditText.allCharacterSame(): Boolean {
    return this.text.allCharacterSame()
}

fun EditText.matches(regex: Regex): Boolean {
    return this.text.matches(regex)
}