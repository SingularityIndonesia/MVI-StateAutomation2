package com.mvi_state_automation.example.util

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes

fun Context.getColorAttribute(
    @AttrRes colorId: Int
): Int {
    val typedValue = TypedValue()

    theme.apply {
        resolveAttribute(
            colorId,
            typedValue,
            true
        )
    }

    return typedValue.data
}