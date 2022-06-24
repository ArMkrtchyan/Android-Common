package com.github.armkrtchyan.common.shared.extensions

import android.util.Patterns
import java.util.regex.Pattern

fun String.isValidEmail(newRegex: String? = null, withRegex: String? = null): Boolean {
    newRegex?.let {
        withRegex?.let {
            return Pattern.compile(newRegex).matcher(this).matches() && Pattern.compile(withRegex).matcher(this).matches()
        }
        return Pattern.compile(newRegex).matcher(this).matches()
    }
    return Patterns.EMAIL_ADDRESS.pattern().toPattern().matcher(this).matches()
}

fun String.isValidPassword(newRegex: String? = null, withRegex: String? = null): Boolean {
    newRegex?.let {
        withRegex?.let {
            return Pattern.compile(newRegex).matcher(this).matches() && Pattern.compile(withRegex).matcher(this).matches()
        }
        return Pattern.compile(newRegex).matcher(this).matches()
    }
    return Pattern.compile("(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9].*[0-9])(?=.*[^a-zA-Z0-9]).{6,}").matcher(this).matches()
}

fun String.isValidRegex(withRegex: String): Boolean {
    return Pattern.compile(withRegex).matcher(this).matches()
}