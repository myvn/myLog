package com.custom.plugin.mylog

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "MyLogSettings",
    storages = [Storage("mylog-settings.xml")]
)
class MyLogSettings : PersistentStateComponent<MyLogSettings> {

    var jsTemplate: String = "console.log(\"[MyLog][\${file}:\${line}] \${var} = \", \${var});"
    var javaTemplate: String = "log.info(\"[MyLog][\${class}:\${line}] \${var} = {}\", \${var});"
    var kotlinTemplate: String = "log.info(\"[MyLog][\${class}:\${line}] \${var} = \$\${var}\")"
    var pythonTemplate: String = "print(f\"[MyLog][\${file}:\${line}] \${var} = {\${var}}\")"

    override fun getState(): MyLogSettings = this

    override fun loadState(state: MyLogSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        val instance: MyLogSettings
            get() = ApplicationManager.getApplication().getService(MyLogSettings::class.java)
    }
}