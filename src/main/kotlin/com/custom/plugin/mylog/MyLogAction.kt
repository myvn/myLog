package com.custom.plugin.mylog

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor

class MyLogAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val project = e.project ?: return

        val selectionModel = editor.selectionModel
        val selectedText = selectionModel.selectedText ?: return

        val document = editor.document
        val selectionStart = selectionModel.selectionStart
        val selectionEnd = selectionModel.selectionEnd

        val startLine = document.getLineNumber(selectionStart)
        val endLine = document.getLineNumber(selectionEnd)
        val insertLine = if (endLine == startLine) startLine + 1 else endLine + 1

        val fileName = e.getData(CommonDataKeys.VIRTUAL_FILE)?.name ?: "Unknown"
        val lineNum = startLine + 1

        val settings = MyLogSettings.instance
        val isJavaFile = e.getData(CommonDataKeys.VIRTUAL_FILE)?.name?.endsWith(".java") == true
        val template = if (isJavaFile) settings.javaTemplate else settings.jsTemplate

        val logStatement = template
            .replace("\${file}", fileName)
            .replace("\${line}", lineNum.toString())
            .replace("\${var}", selectedText)
            .replace("\${class}", fileName.removeSuffix(".java")) + "\n"

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