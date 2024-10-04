package it.d4n73.asutils

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.SelectionModel
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class UrlEncodeAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project: Project? = e.project
        val editor: Editor? = e.getData(com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR)

        if (editor != null) {
            val selectionModel: SelectionModel = editor.selectionModel
            val selectedText = selectionModel.selectedText

            if (selectedText != null) {
                try {
                    val encodedText = URLEncoder.encode(selectedText, StandardCharsets.UTF_8.toString())
                    WriteCommandAction.runWriteCommandAction(project) {
                        val document = editor.document
                        document.replaceString(selectionModel.selectionStart, selectionModel.selectionEnd, encodedText)
                    }
                    //Messages.showMessageDialog(project, "Testo codificato come URL", "Codifica completata", Messages.getInformationIcon())
                } catch (exception: Exception) {
                    Messages.showMessageDialog(project, "Errore nella codifica dell'URL", "Errore", Messages.getErrorIcon())
                }
            } else {
                Messages.showMessageDialog(project, "Seleziona del testo da codificare!", "Errore", Messages.getErrorIcon())
            }
        }
    }
}
