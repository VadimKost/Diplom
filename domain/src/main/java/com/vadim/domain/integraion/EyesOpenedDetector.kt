package com.vadim.domain.integraion

import com.vadim.domain.model.Picture

interface EyesOpenedDetector {
    suspend fun analyze(data: Picture):List<Pair<Float, Float>>
}