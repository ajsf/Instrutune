package tech.ajsf.instrutune.rules

import androidx.test.core.app.ApplicationProvider
import org.junit.rules.ExternalResource
import org.kodein.di.Kodein
import tech.ajsf.instrutune.InstrutuneApp

class OverridesRule(private val bindings: Kodein.MainBuilder.() -> Unit = {}) : ExternalResource() {

    private fun app(): InstrutuneApp = ApplicationProvider.getApplicationContext() as InstrutuneApp

    override fun before() {
        app().overrideBindings = bindings
    }

    override fun after() {
        app().overrideBindings = {}
    }
}