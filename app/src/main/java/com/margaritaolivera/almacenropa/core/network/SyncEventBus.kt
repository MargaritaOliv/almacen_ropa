package com.margaritaolivera.almacenropa.core.network

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncEventBus @Inject constructor() {
    private val _syncEvents = MutableSharedFlow<Unit>()
    val syncEvents = _syncEvents.asSharedFlow()

    suspend fun emitSyncFinished() {
        _syncEvents.emit(Unit)
    }
}
