package com.custom.plugin.mylog

import com.intellij.openapi.options.Configurable
import com.intellij.ui.components.JBTextArea
import com.intellij.ui.dsl.builder.*
import java.awt.Dimension
import javax.swing.JComponent

class MyLogSettingsConfigurable : Configurable {

    private val jsTemplateField = JBTextArea(5, 40)
    private val javaTemplateField = JBTextArea(5, 40)
    private val kotlinTemplateField = JBTextArea(5, 40)
    private val pythonTemplateField = JBTextArea(5, 40)

    override fun getDisplayName(): String = "MyLog"

    override fun createComponent(): JComponent {
        jsTemplateField.lineWrap = true
        jsTemplateField.wrapStyleWord = true
        javaTemplateField.lineWrap = true
        javaTemplateField.wrapStyleWord = true
        kotlinTemplateField.lineWrap = true
        kotlinTemplateField.wrapStyleWord = true
        pythonTemplateField.lineWrap = true
        pythonTemplateField.wrapStyleWord = true

        return panel {
            row {
                label("JS/TS 模板：")
                cell(jsTemplateField)
                    .align(AlignX.FILL)
                    .resizableColumn()
                    .comment("可用变量：\${file}, \${line}, \${var}")
            }
            row {
                label("Java 模板：")
                cell(javaTemplateField)
                    .align(AlignX.FILL)
                    .resizableColumn()
                    .comment("可用变量：\${class}, \${line}, \${var}")
            }
            row {
                label("Kotlin 模板：")
                cell(kotlinTemplateField)
                    .align(AlignX.FILL)
                    .resizableColumn()
                    .comment("可用变量：\${class}, \${line}, \${var}")
            }
            row {
                label("Python 模板：")
                cell(pythonTemplateField)
                    .align(AlignX.FILL)
                    .resizableColumn()
                    .comment("可用变量：\${file}, \${line}, \${var}")
            }
        }
    }

    override fun isModified(): Boolean {
        val settings = MyLogSettings.instance
        return jsTemplateField.text != settings.jsTemplate ||
                javaTemplateField.text != settings.javaTemplate ||
                kotlinTemplateField.text != settings.kotlinTemplate ||
                pythonTemplateField.text != settings.pythonTemplate
    }

    override fun apply() {
        val settings = MyLogSettings.instance
        settings.jsTemplate = jsTemplateField.text
        settings.javaTemplate = javaTemplateField.text
        settings.kotlinTemplate = kotlinTemplateField.text
        settings.pythonTemplate = pythonTemplateField.text
    }

    override fun reset() {
        val settings = MyLogSettings.instance
        jsTemplateField.text = settings.jsTemplate
        javaTemplateField.text = settings.javaTemplate
        kotlinTemplateField.text = settings.kotlinTemplate
        pythonTemplateField.text = settings.pythonTemplate
    }
}