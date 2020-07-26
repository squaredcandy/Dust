package com.squaredcandy.dust.conductor.activitymodification

import android.app.Activity

class SoftInputModeModification(private val softInput: Int) : ActivityModification {
    var lastSoftInputMode: Int? = null
    override val onPush: Activity.() -> Unit = {
        lastSoftInputMode = window.attributes.softInputMode
        window.setSoftInputMode(softInput)
    }
    override val onPop: Activity.() -> Unit = {
        lastSoftInputMode?.let(window::setSoftInputMode)
    }
}