package epicarchitect.breakbadhabits.newarch

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlin.time.Duration

class DefaultUpdatingValue<T>(
    updateDelay: Duration,
    getValue: () -> T
) : UpdatingValue<T> {
    private val state = flow {
        while (currentCoroutineContext().isActive) {
            delay(updateDelay)
            emit(getValue())
        }
    }.stateIn(CoroutineScope(Dispatchers.Default), SharingStarted.WhileSubscribed(), getValue())

    override fun state() = state
}