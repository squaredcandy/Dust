package com.squaredcandy.dust.conductor

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.asTransaction
import com.squaredcandy.dust.conductor.activitymodification.ActivityModification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

abstract class BaseController<VM : Any, C : Any>(args: Bundle? = null) : Controller(args) {

    private var viewScope: CoroutineScope? = null
    private var viewModel: VM? = null
    private var coupler: C? = null

    protected val mainActivity: RouterActivity?
        get() = activity as? RouterActivity

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ): View {
        applyModifications().forEach(::pushModification)
        val view = onCreateView(container)
        val context = view.context.applicationContext
        val currentViewModel: VM = viewModel ?: onCreateViewModel(context).also { viewModel = it }
        coupler = onCreateCoupler(view, currentViewModel)
        return view
    }

    final override fun onAttach(view: View) {
        val s = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        val vm = requireNotNull(viewModel)
        val c = requireNotNull(coupler)
        viewScope = s
        onAttach(view, vm, c, s)
    }

    final override fun onDetach(view: View) {
        val s = requireNotNull(viewScope)
        val vm = requireNotNull(viewModel)
        val c = requireNotNull(coupler)
        onDetach(view, vm, c, s)
        s.cancel("View Detached")
        viewScope = null
    }

    final override fun onDestroyView(view: View) {
        val c = requireNotNull(coupler)
        onDestroyView(view, c)
        coupler = null
    }

    final override fun onDestroy() {
        val vm = requireNotNull(viewModel)
        onDestroy(vm)
        viewModel = null
    }

    final override fun onPrepareOptionsMenu(menu: Menu) {
        val c = requireNotNull(coupler)
        val vm = requireNotNull(viewModel)
        onPrepareOptionsMenu(menu, c, vm)
    }

    final override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val c = requireNotNull(coupler)
        val vm = requireNotNull(viewModel)
        onCreateOptionsMenu(menu, inflater, c, vm)
    }

    final override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val c = requireNotNull(coupler)
        val vm = requireNotNull(viewModel)
        return onOptionsItemSelected(item, c, vm)
    }

    fun pushModification(modification: ActivityModification) {
        mainActivity?.pushModification(modification)
    }

    fun popModification(count: Int) {
        mainActivity?.popModification(count)
    }

    fun popAllModifications() {
        mainActivity?.popAllModifications()
    }

    abstract fun onCreateView(container: ViewGroup): View
    abstract fun onCreateViewModel(applicationContext: Context): VM
    abstract fun onCreateCoupler(view: View, viewModel: VM): C
    open fun applyModifications(): List<ActivityModification> = emptyList()
    open fun onAttach(view: View, viewModel: VM, coupler: C, viewScope: CoroutineScope) {}
    open fun onDetach(view: View, viewModel: VM, coupler: C, viewScope: CoroutineScope) {}
    open fun onDestroyView(view: View, coupler: C) {}
    open fun onDestroy(viewModel: VM) {}
    open fun onPrepareOptionsMenu(menu: Menu, coupler: C, viewModel: VM) {}
    open fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater, coupler: C, viewModel: VM) {}
    open fun onOptionsItemSelected(item: MenuItem, coupler: C, viewModel: VM): Boolean {
        return false
    }
}