package com.vadim.domain.model

data class SafetyComfortState(
    val internalState: InternalState = InternalState(),
    val externalState: ExternalState = ExternalState()
)

data class InternalState(
    val input: InternalStateInput = InternalStateInput(),
    val output: InternalStateOutput = InternalStateOutput(),
)

data class InternalStateInput(
    val timeEyesLastOpen: Long = 0,
    val eyesOpenedCoefficient: Pair<Float, Float> = 0f to 0f,
    val coordinate: Pair<Double, Double> = 0.0 to 0.0,
    val extra: String = ""
)

data class InternalStateOutput(
    val isEyesOpen: Boolean = false,
    val emergency:Boolean = false,
)

data class ExternalState(
    val input: ExternalStateInput = ExternalStateInput(),
    val output: ExternalStateOutput = ExternalStateOutput(),
)

data class ExternalStateInput(
    val temperatureInZone1: Float = 0f,
    val temperatureInZone2: Float = 0f,
    val temperatureInZone3: Float = 0f,
    val temperatureInZone4: Float = 0f,
    val extra: String = ""
)
data class ExternalStateOutput(
    val extra: String = ""
)