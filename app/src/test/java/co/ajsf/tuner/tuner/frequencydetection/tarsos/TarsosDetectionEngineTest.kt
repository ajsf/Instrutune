package co.ajsf.tuner.tuner.frequencydetection.tarsos

import co.ajsf.tuner.mapper.TarsosResponseToModelMapper
import co.ajsf.tuner.test.data.DetectionDataFactory
import co.ajsf.tuner.test.data.TestDataFactory
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxkotlin.toFlowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class TarsosDetectionEngineTest {

    @Mock
    lateinit var mockTarsosFlowable: TarsosResponseStream

    @Mock
    lateinit var mockMapper: TarsosResponseToModelMapper

    private val responseList = mutableListOf<TarsosResponse>()

    private lateinit var tarsosDetectionEngine: TarsosDetectionEngine

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        tarsosDetectionEngine = TarsosDetectionEngine(mockTarsosFlowable, mockMapper)
        val numberOfResponses = TestDataFactory.randomInt(10)
        repeat(numberOfResponses) {
            responseList.add(mock())
        }
    }

    @Test
    fun `it calls getResponseStream on the tarsosFlowable when listen is called`() {
        whenever(mockTarsosFlowable.getResponseStream())
            .thenReturn(responseList.toFlowable())

        tarsosDetectionEngine.listen()

        verify(mockTarsosFlowable).getResponseStream()
    }

    @Test
    fun `it calls the mapper for each response received from tarsosFlowable`() {
        whenever(mockTarsosFlowable.getResponseStream())
            .thenReturn(responseList.toFlowable())

        responseList.onEach {
            whenever(mockMapper.invoke(it)).thenReturn(DetectionDataFactory.makeDetectionResult())
        }

        tarsosDetectionEngine.listen().test()

        responseList.onEach { verify(mockMapper).invoke(it) }
    }

    @Test
    fun `it returns the DetectionResults from the mapper`() {
        whenever(mockTarsosFlowable.getResponseStream())
            .thenReturn(responseList.toFlowable())

        val results = responseList.map { DetectionDataFactory.makeDetectionResult() }

        responseList.forEachIndexed { index, pair ->
            responseList.onEach {
                whenever(mockMapper.invoke(pair)).thenReturn(results[index])
            }
        }

        val testSubscriber = tarsosDetectionEngine.listen().test()

        testSubscriber.assertValueSequence(results)
    }
}