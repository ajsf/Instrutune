package co.ajsf.tuner.rules

import androidx.test.platform.app.InstrumentationRegistry
import co.ajsf.tuner.TunerApp
import org.junit.rules.ExternalResource
import org.kodein.di.Kodein

class OverridesRule(private val bindings: Kodein.MainBuilder.() -> Unit = {}) : ExternalResource() {

    private fun app(): TunerApp =
        InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as TunerApp

    override fun before() {
        app().overrideBindings = bindings
    }

    override fun after() {
        app().overrideBindings = {}
    }
}