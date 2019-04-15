package tech.ajsf.instrutune.common.tuner.frequencydetection.tarsos

import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.pitch.PitchProcessor
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class TarsosDispatcherImplTest {

    @Mock
    lateinit var mockDispatcher: AudioDispatcher

    @Mock
    lateinit var mockPitchProcessor: PitchProcessor

    @Mock
    lateinit var mockListener: TarsosListener

    private lateinit var tarsosDispatcherImpl: TarsosDispatcherImpl

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        tarsosDispatcherImpl =
            TarsosDispatcherImpl({ mockDispatcher },
                { mockPitchProcessor })
    }

    @Test
    fun `calling listen adds the pitchProcessor to the dispatcher and calls run on the dispatcher`() {
        tarsosDispatcherImpl.listen(mockListener)
        verify(mockDispatcher).addAudioProcessor(mockPitchProcessor)
        verify(mockDispatcher).run()
    }

    @Test
    fun `calling stop calls stop on the dispatcher`() {
        tarsosDispatcherImpl.listen(mockListener)
        tarsosDispatcherImpl.stop()
        verify(mockDispatcher).stop()
    }

    @Test
    fun `calling stop before calling listen throws a NotListeningException`() {
        Assertions.assertThrows(NotListeningException::class.java) { tarsosDispatcherImpl.stop() }
    }

    @Test
    fun `calling listen twice throws a CurrentlyListeningException`() {
        tarsosDispatcherImpl.listen(mockListener)
        Assertions.assertThrows(CurrentlyListeningException::class.java) {
            tarsosDispatcherImpl.listen(
                mockListener
            )
        }
    }

    @Test
    fun `calling listen, followed by stop, followed by listen works`() {
        tarsosDispatcherImpl.listen(mockListener)
        tarsosDispatcherImpl.stop()
        tarsosDispatcherImpl.listen(mockListener)
        verify(mockDispatcher, times(2)).run()
        verify(mockDispatcher, times(1)).stop()
    }
}