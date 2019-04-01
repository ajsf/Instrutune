package co.ajsf.tuner.test.data

import co.ajsf.tuner.common.tuner.frequencydetection.model.DetectionResult

object DetectionDataFactory {

    fun makeDetectionResult(): DetectionResult =
        DetectionResult(
            TestDataFactory.randomFloat(),
            TestDataFactory.randomBoolean(),
            TestDataFactory.randomBoolean(),
            TestDataFactory.randomFloat(),
            TestDataFactory.randomFloat()
        )
}