package com.vadim.domain.integraion

import com.vadim.domain.model.Picture

interface Camera {
    suspend fun getPicture(): Picture

}