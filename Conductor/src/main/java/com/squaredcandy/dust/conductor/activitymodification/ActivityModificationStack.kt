package com.squaredcandy.dust.conductor.activitymodification

import android.app.Activity
import java.util.*

/**
 * The [ActivityModificationStack] allows us to modify individual controllers
 */
class ActivityModificationStack {
    private val stack: Stack<ActivityModification> =
        Stack()
    fun push(activity: Activity, vararg modifications: ActivityModification) {
        modifications.forEach {
            stack.push(it).onPush(activity)
        }
    }

    fun pop(activity: Activity, count: Int) {
        repeat(count) {
            if (stack.isNotEmpty()) {
                stack.pop().onPop(activity)
            }
        }
    }

    fun popAll(activity: Activity) {
        while (stack.isNotEmpty()) {
            stack.pop().onPop(activity)
        }
    }
}