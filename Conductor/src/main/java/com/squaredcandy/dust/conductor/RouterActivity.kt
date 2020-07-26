package com.squaredcandy.dust.conductor

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bluelinelabs.conductor.*
import com.squaredcandy.dust.conductor.activitymodification.ActivityModification
import com.squaredcandy.dust.conductor.activitymodification.ActivityModificationStack

/**
 * Abstract class for handling boilerplate code in [Conductor]. Supports [ActivityModification] and
 * back presses
 */
abstract class RouterActivity : AppCompatActivity(), ControllerChangeHandler.ControllerChangeListener {

    protected val modificationStack = ActivityModificationStack()
    protected lateinit var router: Router
    abstract val controller: Controller

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootContainer: ViewGroup = ChangeHandlerFrameLayout(this)
        setContentView(rootContainer)
        router = Conductor.attachRouter(this, rootContainer, savedInstanceState).apply {
            addChangeListener(this@RouterActivity)
        }
        if (!router.hasRootController()) {
            router.setRoot(controller.asTransaction())
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            finishAfterTransition()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onChangeStarted(
        to: Controller?,
        from: Controller?,
        isPush: Boolean,
        container: ViewGroup,
        handler: ControllerChangeHandler
    ) {
        modificationStack.popAll(this)
    }

    fun pushModification(modification: ActivityModification) {
        modificationStack.push(this, modification)
    }

    fun popModification(count: Int) {
        modificationStack.pop(this, count)
    }

    fun popAllModifications() {
        modificationStack.popAll(this)
    }
}