package com.custom.plugin.mylog

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.util.TextRange

class MyLogAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val project = e.project ?: return

        val selectionModel = editor.selectionModel
        val selectedText = selectionModel.selectedText ?: return

        val document = editor.document
        val selectionEnd = selectionModel.selectionEnd

        val endLine = document.getLineNumber(selectionEnd)
        val insertLine = endLine + 1

        val fileName = e.getData(CommonDataKeys.VIRTUAL_FILE)?.name ?: "Unknown"
        val lineNum = insertLine + 1
        val fileExt = fileName.substringAfterLast('.', "")

        val settings = MyLogSettings.instance
        val template = when (fileExt) {
            "java" -> settings.javaTemplate
            "kt", "kts" -> settings.kotlinTemplate
            "py" -> settings.pythonTemplate
            "go" -> settings.goTemplate
            "php" -> settings.phpTemplate
            else -> settings.jsTemplate
        }

        // Use indentation of the insert line (next line after selection)
        val insertLineStart = if (insertLine < document.lineCount) document.getLineStartOffset(insertLine) else 0
        val insertLineEnd = if (insertLine < document.lineCount) document.getLineEndOffset(insertLine) else insertLineStart
        val insertLineText = document.getText(TextRange(insertLineStart, insertLineEnd))
        val indentation = insertLineText.takeWhile { it.isWhitespace() }

        val logStatement = template
            .replace("\${file}", fileName)
            .replace("\${line}", lineNum.toString())
            .replace("\${var}", selectedText)
            .replace("\${class}", fileName.removeSuffix(".java"))
            .lineSequence()
            .joinToString("\n") { line ->
                if (line.isEmpty()) "" else "$indentation$line"
            } + "\n"

        WriteCommandAction.runWriteCommandAction(project, "Insert MyLog statement", null, Runnable {
            if (insertLine < document.lineCount) {
                val lineStartOffset = document.getLineStartOffset(insertLine)
                document.insertString(lineStartOffset, logStatement)
            } else {
                val prefix = if (document.textLength > 0 && !document.text.endsWith("\n")) "\n" else ""
                document.insertString(document.textLength, "$prefix$logStatement")
            }
        })

        editor.caretModel.moveToOffset(
            document.getLineStartOffset(insertLine.coerceAtMost(document.lineCount - 1))
        )
    }

    override fun update(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        val hasSelection = editor?.selectionModel?.hasSelection() == true
        e.presentation.isVisible = hasSelection
    }
}