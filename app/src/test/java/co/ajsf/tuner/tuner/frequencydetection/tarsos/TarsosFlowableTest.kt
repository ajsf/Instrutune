package co.ajsf.tuner.tuner.frequencydetection.tarsos

import co.ajsf.tuner.common.tuner.frequencydetection.tarsos.TarsosDispatcher
import co.ajsf.tuner.common.tuner.frequencydetection.tarsos.TarsosFlowable
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.schedulers.TestScheduler
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class TarsosFlowableTest {

    @Mock
    lateinit var mockDispatcher: TarsosDispatcher

    private var scheduler: TestScheduler = TestScheduler()

    private lateinit var tarsosFlowable: TarsosFlowable

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        tarsosFlowable = TarsosFlowable(mockDispatcher, scheduler)
    }

    @Test
    fun `when the response stream is subscribed to, listen is called on the tarsosDispatcher`() {
        tarsosFlowable.getResponseStream().test()
        scheduler.triggerActions()
        verify(mockDispatcher).listen(any())
    }

    @Test
    fun `when the response stream is subscribed to, it stays subscribed`() {
        val testSubscriber = tarsosFlowable.getResponseStream().test()
        scheduler.triggerActions()
        testSubscriber.assertSubscribed()
        testSubscriber.assertNotComplete()
    }

    @Test
    fun `when the subscription is cancelled, stop is called on the tarsosDispatcher`() {
        val testSubscriber = tarsosFlowable.getResponseStream().test()
        scheduler.triggerActions()
        testSubscriber.cancel()
        scheduler.triggerActions()
        verify(mockDispatcher).stop()
    }
}