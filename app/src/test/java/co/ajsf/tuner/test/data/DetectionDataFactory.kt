package co.ajsf.tuner.test.data

import co.ajsf.tuner.tuner.frequencydetection.model.DetectionResult

internal object DetectionDataFactory {

    fun makeDetectionResult(): DetectionResult =
        DetectionResult(
            TestDataFactory.randomFloat(),
            TestDataFactory.randomBoolean(),
            TestDataFactory.randomBoolean(),
            TestDataFactory.randomFloat(),
            TestDataFactory.randomFloat()
        )
}