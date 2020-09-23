package com.sample.myapplication.utils

import android.content.Context
import android.os.Build
import android.widget.Toast


val Any.TAG: String
    get() {
        return if (!javaClass.isAnonymousClass) {
            val name = javaClass.simpleName
            if (name.length <= 23 || Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) name else
                name.substring(0, 23)// first 23 chars
        } else {
            val name = javaClass.name
            if (name.length <= 23 || Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                name else name.substring(name.length - 23, name.length)// last 23 chars
        }
    }

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
