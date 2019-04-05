package tech.ajsf.instrutune.test.data

import tech.ajsf.instrutune.common.tuner.frequencydetection.model.DetectionResult

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