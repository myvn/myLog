package com.custom.plugin.mylog

import com.intellij.openapi.options.Configurable
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextArea
import com.intellij.ui.dsl.builder.*
import javax.swing.JComponent

class MyLogSettingsConfigurable : Configurable {

    private val jsTemplateField = JBTextArea()
    private val javaTemplateField = JBTextArea()

    override fun getDisplayName(): String = "MyLog"

    override fun createComponent(): JComponent {
        jsTemplateField.lineWrap = true
        jsTemplateField.wrapStyleWord = true
        javaTemplateField.lineWrap = true
        javaTemplateField.wrapStyleWord = true

        return panel {
            row("JS/TS 模板：") {
                cell(jsTemplateField)
                    .resizableColumn()
                    .comment("可用变量：\${file}, \${line}, \${var}")
            }
            row("Java 模板：") {
                cell(javaTemplateField)
                    .resizableColumn()
                    .comment("可用变量：\${class}, \${line}, \${var}")
            }
        }
    }

    override fun isModified(): Boolean {
        val settings = MyLogSettings.instance
        return jsTemplateField.text != settings.jsTemplate ||
                javaTemplateField.text != settings.javaTemplate
    }

    override fun apply() {
        val settings = MyLogSettings.instance
        settings.jsTemplate = jsTemplateField.text
        settings.javaTemplate = javaTemplateField.text
    }

    override fun reset() {
        val settings = MyLogSettings.instance
        jsTemplateField.text = settings.jsTemplate
        javaTemplateField.text = settings.javaTemplate
    }
}