package com.custom.plugin.mylog

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor

private fun findDeclarationEnd(document: Document, startLine: Int): Int {
    for (line in startLine until document.lineCount) {
        val lineStartOffset = document.getLineStartOffset(line)
        val lineEndOffset = document.getLineEndOffset(line)
        val lineText = document.getText(TextRange(lineStartOffset, lineEndOffset)).trimEnd()
        if (lineText.endsWith(";") || lineText.endsWith("}")) {
            return line + 1
        }
    }
    return startLine + 1
}

class MyLogAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val project = e.project ?: return

        val selectionModel = editor.selectionModel
        val selectedText = selectionModel.selectedText ?: return

        val document = editor.document
        val selectionStart = selectionModel.selectionStart

        val startLine = document.getLineNumber(selectionStart)

        // Find the end of the declaration by scanning for ';' or standalone '}' from startLine onwards
        val insertLine = findDeclarationEnd(document, startLine)


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

        // Preserve indentation from the original selection line
        val originalLineStart = document.getLineStartOffset(startLine)
        val originalLineText = document.getText(
            TextRange(originalLineStart, document.getLineEndOffset(startLine))
        )
        val indentation = originalLineText.takeWhile { it.isWhitespace() }

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