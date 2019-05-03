package tech.ajsf.instrutune.common.data

import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import tech.ajsf.instrutune.common.data.db.InstrumentDao
import tech.ajsf.instrutune.common.data.db.InstrumentEntity
import tech.ajsf.instrutune.common.data.mapper.InstrumentMapper
import tech.ajsf.instrutune.common.model.Instrument
import tech.ajsf.instrutune.common.model.InstrumentCategory
import tech.ajsf.instrutune.test.data.InstrumentDataFactory
import tech.ajsf.instrutune.test.data.TestDataFactory.randomInt
import tech.ajsf.instrutune.test.data.TestDataFactory.randomString

internal class InstrumentRepositoryImplTest {

    @Mock
    private lateinit var mockPrefs: SharedPreferences

    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    @Mock
    private lateinit var mockDao: InstrumentDao

    @Mock
    private lateinit var mockMapper: InstrumentMapper

    private lateinit var scheduler: TestScheduler

    private lateinit var repository: InstrumentRepository

    private lateinit var randomCategory: String
    private lateinit var entities: List<InstrumentEntity>
    private lateinit var instruments: List<Instrument>
    private var id = 0

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        scheduler = TestScheduler()
        repository = InstrumentRepositoryImpl(mockPrefs, mockDao, mockMapper, scheduler)

        randomCategory = randomString()
        entities = InstrumentDataFactory.randomInstrumentEntityList()
        instruments = InstrumentDataFactory.randomInstrumentList()
        id = randomInt()
    }

    @Test
    fun `when getAllTunings is called, it calls getAllInstruments on the dao`() {
        stubGetAllInstruments()

        repository.getAllTunings()

        verify(mockDao).getAllInstruments()
    }

    @Test
    fun `when getAllTunings is subscribed to, it calls toInstrumentList on the mapper with the entities returned from the dao`() {
        stubGetAllInstruments()

        repository.getAllTunings().test()
        scheduler.triggerActions()

        verify(mockMapper).toInstrumentList(entities)
    }

    @Test
    fun `when getAllTunings is subscribed to, it sends the instruments returned by the mapper`() {
        stubGetAllInstruments()

        val testSubscriber = repository.getAllTunings().test()
        scheduler.triggerActions()

        testSubscriber.assertValue(instruments)
    }

    @Test
    fun `when getTuningsForCategory is called, it calls getInstrumentsForCategory on the dao`() {
        stubGetInstrumentsForCategory()
        repository.getTuningsForCategory(randomCategory)

        verify(mockDao).getInstrumentsForCategory(randomCategory)
    }

    @Test
    fun `when getTuningsForCategory is subscribed to, it calls toInstrumentList on the mapper with the entities returned from the dao`() {
        stubGetInstrumentsForCategory()

        repository.getTuningsForCategory(randomCategory).test()
        scheduler.triggerActions()

        verify(mockMapper).toInstrumentList(entities)
    }

    @Test
    fun `when getTuningsForCategory is subscribed to, it sends the instruments returned by the mapper`() {
        stubGetInstrumentsForCategory()

        val testSubscriber = repository.getTuningsForCategory(randomCategory).test()
        scheduler.triggerActions()

        testSubscriber.assertValue(instruments)
    }

    @Test
    fun `when getTuningById is called, it calls getInstrumentById on the dao`() {
        stubGetInstrumentById()

        repository.getTuningById(id)

        verify(mockDao).getInstrumentById(id)
    }

    @Test
    fun `when getTuningById is subscribed to, it calls toInstrument on the mapper`() {
        stubGetInstrumentById()

        repository.getTuningById(id).test()
        scheduler.triggerActions()

        verify(mockMapper).toInstrument(entities.first())
    }

    @Test
    fun `when getTuningById is subscribed to, it sends the instrument returned by the mapper`() {
        stubGetInstrumentById()

        val testSubscriber = repository.getTuningById(id).test()
        scheduler.triggerActions()

        testSubscriber.assertValue(instruments.first())
    }

    @Test
    fun `when saveTuning is called, it creates an InstrumentEntity and calls insert on the dao`() {
        val entity = stubInsert()

        repository.saveTuning(entity.tuningName, entity.numberedNotes, entity.id)

        verify(mockDao).insert(eq(entity))
    }

    @Test
    fun `when saveTuning is subscribed to, it returns the id returned by the dao`() {
        val entity = stubInsert()

        val testSubscriber = repository
            .saveTuning(entity.tuningName, entity.numberedNotes, entity.id)
            .test()
        scheduler.triggerActions()

        testSubscriber.assertValue(id)
    }

    @Test
    fun `when deleteTuning is called, it calls getInstrumentById on the dao`() {
        stubDelete()

        repository.deleteTuning(id)

        verify(mockDao).getInstrumentById(id)
    }

    @Test
    fun `when deleteTuning is subscribed to, it calls deleteInstrumentById on the dao if the category is custom`() {
        stubDelete()

        repository.deleteTuning(id).test()
        scheduler.triggerActions()

        verify(mockDao).deleteInstrumentById(id)
    }

    @Test
    fun `when deleteTuning is subscribed to, it completes if the category is custom`() {
        stubDelete()

        val testSubscriber = repository.deleteTuning(id).test()
        scheduler.triggerActions()

        testSubscriber.assertComplete()
    }

    @Test
    fun `when deleteTuning is subscribed to, it does not call deleteInstrumentById on the dao if the category is not custom`() {
        stubDelete(InstrumentCategory.Guitar)

        repository.deleteTuning(id).test()
        scheduler.triggerActions()

        verify(mockDao, never()).deleteInstrumentById(id)
    }

    @Test
    fun `when deleteTuning is subscribed to, it sends an error if the category is not custom`() {
        stubDelete(InstrumentCategory.Guitar)

        val testSubscriber = repository.deleteTuning(id).test()
        scheduler.triggerActions()

        testSubscriber.assertError(RuntimeException::class.java)
    }

    @Test
    fun `when getCategories is called, it calls getInstruments on the dao with the custom category`() {
        val custom = InstrumentCategory.Custom.toString()
        stubGetInstrumentsForCategory(custom)

        repository.getCategories()

        verify(mockDao).getInstrumentsForCategory(custom)
    }

    @Test
    fun `when getCategories is subscribed to, and the dao returns custom instruments, all categories are sent`() {
        val custom = InstrumentCategory.Custom.toString()
        stubGetInstrumentsForCategory(custom)

        val testSubscriber = repository.getCategories().test()
        scheduler.triggerActions()

        val expectedCategories = InstrumentCategory.values().map { it.toString() }
        testSubscriber.assertValue(expectedCategories)
    }

    @Test
    fun `when getCategories is subscribed to, and the dao returns no custom instruments, the custom category is not sent`() {
        val custom = InstrumentCategory.Custom.toString()
        whenever(mockDao.getInstrumentsForCategory(custom)).thenReturn(Single.just(emptyList()))

        val testSubscriber = repository.getCategories().test()
        scheduler.triggerActions()

        val expectedCategories =
            InstrumentCategory.values().map { it.toString() }.filter { it != custom }
        testSubscriber.assertValue(expectedCategories)
    }

    @Test
    fun `when getSelectedTuning is called, it calls getInt on SharedPreferences, with a default value of -1`() {
        stubSelectedTuning()

        repository.getSelectedTuning()

        verify(mockPrefs).getInt(any(), eq(-1))
    }

    @Test
    fun `when getSelectedTuning is called, and shared prefs returns a value greater than -1, it calls getInstrumentById on the dao`() {
        stubSelectedTuning()

        repository.getSelectedTuning()

        verify(mockDao).getInstrumentById(id)
    }

    @Test
    fun `when getSelectedTuning is called, and shared prefs returns -1, it calls getAllInstruments on the dao`() {
        stubSelectedTuning(-1)

        repository.getSelectedTuning()

        verify(mockDao).getAllInstruments()
    }

    @Test
    fun `when getSelectedTuning is subscribed to, and shared prefs returns a value greater than -1, it calls toInstrument on the mapper with the entity returned by the dao`() {
        stubSelectedTuning()

        repository.getSelectedTuning().test()
        scheduler.triggerActions()

        verify(mockMapper).toInstrument(entities.first())
    }

    @Test
    fun `when getSelectedTuning is subscribed to, and shared prefs returns a value greater than -1, it sends the instrument returned by the mapper`() {
        stubSelectedTuning()

        val testSubscriber = repository.getSelectedTuning().test()
        scheduler.triggerActions()

        testSubscriber.assertValue(instruments.first())
    }

    @Test
    fun `when getSelectedTuning is subscribed to, and shared prefs returns -1, it calls toInstrument on the mapper with the first entity returned by the dao`() {
        stubSelectedTuning(-1)

        repository.getSelectedTuning().test()
        scheduler.triggerActions()

        verify(mockMapper).toInstrument(entities.first())
    }

    @Test
    fun `when getSelectedTuning is subscribed to, and shared prefs returns -1, it sends the instrument returned by the mapper`() {
        stubSelectedTuning(-1)

        val testSubscriber = repository.getSelectedTuning().test()
        scheduler.triggerActions()

        testSubscriber.assertValue(instruments.first())
    }

    @Test
    fun `when savedSelectedTuning is called, it calls edit on prefs`() {
        whenever(mockPrefs.edit()).thenReturn(mockEditor)

        repository.saveSelectedTuning(id)

        verify(mockPrefs).edit()
    }

    @Test
    fun `when savedSelectedTuning is called, calls putInt on the Editor with the id`() {
        whenever(mockPrefs.edit()).thenReturn(mockEditor)

        repository.saveSelectedTuning(id)

        verify(mockEditor).putInt(any(), eq(id))
    }

    @Test
    fun `when getOffset is called, it returns the offset returned by prefs`() {
        val offset = randomInt()
        whenever(mockPrefs.getInt(any(), any())).thenReturn(offset)

        repository = InstrumentRepositoryImpl(mockPrefs, mockDao, mockMapper, scheduler)

        assertEquals(offset, repository.getOffset())
    }

    @Test
    fun `when saveOffset is called, it calls edit on prefs`() {
        whenever(mockPrefs.edit()).thenReturn(mockEditor)

        val offset = randomInt()
        repository.saveOffset(offset)

        verify(mockPrefs).edit()
    }

    @Test
    fun `when saveOffset is called, it calls putInt on the Editor with the offset`() {
        whenever(mockPrefs.edit()).thenReturn(mockEditor)

        val offset = randomInt()
        repository.saveOffset(offset)

        verify(mockEditor).putInt(any(), eq(offset))
    }

    private fun stubGetAllInstruments() {
        whenever(mockDao.getAllInstruments()).thenReturn(Single.just(entities))
        whenever(mockMapper.toInstrumentList(entities)).thenReturn(instruments)
    }

    private fun stubGetInstrumentsForCategory(category: String = randomCategory) {
        whenever(mockDao.getInstrumentsForCategory(category)).thenReturn(Single.just(entities))
        whenever(mockMapper.toInstrumentList(entities)).thenReturn(instruments)
    }

    private fun stubSelectedTuning(tuningId: Int = id) {
        whenever(mockPrefs.getInt(any(), any())).thenReturn(tuningId)
        if (tuningId > -1) {
            whenever(mockDao.getInstrumentById(tuningId)).thenReturn(Single.just(entities.first()))
        } else {
            whenever(mockDao.getAllInstruments()).thenReturn(Single.just(entities))
        }
        whenever(mockMapper.toInstrument(entities.first())).thenReturn(instruments.first())
    }

    private fun stubGetInstrumentById() {
        val entity = entities.first()
        val instrument = instruments.first()
        whenever(mockDao.getInstrumentById(id)).thenReturn(Single.just(entity))
        whenever(mockMapper.toInstrument(entity)).thenReturn(instrument)
    }

    private fun stubInsert(): InstrumentEntity {
        val entity = entities.first().copy(category = InstrumentCategory.Custom.toString())
        whenever(mockDao.insert(entity)).thenReturn(Single.just(id.toLong()))
        return entity
    }

    private fun stubDelete(category: InstrumentCategory = InstrumentCategory.Custom) {
        val entity = entities.first().copy(category = category.toString())
        whenever(mockDao.getInstrumentById(id)).thenReturn(Single.just(entity))
        whenever(mockDao.deleteInstrumentById(id)).thenReturn(Completable.complete())
    }
}