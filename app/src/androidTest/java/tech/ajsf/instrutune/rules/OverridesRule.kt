package tech.ajsf.instrutune.rules

import androidx.test.platform.app.InstrumentationRegistry
import tech.ajsf.instrutune.InstrutuneApp
import org.junit.rules.ExternalResource
import org.kodein.di.Kodein

class OverridesRule(private val bindings: Kodein.MainBuilder.() -> Unit = {}) : ExternalResource() {

    private fun app(): InstrutuneApp =
        InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as InstrutuneApp

    override fun before() {
        app().overrideBindings = bindings
    }

    override fun after() {
        app().overrideBindings = {}
    }
}