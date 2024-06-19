package com.vadim.integration.eyesdetector

import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.vadim.domain.integraion.EyesOpenedDetector
import com.vadim.domain.model.Picture
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class EyesOpenedML @Inject constructor() : EyesOpenedDetector {
    override suspend fun analyze(picture: Picture): List<Pair<Float, Float>> = suspendCoroutine {
        val options =
            FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build()
        val faceDetector: FaceDetector = FaceDetection.getClient(options)
        val image = InputImage.fromByteArray(
            picture.data,
            picture.with,
            picture.height,
            picture.rotation,
            InputImage.IMAGE_FORMAT_NV21 // or IMAGE_FORMAT_YV12
        )
        val start = System.currentTimeMillis()
        faceDetector.process(image)
            .addOnSuccessListener { faces: List<Face> ->
                val list = mutableListOf<Pair<Float,Float>>()
                for (face in faces) {
                    if (face.leftEyeOpenProbability != null && face.rightEyeOpenProbability != null) {
                        val leftEyeOpenProbability = face.leftEyeOpenProbability!!
                        val rightEyeOpenProbability = face.rightEyeOpenProbability!!
                        Log.e("CSS", "Time ${System.currentTimeMillis() - start}")
                        list.add(leftEyeOpenProbability to rightEyeOpenProbability)
                    }
                }
                it.resume(list)
            }
            .addOnFailureListener { e: Exception ->
                Log.e("FaceAnalysis", "Error detecting faces: " + e.message)
                it.resume(mutableListOf())
            }
    }
}