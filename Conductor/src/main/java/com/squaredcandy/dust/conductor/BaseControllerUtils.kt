package com.squaredcandy.dust.conductor

import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.asTransaction

/**
 * Convenience method for creating a [RouterTransaction] from a [BaseController].
 */
fun BaseController<*, *>.asTransaction(destination: BaseDestination? = null): RouterTransaction =
    this.asTransaction(destination?.popChangeHandler, destination?.pushChangeHandler)

/**
 * Tried to hide the keyboard
 * Returns true if tried to hide the keyboard
 * Note: this guarantee that the keyboard will hide
 */
fun BaseController<*, *>.hideKeyboard(): Boolean {
    activity?.let { activity ->
        val token = activity.currentFocus?.windowToken ?: return@let
        val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(token, 0)
        return true
    }
    return false
}