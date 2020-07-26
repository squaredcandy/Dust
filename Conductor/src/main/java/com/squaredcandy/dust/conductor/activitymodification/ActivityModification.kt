package com.squaredcandy.dust.conductor.activitymodification

import android.app.Activity

interface ActivityModification {
    val onPush: Activity.() -> Unit
    val onPop: Activity.() -> Unit
}