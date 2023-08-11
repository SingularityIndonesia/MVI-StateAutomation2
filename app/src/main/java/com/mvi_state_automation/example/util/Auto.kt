package com.mvi_state_automation.example.util

import androidx.lifecycle.ViewModel
import com.singularity_code.codebase.pattern.LazyFunction
import com.singularity_code.codebase.util.flow.collect
import com.singularity_code.codebase.util.flow.lazyFunction
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.getScopeName

fun <R> ViewModel.auto(block: () -> R): Lazy<R> = lazy { block.invoke() }

fun ViewModel.updateOn(
    vararg flows: Pair<Flow<*>, Boolean>,
    block: suspend () -> Unit
): LazyFunction {
    val updater = lazyFunction { block.invoke() }

    flows.forEach { flow ->
        collect(
            flow.first,
        ) { _ ->
            if (flow.second)
                updater.forceInvoke(
                    "${flow.getScopeName()} ${System.currentTimeMillis()}"
                )
            else
                updater.tryInvoke(
                    "${flow.getScopeName()} ${System.currentTimeMillis()}"
                )
        }
    }

    return updater
}