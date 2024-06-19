package com.vadim.domain.integraion

import com.vadim.domain.model.SafetyComfortState

interface PeripheralConnector {
    fun onReceiveNewState(state: SafetyComfortState)
}