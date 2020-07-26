package com.squaredcandy.dust.conductor

import com.bluelinelabs.conductor.changehandler.AnimatorChangeHandler
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler

/**
 * Base class for adding [HorizontalChangeHandler] to all controller changes
 */
abstract class BaseDestination(
    val pushChangeHandler: AnimatorChangeHandler? = HorizontalChangeHandler(),
    val popChangeHandler: AnimatorChangeHandler? = HorizontalChangeHandler()
)