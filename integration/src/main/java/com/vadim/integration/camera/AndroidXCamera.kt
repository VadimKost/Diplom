package com.vadim.integration.camera

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.vadim.domain.integraion.Camera
import com.vadim.domain.model.Picture
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.delay
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

typealias LumaListener = (luma: Double) -> Unit

@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
class AndroidXCamera @Inject constructor(
    @ActivityContext val activityContext: Context,
) : Camera {
    private lateinit var cameraExecutor: ExecutorService
    val activity = activityContext as ComponentActivity

    var imageProxy: ImageProxy? = null

    init {
        startCamera()
    }

    private fun startCamera() {
        cameraExecutor = Executors.newSingleThreadExecutor()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) {
                        imageProxy = it
                    }
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    activity, cameraSelector, imageAnalyzer
                )

            } catch (exc: Exception) {
                Log.e("CSS", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(activity))
    }

    override suspend fun getPicture(): Picture {
        imageProxy?.close()
        imageProxy = null

        var image = imageProxy
        while (image == null) {
            delay(30)
            image = imageProxy
        }
        val picture =
            Picture(image.toByteArray(), image.height, image.width, image.imageInfo.rotationDegrees)
        return picture


    }
}

fun ImageProxy.toByteArray(): ByteArray {
    val planes = this.planes
    val yBuffer = planes[0].buffer // Y
    val uBuffer = planes[1].buffer // U
    val vBuffer = planes[2].buffer // V

    val ySize = yBuffer.remaining()
    val uSize = uBuffer.remaining()
    val vSize = vBuffer.remaining()

    val nv21 = ByteArray(ySize + uSize + vSize)

    // Copy Y plane
    yBuffer[nv21, 0, ySize]

    // Interleave U and V planes
    val uBytes = ByteArray(uSize)
    val vBytes = ByteArray(vSize)
    uBuffer[uBytes]
    vBuffer[vBytes]

    for (i in 0 until uSize) {
        nv21[ySize + (i * 2)] = vBytes[i]
        nv21[ySize + (i * 2) + 1] = uBytes[i]
    }

    return nv21
}