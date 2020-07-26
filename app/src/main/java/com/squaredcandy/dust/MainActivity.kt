package com.squaredcandy.dust

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.squaredcandy.dust.conductor.RouterActivity

class MainController : Controller() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ): View {
        return inflater.inflate(R.layout.activity_main, container, false)
    }
}

class MainActivity : RouterActivity() {
    override val controller: Controller
        get() = MainController()
}
